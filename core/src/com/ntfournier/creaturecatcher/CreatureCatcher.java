package com.ntfournier.creaturecatcher;

import static com.ntfournier.creaturecatcher.Constants.DEFAULT_CAMERA_ZOOM;
import static com.ntfournier.creaturecatcher.Constants.DEFAULT_ZOOM;
import static com.ntfournier.creaturecatcher.Constants.TILE_SIZE;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class CreatureCatcher extends Game {
    GameActor player = new GameActor(4, 4);

    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Animation<TextureAtlas.AtlasRegion> animation;
    float time = 0;
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;

    TileSetProperties tileSetProperties = new TileSetProperties();

    @Override
    public void create() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.zoom = DEFAULT_CAMERA_ZOOM;
        camera.setToOrtho(false, width, height);
        camera.update();

        tiledMap = new TmxMapLoader().load("maps/small.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas("sprites.txt");
        animation = new Animation(.1f, textureAtlas.getRegions());
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
        // The player image is always in the center of the screen and the drawing emplacement never changes.
        batch.draw(img, TILE_SIZE * 4 * DEFAULT_ZOOM, TILE_SIZE * 4 * DEFAULT_ZOOM, TILE_SIZE * DEFAULT_ZOOM, TILE_SIZE * DEFAULT_ZOOM);

        this.time += Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(this.time, true), 0, 0);
        batch.end();
    }

    public void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            TileProperty nextTile = nextTile(GameActor.Direction.UP, player);
            if (nextTile.isWalkable) {
                player.y += 1;
                camera.position.y += TILE_SIZE;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            TileProperty nextTile = nextTile(GameActor.Direction.DOWN, player);
            if (nextTile.isWalkable) {
                player.y -= 1;
                camera.position.y -= TILE_SIZE;
            }
            if (nextTile.isJumpable) {
                player.y -= 2;
                camera.position.y -= TILE_SIZE * 2;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            TileProperty nextTile = nextTile(GameActor.Direction.RIGHT, player);
            if (nextTile.isWalkable) {
                player.x += 1;
                camera.position.x += TILE_SIZE;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            TileProperty nextTile = nextTile(GameActor.Direction.LEFT, player);
            if (nextTile.isWalkable) {
                player.x -= 1;
                camera.position.x -= TILE_SIZE;
            }
        }

    }


    public TileProperty nextTile(GameActor.Direction direction, GameActor actor) {
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
        img.dispose();
    }
}
