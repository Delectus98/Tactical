package app;

import Graphics.Vector2i;
import System.ConstTime;
import System.Event;
import System.GLFWWindow;
import System.RenderTarget;
import app.map.Map;

import java.util.Set;

public abstract class Game
{
    protected GLFWWindow context;
    protected int currentPlayer; //1 ou 0
    protected Player[] players;
    protected Map map;
    protected Unite selected;
    private int squadCreationPoints=5;//TODO à changer après manip dans lobby

    /**
     * Finish the current turn to let the next player play.
     */
    public abstract void endTurn(); //change le joueur

    /**
     * Checks if the game is over.
     * @return true if the game is over.
     */
    public abstract boolean isFinished(); //return true si un joueur n'as plus d'unites

    /**
     * Init params and start the game.
     */
    public abstract void start();

    /**
     * Draws all graphic component of the game on window.
     * @param target render target
     */
    public abstract void draw(RenderTarget target); //si initialisation (placement unites debut)

    /**
     * Handles window event (as resizing, key released, etc..)
     * @param event window event
     */
    public abstract void handle(Event event);

    /**
     * Updates game using main loop elapsed time.
     * @param time elapsed time
     */
    public abstract void update(ConstTime time);

    /**
     * Gives the map used by the game.
     * @return the map used by the game.
     */
    public final Map getMap()
    {
        return map;
    }

    /**
     * Gives the current visibles tiles from the current player.
     * Useful for external classes as Shooting or Moving.
     * @return the current visible tiles from the current player
     */
    public abstract Set<Vector2i> getCurrentVisibles();

    /**
     * Updates the Fog of War of unites.
     * Useful for external classes as Moving
     */
    public abstract void updateFOG();

    /**
     * Gives all players who are playing
     * @return all players who are playing
     */
    public final Player[] getPlayers(){
        return players;
    }

    public void setMap(Map map) {
        this.map=map;
    }

    public int getSquadCreationPoints() {
        return squadCreationPoints;
    }

    public void setSquadCreationPoints(int squadCreationPoints) {
        this.squadCreationPoints = squadCreationPoints;
    }
}
