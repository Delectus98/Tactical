package util;

import Graphics.FloatRect;
import Graphics.Vector2f;
import System.*;

/**
 * Classe permettant d'obtenir des informations sur les inputs fenêtre selon les parametres d'un seul joueur.
 * La classe adapte la position de la souris selon le viewport et la camera.
 * La classe adapte la dimension du jeu selon le viewport.
 */
public class GameInput
{
    private Camera2D cam;
    private Viewport viewport;

    private Keyboard keyboard;
    private Mouse mouse;

    private GameInput(Camera2D cam, Viewport viewport, Mouse mouse, Keyboard keyboard)
    {
        this.cam = cam;
        this.viewport = viewport;
        this.mouse = mouse;
        this.keyboard = keyboard;
    }

    // keyboard methods
    public Keyboard getKeyboard()
    {
        return keyboard;
    }

    // mouse methods
    public boolean isLeftPressed()
    {
        return mouse.isButtonPressed(Mouse.Button.Left);
    }

    public boolean isRightPressed()
    {
        return mouse.isButtonPressed(Mouse.Button.Right);
    }

    public Vector2f getMousePosition()
    {
        return WindowUtils.mapCoordToPixel(mouse.getRelativePosition(), viewport, cam);
    }

    // window methods
    public FloatRect getFrameRectangle()
    {
        return new FloatRect(viewport.getTopLeftCorner().x,  viewport.getTopLeftCorner().y, viewport.getDimension().x, viewport.getDimension().y);
    }

}
