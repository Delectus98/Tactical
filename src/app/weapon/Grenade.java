package app.weapon;

import Graphics.Sprite;
import Graphics.Vector2i;
import app.Weapon;
import app.map.Map;
import util.MapUtil;


public class Grenade extends Weapon {
    private int ammunition = 3;
    private Sprite sprite;

    @Override
    public boolean isInRange(float distance) {
        return 3 <= distance && distance <= 10;
    }

    @Override
    public int getCost() {
        return 4;
    }

    @Override
    public int getAmmunition() {
        return ammunition;
    }

    @Override
    public float getAccuracy(float distance) {
        return 1.F;
    }

    @Override
    public Projectile buildProjectile() {
        ammunition--;
        return new BlastingProjectile();
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        Impact impact = null;

        // add radius if not wall
        //impact = new Impact(MapUtil.getVisibles())

        return impact;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
