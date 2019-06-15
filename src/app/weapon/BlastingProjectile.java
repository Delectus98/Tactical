package app.weapon;

import System.*;
import Graphics.*;
import app.animations.SpriteAnimation;
import util.ResourceHandler;

public class BlastingProjectile extends Projectile {
    // graphics
    private transient Sprite sprite;
    private transient Sprite explosionSprite;
    private transient SpriteAnimation animation;

    //network
    private String projectile;
    private String explosion;
    private FloatRect projectileRect;
    private FloatRect explosionRect;
    private float torque;
    private float duration;

    //
    private transient float advance = 0.f;
    private transient float advanceExplosion = 0.f;


    public BlastingProjectile()
    {

    }

    public BlastingProjectile(String projectile, FloatRect projectileRect, String explosion, FloatRect explosionRect, Vector2f p1, Vector2f target, float seconds){
        this.projectile = projectile;
        this.projectileRect = projectileRect;
        this.explosion = explosion;
        this.explosionRect = explosionRect;
        super.firstPos = p1;
        super.lastPos = target;

        this.duration = seconds;
        this.torque = 5.f;

        this.sprite = new Sprite(ResourceHandler.getTexture(projectile));
        sprite.setTextureRect(projectileRect.l, projectileRect.t, projectileRect.w, projectileRect.h);
        sprite.setPosition(firstPos.x, firstPos.y);
        sprite.setOrigin(sprite.getBounds().w / 2.f, sprite.getBounds().h / 2.f);

        explosionSprite = new Sprite();
        explosionSprite.setPosition(firstPos.x, firstPos.y);
        explosionSprite.setOrigin(explosionRect.w / 2.f, explosionRect.h / 2.f);

        animation = new SpriteAnimation(ResourceHandler.getTexture(explosion), explosionRect, Time.seconds(1), 0, 15);
    }

    @Override
    public void init() {
        sprite = new Sprite(ResourceHandler.getTexture(projectile));
        sprite.setTextureRect(projectileRect.l, projectileRect.t, projectileRect.w, projectileRect.h);
        sprite.setPosition(firstPos.x, firstPos.y);
        sprite.setOrigin(sprite.getBounds().w / 2.f, sprite.getBounds().h / 2.f);

        explosionSprite = new Sprite();
        explosionSprite.setPosition(super.lastPos.x, lastPos.y);
        explosionSprite.setOrigin(explosionRect.w / 2.f, explosionRect.h / 2.f);

        animation = new SpriteAnimation(ResourceHandler.getTexture(explosion), explosionRect, Time.seconds(1), 0, 15);
    }

    @Override
    public boolean hasFinishedHitting() {
        return advance >= duration && advanceExplosion >= 1;
    }

    @Override
    public void draw(RenderTarget target) {
        if (advance < duration)
            target.draw(sprite);
        else
            target.draw(explosionSprite);
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
            animation.update(time);
            animation.apply(explosionSprite);
        }
    }
}
