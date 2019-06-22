package app.menu;

import Graphics.Vector2f;
import System.GLFWWindow;

import java.io.IOException;

public class OnlineLobby extends Lobby {

    int myPlayerId;

    public OnlineLobby(GLFWWindow window, String title, int parentMenuId, Vector2f normalButtonOrigin, Vector2f specialButtonOrigin, Vector2f titleposition, boolean backbutton) throws IOException {
        super(window, title, parentMenuId, normalButtonOrigin, specialButtonOrigin, titleposition, backbutton);
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }
}
