package app.actions;

import System.*;
import Graphics.*;


public class Shooting extends Action {
    // déroulement
    private Sprite projectile;
    private Vector2f trajectory;
    private float duration;
    private float elapsed;


    public Shooting() {}

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean isFinished() {
        return elapsed >= duration;
    }

    @Override
    public void drawAboveHUD(RenderTarget target) {

    }

    @Override
    public void drawAboveEntity(RenderTarget target) {

    }

    @Override
    public void drawAboveStruct(RenderTarget target) {

    }

    @Override
    public void drawAboveFloor(RenderTarget target) {

    }

    @Override
    public void update(ConstTime time) {
        elapsed += time.asSeconds();
        float percent = Math.min(Math.min(elapsed, duration) / duration, 1);
        projectile.setPosition(trajectory.mul(percent).x, trajectory.mul(percent).y);
    }

}
