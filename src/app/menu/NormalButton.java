package app.menu;

import Graphics.Shape;
import Graphics.Vector2f;
import app.MainMENU;

public class NormalButton extends MenuButton {


    private final int goTOMenuNumber;

    /**
     * Un NormalButton est un bouton qui mène simplement à un autre menu
     *
     * @param title          Texte affiché sur le bouton
     * @param goTOMenuNumber Index du menu où il mène ( utiliser MainMenu.
     * @param shape
     */
    public NormalButton(String title, int goTOMenuNumber, Shape shape) {
        super(title, shape);
        this.goTOMenuNumber = goTOMenuNumber;
    }

    public void clicked() {
        System.out.println("Button: " + this.text);
        MainMENU.currentMenu = goTOMenuNumber;
    }

    public boolean collide(Vector2f mousse) {
        return shape.getBounds().contains(mousse.x, mousse.y);
    }

}

