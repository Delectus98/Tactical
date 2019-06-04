package app.weapon;

import System.*;
import Graphics.*;

/**
 * Represents the graphics component shown when a Weapon is used
 */
public abstract class Projectile {

    protected Vector2f firstPos;
    protected Vector2f lastPos;

    /**
     * Must be initialized before used
     */
    public abstract void init();

    /**
     * Sets the position of the unite that throw the projectile
     * @param pos thrower position
     */
    public final void setTrackerPosition(Vector2f pos)
    {
        firstPos = pos;
    }

    /**
     * Sets the position of the tile that is targeted
     * @param pos target position
     */
    public final void setTargetPosition(Vector2f pos)
    {
        lastPos = pos;
    }

    /**
     * Checks if projectile has finished hitting (animation, trajectory, etc...)
     * @return true if projectile has finished hitting (animation, trajectory, etc...) else false
     */
    public abstract boolean hasFinishedHitting();

    /**
     * Updates projectile specific stuff (as animation, trajectory, explosion...)
     * @param time
     */
    public abstract void update(ConstTime time);

    /**
     * Draws graphics components
     * @param target render target
     */
    public abstract void draw(RenderTarget target);
}
