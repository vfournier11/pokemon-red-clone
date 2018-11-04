package com.ntfournier.creaturecatcher;

import static com.ntfournier.creaturecatcher.Constants.DEFAULT_CAMERA_ZOOM;
import static com.ntfournier.creaturecatcher.Constants.DEFAULT_ZOOM;
import static com.ntfournier.creaturecatcher.Constants.TILE_SIZE;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class CreatureCatcher extends Game {
    static TextureAtlas textureAtlas;

    GameActor player;

    SpriteBatch batch;
    TiledMap tiledMap;

    OverworldCamera camera;

    TiledMapRenderer tiledMapRenderer;

    TileSetProperties tileSetProperties = new TileSetProperties();

    @Override
    public void create() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OverworldCamera(width, height, DEFAULT_CAMERA_ZOOM);

        tiledMap = new TmxMapLoader().load("maps/small.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("sprites.txt");

        this.player = new GameActor(4, 4);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        this.player.update(deltaTime);
        this.camera.update(deltaTime);
        this.handleInputs();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();
        // The player image is always in the center of the screen and the drawing emplacement never changes.
        batch.draw(this.player.getTextureRegion(),
                   TILE_SIZE * 4 * DEFAULT_ZOOM,
                   TILE_SIZE * 4 * DEFAULT_ZOOM,
                   TILE_SIZE * DEFAULT_ZOOM,
                   TILE_SIZE * DEFAULT_ZOOM);
        batch.end();
    }

    public void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            this.player.direction = Direction.UP;
            TileProperty nextTile = nextTile(Direction.UP, player);
            if (nextTile.isWalkable) {
                player.y += 1;
                this.player.walk(Direction.UP);
                this.camera.move(Direction.UP, 1);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            this.player.direction = Direction.DOWN;
            TileProperty nextTile = nextTile(Direction.DOWN, player);
            if (nextTile.isWalkable) {
                player.y -= 1;
                this.player.walk(Direction.DOWN);
                this.camera.move(Direction.DOWN, 1);
            }
            if (nextTile.isJumpable) {
                player.y -= 2;
                this.player.walk(Direction.DOWN);
                this.camera.move(Direction.DOWN, 2);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            this.player.direction = Direction.RIGHT;
            TileProperty nextTile = nextTile(Direction.RIGHT, player);
            if (nextTile.isWalkable) {
                player.x += 1;
                this.player.walk(Direction.RIGHT);
                this.camera.move(Direction.RIGHT, 2);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            this.player.direction = Direction.LEFT;
            TileProperty nextTile = nextTile(Direction.LEFT, player);
            if (nextTile.isWalkable) {
                player.x -= 1;
                this.player.walk(Direction.LEFT);
                this.camera.move(Direction.LEFT, 1);
            }
        }
    }


    public TileProperty nextTile(Direction direction, GameActor actor) {
        TileMapPosition location = new TileMapPosition(actor.x, actor.y);

        switch (direction) {
            case UP:
                location.y += 1;
                break;
            case DOWN:
                location.y -= 1;
                break;
            case LEFT:
                location.x -= 1;
                break;
            case RIGHT:
                location.x += 1;
                break;
        }

        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(location.x, location.y);
        TileProperty tileProperty;
        if (cell == null) {
            return new TileProperty("OUT_OF_MAP");
        }

        int id = cell.getTile().getId();
        tileProperty = tileSetProperties.get(id);
        System.out.println(String.format("[location: %s] [tile: id:%d, %s]", location, id, tileProperty));
        return tileProperty;
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
    }
}
