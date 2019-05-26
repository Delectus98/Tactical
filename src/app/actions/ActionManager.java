package app.actions;

import System.*;
import app.Game;
import app.Unite;

/**
 * Tool for action preparation handling
 */
public abstract class ActionManager {

    protected Unite unite;
    protected Game game;

    public ActionManager(Unite user, Game game)
    {
        this.unite = user;
        this.game = game;
    }

    public abstract void updatePreparation(ConstTime time); // met a jour la phase préparation où le joueur choisit encore comment elle va se faire

    public abstract void drawAboveFloor(RenderTarget target);
    public abstract void drawAboveStruct(RenderTarget target);
    public abstract void drawAboveEntity(RenderTarget target);
    public abstract void drawAboveHUD(RenderTarget target);

    public abstract int getCost(); // renvoie le cout de l'action en PA

    public abstract boolean isAvailable(); // renvoie vrai si l'action est possible/disponible <=> renvoie vrai si la préparation est possible
    /**
     * Construit le déroulement d'une action
     * @return
     */
    public abstract Action build();
}
