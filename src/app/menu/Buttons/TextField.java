package app.menu.Buttons;

import Graphics.Text;
import System.Event;
import System.IO.AZERTYLayout;
import System.Keyboard;
import app.MainMENU;
import app.menu.Menu;
import util.ResourceHandler;
import util.TextInput;

public abstract class TextField extends SpecialButton {


    protected Text textZone;
    private Keyboard keyboard = new Keyboard(MainMENU.window);
    private String acceptedCharacters;

    public TextField(int x, int y, String acceptedCharacters) {
        super("here", Menu.newButtonSprite("menuSmall"));
        setPosition(x,y);
        this.acceptedCharacters=acceptedCharacters;
        this.textZone = new Text(ResourceHandler.getFont("default"), "");
    }


    @Override
    protected void clickedIfReady() {
        Event event;
       TextInput tx = new TextInput();
       textZone.setString("");
        while (!keyboard.isKeyPressed(AZERTYLayout.RETURN.getKeyID())) {
            if(tx.hasBeenUpdated())
                if(keyboard.isKeyPressed(AZERTYLayout.PAD_RETURN.getKeyID())){
            textZone.setString(textZone.getString().substring(0,textZone.getString().length()-2));}//!!null
            else{
                textZone.setString(textZone.getString()+tx.getChar());
                tx.reset();
                }
        }

    }

    @Override
    public String getString() {
        return super.getString();
    }
}
