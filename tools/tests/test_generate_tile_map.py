import os
import unittest

from tile_map_generator.generate_tile_map import Tileset, MapsImage, MapFile

RESOURCE_FOLDER = os.path.join(os.path.dirname(os.path.dirname(os.path.realpath(__file__))), "resources")

DEFAULT_TILESET = os.path.join(RESOURCE_FOLDER, "tileset.png")
PALLET_TOWN_MAPS = os.path.join(RESOURCE_FOLDER, "Pallet Town.png")
TEST_MAP = os.path.join(RESOURCE_FOLDER, "test_map.png")
TEST_MAP_INDEXES = [469, 688, 689, 690, 427, 1493, 428, 767, 1299, 1300,
                    7, 45, 8, 8, 8, 1, 77, 78, 78, 79,
                    7, 39, 39, 39, 1142, 77, 115, 116, 116, 117,
                    7, 39, 39, 39, 1142, 115, 153, 154, 154, 155,
                    7, 120, 118, 39, 39, 153, 191, 192, 154, 191,
                    7, 41, 41, 238, 239, 1, 45, 41, 41, 7,
                    7, 41, 41, 276, 277, 5, 45, 41, 41, 11,
                    7, 41, 41, 314, 315, 1, 45, 41, 41, 49,
                    7, 7, 7, 1, 1, 1, 46, 1, 11, 87,
                    1, 2, 1, 2, 6, 1, 1, 2, 49, 48,
                    1, 1, 2, 1, 2, 1, 46, 2, 49, 48]

# Color to not
DEFAULT_EXCEPTION_COLOR = [(106, 148, 82)]

class TestGenerateTileMap(unittest.TestCase):
    def test_tileset_is_properly_parsed(self):
        asset_tileset = Tileset.from_file(DEFAULT_TILESET)

        white_line = 'WH ' * 15 + 'WH'
        first_tile = (white_line + '\n') * 15 + white_line
        self.assertEqual(first_tile, asset_tileset.tiles[0].representation)

        tile = 'LG WH ' * 3 + 'LG ' * 3 + 'WH LG ' * 3 + 'LG'
        self.assertTrue(asset_tileset.tiles[38].representation.startswith(tile))

    def test_map_correctly_indexed(self):
        asset_tileset = Tileset.from_file(DEFAULT_TILESET)
        map_images = MapsImage(PALLET_TOWN_MAPS, exception_colors=DEFAULT_EXCEPTION_COLOR)
        self.assertEqual(5, len(map_images.tilesets))

        first_tileset = map_images.tilesets[0]
        map_file = MapFile(asset_tileset, first_tileset)
        self.assertEqual(20, map_file.width)
        self.assertEqual(18, map_file.height)


    def test_map_correctly_parsed(self):
        asset_tileset = Tileset.from_file(DEFAULT_TILESET)
        map_images = MapsImage(TEST_MAP)
        self.assertEqual(1, len(map_images.tilesets))

        first_tileset = map_images.tilesets[0]
        for idx, tile in enumerate(first_tileset.tiles):
            with self.subTest(f'Test index: {idx}'):
                self.assertEqual(asset_tileset.tiles[TEST_MAP_INDEXES[idx] - 1].representation, tile.representation)

    def test_route_one_is_correctly_parsed(self):
        asset_tileset = Tileset.from_file(DEFAULT_TILESET)
        map_images = MapsImage(os.path.join(RESOURCE_FOLDER, "route_1.png"), exception_colors=DEFAULT_EXCEPTION_COLOR)
        self.assertEqual(1, len(map_images.tilesets))

    def test_map_tmx_properly_generated(self):
        asset_tileset = Tileset.from_file(DEFAULT_TILESET)
        map_images = MapsImage(TEST_MAP)

        expected_file_content = open(os.path.join(RESOURCE_FOLDER, 'test_map.tmx'), 'r').read().split('\n')
        # we split to be easier to diff
        expected_file_content = expected_file_content.split('\n')

        self.assertEqual(expected_file_content, MapFile(asset_tileset, map_images.tilesets[0]).file_repr().split('\n'))

