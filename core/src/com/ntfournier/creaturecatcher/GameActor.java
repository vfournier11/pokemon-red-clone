package com.ntfournier.creaturecatcher;

public class GameActor {
    float x = 0;
    float y = 0;

    public GameActor(float x, int y) {
        this.x = x;
        this.y = y;
    }

    enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }


}
