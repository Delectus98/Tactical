package app.weapon;

import Graphics.*;
import System.*;
import app.animations.SpriteAnimation;
import util.ResourceHandler;

public class BladeProjectile extends Projectile {
    private transient Sprite smite;
    private transient float advance = 0;
    private transient SpriteAnimation animation;

    private String spritesheet;
    private FloatRect rect;
    private float duration = 1;


    public BladeProjectile(String spritesheet, FloatRect imageRect, Vector2f f1, Vector2f l) {
        this.spritesheet = spritesheet;
        this.rect = imageRect;
        super.firstPos = f1;
        super.lastPos = l;
    }

    @Override
    public void init() {
        smite = new Sprite();
        smite.setPosition(lastPos.x + 32, lastPos.y + 32);
        smite.setScale(0.5f, 0.5f);
        smite.setOrigin(rect.w / 4.f, rect.h / 4.f);
        Vector2f trajectory = firstPos.neg().sum(lastPos).unit();
        smite.setRotation((float)Math.atan2(-trajectory.y, trajectory.x));
        animation = new SpriteAnimation(ResourceHandler.getTexture(spritesheet), rect, Time.seconds(1), 0, 15);
    }

    @Override
    public boolean hasFinishedHitting() {
        return advance >= duration;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(smite);
    }

    @Override
    public void update(ConstTime time) {
        advance += time.asSeconds();
        animation.update(time);
        animation.apply(smite);
    }
}
