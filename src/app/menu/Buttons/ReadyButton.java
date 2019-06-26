package app.menu.Buttons;

import System.VideoMode;
import app.MainMENU;
import app.Player;
import app.Unite;
import app.menu.Lobby;
import app.menu.Menu;
import app.menu.OnlineLobby;
import app.network.ClientImpl;
import app.network.ReadyPacket;
import app.network.ServerImpl;
import app.sounds.Music;

import java.io.IOException;

import static app.MainMENU.*;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class ReadyButton extends SpecialButton {
    public ReadyButton() {
        super("Ready", Menu.newButtonSprite("menuSmall"));
    }//TODO WARNING TEST NEED CONDITIONS


    @Override
    protected void clickedIfReady() throws IOException {

        //MainMENU.state = MainMENU.STATE.GAME;
        Music.stopMusic();
        if (MainMENU.LOBBY == MainMENU.LOCAL) {
            MainMENU.state = MainMENU.STATE.GAME;

            window.setDimension(VideoMode.getDesktopMode());
            glfwMaximizeWindow(window.getGlId());

            if (((Lobby) menulist[MainMENU.LOBBY]).getGame() != null) {
                MainMENU.currentGame = ((Lobby) menulist[MainMENU.currentMenu]).getGame();
                MainMENU.currentGame.start();
            } else {
                System.out.println("readybutton: game Not ready");
            }

        } else if (MainMENU.LOBBY == MainMENU.HOST) {
            if (((OnlineLobby) menulist[LOBBY]).isClientReady) {
                ((ServerImpl) ((OnlineLobby) menulist[LOBBY]).listener).send(new ReadyPacket());
                if (MainMENU.LOBBY == MainMENU.LOCAL) {
                    MainMENU.state = MainMENU.STATE.GAME;

                    window.setDimension(VideoMode.getDesktopMode());
                    glfwMaximizeWindow(window.getGlId());

                    if (((Lobby) menulist[MainMENU.LOBBY]).getGame() != null) {
                        MainMENU.currentGame = ((Lobby) menulist[LOBBY]).getGame();
                        MainMENU.currentGame.start();
                    } else {
                        System.out.println("readybutton: game Not ready");
                    }
                }
            }
//if playerclient is ready => send them game is starting

        } else if (MainMENU.LOBBY == MainMENU.JOIN) {
            ((ClientImpl) ((OnlineLobby) menulist[LOBBY]).listener).send(new ReadyPacket());
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
