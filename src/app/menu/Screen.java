package app.menu;

import System.Event;
import System.ConstTime;
import System.RenderTarget;

public abstract class Screen {
    public abstract void open(); // init and open the screen

    public abstract boolean isClosing(); // screen wants to be closed

    public abstract Screen close(); // close screen and return next screen

    public abstract void update(ConstTime elapsed); // update screen with elapsed time

    public abstract void handle(Event event); // handle window event as resizing, closing, etc..

    public abstract void draw(RenderTarget target); // render to target

    public abstract String toString(); //baddraw
}
