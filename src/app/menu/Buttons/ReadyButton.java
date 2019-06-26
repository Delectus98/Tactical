package app.menu.Buttons;

import Graphics.Vector2i;
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
    public ReadyButton() {
        super("Ready", Menu.newButtonSprite("menuSmall"));
    }//TODO WARNING TEST NEED CONDITIONS


    @Override
    protected void clickedIfReady() {
        if (MainMENU.LOBBY == MainMENU.LOCAL) {
            MainMENU.state = MainMENU.STATE.GAME;

            try {
                window.setDimension(VideoMode.getDesktopMode());
                glfwMaximizeWindow(window.getGlId());

                for (int i = 0; i < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers().length; i++) {//
                    for (int k = 0; k < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).size() && k < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().size(); k++) { //pour chaque tile
                        ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).get(k).toString();
                        ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).setMapPosition(new Vector2i(-1, -1));
                        //((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).setMapPosition(((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getSpawnPoints(i).get(k));//on met une unité //unseless placement ingame
                        ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getSprite().setPosition(64 * ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getMapPosition().x, 64 * ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[i].getUnites().get(k).getMapPosition().y);
                    }
                }

                MainMENU.currentGame = new LocalhostGame(window, ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[0], ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[1], ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap());
                MainMENU.currentGame.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Echec démmarage partie");
                MainMENU.currentMenu = MainMENU.LOBBY; //TODO marche?
            }
        }
        else if(MainMENU.LOBBY==MainMENU.HOST){



        }else if (MainMENU.LOBBY==MainMENU.JOIN){
//send to host "I am ready"



        }

    }
    private boolean isMapReady() {
        return ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap() != null;

    }

    private boolean arePlayersReady() {
        if (((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers().length < 2 || ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[0].getTeam() == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getPlayers()[1].getTeam()) {
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
