package app.weapon;

import System.*;
import Graphics.*;
import util.ResourceHandler;

public class BlastingProjectile extends Projectile {
    // graphics
    private transient Sprite sprite;
    private transient Sprite explosion;

    //network
    private String texture;
    private FloatRect rect;
    private float torque;
    private float duration;

    //
    private transient float advance = 0.f;
    private transient float advanceExplosion = 0.f;


    public BlastingProjectile()
    {

    }

    public BlastingProjectile(String texture, FloatRect textureRect, Vector2f p1, Vector2f target, float seconds){
        this.texture = texture;
        this.rect = textureRect;
        super.firstPos = p1;
        super.lastPos = target;

        this.duration = seconds;
        this.torque = 5.f;

        this.sprite = new Sprite(ResourceHandler.getTexture(texture));
        sprite.setTextureRect(rect.l, rect.t, rect.w, rect.h);
        sprite.setPosition(firstPos.x, firstPos.y);
        sprite.setOrigin(sprite.getBounds().w / 2.f, sprite.getBounds().h / 2.f);
    }

    @Override
    public void init() {
        sprite = new Sprite(ResourceHandler.getTexture(texture));
        sprite.setTextureRect(rect.l, rect.t, rect.w, rect.h);
        sprite.setPosition(firstPos.x, firstPos.y);
        sprite.setOrigin(sprite.getBounds().w / 2.f, sprite.getBounds().h / 2.f);
    }

    @Override
    public boolean hasFinishedHitting() {
        return advance >= duration;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(sprite);
    }

    @Override
    public void update(ConstTime time) {
        if (advance < duration) {
            advance += time.asSeconds();
            Vector2f pos = lastPos.sum(firstPos.neg()).fact(advance / duration).add(firstPos);
            sprite.setPosition(pos.x, pos.y);
            sprite.rotate(torque * (float) time.asSeconds());
        } else {
            advanceExplosion += time.asSeconds();
            //explosion.setTexture(explosions[0]);
        }
    }
}
