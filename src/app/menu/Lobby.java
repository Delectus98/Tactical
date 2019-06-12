package app.menu;

import Graphics.ConstColor;
import Graphics.RectangleShape;
import Graphics.Vector2f;
import app.MainMENU;
import app.menu.SpecialButtons.LaunchGame;

import java.util.HashMap;

public class Lobby extends Menu {

    /**
     * Menu constructor
     *
     * @param width
     * @param height
     * @param lobbyType 0=Local, 1=Host, 2=Client
     */
    public Lobby(int width, int height, int buttonWidth, int buttonHeight, byte lobbyType) {
        super(width, height, "Game Lobby" + lobbyType, MainMENU.GAMEMODE + 2 * ((lobbyType + 1) / 2), buttonWidth, buttonHeight, new Vector2f(), new HashMap<>());
        //Ajoute le bouton de lancement de jeu
        this.getButtons().add(new LaunchGame(String.valueOf(lobbyType), new RectangleShape(width - buttonWidth, height - buttonHeight, buttonWidth, buttonHeight)));
        switch (lobbyType) {
            //TODO
        }
    }
}
