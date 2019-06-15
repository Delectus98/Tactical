package app.animations;


import System.*;
import Graphics.*;

public class SpriteAnimation {
    private ConstTexture spritesheet;
    private FloatRect spriteRectangle;
    private int current = 0;
    private ConstTime frameDuration;
    private Time totalElapsed = Time.zero();
    private int firstImage;
    private int lastImage;
    private int texW;
    private int texH;

    public SpriteAnimation(ConstTexture spritesheet, FloatRect spriteRect, ConstTime frameDuration, int first, int last) {
        this.spritesheet = spritesheet;
        this.frameDuration = frameDuration;
        this.firstImage = first;
        this.lastImage = last;
        this.spriteRectangle = spriteRect;

        this.texW = Math.max(1, (int)(spritesheet.getWidth() / spriteRectangle.w));
        this.texH = Math.max(1, (int)(spritesheet.getHeight() / spriteRectangle.h));
    }

    public void update(ConstTime elapsed) {
        totalElapsed.add(Time.nanoseconds((long)elapsed.asNanoseconds()));
        current =  (int)((lastImage - firstImage)* (float)totalElapsed.asSeconds() / (float)frameDuration.asSeconds()) % (lastImage - firstImage) + firstImage;
    }

    public boolean isLastImage() {
        return current == lastImage - 1;
    }

    public void apply(Sprite s){
        s.setTexture(spritesheet);
        s.setTextureRect((current % texW) * spriteRectangle.w + spriteRectangle.l, (current / texW) * spriteRectangle.h + spriteRectangle.t, spriteRectangle.w, spriteRectangle.h);
    }
}
