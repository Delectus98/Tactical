package app.menu.Buttons;


import Graphics.Color;
import Graphics.Sprite;
import app.MainMENU;
import app.map.Map;
import app.map.MapImpl;
import app.menu.MapMenu;

public class MapButton extends SpecialButton {

    public Map map;
    public int index;


    public MapButton( int index, float x, float y) {

        super("", new Sprite(((MapImpl)MainMENU.availableMaps[index]).getMiniature())) ;
        setPosition(x,y);
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
