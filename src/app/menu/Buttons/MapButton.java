package app.menu.Buttons;


import Graphics.Color;
import Graphics.Sprite;
import app.MainMENU;
import app.map.Map;
import app.menu.MapMenu;
import app.menu.Menu;

public class MapButton extends SpecialButton {

    private Sprite sprite;
    public Map map;


    public MapButton(Sprite mapMiniature, Map map, float x, float y) {

        super("", Menu.newButtonSprite("menuBig"));
        setPosition(x,y);
        this.sprite = mapMiniature;
        this.map = map;

    }

    public void clickedIfReady() {
        ((MapMenu)MainMENU.menulist[MainMENU.MAPCHOICE]).selectedMap.getSprite().setFillColor(new Color(169,169,169));
        ((MapMenu)MainMENU.menulist[MainMENU.MAPCHOICE]).selectedMap=this;
        this.getSprite().setFillColor(Color.Red);
    }

    @Override
    public void checkIfButtonReady() {
        //setReady(true); //useless, true as created
    }
}
