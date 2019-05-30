package app.actions;
import System.*;
import app.Game;

/**
 * Tool for running action handling
 */
public abstract class Action
{
    protected transient Game game;

    // définit un context de jeu pour pouvoir modifier certains paramètres

    /**
     * Initialise 'transient' attributes
     * Defines a game context (where the action is done).
     * Useful when Action is send to network to init 'transient' attributes.
     * @param gameContext context
     */
    public void build(Game gameContext)
    {
        this.game = gameContext;
    }

    /**
     * Gives the cost of the action
     * @return the cost of the action
     */
    public abstract int getCost(); // renvoie le cout de l'action en PA

    /**
     * Checks if the action is finished.
     * @return true if the action is finished.
     */
    public abstract boolean isFinished(); // l'action s'est bien déroulé et est maintenant terminée

    // met a jour la phase déroulement de l'action où le joueur regarde l'action

    /**
     * Updates action progress.
     * @param time elapsed time since last frame.
     */
    public abstract void update(ConstTime time);

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

}
