package app.menu;

import Graphics.Vector2f;
import app.MainMENU;
import app.menu.Buttons.MapButton;
import app.menu.Buttons.SpecialButton;

import java.util.HashMap;


public class MapMenu extends Menu {
    public MapButton selectedMap;

    /**
     * Menu constructor
     *
     * @param width
     * @param height
     */
    public MapMenu(int width, int height) {
        super(width, height, "Select Map", MainMENU.LOBBY, 90, 30, new Vector2f(20, 50 + height / 10), new HashMap<>(), true);
        this.getButtons().add(new okMapButton(width - 40, height - 40));

        // Init
        for (int i = 0; i < MainMENU.availableMaps.length; i++) {
            MapButton m = new MapButton(null, MainMENU.availableMaps[i], MainMENU.WIDTH / 10 + ((i % 5) * (30 + 170)), MainMENU.HEIGHT / 4 + (i / 5) * 130);
            this.getButtons().add(m);
            if (i == 0)
                selectedMap = m;
        }

    }


    private class okMapButton extends SpecialButton {
        protected okMapButton(float x, float y) {
            super("Ok", Menu.newButtonSprite("menuSmall"));
            setPosition(x,y);

        }

        @Override
        protected void clickedIfReady() {
            ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).setMap(selectedMap.map);
            MainMENU.currentMenu = MainMENU.LOBBY;
        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }
}
