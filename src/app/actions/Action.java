package app.actions;
import System.*;

/**
 * Tool for running action handling
 */
public abstract class Action
{
    public abstract int getCost(); // renvoie le cout de l'action en PA

    public abstract boolean isFinished(); // l'action s'est bien déroulé et est maintenant terminée

    // met a jour la phase déroulement de l'action où le joueur regarde l'action
    public abstract void update(ConstTime time);

    public abstract void drawAboveFloor(RenderTarget target);
    public abstract void drawAboveStruct(RenderTarget target);
    public abstract void drawAboveEntity(RenderTarget target);
    public abstract void drawAboveHUD(RenderTarget target);

}
