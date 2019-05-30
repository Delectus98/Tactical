package app.menu.SpecialButtons;

import Graphics.Shape;
import Graphics.Vector2f;
import app.MainMENU;
import app.menu.MenuButton;

public class LaunchGame extends MenuButton {
    public LaunchGame(Shape shape) {
        super("Ready", shape);
    }

    public LaunchGame(String txt, Shape shape) {
        super(txt, shape);
    }

    //MOCHE. TRES MOCHE.
    public void clicked() {
        System.out.println(this.text);
        MainMENU.state = MainMENU.STATE.GAME;
    }
}
