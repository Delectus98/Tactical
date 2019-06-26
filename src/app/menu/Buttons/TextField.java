package app.menu.Buttons;

import Graphics.Shape;
import Graphics.Sprite;
import Graphics.Text;
import System.Event;
import System.IO.AZERTYLayout;
import System.Keyboard;
import app.MainMENU;
import app.menu.Menu;
import util.ResourceHandler;

import java.io.IOException;

public abstract class TextField extends SpecialButton {
    protected Shape shape;
    private Sprite sprite;
    protected Text textZone;
    private Keyboard keyboard = new Keyboard(MainMENU.window);
    private String acceptedCharacters;

    public TextField(int x, int y, String acceptedCharacters) throws IOException {
        super("", Menu.newButtonSprite("menuSmall"));
        this.acceptedCharacters=acceptedCharacters;
        this.textZone = new Text(ResourceHandler.getFont("default"), "");
    }


    @Override
    protected void clickedIfReady() {
        while (!(keyboard.isKeyPressed(AZERTYLayout.PAD_RETURN.getKeyID())||keyboard.isKeyPressed(AZERTYLayout.RETURN.getKeyID()))){
            Event event;
     //       event.textEntered;
        }

    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
