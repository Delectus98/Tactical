package app.menu.Buttons;

import Graphics.Color;
import Graphics.Sprite;

import java.io.IOException;

public abstract class SpecialButton extends MenuButton {
    private boolean ready = true;

    public SpecialButton(String title, Sprite shape) {
        super(title, shape);
    }

    public void setReady(boolean b) {
        ready = b;

        if (this.ready) {
         getSprite().setFillColor(new Color(0.86f, 0.86f, 0.86f));
        } else {
            getSprite().setFillColor(new Color(0.7f, 0.13f, 0.13f));}
        }

        public void clicked () throws IOException, InterruptedException {
            //checkIfButtonReady ();
            if (this.ready) {
                this.clickedIfReady();
            } else {
                System.out.println(textZone.getString() + " Not ready");
            }
        }

        protected abstract void clickedIfReady () throws IOException, InterruptedException;
        public abstract void checkIfButtonReady () throws IOException, InterruptedException;
    }
