package app.menu.Buttons;

import Graphics.Color;
import Graphics.Sprite;

public abstract class SpecialButton extends MenuButton {
    private boolean ready = true;

    public SpecialButton(String title, Sprite shape) {
        super(title, shape);
    }

    protected void setReady(boolean b) {
        ready = b;

        if (ready) {

         getSprite().setFillColor(new Color(169, 169, 169));

        } else {
            getSprite().setFillColor(new Color(178, 34, 34));}

        }

        public void clicked () {
            if (ready) {
                this.clickedIfReady();
            } else {
                System.out.println(textZone.getString() + " Not ready");
            }
        }

        protected abstract void clickedIfReady ();
        public abstract void checkIfButtonReady ();

    }
