package app.menu.Buttons;

import Graphics.Sprite;
import Graphics.Vector2f;
import app.MainMENU;

import java.io.IOException;

public class NormalButton extends MenuButton {


    private final int goTOMenuNumber;

    /**
     * Un NormalButton est un bouton qui mène simplement à un autre menu
     *
     * @param title          Texte affiché sur le bouton
     * @param goTOMenuNumber Index du menu où il mène ( utiliser MainMenu.
     * @param sprite
     */
    public NormalButton(String title, int goTOMenuNumber, Sprite sprite) {
        super(title, sprite);
        this.goTOMenuNumber = goTOMenuNumber;
    }

    public void clicked() throws IOException {
        MainMENU.currentMenu = goTOMenuNumber;
    }

    public boolean collide(Vector2f mousse) {
        return getSprite().getBounds().contains(mousse.x, mousse.y);
    }

}

