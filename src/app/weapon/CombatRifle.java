package app.weapon;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Vector2f;
import Graphics.Vector2i;
import app.Weapon;
import app.map.Map;


public class CombatRifle extends Weapon {
    private static String weaponSprite = "";
    private static FloatRect weaponRect;

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
    public Projectile buildProjectile(Vector2i thrower, Vector2i target) {
        return new BulletProjectile(weaponSprite, weaponRect, new Vector2f(thrower.x*64.f, thrower.y*64.f), new Vector2f(target.x*64.f, target.y*64.f));
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        Impact i = new Impact();
        i.add(target, 5);
        return i;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }
}
