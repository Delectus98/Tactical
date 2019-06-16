package app.menu.Buttons;

import Graphics.Shape;
import app.MainMENU;
import app.Player;
import app.Unite;
import app.play.LocalhostGame;

import java.io.IOException;

public class ReadyButton extends SpecialButton {
    public ReadyButton(Shape shape) {
        super("Ready", shape);
    }//TODO WARNING TEST NEED CONDITIONS


    @Override
    protected void clickedIfReady() {

        MainMENU.state = MainMENU.STATE.GAME;

/*
        for (Player p : MainMENU.currentGame.getPlayers())
            for (Unite u : p.getUnites())
                System.out.println(p.getName() + " // " + u.getMapPosition().toString());
*/



            //TODO MODE DEGEU AS FUCK mais marche
        try {
            MainMENU.currentGame=new LocalhostGame(MainMENU.window,MainMENU.currentGame.getPlayers()[0],MainMENU.currentGame.getPlayers()[1],MainMENU.currentGame.getMap());
       //MainMENU.currentGame=MainMENU.demo(MainMENU.window);
       MainMENU.currentGame.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Echec démmarage partie");
        }
    }








    private boolean isMapReady() {
        if (MainMENU.currentGame.getMap() != null) {
            return true;
        } else {
            System.out.println("Map is null");
            System.out.println("this shouldnt happend. ever. unless Maplist changed or mainmenu.avilablemaps");
            return false;
        }
    }

    private boolean arePlayersReady() {
        if (MainMENU.currentGame.getPlayers().length < 2) {
            System.out.println("Not enough players");
            return false;
        }
        for (Player p : MainMENU.currentGame.getPlayers()) {
            if (p.getUnites().size() <= 0)
                return false;
            int cost = 0;
            for (Unite u : p.getUnites()) {
                cost += u.getCreationCost();
            }
            if (cost > MainMENU.currentGame.getSquadCreationPoints()) {
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
