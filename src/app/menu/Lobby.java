package app.menu;

import Graphics.Vector2f;
import System.GLFWWindow;

import java.util.HashMap;

public abstract class Lobby extends Menu {
    public Lobby(GLFWWindow window, String title, int parentMenuId, Vector2f normalButtonOrigin, Vector2f specialButtonOrigin, Vector2f titleposition, boolean backbutton) {
        super(window, title, parentMenuId, normalButtonOrigin, specialButtonOrigin, titleposition, backbutton);
    }
    public Lobby(int width, int height, String title, int parentMenuId, int buttonWidth, int buttonHeight, Vector2f buttonOrigin, HashMap<String, Integer> correspondances, boolean backbutton){
        super(width,height,title,parentMenuId,buttonWidth,buttonHeight,buttonOrigin,correspondances,backbutton);
    }
}
