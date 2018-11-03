package com.ntfournier.creaturecatcher;

public class TileMapPosition {
    int x;
    int y;

    public TileMapPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.x, this.y);
    }
}
