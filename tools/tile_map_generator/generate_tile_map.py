import argparse

from PIL import Image

from . import term_colors


class GreyShades:
    terminal_colors = {
        'BL': 234, 'DG': 340, 'LG': 247, 'WH': 254, 'ER': 1
    }

    def __init__(self, black, dark_grey, light_grey, white):
        self.colors = [black, dark_grey, light_grey, white]
        self.color_mapping = {
            black[0]: 'BL',
            dark_grey[0]: 'DG',
            light_grey[0]: 'LG',
            white[0]: 'WH',
        }

    def from_chars_color(self, chars):
        if chars not in self.terminal_colors:
            return 1
        return self.terminal_colors[chars]

    def from_pixel_color(self, color):
        if color not in self.color_mapping:
            return 'ER'
        return self.color_mapping[color]


class Tile:
    def __init__(self, pixels, start_x, start_y, grey_shades):
        self.grey_shade = grey_shades

        self.representation = ""
        for j in range(start_y, start_y + 16):
            for i in range(start_x, start_x + 16):
                self.representation += self.grey_shade.from_pixel_color(pixels[i, j][0])
                self.representation += ' ' if i != start_x + 15 else ''

            if j != start_y + 15:
                self.representation += '\n'

    def print_to_terminal(self):
        for line in self.representation.split('\n'):
            for char in line.split(' '):
                term_colors.print_color('  ', bg=self.grey_shade.from_chars_color(char), end='')

            term_colors.reset_color()
            print()
        print()


class Tileset:
    def __init__(self, tiles, width, height):
        self.tiles = tiles
        if not width.is_integer() or not height.is_integer():
            assert (f'Receive width and height are not whole number {width}, {height}')
        self.width = int(width)
        self.height = int(height)

    @classmethod
    def from_file(cls, filename, grey_shades=None):
        image = Image.open(filename)
        pixels = image.load()
        region = Region(0, image.width, 0, image.height)
        return Tileset.from_pixels(pixels, region, grey_shades)

    @classmethod
    def from_pixels(cls, pixels, region, grey_shades=None):
        if grey_shades is None:
            grey_shades = cls.autodetect_grey_shades(pixels, region)

        tiles = []
        for y in range(region.y1, region.y2, 16):
            for x in range(region.x1, region.x2, 16):
                tiles.append(Tile(pixels, x, y, grey_shades))

        return cls(tiles, (region.x2 - region.x1) / 16, (region.y2 - region.y1) / 16)

    @classmethod
    def autodetect_grey_shades(cls, pixels, region):
        shades = []
        for y in range(region.y1, region.y2):
            for x in range(region.x1, region.x2):
                current_pixel = pixels[x, y]
                if current_pixel not in shades and current_pixel[0] == current_pixel[1] and \
                        current_pixel[1] == current_pixel[2] and \
                        (not len(current_pixel) == 4 or current_pixel[3] == 255):
                    shades.append(current_pixel)

        if len(shades) != 4:
            assert (f'Unable to autodetect shades of grey expecting 4 found {len(shades)}')

        shades.sort()
        return GreyShades(shades[0], shades[1], shades[2], shades[3])

    def find_tile(self, tile):
        for idx, val in enumerate(self.tiles):
            if tile.representation == val.representation:
                return idx

        return 610


class Region:
    def __init__(self, x1, x2, y1, y2):
        self.x1 = x1
        self.x2 = x2
        self.y1 = y1
        self.y2 = y2

    def calculate_width(self):
        return self.x2 - self.x1

    def calculate_height(self):
        return self.y2 - self.y1

    def __str__(self):
        return f'{self.x1}, {self.x2}, {self.y1}, {self.y2}\n' \
               f' METADATA: nbHorizontalTiles: {self.calculate_width() / 16}\n' \
               f'           nbVerticalTiles  : {self.calculate_height() /16}'


