package com.ntfournier.creaturecatcher;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class CreatureCatcher extends Game {
    GameActor player = new GameActor(16 * 4, 16 * 4);

    SpriteBatch batch;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;

    TileSetProperties tileSetProperties = new TileSetProperties();

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("maps/small.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();
        img = new Texture("red.png");
    }

    @Override
    public void render() {
        this.handleInputs();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();
        batch.draw(img, 16 * 4, 16 * 4);
        batch.end();
    }

    public void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            TileProperty nextTile = nextTile(GameActor.Direction.UP, player);
            if (nextTile.isWalkable) {
                player.y += 16;
                camera.position.y += 16;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            TileProperty nextTile = nextTile(GameActor.Direction.DOWN, player);
            if (nextTile.isWalkable) {
                player.y -= 16;
                camera.position.y -= 16;
            }
            if (nextTile.isJumpable) {
                player.y -= 32;
                camera.position.y -= 32;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            TileProperty nextTile = nextTile(GameActor.Direction.RIGHT, player);
            if (nextTile.isWalkable) {
                player.x += 16;
                camera.position.x += 16;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            TileProperty nextTile = nextTile(GameActor.Direction.LEFT, player);
            if (nextTile.isWalkable) {
                player.x -= 16;
                camera.position.x -= 16;
            }
        }

    }


    public TileProperty nextTile(GameActor.Direction direction, GameActor actor) {
        TileMapPosition location = Utils.getTileMapLocation(actor);

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

        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getCell(location.x,
                                                                                                location.y);
        if (cell == null) {
            return new TileProperty("OUT_OF_MAP");
        }
        return tileSetProperties.get(cell.getTile().getId());
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
