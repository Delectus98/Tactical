package app;

import Graphics.Vector2i;
import app.map.Map;

import System.*;

import java.util.Set;

public abstract class Game
{
    protected GLFWWindow context;
    protected int currentPlayer; //1 ou 0
    protected Player[] players;
    protected Map map;
    protected Set<Vector2i>[] visibles;
    protected Unite selected;
    protected boolean initialisation;

    public abstract void endTurn(); //change le joueur

    public abstract boolean isFinished(); //return true si un joueur n'as plus d'unites

    public Set<Vector2i> getCurrentVisibles()
    {
        return visibles[currentPlayer];
    }

    public abstract void draw(RenderTarget target); //si initialisation (placement unites debut)

    public abstract void handle(Event event);

    public abstract void update(ConstTime time);

    public final Map getMap()
    {
        return map;
    }

    public final Player[] getPlayers(){
        return players;
    }
}
