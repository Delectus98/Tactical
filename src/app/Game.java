package app;

import app.map.Map;
import com.sun.tools.javac.util.Pair;

import System.*;

import java.util.Set;

public abstract class Game
{
    protected int currentPlayer; //1 ou 0
    protected Player[] players;
    protected Map map;
    protected Set<Pair<Integer, Integer>>[] visibles;
    protected Unite selected;
    protected boolean initialisation;

    public abstract void endTurn(); //change le joueur

    public abstract boolean isFinished(); //return true si un joueur n'as plus d'unites

    public Set<Pair<Integer, Integer>> getCurrentVisibles()
    {
        return visibles[currentPlayer];
    }

    public abstract void draw(RenderTarget target); //si initialisation (placement unites debut)

    public abstract void update(ConstTime time);

    public final Map getMap()
    {
        return map;
    }

    public final Player[] getPlayers(){
        return players;
    }

    public abstract void baddraw();
}
