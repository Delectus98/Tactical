package app.actions;

import System.*;
import app.Game;
import app.Unite;

public class MovingManager extends ActionManager {
    /**
     * Creates an Action Manager
     *
     * @param user launcher
     * @param game context
     */
    public MovingManager(Unite user, Game game) {
        super(user, game);
    }

    @Override
    public void updatePreparation(ConstTime time) {

    }

    @Override
    public void drawAboveFloor(RenderTarget target) {

    }

    @Override
    public void drawAboveStruct(RenderTarget target) {

    }

    @Override
    public void drawAboveEntity(RenderTarget target) {

    }

    @Override
    public void drawAboveHUD(RenderTarget target) {

    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public Action build() {
        return null;
    }
}
