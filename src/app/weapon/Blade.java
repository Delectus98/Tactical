package app.weapon;

import System.*;
import Graphics.*;
import app.Weapon;
import app.map.Map;

public class Blade extends Weapon {
    @Override
    public boolean isInRange(float distance) {
        return false;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public int getAmmunition() {
        return 0;
    }

    @Override
    public float getAccuracy(float distance) {
        return 0;
    }

    @Override
    public Projectile buildProjectile(Vector2i thrower, Vector2i target) {
        return new BladeProjectile("");
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        return null;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }
}
