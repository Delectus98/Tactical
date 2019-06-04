package app.weapon;

import Graphics.*;
import System.*;
import util.ResourceHandler;

public class BladeProjectile extends Projectile {
    private transient Sprite smite;

    private float duration;
    private transient float advance = 0;

    @Override
    public void init() {
        smite = new Sprite();
    }

    @Override
    public boolean isTerminated() {
        return advance >= duration;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(smite);
    }

    @Override
    public void update(ConstTime time) {
        advance += time.asSeconds();
    }
}
