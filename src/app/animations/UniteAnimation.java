package app.animations;

import Graphics.*;
import System.ConstTime;
import System.Time;
import app.Unite;

import java.util.ArrayList;
import java.util.List;

public class UniteAnimation {
    private static final int UP_DIRECTION = 9;
    private static final int DOWN_DIRECTION = 0;
    private static final int RIGHT_DIRECTION = 6;
    private static final int LEFT_DIRECTION = 3;

    private static final int IDLE_STATE = 1;

    private static final int DOWN = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int LEFT = 3;

    private ConstTexture texture;
    private Sprite uniteSprite;
    private Unite unite;
    private SpriteAnimation[] imageAnimation;
    private int millisecondsPerTile;
    private List<Integer> directions;
    private List<Vector2i> tiles;
    private int step = 1;
    private int stepElasped = 0;

    private int currentDirection = DOWN;

    private static int direction(Vector2i v) {
        if (Math.abs(v.x) > Math.abs(v.y)) {
            return (v.x < 0) ? LEFT : RIGHT;
        } else {
            return (v.y < 0) ? UP : DOWN;
        }
    }

    private static int directionOffsetOnTexture(int direction) {
        switch (direction) {
            case UP: return UP_DIRECTION;
            case DOWN: return DOWN_DIRECTION;
            case LEFT: return LEFT_DIRECTION;
            case RIGHT: return RIGHT_DIRECTION;
            default: return -1;
        }
    }

    public UniteAnimation(Unite unite, List<Vector2i> tiles, int millisecondsPerTile){
        this.millisecondsPerTile = millisecondsPerTile;

        this.unite = unite;
        this.tiles = tiles;
        this.texture = unite.getSpriteSheet();
        this.uniteSprite = unite.getSprite();
        this.imageAnimation = new SpriteAnimation[4];
        this.directions = new ArrayList<>();
        //this.moveAnimation = new ArrayList<>();
        for (int i=1; i < tiles.size() ; ++i) {
            Vector2i v = new Vector2i(tiles.get(i).x - tiles.get(i - 1).x, tiles.get(i).y - tiles.get(i - 1).y);
            directions.add(direction(v));
        }
        System.out.println("d:"+directions.size());
        System.out.println("t:"+tiles.size());

        this.currentDirection = directions.get(0);

        this.texture = texture;
        this.imageAnimation[UP] = new SpriteAnimation(texture, new FloatRect(0,0,64,64), Time.milliseconds(millisecondsPerTile / 2), UP_DIRECTION, UP_DIRECTION + 3);
        this.imageAnimation[DOWN] = new SpriteAnimation(texture, new FloatRect(0,0,64,64), Time.milliseconds(millisecondsPerTile / 2), DOWN_DIRECTION, DOWN_DIRECTION + 3);
        this.imageAnimation[RIGHT] = new SpriteAnimation(texture, new FloatRect(0,0,64,64), Time.milliseconds(millisecondsPerTile / 2), RIGHT_DIRECTION, RIGHT_DIRECTION + 3);
        this.imageAnimation[LEFT] = new SpriteAnimation(texture, new FloatRect(0,0,64,64), Time.milliseconds(millisecondsPerTile / 2), LEFT_DIRECTION, LEFT_DIRECTION + 3);
    }

    public void update(ConstTime elapsed) {
        stepElasped += elapsed.asMilliseconds();

        //moveAnimation.advanceCoords(elapsed);
        imageAnimation[currentDirection].update(elapsed);
        imageAnimation[currentDirection].apply(uniteSprite);

        float ratio = Math.min(stepElasped / (float) millisecondsPerTile, 1);
        Vector2f pos = new Vector2f(tiles.get(step-1)).neg().sum(new Vector2f(tiles.get(step))).mul(ratio).sum(new Vector2f(tiles.get(step-1))).mul(64.f);

        uniteSprite.setPosition(pos.x, pos.y);

        if (stepElasped >= millisecondsPerTile) {
            step = step + 1;

            if (step <= directions.size()) {
                int newDirection = directions.get(step - 1);

                imageAnimation[newDirection].reset();
                imageAnimation[newDirection].setStep(imageAnimation[currentDirection].getGlobalStep());

                currentDirection = newDirection;
            }

            stepElasped = 0;
        }

        if (!isTerminated()) {
            unite.setMapPosition(tiles.get(step));
        } else {
            imageAnimation[currentDirection].setStep(directionOffsetOnTexture(currentDirection)+IDLE_STATE);
            imageAnimation[currentDirection].apply(uniteSprite);
        }
    }

    public boolean isTerminated() {
        return step > directions.size();
    }

}