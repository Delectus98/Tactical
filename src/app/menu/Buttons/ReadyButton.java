package app.menu.Buttons;

import Graphics.Shape;
import app.MainMENU;
import app.Player;
import app.Unite;

public class ReadyButton extends SpecialButton {
    public ReadyButton(Shape shape) {
        super("Ready", shape);
    }

    public ReadyButton(String txt, Shape shape) {
        super(txt, shape);//TODO WARNING TEST NEED CONDITIONS
    }

    @Override
    protected void clickedIfReady() {
        System.out.println(this.textZone.getString());
        MainMENU.currentGame.start();
        MainMENU.state = MainMENU.STATE.GAME;


        for (Player p : MainMENU.currentGame.getPlayers())
            for (Unite u : p.getUnites())
                System.out.println(p.getName()+" // "+u.getMapPosition().toString());
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
