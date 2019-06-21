package app.menu.Buttons;

import Graphics.Color;
import Graphics.Shape;

public abstract class SpecialButton extends MenuButton {
    boolean ready = true;

    public SpecialButton(String title, Shape shape) {
        super(title, shape);
    }

    public void setReady(boolean b) {
        ready = b;

        if (ready){
            this.shape.setFillColor(Color.Blue);
        }
          else
              this.shape.setFillColor(Color.Black);
    }

    public void clicked() {
        if (ready) {
            this.clickedIfReady();
        } else {
            System.out.println(textZone.getString()+" Not ready");
        }
    }

    protected abstract void clickedIfReady();
    public abstract void checkIfButtonReady();

}
