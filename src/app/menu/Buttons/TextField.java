package app.menu.Buttons;

import Graphics.Color;
import Graphics.Shape;
import Graphics.Text;
import System.IO.AZERTYLayout;
import System.Keyboard;
import app.MainMENU;
import app.menu.Menu;
import util.ResourceHandler;

import java.io.IOException;

public abstract class TextField extends SpecialButton {
    protected Shape shape;
    protected Text textZone;
    private Keyboard keyboard = new Keyboard(MainMENU.window);
    private String acceptedCharacters;

    public TextField(int x, int y, int width, int height,String acceptedCharacters) throws IOException {
        super("", Menu.newButtonSprite("menuSmall"));
        this.acceptedCharacters=acceptedCharacters;
        this.shape.setFillColor(Color.White);
        if (ResourceHandler.getFont("default") == null) {
            ResourceHandler.loadFont("res/font.ttf", 20, "default");
        }
        this.textZone = new Text(ResourceHandler.getFont("default"), "");
    }


    @Override
    protected void clickedIfReady() {
        while (!(keyboard.isKeyPressed(AZERTYLayout.PAD_RETURN.getKeyID())||keyboard.isKeyPressed(AZERTYLayout.RETURN.getKeyID()))){

        }

    }

}
