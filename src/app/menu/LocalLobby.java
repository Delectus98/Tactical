package app.menu;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Vector2f;
import app.MainMENU;
import app.Player;
import app.Team;
import app.menu.Buttons.*;

import java.io.IOException;
import java.util.HashMap;

public class LocalLobby extends Lobby {
    private Player[] playerlist = new Player[2];



    /**
     * Menu constructor
     *
     * @param width
     * @param height
     */
    public LocalLobby(int width, int height, int buttonWidth, int buttonHeight) throws IOException {
        super(width, height, "Game Local: Lobby", MainMENU.GAMEMODE, buttonWidth, buttonHeight, new Vector2f(), new HashMap<>(), true);
        //Ajoute le bouton de lancement de jeu
        this.getButtons().add(new ReadyButton(new RectangleShape(MainMENU.WIDTH - buttonWidth, MainMENU.HEIGHT - buttonHeight, buttonWidth, buttonHeight)));
        this.getButtons().add(new toMapButton());


        playerlist[0] = new Player("Player1");
        playerlist[0].setTeam(Team.MAN);
        playerlist[1] = new Player("Player2");
        playerlist[1].setTeam(Team.APE);
        setPlayers(playerlist);
        for (int i = 0; i < getPlayers().length; i++) {
            SquadButton b = new SquadButton(getPlayers()[i], MainMENU.WIDTH, MainMENU.HEIGHT);
            b.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b.getShape().getBounds().l + i * (15 + b.shape.getBounds().h + b.getShape().getBounds().l));
            getButtons().add(b);

            RenamePlayer rename = new RenamePlayer(getPlayers()[i]);
            rename.setPosition(b.getShape().getBounds().l + 15+b.shape.getBounds().w, b.shape.getBounds().t);
            getButtons().add(rename);

            TeamButton t = new TeamButton(getPlayers()[i],new RectangleShape(rename.shape.getBounds().l, rename.shape.getBounds().t+rename.shape.getBounds().h+10, rename.shape.getBounds().w, rename.shape.getBounds().h));
            getButtons().add(t);

//TODO CHANGER OU BOUTON CHOIX TEAM
        }
     //   MainMENU.currentGame = new LocalhostGame(MainMENU.window, playerlist[0], playerlist[1], );

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
                    if (((MapButton) m).map == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap()) {
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
