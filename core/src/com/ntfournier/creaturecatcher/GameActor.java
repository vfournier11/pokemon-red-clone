package com.ntfournier.creaturecatcher;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameActor {
    int x = 0;
    int y = 0;

    Map<Direction, Animation<TextureRegion>> animations;

    float stateTime = 0;
    Direction direction = Direction.DOWN;
    boolean isMoving = false;

    public GameActor(int x, int y) {
        this.x = x;
        this.y = y;

        this.instantiateAnimations();
    }

    private void instantiateAnimations() {
        this.animations = new HashMap<>(4);
        this.animations.put(Direction.UP, this.createAnimation(Direction.UP.toString().toLowerCase()));
        this.animations.put(Direction.DOWN, this.createAnimation(Direction.DOWN.toString().toLowerCase()));
        this.animations.put(Direction.LEFT, this.createAnimation(Direction.LEFT.toString().toLowerCase()));
        this.animations.put(Direction.RIGHT, this.createAnimation(Direction.RIGHT.toString().toLowerCase()));
    }

    private Animation<TextureRegion> createAnimation(String action) {
        return new Animation<TextureRegion>(.25f,
                                            CreatureCatcher.textureAtlas.findRegions("player_" + action),
                                            Animation.PlayMode.LOOP_PINGPONG);
    }

    public void walk(Direction direction) {
        this.direction = direction;
        this.stateTime = 0;
        this.isMoving = true;
    }

    public void update(float delta) {
        if(this.isMoving) {
            this.stateTime += delta;

            if(this.stateTime > .5f) {
                this.isMoving = false;
                this.stateTime = 0;
            }
        }
    }

    public TextureRegion getTextureRegion() {
        return this.animations.get(this.direction).getKeyFrame(this.stateTime);
    }
}
