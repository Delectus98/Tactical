package app.weapon;

import Graphics.*;
import System.*;
import util.ResourceHandler;

public class BulletProjectile extends Projectile {
    // graphics
    private transient Sprite bullet;

    //network
    private String texture;
    private FloatRect shape;
    private float radian;
    private Vector2f firstPos;
    private Vector2f lastPos;
    private float duration;

    //
    private transient float advance = 0.f;

    @Override
    public void init() {
        bullet = new Sprite(ResourceHandler.getTexture(texture));
        bullet.setTextureRect(shape.l, shape.t, shape.w, shape.h);
        bullet.setRotation(radian);
    }

    @Override
    public boolean isTerminated() {
        return advance >= duration;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(bullet);
    }

    @Override
    public void update(ConstTime time) {
        advance += time.asSeconds();
        Vector2f pos = lastPos.sum(firstPos.neg()).fact(advance / duration).add(firstPos);
        bullet.setPosition(pos.x, pos.y);
    }
}
