package app.actions;
import System.*;
import app.Game;
import app.Unite;

public abstract class Action
{
    Unite unite;
    Game game;
    //Position depart/arrivee
    boolean inAction;

    abstract void update(ConstTime time);

    abstract boolean isFinished();

    abstract void draw(RenderTarget target);
}
