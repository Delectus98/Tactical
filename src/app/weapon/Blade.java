package app.weapon;

import System.*;
import Graphics.*;
import app.Weapon;
import app.map.Map;

public class Blade extends Weapon {
    public Blade(){}

    @Override
    public boolean isInRange(float distance) {
        return 0 < distance && distance <= 1.5;
    }

    @Override
    public int getCost() {
        return -3;
    }

    @Override
    public int getAmmunition() {
        return -1;
    }

    @Override
    public float getAccuracy(float distance) {
        return 1;
    }

    @Override
    public Projectile buildProjectile(Vector2i thrower, Vector2i target) {
        return new BladeProjectile("explosion", new FloatRect(0,0,192,192), new Vector2f(thrower).mul(64.f), new Vector2f(target).mul(64.f), getWeaponSound());
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        Impact i = new Impact();
        i.add(target, 12);
        return i;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public String getWeaponSound() {
        return "smite";
    }
}
