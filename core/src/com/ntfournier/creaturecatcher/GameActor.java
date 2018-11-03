package com.ntfournier.creaturecatcher;

public class GameActor {
    int x = 0;
    int y = 0;

    public GameActor(int x, int y) {
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
