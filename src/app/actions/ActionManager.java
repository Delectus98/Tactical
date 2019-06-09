package app.actions;

import System.*;
import app.Game;
import app.Player;
import app.Unite;

/**
 * Tool for action preparation handling
 */
public abstract class ActionManager {

    protected Unite unite;
    protected Player player;
    protected Game game;

    /**
     * Creates an Action Manager
     * @param user launcher
     * @param game context
     */
    public ActionManager(Player player, Unite user, Game game)
    {
        this.player = player;
        this.unite = user;
        this.game = game;
    }

    /**
     * Updates preparation
     * @param time elapsed time since last frame
     */
    public abstract void updatePreparation(ConstTime time); // met a jour la phase préparation où le joueur choisit encore comment elle va se faire

    /**
     * Draw stuff above floor (on map)
     * @param target render target
     */
    public abstract void drawAboveFloor(RenderTarget target);
    /**
     * Draw stuff above struct (on map)
     * @param target render target
     */
    public abstract void drawAboveStruct(RenderTarget target);
    /**
     * Draw stuff above entity (on map)
     * @param target render target
     */
    public abstract void drawAboveEntity(RenderTarget target);
    /**
     * Draw stuff above hud (on hud)
     * @param target render target
     */
    public abstract void drawAboveHUD(RenderTarget target);

    /**
     * Gives the cost of the action
     * @return the cost of the action
     */
    public abstract int getCost(); // renvoie le cout de l'action en PA

    /**
     * Checks if the preparation is finished and the built action possible
     * @return true if the preparation is finished and the built action possible else false.
     */
    public abstract boolean isAvailable(); // renvoie vrai si l'action est possible/disponible <=> renvoie vrai si la préparation est possible
    /**
     * Build an action corresponding to action manager
     * @return an action corresponding to action manager
     */
    public abstract Action build();
}
