package app.menu;

import Graphics.Vector2f;
import app.Game;
import app.MainMENU;
import app.Player;
import app.map.Map;

import java.io.IOException;
import java.util.HashMap;

public abstract class Lobby extends Menu {
    Map map= MainMENU.availableMaps[3];
    Player[] players;
    private int squadCreationPoints=4;

    public Lobby(String title, int parentMenuId){
        super(title,parentMenuId, new Vector2f(),new HashMap<>(),true);
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
        return Math.min(5, Math.min(squadCreationPoints,getMap().getSpawnPoints(0).size()/2));//TODO change si bug corrigé
    }

    public void setSquadCreationPoints(int squadCreationPoints) {
        this.squadCreationPoints = squadCreationPoints;
    }


    public abstract Game getGame() throws IOException;
}
