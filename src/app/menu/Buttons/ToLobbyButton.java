package app.menu.Buttons;


import app.MainMENU;
import app.menu.LocalLobby;
import app.menu.Menu;
import app.menu.OnlineLobby;

import java.io.IOException;

import static app.MainMENU.menulist;

public class ToLobbyButton extends NormalButton {
    byte lobbyType; //lobbyType 0=Local, 1=Host, 2=Client

    public ToLobbyButton(String title, byte lobbyType) {
        super(title, MainMENU.LOBBY, Menu.newButtonSprite("menuSmall"));
        this.lobbyType = lobbyType;

    }

    @Override
    public void clicked() throws IOException {

        switch (lobbyType) {
            case 0:

                MainMENU.LOBBY = MainMENU.LOCAL;
                menulist[MainMENU.LOCAL] = new LocalLobby();
                break;
            case 1:

                MainMENU.LOBBY = MainMENU.HOST;
                menulist[MainMENU.HOST] = new OnlineLobby(true);
                break;
            case 2:

                MainMENU.LOBBY = MainMENU.JOIN;
                menulist[MainMENU.JOIN] = new OnlineLobby(false);
                break;
        }

        MainMENU.currentMenu = MainMENU.LOBBY;
    }
}
