package app.menu;

import Graphics.Vector2f;
import app.MainMENU;
import app.map.MapImpl;
import app.menu.Buttons.MapButton;
import app.menu.Buttons.SpecialButton;

import java.util.HashMap;


public class MapMenu extends Menu {
    public MapButton selectedMap;

    /**
     * Menu constructor
     *
     */
    public MapMenu() {
        super("Select Map", MainMENU.LOBBY, new Vector2f(20, 50 + MainMENU.HEIGHT / 10), new HashMap<>(), true);
        this.getButtons().add(new okMapButton(MainMENU.WIDTH - 40, MainMENU.HEIGHT - 40));

        // Init
        for (int i = 0; i < MainMENU.availableMaps.length; i++) {
            MapButton m = new MapButton((MapImpl) MainMENU.availableMaps[i], MainMENU.WIDTH / 10 + ((i % 5) * (30 + 170)), MainMENU.HEIGHT / 4 + (i / 5) * 130);
            this.getButtons().add(m);
            if (i == 0)
                selectedMap = m;
        }

    }


    private class okMapButton extends SpecialButton {
        protected okMapButton(float x, float y) {
            super("Ok", Menu.newButtonSprite("menuSmall"));
            setPosition(x, y);
        }

        @Override
        protected void clickedIfReady() {
            ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).setMap(selectedMap.map);
            MainMENU.menulist[MainMENU.LOBBY].update();
            MainMENU.currentMenu = MainMENU.LOBBY;
        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }
}