class MapsImage:
    def __init__(self, filename, grey_shades=None, exception_colors=None):
        image = Image.open(filename)
        pixels = image.load()

        delimiter_colors = []
        greys = []
        for delimiter in self.__find_colors(pixels, image.width, image.height):
            if delimiter[0] == delimiter[1] == delimiter[2]:
                greys.append(delimiter)
            elif exception_colors is None or delimiter not in exception_colors:
                delimiter_colors.append(delimiter)

        if grey_shades is None:
            greys.sort()
            grey_shades = GreyShades(greys[0], greys[1], greys[2], greys[3])

        regions = self.__find_regions(pixels, image.width, image.height, grey_shades, delimiter_colors)
        self.tilesets = []
        print(f'Found {len(regions)} regions')
        regions = sorted(regions, key=lambda region: region.calculate_height() * region.calculate_width(), reverse=True)
        for region in regions:
            print(region)
            self.tilesets.append(Tileset.from_pixels(pixels, region, grey_shades))


    def __find_regions(self, pixels, width, height, grey_shades, delimiter_colors):
        regions = []

        for y in range(height):
            for x in range(width):
                if pixels[x, y] in grey_shades.colors:
                    left_pixel = pixels[x - 1, y]
                    top_pixel = pixels[x, y - 1]

                    if left_pixel in delimiter_colors and top_pixel in delimiter_colors:
                        region = Region(x, 0, y, 0)
                        for next_x in range(x + 1, width):
                            if pixels[next_x, y] in delimiter_colors:
                                region.x2 = next_x
                                break

                        for next_y in range(y + 1, height):
                            if pixels[x, next_y] in delimiter_colors:
                                region.y2 = next_y
                                break

                        if region.calculate_width() > 16 and region.calculate_height() > 16:
                            regions.append(region)

        indexes_to_remove = []
        for idx, region in enumerate(regions):
            for other_idx, other_region in enumerate(regions):
                if idx != other_idx and \
                        region.x1 >= other_region.x1 and \
                        region.x2 <= other_region.x2 and \
                        region.y1 >= other_region.y1 and \
                        region.y2 <= other_region.y2:
                    indexes_to_remove.append(idx)

        return [region for idx, region in enumerate(regions) if idx not in indexes_to_remove]

    def __find_colors(self, pixels, width, height):
        delimiter_colors = []
        for y in range(height):
            for x in range(width):
                current_pixel = pixels[x, y]
                if current_pixel not in delimiter_colors:
                    delimiter_colors.append(current_pixel)

        return delimiter_colors


class MapFile:
    def __init__(self, tileset_assets, tileset_map):
        self.rows = []
        for j in range(tileset_map.height):
            line = []
            for i in range(tileset_map.width):
                line.append(tileset_assets.find_tile(tileset_map.tiles[j * tileset_map.width + i]))
            self.rows.append(line)

        self.width = len(self.rows[0])
        self.height = len(self.rows)

    def file_repr(self):
        representation = '<?xml version="1.0" encoding="UTF-8"?>\n' \
               '<map version="1.0" tiledversion="1.1.6" orientation="orthogonal" renderorder="right-down"' \
               f' width="{self.width}" height="{self.height}"' \
               ' tilewidth="16" tileheight="16" infinite="0" nextobjectid="0">\n' \
               '<tileset firstgid="0" source="tileset.tsx"/>\n' \
               f'<layer name="world_tiles" width="{self.width}" height="{self.height}">' \
               '<data encoding="csv">'
        for line in self.rows:
            representation += ','.join(str(x) for x in line) + ',\n'
        representation = representation[:-2] + '\n'  # Remove last comma

        representation += '</data>\n' \
                          '</layer>\n' \
                          '</map>'
        return representation

    def print(self):
        print(self.file_repr())

    def save_to_file(self, filename):
        with open(filename, 'w') as f:
            f.write(self.file_repr())


def cli():
    parser = argparse.ArgumentParser(description='Generate TileMaps from picture')
    parser.add_argument('tileset_location', type=str)
    parser.add_argument('map_image', type=str)
    parser.add_argument('exception_colors', type=tuple)
    parser.add_argument('save_prefix', type=str, )
    args = parser.parse_args()

    map_image = MapsImage(args.map_image, exception_colors=args.exception_colors)
    asset_tileset = Tileset.from_file(args.tileset_location)
    for i, tileset in enumerate(map_image.tilesets):
        MapFile(asset_tileset, tileset).save_to_file(f'{args.save_prefix}_{i}.tmx')
