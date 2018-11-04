from setuptools import setup, find_packages
setup(
    name="TileMapGenerator",
    version="0.1",
    packages=find_packages(),
    install_requires=[
        "pillow"
    ],
    entry_points='''
        [console_scripts]
        tmg=tile_map_generator.generate_tile_map:cli
    '''
)
