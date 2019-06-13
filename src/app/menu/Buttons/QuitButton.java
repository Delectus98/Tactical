package app.menu.Buttons;

import Graphics.RectangleShape;
import Graphics.Shape;
import app.MainMENU;

public class QuitButton extends SpecialButton {
    public QuitButton() {
        super("Quit", new RectangleShape(0,0,30,30));

    }

    @Override
    protected void clickedIfReady() {
        System.out.println("Leaving game...");
        System.exit(0);
    }

    @Override
    public void checkIfButtonReady() {
        //setReady(true); //useless, true when created
    }
}
