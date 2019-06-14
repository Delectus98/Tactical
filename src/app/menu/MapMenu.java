package app.menu;

import Graphics.RectangleShape;
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
        super(width, height, "Select Map", MainMENU.LOBBY, 90, 30, new Vector2f(20, 50 + height / 10), new HashMap<>(),true);
        this.getButtons().add(new okMapButton(new RectangleShape(width - 20, height - 100, 40, 40)));

        // Init
        for (int i=0;i< MainMENU.availableMaps.length;i++) {
            MapButton m =new MapButton(this,null, MainMENU.availableMaps[i], (width/4)+ (this.getButtons().size()%2)*(2*170+40),(this.getButtons().size()/2)*(2*100+40));
            this.getButtons().add(m);
            if(i==0)
                selectedMap=m;
        }

    }


    private class okMapButton extends SpecialButton {
        protected okMapButton( RectangleShape rectangleShape) {
            super("Ok",rectangleShape);

        }

        @Override
        protected void clickedIfReady() {
            MainMENU.currentGame.setMap(selectedMap.map);
            MainMENU.currentMenu=MainMENU.LOBBY;
        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }
}
