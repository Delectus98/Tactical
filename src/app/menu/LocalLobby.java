package app.menu;

import Graphics.Color;
import Graphics.Sprite;
import Graphics.Vector2f;
import app.MainMENU;
import app.Player;
import app.Team;
import app.menu.Buttons.*;

import java.util.HashMap;

public class LocalLobby extends Lobby {
    private Player[] playerlist = new Player[2];


    /**
     * Menu constructor
     *
     * @param width
     * @param height
     */
    public LocalLobby(int width, int height, int buttonWidth, int buttonHeight) {
        super(width, height, "Game Local: Lobby", MainMENU.GAMEMODE, buttonWidth, buttonHeight, new Vector2f(), new HashMap<>(), true);
        //Ajoute le bouton de lancement de jeu

        ReadyButton ready = new ReadyButton(Menu.newButtonSprite("menuSmall"));
        ready.setPosition(MainMENU.WIDTH - buttonWidth, MainMENU.HEIGHT - buttonHeight);
        this.getButtons().add(ready);
        this.getButtons().add(new toMapButton(Menu.newButtonSprite("menuBig")));


        playerlist[0] = new Player("Player1");
        playerlist[0].setTeam(Team.MAN);
        playerlist[1] = new Player("Player2");
        playerlist[1].setTeam(Team.APE);
        setPlayers(playerlist);
        for (int i = 0; i < getPlayers().length; i++) {
            SquadButton b = new SquadButton(getPlayers()[i], Menu.newButtonSprite("menuLarge"));
            b.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b.getSprite().getBounds().l + i * (15 + b.getSprite().getBounds().h + b.getSprite().getBounds().l));
            getButtons().add(b);


            RenamePlayer rename = new RenamePlayer(getPlayers()[i]);
            rename.setPosition(b.getSprite().getBounds().l + 15 + b.getSprite().getBounds().w, b.getSprite().getBounds().t);
            getButtons().add(rename);


            TeamButton t = new TeamButton(getPlayers()[i], Menu.newButtonSprite("menuSmall"));
            t.setPosition(rename.getSprite().getBounds().l, rename.getSprite().getBounds().t + rename.getSprite().getBounds().h + 10);
            getButtons().add(t);
        }

    }

  /*  @Override
    public void update() {
        //int nbplayers = players.length;
        for (MenuComponent b : getButtons()) {
            int nbUnits = 0;
            if (b instanceof SquadButton) {

                for (Unite u : ((SquadButton) b).getPlayer().getUnites()) {
                    u.getSprite().setPosition(b.getSprite().getX() + 100 + 75 * nbUnits, b.getSprite().getY() + b.getSprite().getBounds().h / 2);
                    nbUnits++;
                }
            }
        }
    }*/
    private class toMapButton extends SpecialButton {
        public toMapButton(Sprite sprite) {
            super("Chose Map", sprite);
            setPosition(MainMENU.WIDTH - getSprite().getBounds().w, 50 + MainMENU.HEIGHT / 10);

        }

        @Override
        protected void clickedIfReady() {
            /*WARNING, REPETITION DE LA COLORATION
            Ici, afin que lorsqu'on arrive sur le menu, le selectionné soit coloré.
            dans MapButton, pour qu'il se colore bien par rapport au clic

             */
            for (MenuComponent m : MainMENU.menulist[MainMENU.MAPCHOICE].getButtons()) {
                if (m instanceof MapButton) {
                    // m.getSprite().setFillColor(new Color(169, 169, 169));
                    if (((MapButton) m).map == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap()) {
                        System.out.println("gud");
                        m.getSprite().setFillColor(new Color(255, 255, 153));
                    }else{
                        System.out.println("notgood");}
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
