package app.menu;

import Graphics.Vector2f;
import app.MainMENU;
import app.menu.Buttons.MapButton;
import app.menu.Buttons.SpecialButton;
import app.network.MapPacket;

import java.io.IOException;
import java.util.HashMap;

import static app.MainMENU.LOBBY;
import static app.MainMENU.menulist;


public class MapMenu extends Menu {
    public MapButton selectedMap;

    /**
     * Menu constructor
     *
     */
    MapMenu() {
        super("Select Map", MainMENU.LOBBY, new Vector2f(20, 50 + MainMENU.HEIGHT / 10), new HashMap<>(), true);
        this.getButtons().add(new okMapButton(MainMENU.WIDTH - 40, MainMENU.HEIGHT - 40));

        // Init
        for (int i = 0; i < MainMENU.availableMaps.length; i++) {
            MapButton m = new MapButton(i, MainMENU.WIDTH / 10 + ((i % 5) * (30 + 170)), MainMENU.HEIGHT / 4 + (i / 5) * 130);
            this.getButtons().add(m);
            m.index = i;
            if (i == 0)
                selectedMap = m;

        }

    }


    private class okMapButton extends SpecialButton {
        okMapButton(float x, float y) {
            super("Ok", Menu.newButtonSprite("menuSmall"));
            setPosition(x, y);
        }

        @Override
        protected void clickedIfReady() throws IOException, InterruptedException {
            ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).setMap(selectedMap.index);
            menulist[LOBBY].update();
            MainMENU.currentMenu = MainMENU.LOBBY;
            if(MainMENU.LOBBY==MainMENU.HOST){
                MapPacket m = new MapPacket();
                m.index=selectedMap.index;
               ((OnlineLobby)MainMENU.menulist[MainMENU.HOST]).addToSend(m);
            }
        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }
}
