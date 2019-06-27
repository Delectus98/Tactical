package app.menu.Buttons;

import app.MainMENU;
import app.Player;
import app.menu.Menu;
import app.menu.OnlineLobby;
import app.network.PlayerPacket;

import java.util.Scanner;

public class RenamePlayer extends SpecialButton{
    private Player p;

    public RenamePlayer(Player player) {
        super("Rename", Menu.newButtonSprite("menuSmall"));
        this.p=player;


    }

    private void changeName(){
        String s;
        //TODO TextField
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer nouveau nom: ");
        s=sc.nextLine();
        p.setName(s);
    }

    @Override
    protected void clickedIfReady() {

        changeName();
        if( MainMENU.LOBBY!=MainMENU.LOCAL){
            PlayerPacket pk = new PlayerPacket();
            pk.name=p.getName();
            pk.id=p.getId();
            ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).addToSend(pk);
        }
//New text zone
    }

    @Override
    public void checkIfButtonReady() {
        setReady((MainMENU.LOBBY==MainMENU.LOCAL)|| p.getId()== ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }
}
