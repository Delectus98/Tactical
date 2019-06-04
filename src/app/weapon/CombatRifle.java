package app.weapon;

import Graphics.Sprite;
import Graphics.Vector2i;
import app.Weapon;
import app.map.Map;


public class CombatRifle extends Weapon {
    private Sprite sprite;

    @Override
    public boolean isInRange(float distance) {
        return 0 < distance && distance <= 8;
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public int getAmmunition() {
        return -1;
    }

    @Override
    public float getAccuracy(float distance) {
        return (float)Math.log(distance) / distance;
    }

    @Override
    public Projectile buildProjectile() {
        return new BulletProjectile();
    }

    @Override
    public Impact getImpactZone(Vector2i target, Map map) {
        return new Impact();
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
