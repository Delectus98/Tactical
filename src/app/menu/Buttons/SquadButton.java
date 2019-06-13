package app.menu.Buttons;

import Graphics.RectangleShape;
import app.MainMENU;
import app.Player;
import app.menu.MakeSquad;
import app.menu.OnlineLobby;

public class SquadButton extends SpecialButton {
    Player player;


    public SquadButton(Player p, int windowWidth, int windowHeight){
        super(p.getName(),new RectangleShape(0,0, windowWidth/2, windowHeight/10));
        this.player=p;
        //TODO Editable par le joueur courant uniquement ( online. et par les deux en local)
    }


    @Override
    protected void clickedIfReady() {
        MainMENU.menulist[MainMENU.MAKESQUAD]=new MakeSquad(MainMENU.WIDTH,MainMENU.HEIGHT,player);
        MainMENU.currentMenu=MainMENU.MAKESQUAD;
    }

    @Override
    public void checkIfButtonReady() {
        this.textZone.setString(player.getName());
        setReady((MainMENU.LOBBY==MainMENU.LOCAL)|| player.getId()== ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }

}
