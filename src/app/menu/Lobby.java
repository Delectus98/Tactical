package app.menu;

import Graphics.Sprite;
import Graphics.Vector2f;
import app.Game;
import app.MainMENU;
import app.Player;
import app.map.Map;
import app.menu.Buttons.MapButton;
import app.menu.Buttons.MenuComponent;
import app.menu.Buttons.SpecialButton;

import java.io.IOException;
import java.util.HashMap;

public abstract class Lobby extends Menu {


    private Map map= MainMENU.availableMaps[3];
    private int mapIndex=3;
    Player[] players;
    private int squadCreationPoints=5;

    Lobby(String title, int parentMenuId){
        super(title,parentMenuId, new Vector2f(),new HashMap<>(),true);
    }

   /* public void setMap(Map map) {
        this.map = map;
    }*/
    public void setMap(int index) {
mapIndex = index;
        this.map = MainMENU.availableMaps[index];
    }

    public Map getMap() {
        return MainMENU.availableMaps[mapIndex];
    }

    int getMapIndex() {
        return mapIndex;
    }

    public Player[] getPlayers() {
        return players;
    }

    void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getSquadCreationPoints() {
        return Math.min(5, Math.min(squadCreationPoints,getMap().getSpawnPoints(0).size()/2));//TODO change si bug corrigé
    }


    public abstract Game getGame() throws IOException;

    protected class toMapButton extends SpecialButton {
        toMapButton(Sprite sprite) {
            super("Chose Map", sprite);
            setPosition(MainMENU.WIDTH - getSprite().getBounds().w, 50 + MainMENU.HEIGHT / 10);
        }

        @Override
        protected void clickedIfReady() {
            for (MenuComponent m : MainMENU.menulist[MainMENU.MAPCHOICE].getButtons()) {
                if (m instanceof MapButton) {
                    // m.getSprite().setFillColor(new Color(169, 169, 169));
                    if (((MapButton) m).map == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap()) {
                       // m.getSprite().setFillColor(new Color(1f, 1f, 1f));
                    }
                }
            }
            MainMENU.currentMenu = MainMENU.MAPCHOICE;

        }

        @Override
        public void checkIfButtonReady() {
            setReady((MainMENU.LOBBY == MainMENU.LOCAL) || MainMENU.LOBBY == MainMENU.HOST);
        }
    }
}
