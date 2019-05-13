package app;

import app.map.Map;
import com.sun.tools.javac.util.Pair;

import System.*;

import java.util.Set;

abstract class Game
{
    protected int currentPlayer; //1 ou 0
    protected Player[] players;
    protected Map map;
    protected Set<Pair<Integer, Integer>>[] visibles;

    public abstract void endTurn(); //change le joueur

    public abstract boolean isFinished(); //return true si un joueur n'as plus d'unites

    public Set<Pair<Integer, Integer>> getCurrentVisibles()
    {
        return visibles[currentPlayer];
    }

    abstract void draw(RenderTarget target);

}
