package app;

import Graphics.Sprite;
import System.ConstTime;
import Graphics.FloatRect;
import Graphics.Vector2i;
import app.map.Map;
import app.weapon.Impact;
import app.weapon.Projectile;

import java.util.List;

public abstract class Weapon
{
    /**
     * Checks if a weapon can shoot in a its own specific range
     * @param distance between unite that shoot and tile where the target is
     * @return true if a weapon can shoot in a its own specific range else false
     */
    public abstract boolean isInRange(float distance);

    /**
     * Gives the cost of one use
     * @return the cost of one use
     */
    public abstract int getCost(); //negatif = vide les PA si déjà déplacé; sinon val absolue

    /**
     *
     * @return
     */
    public abstract int getAmmunition(); //-1 = infini, sinon utile pour armes speciales

    public abstract float getAccuracy(float distance);

    public abstract Projectile buildProjectile();

    public abstract Impact getImpactZone(Vector2i target, Map map);

    public abstract Sprite getSprite();

    //public abstract ProjectileInfo ...

    //public abstract WeaponInfo ...
}
