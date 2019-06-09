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
    private float duration = 1.f;

    //
    private transient float advance = 0.f;

    public BulletProjectile()
    {

    }

    public BulletProjectile(String texture, FloatRect textureRect, Vector2f p1, Vector2f target){
        this.texture = texture;
        this.shape = textureRect;
        super.firstPos = p1;
        super.lastPos = target;

        this.duration = 2.f;
        Vector2f trajectory = p1.neg().sum(target).unit();
        this.radian = (float)Math.atan2(-trajectory.y, trajectory.x);

        bullet = new Sprite(ResourceHandler.getTexture(texture));
        bullet.setTextureRect(shape.l, shape.t, shape.w, shape.h);
        bullet.setPosition(firstPos.x, firstPos.y);
        bullet.setOrigin(bullet.getBounds().w / 2.f, bullet.getBounds().h / 2.f);
        bullet.setRotation(radian);
    }

    @Override
    public void init() {
        bullet = new Sprite(ResourceHandler.getTexture(texture));
        bullet.setTextureRect(shape.l, shape.t, shape.w, shape.h);
        bullet.setPosition(firstPos.x, firstPos.y);
        bullet.setOrigin(bullet.getBounds().w / 2.f, bullet.getBounds().h / 2.f);
        bullet.setRotation(radian);
    }

    @Override
    public boolean hasFinishedHitting() {
        return advance >= duration;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(bullet);
    }

    @Override
    public void update(ConstTime time) {
        advance += time.asSeconds();

        if (advance <= duration) {
            Vector2f pos = lastPos.sum(firstPos.neg()).fact(advance / duration).add(firstPos);
            bullet.setPosition(pos.x, pos.y);
        }
    }
}
