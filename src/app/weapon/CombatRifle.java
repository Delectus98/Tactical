package app.weapon;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Vector2f;
import Graphics.Vector2i;
import app.Weapon;
import app.map.Map;


public class CombatRifle extends Weapon {
    private static final String projectileSprite = "ammo";
    private static final FloatRect projectileRect = new FloatRect(32,0,32,32);

    private Sprite sprite;

    @Override
    public boolean isInRange(float distance) {
        return .5f < distance && distance <= 8.5;
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
        return new BulletProjectile(projectileSprite, projectileRect, new Vector2f(thrower.x*64.f + 32, thrower.y*64.f + 32), new Vector2f(target.x*64.f + 32, target.y*64.f + 32), getWeaponSound());
    }

    @Override
    public Impact getImpactZone(Vector2i thrower, Vector2i target, Map map) {
        Impact i = new Impact();
        i.add(target, 4);
        return i;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getWeaponSound() {
        return "assault";
    }
}
