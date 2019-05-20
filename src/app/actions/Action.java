package app.actions;
import System.*;
import app.Game;
import app.Unite;

public abstract class Action
{
    protected Unite unite;
    protected Game game;
    private boolean inAction;

    public Action(Unite user, Game game){
        this.unite = user;
        this.game = game;
        this.inAction = false;
    }

    public abstract int getCost(); // renvoie le cout de l'action en PA

    public abstract boolean isAvailable(); // renvoie vrai si l'action est possible/disponible <=> renvoie vrai si la préparation est possible

    protected abstract void updatePreparation(ConstTime time); // met a jour la phase préparation où le joueur choisit encore comment elle va se faire

    protected abstract void updateProcess(ConstTime time); // met a jour la phase déroulement de l'action où le joueur regarde l'action

    public final void update(ConstTime time) {
        if (inAction) updateProcess(time);
        else updatePreparation(time);
    } // met a jour la phase préparation lorsque le joueur choisit puis déroulement lorsque l'action est déclanchée

    public final void trigger() { inAction = true; } // déclanche le déroulement de l'action

    public abstract boolean isFinished(); // l'action s'est bien déroulé et est maintenant terminée

    public abstract void drawPreparation(RenderTarget target);

    public abstract void drawProgress(RenderTarget target);

    public final void draw(RenderTarget target) {
        if (inAction) drawProgress(target);
        else drawPreparation(target);
    }
}
