package app.menu;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Vector2f;
import app.MainMENU;
import app.Player;
import app.Team;
import app.map.MapImpl;
import app.map.MapList;
import app.menu.Buttons.*;
import app.play.LocalhostGame;

import java.io.IOException;
import java.util.HashMap;

public class LocalLobby extends Lobby {
    private Player[] playerlist = new Player[2];
    // protected static Map map = new MapImpl(MapList.Example1); //default

    /**
     * Menu constructor
     *
     * @param width
     * @param height
     */
    public LocalLobby(int width, int height, int buttonWidth, int buttonHeight) throws IOException {
        super(width, height, "Game Local: Lobby", MainMENU.GAMEMODE, buttonWidth, buttonHeight, new Vector2f(), new HashMap<>(), true);
        //Ajoute le bouton de lancement de jeu
        this.getButtons().add(new ReadyButton(new RectangleShape(width - buttonWidth, height - buttonHeight, buttonWidth, buttonHeight)));
        this.getButtons().add(new toMapButton());


        playerlist[0] = new Player("Player1");
        playerlist[0].setTeam(Team.MAN);
        playerlist[1] = new Player("Player2");
        playerlist[1].setTeam(Team.APE);
        for (int i = 0; i < playerlist.length; i++) {
            SquadButton b = new SquadButton(playerlist[i], width, height);
            b.setPosition(20, 50 + height / 10 + b.getShape().getBounds().l + i * (15 + b.shape.getBounds().h + b.getShape().getBounds().l));
            getButtons().add(b);

            RenamePlayer rename = new RenamePlayer(playerlist[i]);
            rename.setPosition(b.shape.getBounds().l + b.getShape().getBounds().w + 15, b.shape.getBounds().h + b.getShape().getBounds().l + i * (15 + b.shape.getBounds().h + b.getShape().getBounds().l));
            getButtons().add(rename);

//TODO CHANGER OU BOUTON CHOIX TEAM
        }
        MainMENU.currentGame = new LocalhostGame(MainMENU.window, playerlist[0], playerlist[1], new MapImpl(MapList.Example1));

    }


    private class toMapButton extends SpecialButton {
        public toMapButton() {
            super("Chose Map", new RectangleShape(9 * MainMENU.window.getDimension().x / 10, 50 + MainMENU.window.getDimension().y / 10, 160, 90));

        }

        @Override
        protected void clickedIfReady() {
            /*WARNING, REPETITION DE LA COLORATION
            Ici, afin que lorsqu'on arrive sur le menu, le selectionné soit coloré.
            dans MapButton, pour qu'il se colore bien par rapport au clic

             */
            for (MenuComponent m : MainMENU.menulist[MainMENU.MAPCHOICE].getButtons()) {
                if (m instanceof MapButton) {
                    m.shape.setFillColor(Color.Red);
                    if (((MapButton) m).map == MainMENU.currentGame.getMap()) {
                        m.shape.setFillColor(Color.Yellow);
                    }
                }
            }
            MainMENU.currentMenu = MainMENU.MAPCHOICE;
        }

        @Override
        public void checkIfButtonReady() {
            setReady((MainMENU.LOBBY == MainMENU.LOCAL) || MainMENU.LOBBY == MainMENU.HOST);
        }
    }


}
