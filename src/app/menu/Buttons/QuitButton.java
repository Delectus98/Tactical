package app.menu.Buttons;

import app.menu.Menu;

public class QuitButton extends SpecialButton {
    public QuitButton() {
        super("Quit", Menu.newButtonSprite("menuSmall"));

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
