package app.menu;

import Graphics.Vector2f;
import System.GLFWWindow;
import app.Player;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;

import java.util.HashMap;

public abstract class Lobby extends Menu {
    Map map=new MapImpl(MapList.DemoField);
    Player[] players;
    private int squadCreationPoints=3;

    public Lobby(GLFWWindow window, String title, int parentMenuId, Vector2f normalButtonOrigin, Vector2f specialButtonOrigin, Vector2f titleposition, boolean backbutton) {
        super(window, title, parentMenuId, normalButtonOrigin, specialButtonOrigin, titleposition, backbutton);
    }
    public Lobby(int width, int height, String title, int parentMenuId, int buttonWidth, int buttonHeight, Vector2f buttonOrigin, HashMap<String, Integer> correspondances, boolean backbutton){
        super(width,height,title,parentMenuId,buttonWidth,buttonHeight,buttonOrigin,correspondances,backbutton);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public int getSquadCreationPoints() {
        return squadCreationPoints;
    }

    public void setSquadCreationPoints(int squadCreationPoints) {
        this.squadCreationPoints = squadCreationPoints;
    }
}
