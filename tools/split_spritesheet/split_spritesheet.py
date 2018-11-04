from PIL import Image

folder = 'sprites'
names = [
    "player_down_0",
    "player_down_1",
    "player_up_0",
    "player_up_1",
    "player_left_0",
    "player_left_1",
    "SKIP",
    "SKIP",
]

if __name__ == '__main__':
    image = Image.open('characters_overworld.png')
    pixels = image.load()
    index = -1
    for y in range(0, 32, 16):
        for x in range(0, image.width, 16):
            index += 1
            if len(names) > index and names[index] is "SKIP":
                continue

            current_sprite = image.crop((x, y, x + 16, y + 16))
            sprite_name = names[index] if index < len(names) else f'sprite_{index}'
            current_sprite.save(f'{folder}/{sprite_name}.png')



