package app.menu.Buttons;


import Graphics.RectangleShape;
import app.MainMENU;

public class ToLobbyButton extends NormalButton {
    byte lobbyType; //lobbyType 0=Local, 1=Host, 2=Client

    public ToLobbyButton(String title, byte lobbyType) {
        super(title, MainMENU.LOBBY, new RectangleShape());
        this.lobbyType = lobbyType;

    }

    @Override
    public void clicked() {

        switch (lobbyType) {
            case 0:

                MainMENU.LOBBY = MainMENU.LOCAL;
                break;
            case 1:

                MainMENU.LOBBY = MainMENU.HOST;
                break;
            case 2:

                MainMENU.LOBBY = MainMENU.JOIN;
                break;
        }

        MainMENU.currentMenu = MainMENU.LOBBY;
//        System.out.println("Tolobby"+MainMENU.LOBBY);
    }
}
