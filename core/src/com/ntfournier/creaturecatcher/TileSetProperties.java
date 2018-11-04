package com.ntfournier.creaturecatcher;

import java.util.*;

public class TileSetProperties {
    private static final String[] WALKABLE_TILE_RANGES = {
            "1-4", "38-43", "237-241", "275-281",
            "313-318", "352-356"
    };

    private static final String[] JUMPABLE_TITLE_RANGES = {
            "117-125"
    };

    private static final Map<Integer, TileProperty> tileSet = instantiateMap();

    private static Map<Integer, TileProperty> instantiateMap() {
        Map<Integer, TileProperty> map = new HashMap<>();

        for (String tileRange: TileSetProperties.WALKABLE_TILE_RANGES) {
            String[] range = tileRange.split("-");
            for (int i = Integer.parseInt(range[0]); i <= Integer.parseInt(range[1]); ++i) {
                map.put(i, new TileProperty("generic_walkable", true));
            }
        }

        for (String tileRange: TileSetProperties.JUMPABLE_TITLE_RANGES) {
            String[] range = tileRange.split("-");
            for (int i = Integer.parseInt(range[0]); i <= Integer.parseInt(range[1]); ++i) {
                TileProperty tileProperty = new TileProperty("generic_jumpable", false);
                tileProperty.isJumpable = true;
                map.put(i, tileProperty);
            }
        }

        map.put(1, new TileProperty("empty", true));
        map.put(2, new TileProperty("empty_grass", true));
        map.put(5, new TileProperty("panel"));
        map.put(6, new TileProperty("panel_two_pillar"));
        map.put(7, new TileProperty("barrier_pod"));
        map.put(8, new TileProperty("barrier_pillar"));
        map.put(9, new TileProperty("water_top_right_corner"));
        map.put(11, new TileProperty("water_top_left_corner"));
        map.put(13, new TileProperty("water_heavy_top_right_corner"));
        map.put(15, new TileProperty("water_heavy_top_left_corner"));
        map.put(45, new TileProperty("bush_1"));
        map.put(83, new TileProperty("bush_1"));
        map.put(46, new TileProperty("cut_tree"));
        return Collections.unmodifiableMap(map);
    }

    public TileProperty get(int tileId) {
        if (!tileSet.containsKey(tileId)) {
            return new TileProperty("untagged");
        }

        return tileSet.get(tileId);
    }

}

