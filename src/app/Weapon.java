package app;

import Graphics.Sprite;
import System.ConstTime;
import Graphics.FloatRect;
import Graphics.Vector2i;
import app.map.Map;
import app.weapon.Impact;

import java.util.List;

public abstract class Weapon
{

    public static class WeaponInfo {
        private String texture;
        private FloatRect rect;
    }

    public static class ProjectileInfo {
        private String texture;
        private List<FloatRect> spriteAnimation;
        private ConstTime frameDuration;
        //private Trajectory trajectory;

        private float radian;
    }

    public abstract boolean isInRange(float distance);

    public abstract int getCost(); //negatif = vide les PA si déjà déplacé; sinon val absolue

    public abstract int getAmmunition(); //-1 = infini, sinon utile pour armes speciales

    public abstract float getAccuracy(float distance);

    public abstract Impact getImpactZone(Vector2i target, Map map);

    public abstract Sprite getSprite();
    //public abstract ProjectileInfo ...

    //public abstract WeaponInfo ...
}
