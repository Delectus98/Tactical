package app.weapon;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Vector2f;
import System.ConstTime;
import System.RenderTarget;
import app.sounds.Sound;
import util.ResourceHandler;

public class BulletProjectile extends Projectile {
    // graphics
    private transient Sprite bullet;

    //network
    private String texture;
    private float rectT;
    private float rectL;
    private float rectW;
    private float rectH;
    private float radian;
    private float duration = 1.f;
    private String weaponSound = "";

    //
    private transient float advance = 0.f;
    private transient boolean triggeredSound = false;

    public BulletProjectile()
    {

    }

    public BulletProjectile(String texture, FloatRect textureRect, Vector2f p1, Vector2f target, String weaponSound){
        this.texture = texture;
        this.rectT = textureRect.t;
        this.rectL = textureRect.l;
        this.rectW = textureRect.w;
        this.rectH = textureRect.h;
        super.firstPos = p1;
        super.lastPos = target;

        Vector2f trajectory = p1.neg().sum(target).unit();
        this.duration = 0.085f * p1.neg().sum(target).length() / 64;
        this.radian = (float)Math.atan2(-trajectory.y, trajectory.x);

        bullet = new Sprite(ResourceHandler.getTexture(texture));
        bullet.setTextureRect(rectL, rectT, rectW, rectH);
        bullet.setPosition(firstPos.x, firstPos.y);
        bullet.setOrigin(bullet.getBounds().w / 2.f, bullet.getBounds().h / 2.f);
        bullet.setRotation(radian);

        this.weaponSound = weaponSound;
    }

    @Override
    public void init() {
        bullet = new Sprite(ResourceHandler.getTexture(texture));
        bullet.setTextureRect(rectL, rectT, rectW, rectH);
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
        if (!triggeredSound) {
            triggeredSound = true;
            Sound s = ResourceHandler.getSound(weaponSound);
            if (s != null) s.play();
        }

        advance += time.asSeconds();

        if (advance <= duration) {
            Vector2f pos = lastPos.sum(firstPos.neg()).fact(advance / duration).add(firstPos);
            bullet.setPosition(pos.x, pos.y);
        }
    }
}
