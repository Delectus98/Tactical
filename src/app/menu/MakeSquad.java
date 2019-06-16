package app.menu;

import Graphics.*;
import app.*;
import app.menu.Buttons.SpecialButton;
import util.ResourceHandler;

import java.util.HashMap;

public class MakeSquad extends Menu {


    private class defaultButton extends SpecialButton {

        private Player player;
        public defaultButton(Player player) {
            super("Default squad: 3 soldiers", new RectangleShape(MainMENU.window.getDimension().x/2,MainMENU.window.getDimension().y/2, 250, 50));
            this.shape.setFillColor(Color.Blue);
            this.player=player;
        }

        @Override
        protected void clickedIfReady() {
            player.getUnites().clear();

            for(int i=0;i<3;i++){
                Unite u=new Main.UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0, 0, 64, 64));
                System.out.println(u.getSprite());
                player.addUnite(u);
                Vector2i v=new Vector2i((int)(Math.random()*MainMENU.currentGame.getMap().getWorld()[0].length),(int)(Math.random()*MainMENU.currentGame.getMap().getWorld().length));
                u.setMapPosition(v);
                u.getSprite().setPosition(64*v.x,64*v.y);
                u.setTeam((player.getTeam()==null)? Team.MAN :player.getTeam());
            }
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
