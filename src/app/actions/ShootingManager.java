package app.actions;

import System.*;
import app.Game;
import app.Unite;
import app.Weapon;

public class ShootingManager extends ActionManager {
    private Weapon selectedWeapon;

    public ShootingManager(Unite user, Game game) {
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
        int cost = selectedWeapon.getCost();
        if (cost < 0)
            cost = super.unite.getSparePoints();

        return cost;
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
