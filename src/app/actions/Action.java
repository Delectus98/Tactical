package app.actions;
import System.*;
import app.Game;
import app.Unite;

public abstract class Action
{
    protected Unite unite;
    protected Game game;
    protected boolean inAction;


    public Action(Unite user, Game game){
        this.unite = user;
        this.game = game;
        this.inAction = false;
    }

    //Position depart/arrivee

    public abstract void update(ConstTime time);

    public abstract boolean isFinished();

    public abstract void draw(RenderTarget target);
}
