package app.menu.Buttons;

import Graphics.Sprite;
import app.MainMENU;
import app.Player;
import app.menu.Lobby;
import app.menu.MakeSquad;
import app.menu.OnlineLobby;

public class SquadButton extends SpecialButton {
    Player player;


    public SquadButton(Player p, Sprite sprite){
        super(p.getName(),sprite);
        this.player=p;
        //TODO Editable par le joueur courant uniquement ( online. et par les deux en local)
    }


    @Override
    protected void clickedIfReady() {
        MainMENU.menulist[MainMENU.MAKESQUAD]=new MakeSquad(player,((Lobby)MainMENU.menulist[MainMENU.LOBBY]).getSquadCreationPoints() );
        MainMENU.currentMenu=MainMENU.MAKESQUAD;
    }

    @Override
    public void checkIfButtonReady() {
        this.textZone.setString(player.getName());
        setReady((MainMENU.LOBBY==MainMENU.LOCAL)|| player.getId()== ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }

    public Player getPlayer(){
        return this.player;
    }
}
