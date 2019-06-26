package app;

import Graphics.Sprite;
import Graphics.Vector2i;
import app.map.Map;
import app.weapon.Impact;
import app.weapon.Projectile;

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
     * Gives the total remaining ammunition of the weapon.
     * @return the total remaining ammunition of the weapon.
     */
    public abstract int getAmmunition(); //-1 = infini, sinon utile pour armes speciales

    /**
     * Gives the chance of a weapon to touch the enemy.
     * Accuracy must be in [0,1] range
     */
    public abstract float getAccuracy(float distance);

    /**
     * Creates a specific weapon projectile that will be drawn and updated.
     * @return a specific weapon projectile that will be drawn and updated.
     */
    public abstract Projectile buildProjectile(Vector2i thrower, Vector2i target);

    /**
     * Gives an interface that describe the damages inflected on an world map.
     * @param thrower unite that use the weapon
     * @param target tile where thrower is pointing to.
     * @param map world map.
     * @return impact of the weapon on the map.
     */
    public abstract Impact getImpactZone(Vector2i thrower, Vector2i target, Map map);

    /**
     * Gives the graphic component of the weapon.
     * @return the graphic component of the weapon.
     */
    public abstract Sprite getSprite();

    /**
     * Gives the triggered sound when the weapon is used
     * @return the triggered sound when the weapon is used
     */
    public abstract String getWeaponSound();
}
