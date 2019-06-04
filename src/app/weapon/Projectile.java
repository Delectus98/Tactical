package app.weapon;

import System.*;
import Graphics.*;

public abstract class Projectile {

    public abstract void init();

    public abstract boolean isTerminated();

    public abstract void update(ConstTime time);

    public abstract void draw(RenderTarget target);
}
