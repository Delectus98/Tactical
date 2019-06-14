package app.weapon;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Vector2f;
import Graphics.Vector2i;
import app.Weapon;
import app.map.Map;
import util.MapUtil;


public class Grenade extends Weapon {
    private static final String projectileSprite = "ammo";
    private static final FloatRect projectileRect = new FloatRect(0,0,32,32);

    private int ammunition = 3;
    private Sprite sprite;
    private int radius = 5;

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
    public Projectile buildProjectile(Vector2i thrower, Vector2i target) {
        ammunition--;
        return new BlastingProjectile(projectileSprite, projectileRect, new Vector2f(thrower.x*64.f + 32, thrower.y*64.f + 32), new Vector2f(target.x*64.f + 32, target.y*64.f + 32), 1);
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        Impact i = new Impact();

        MapUtil.getVisibles(target, radius, map).forEach(t -> i.add(t, radius + 1 - (int)(new Vector2f(target).sum(new Vector2f(t).neg()).length())));

        return i;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
