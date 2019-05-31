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
    private Camera2D hud;
    private Viewport viewport;

    private Keyboard keyboard;
    private Mouse mouse;
    private boolean leftReleased;
    private boolean rightReleased;

    public GameInput(Camera2D cam, Camera2D hud, Viewport viewport, Mouse mouse, Keyboard keyboard)
    {
        this.cam = cam;
        this.hud = hud;
        this.viewport = viewport;
        this.mouse = mouse;
        this.keyboard = keyboard;
    }

    // keyboard methods

    /**
     * Gives keyboard interface to manage key input.
     * @return keyboard interface
     */
    public Keyboard getKeyboard()
    {
        return keyboard;
    }

    // mouse methods
    /**
     * Checks if left mouse is clicked.
     * @return true if left mouse is clicked else false.
     */
    public boolean isLeftPressed()
    {
        return mouse.isButtonPressed(Mouse.Button.Left);
    }

    /**
     * Checks if right mouse is clicked.
     * @return true if right mouse is clicked else false.
     */
    public boolean isRightPressed()
    {
        return mouse.isButtonPressed(Mouse.Button.Right);
    }

    /**
     * Checks if left mouse is clicked.
     * @return true if left mouse is clicked else false.
     */
    public boolean isLeftReleased()
    {
        return leftReleased;
    }

    /**
     * Checks if right mouse is clicked.
     * @return true if right mouse is clicked else false.
     */
    public boolean isRightReleased()
    {
        return rightReleased;
    }


    /**
     * Gives the mouse position relative to game screen.
     * @return the mouse position relative to game screen.
     */
    public Vector2f getMousePosition() {
        return mouse.getRelativePosition();
    }

    /**
     * Gives the mouse position relative to map camera and viewport.
     * @return the mouse position relative to map camera and viewport.
     */
    public Vector2f getMousePositionOnMap()
    {
        return WindowUtils.mapCoordToPixel(mouse.getRelativePosition(), viewport, cam);
    }

    /**
     * Gives the mouse position relative to hud camera and viewport.
     * @return the mouse position relative to hud camera and viewport.
     */
    public Vector2f getMousePositionOnHUD()
    {
        return WindowUtils.mapCoordToPixel(mouse.getRelativePosition(), viewport, hud);
    }

    // window methods

    /**
     * Gives the area on screen where the current player game is rendering
     * @return the area on screen where the current player game is rendering
     * @see GameInput#getMousePosition() to checks if mouse position is contained by the area
     */
    public FloatRect getFrameRectangle()
    {
        return new FloatRect(viewport.getTopLeftCorner().x,  viewport.getTopLeftCorner().y, viewport.getDimension().x, viewport.getDimension().y);
    }


    public void reset(){
        leftReleased = false;
        rightReleased = false;
    }

    public void update(Event event) {
        if (event.type == Event.Type.BUTTONRELEASED) {
            if (Mouse.Button.Left.getButtonID() == event.keyReleased) {
                leftReleased = true;
            } else if (Mouse.Button.Left.getButtonID() == event.keyReleased) {
                rightReleased = true;
            }
        }
    }
}
