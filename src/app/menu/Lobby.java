package app.menu;

import Graphics.Color;
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


    Map map= MainMENU.availableMaps[3];
    int mapIndex=3;
    Player[] players;
    private int squadCreationPoints=4;

    public Lobby(String title, int parentMenuId){
        super(title,parentMenuId, new Vector2f(),new HashMap<>(),true);
    }

    public void setMap(Map map) {
        this.map = map;
    }
    public void setMap(int index) {
        this.map = MainMENU.availableMaps[index];
    }

    public Map getMap() {
        return MainMENU.availableMaps[mapIndex];
    }

    public int getMapIndex() {
        return mapIndex;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getSquadCreationPoints() {
        return Math.min(5, Math.min(squadCreationPoints,getMap().getSpawnPoints(0).size()/2));//TODO change si bug corrigé
    }

    public void setSquadCreationPoints(int squadCreationPoints) {
        this.squadCreationPoints = squadCreationPoints;
    }


    public abstract Game getGame() throws IOException;

    protected class toMapButton extends SpecialButton {
        public toMapButton(Sprite sprite) {
            super("Chose Map", sprite);
            setPosition(MainMENU.WIDTH - getSprite().getBounds().w, 50 + MainMENU.HEIGHT / 10);
        }

        @Override
        protected void clickedIfReady() {
            for (MenuComponent m : MainMENU.menulist[MainMENU.MAPCHOICE].getButtons()) {
                if (m instanceof MapButton) {
                    // m.getSprite().setFillColor(new Color(169, 169, 169));
                    if (((MapButton) m).map == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap()) {
                        m.getSprite().setFillColor(new Color(255, 255, 153));
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
