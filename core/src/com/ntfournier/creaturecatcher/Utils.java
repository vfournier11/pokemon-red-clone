package com.ntfournier.creaturecatcher;

import com.badlogic.gdx.math.Vector2;

public class Utils {
    public static TileMapPosition getTileMapLocation(GameActor actor) {
        return new TileMapPosition((int) (actor.x / 16), (int) (actor.y / 16));
    }
}
