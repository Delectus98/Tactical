package app.weapon;

import Graphics.*;
import System.*;
import app.animations.SpriteAnimation;
import app.sounds.Sound;
import util.ResourceHandler;

public class BladeProjectile extends Projectile {
    private transient Sprite smite;
    private transient float advance = 0;
    private transient SpriteAnimation animation;

    private String spritesheet;
    private float rectT;
    private float rectL;
    private float rectW;
    private float rectH;
    private float duration = 1;
    private String bladeSound = "";

    private transient boolean triggeredSound = false;

    public BladeProjectile() {
        // kryo net empty constructor required
    }


    public BladeProjectile(String spritesheet, FloatRect imageRect, Vector2f f1, Vector2f l, String bladeSound) {
        this.spritesheet = spritesheet;
        this.rectT = imageRect.t;
        this.rectL = imageRect.l;
        this.rectW = imageRect.w;
        this.rectH = imageRect.h;
        super.firstPos = f1;
        super.lastPos = l;

        this.bladeSound = bladeSound;
    }

    @Override
    public void init() {
        smite = new Sprite();
        smite.setPosition(lastPos.x + 32, lastPos.y + 32);
        smite.setScale(0.5f, 0.5f);
        smite.setOrigin(rectW / 4.f, rectH / 4.f);
        Vector2f trajectory = firstPos.neg().sum(lastPos).unit();
        smite.setRotation((float)Math.atan2(-trajectory.y, trajectory.x));
        animation = new SpriteAnimation(ResourceHandler.getTexture(spritesheet), new FloatRect(rectL, rectH, rectW, rectH), Time.seconds(1), 0, 15);
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
        if (!triggeredSound) {
            triggeredSound = true;
            Sound s = ResourceHandler.getSound(bladeSound);
            if (s != null) s.play();
        }

        advance += time.asSeconds();
        animation.update(time);
        animation.apply(smite);
    }
}
