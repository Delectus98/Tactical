package app.menu;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Vector2f;
import Graphics.Vector2i;
import app.MainMENU;
import app.Player;
import app.Team;
import app.Unite;
import app.menu.Buttons.SpecialButton;
import app.units.SoldierUnit;

import java.util.HashMap;

public class MakeSquad extends Menu {


    private class defaultButton extends SpecialButton {

        public defaultButton(Player player) {
            super("Default squad: 3 soldiers", new RectangleShape(MainMENU.window.getDimension().x/2,MainMENU.window.getDimension().y/2, 250, 50));
            this.shape.setFillColor(Color.Blue);
            player.getUnites().clear();
            player.addUnite(new SoldierUnit(Team.MAN));
            player.addUnite(new SoldierUnit(Team.MAN));
            player.addUnite(new SoldierUnit(Team.MAN));
            for(Unite u:player.getUnites()){
                int x=(int)(Math.random()*MainMENU.currentGame.getMap().getWorld()[0].length);
                int y=(int)(Math.random()*MainMENU.currentGame.getMap().getWorld().length);
                Vector2i v=new Vector2i(x,y);

                u.setMapPosition(v);
            }

        }

        @Override
        protected void clickedIfReady() {
            MainMENU.currentMenu=MainMENU.LOBBY;

        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }
    /**
     * Menu constructor
     *  @param width
     * @param height
     * @param player
     */
    public MakeSquad(int width, int height, Player player) {
        super(width, height, "Squad Maker", MainMENU.LOBBY, 0, 0, new Vector2f(), new HashMap<>(), true);
        this.getButtons().add(new defaultButton(player));
    }

    public void selectSquad() {
        //TODO
    }

}
