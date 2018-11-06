package com.ntfournier.creaturecatcher;

import static com.ntfournier.creaturecatcher.Constants.PLAYER_MOVEMENT_SPEED;
import static com.ntfournier.creaturecatcher.Constants.TILE_SIZE;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class OverworldCamera extends OrthographicCamera {

    Direction direction = null;

    float stateTime = 0;

    int initialX;
    int initialY;

    OverworldCamera(float width, float height, float zoom) {
        super();

        this.zoom = zoom;
        this.setToOrtho(false, width, height);

        super.update();
        this.initialX = (int) this.position.x;
        this.initialY = (int) this.position.y;
    }

    public boolean move(Direction direction) {
        if (this.direction != null) {
            return false;
        }

        this.direction = direction;
        return true;
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
                case JUMP_DOWN:
                    this.position.y -= delta * 2 * PLAYER_MOVEMENT_SPEED;
                    break;
            }

            this.stateTime += delta;

            if(this.stateTime >= 0.5f) {
                this.position.x = Math.round((this.position.x - this.initialX) / (TILE_SIZE * 1.0f)) * TILE_SIZE + this.initialX;
                this.position.y = Math.round((this.position.y - this.initialY) / (TILE_SIZE * 1.0f)) * TILE_SIZE + this.initialY;
                System.out.println(this);

                this.stateTime = 0;
                this.direction = null;
            }
        }

        super.update();
    }

    @Override
    public String toString() {
        return String.format("[OverworldCamera] location: (%f, %f)", this.position.x, this.position.y);
    }
}
