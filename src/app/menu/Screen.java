package app.menu;

import System.Event;
import System.ConstTime;
import System.RenderTarget;

/**
 * Interface used to manage screens
 */
public abstract class Screen {
    public abstract void open(); // init and open the screen

    /**
     * Checks if this screen wants to be closes
     * @return true if this screen wants to be closes else false
     */
    public abstract boolean isClosing(); // screen wants to be closed

    /**
     * Close the screen and return the next screen to be manage
     * @return next screen
     */
    public abstract Screen close(); // close screen and return next screen

    /**
     * Updates screen using elapsed time
     * @param elapsed time
     */
    public abstract void update(ConstTime elapsed); // update screen with elapsed time

    /**
     * Updates screen using Window Event
     * @param event window event
     */
    public abstract void handle(Event event); // handle window event as resizing, closing, etc..

    /**
     * Draw all graphics component to a render target
     * @param target render target (where graphics will be drawn)
     */
    public abstract void draw(RenderTarget target); // render to target
}
