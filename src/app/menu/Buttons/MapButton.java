package app.menu.Buttons;


import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Sprite;
import app.MainMENU;
import app.map.Map;
import app.menu.MapMenu;
import app.menu.Menu;

public class MapButton extends SpecialButton {

    private Sprite mapMiniature;
    public Map map;


    public MapButton(MapMenu makeMap, Sprite mapMiniature, Map map, float x, float y) {

        super("", new RectangleShape(x, y, 170, 100));
        this.mapMiniature = mapMiniature;
        shape.setFillColor(Color.Red);
        this.map = map;

    }

    public void clickedIfReady() {
        ((MapMenu)MainMENU.menulist[MainMENU.MAPCHOICE]).selectedMap.shape.setFillColor(Color.Red);
        ((MapMenu)MainMENU.menulist[MainMENU.MAPCHOICE]).selectedMap=this;
        this.shape.setFillColor(Color.Yellow);
    }

    @Override
    public void checkIfButtonReady() {
        //setReady(true); //useless, true as created
    }
}
