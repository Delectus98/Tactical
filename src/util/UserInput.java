package util;

import Graphics.Vector2f;
import System.*;

public class UserInput
{
    private Keyboard keyboard;
    private Vector2f mousePosition;

    private boolean leftPressed;
    private boolean rightPressed;

    public Keyboard getKeyboard()
    {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard)
    {
        this.keyboard = keyboard;
    }


    public boolean isLeftPressed()
    {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed)
    {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed()
    {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed)
    {
        this.rightPressed = rightPressed;
    }

    public Vector2f getMousePosition()
    {
        return mousePosition;
    }

    public void setMousePosition(Vector2f mousePosition)
    {
        this.mousePosition = mousePosition;
    }
}
