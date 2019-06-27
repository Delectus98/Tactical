package app.menu;

import Graphics.Vector2f;
import app.MainMENU;
import app.menu.Buttons.IPField;
import app.menu.Buttons.ToLobbyButton;

import java.io.IOException;
import java.util.HashMap;

public class OnlineMenu extends Menu {


    public OnlineMenu(HashMap<String, Integer> correspondance) {
        super("Online", MainMENU.GAMEMODE, new Vector2f(MainMENU.WIDTH / 2, 100), correspondance, true);
        ToLobbyButton host = new ToLobbyButton("Host game", (byte) 1);
        host.setPosition(getButtons().get(0).getSprite().getX(), getButtons().get(0).getSprite().getY());
        getButtons().set(0, host);

//JOIN
       /* ToLobbyButton join = new ToLobbyButton("Join game", (byte) 2);
        join.setPosition(getButtons().get(1).getSprite().getX(), getButtons().get(1).getSprite().getY());
        getButtons().set(1, join);*/

        IPField upfield = new IPField("Join", 0, 0);
        upfield.setPosition(getButtons().get(0).getSprite().getBounds().l, getButtons().get(0).getSprite().getBounds().t + 60);
        getButtons().add(1,upfield);
    }

    protected String getIP() {
        return getButtons().get(1).getString();
    }

    @Override
    public void update() throws IOException, InterruptedException {
        if(MainMENU.menulist[MainMENU.ONLINE].toUpdate!=null) {
            MainMENU.menulist[MainMENU.ONLINE].toUpdate.update();
        }
    }
}
