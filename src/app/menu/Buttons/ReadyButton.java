package app.menu.Buttons;

import Graphics.Shape;
import System.VideoMode;
import app.MainMENU;
import app.Player;
import app.Unite;
import app.menu.Lobby;
import app.menu.Menu;
import app.play.LocalhostGame;

import java.io.IOException;

import static app.MainMENU.window;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class ReadyButton extends SpecialButton {
    public ReadyButton(Shape shape) {
        super("Ready", Menu.newButtonSprite("menuSmall"));
    }//TODO WARNING TEST NEED CONDITIONS


    @Override
    protected void clickedIfReady() {

        MainMENU.state = MainMENU.STATE.GAME;

        try {
            window.setDimension(VideoMode.getDesktopMode());
            glfwMaximizeWindow(window.getGlId());

            for (int i = 0; i < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers().length; i++) {//
                for (int k = 0; k < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).size() && k < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().size(); k++) { //pour chaque tile
                    ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).get(k).toString();
                    ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).setMapPosition(((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).get(k));//on met une unité
                    ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getSprite().setPosition( 64*((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getMapPosition().x, 64*((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getMapPosition().y);
                }
            }

            MainMENU.currentGame = new LocalhostGame(window, ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[0], ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[1], ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap());
            MainMENU.currentGame.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Echec démmarage partie");
            MainMENU.currentMenu=MainMENU.LOBBY; //TODO marche?
        }
    }


    private boolean isMapReady() {
        if (((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap() != null) {
            return true;
        } else {
            System.out.println("Map is null");
            System.out.println("this shouldnt happend. ever. unless Maplist changed or mainmenu.avilablemaps");
            return false;
        }
    }

    private boolean arePlayersReady() {
        if (((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers().length < 2) {
            System.out.println("Not enough players");
            return false;
        }
        for (Player p : ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()) {
            if (p.getUnites().size() <= 0)
                return false;
            int cost = 0;
            for (Unite u : p.getUnites()) {
                cost += u.getCreationCost();
            }
            if (cost > ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getSquadCreationPoints()) {
                System.out.println("Player " + p.getName() + " :Squad too expensive");
                return false;
            }

        }
        return true;
    }

    @Override
    public void checkIfButtonReady() {
        setReady(isMapReady() && arePlayersReady());
    }
}
