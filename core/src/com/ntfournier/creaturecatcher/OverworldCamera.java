package com.ntfournier.creaturecatcher;

import static com.ntfournier.creaturecatcher.Constants.PLAYER_MOVEMENT_SPEED;
import static com.ntfournier.creaturecatcher.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class OverworldCamera extends OrthographicCamera {

    Direction direction = null;

    float stateTime = 0;

    OverworldCamera(float width, float height, float zoom) {
        super();

        this.zoom = zoom;
        this.setToOrtho(false, width, height);

        super.update();
    }

    public void move(Direction direction, int unit) {
        this.direction = direction;

    }

    public void update(float delta) {
        if (this.direction != null) {
            switch(this.direction) {
                case UP:
                    this.position.y += delta * PLAYER_MOVEMENT_SPEED;
                    break;
                case DOWN:
                    this.position.y -= delta * PLAYER_MOVEMENT_SPEED;
                    break;
                case LEFT:
                    this.position.x -= delta * PLAYER_MOVEMENT_SPEED;
                    break;
                case RIGHT:
                    this.position.x += delta * PLAYER_MOVEMENT_SPEED;
                    break;
            }

            this.stateTime += delta;

            if(this.stateTime >= 0.5f) {
                this.stateTime = 0;
                this.direction = null;
            }
        }

        super.update();
    }
}
