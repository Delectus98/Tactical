package app.menu.Buttons;

import app.MainMENU;
import app.menu.Menu;
import app.menu.OnlineLobby;
import util.TextInput;

import java.io.IOException;

import static app.MainMENU.*;

public class IPField extends SpecialButton {
    String tmp = "JOIN";
    ;

    public IPField(String title, int x, int y) {
        super(title, Menu.newButtonSprite("menuBig"));
        setPosition(x, y);
        this.textZone.setString(tmp);
       // this.textZone = new Text(ResourceHandler.getFont("default"), "Join");
    }


    @Override
    protected void clickedIfReady() {
//        if(parentMenu instanceof Lobby){
        //      ((Lobby)parentMenu).toUpdate = this;}
        //   else if(parentMenu instanceof OnlineMenu){
        //   ((OnlineMenu)parentMenu).toUpdate=this;
        //}
        MainMENU.menulist[MainMENU.ONLINE].toUpdate = this;

    }

    @Override
    public void checkIfButtonReady() {

        setReady(true);
    }

    @Override
    public void update() throws IOException, InterruptedException {
        if (isEditing) {
            if (TextInput.isTextEntered()) {
                tmp += TextInput.getChar();
            } else if (TextInput.isBackspaceEntered()) {
                if (tmp.length() > 0)
                    tmp = tmp.substring(0, tmp.length() - 1);
            } else if (TextInput.isReturnCarriageEntered()) {
                this.textZone.setString(tmp);
                MainMENU.menulist[MainMENU.ONLINE].toUpdate = null;
                isEditing = false;
                MainMENU.LOBBY = MainMENU.JOIN;
                menulist[MainMENU.JOIN] = new OnlineLobby(false);
                currentMenu = LOBBY;

            }
            this.textZone.setString(tmp);
        } else {
            tmp = "";
            isEditing = true;
        }


    }

    @Override
    public String getString() {
        return tmp;
    }
}

