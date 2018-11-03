package com.ntfournier.creaturecatcher;

public class TileProperty {
    String name;

    boolean isJumpable = false;
    boolean isWalkable = false;

    public TileProperty(String name) {
        this.name = name;
    }

    public TileProperty(String name, boolean isWalkable) {
        this.name = name;
        this.isWalkable = isWalkable;
    }

    public String toString() {
        return String.format("%s (W:%s)", this.name, this.isWalkable ? 'T' : 'F');
    }
}
