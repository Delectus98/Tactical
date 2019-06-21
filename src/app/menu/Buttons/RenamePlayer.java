package app.menu.Buttons;

import Graphics.RectangleShape;
import app.MainMENU;
import app.Player;
import app.menu.OnlineLobby;

import java.util.Scanner;

public class RenamePlayer extends SpecialButton{
Player p;

    public RenamePlayer(Player player) {
        super("Rename",new RectangleShape(0,0,120,55));
        this.p=player;


    }

    public void changeName(){
        String s = "PPlayer";
        //TODO Text box in which the player types a thing
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer nouveau nom: ");
        s=sc.nextLine();
        p.setName(s);
    }

    @Override
    protected void clickedIfReady() {
        changeName();
//New text zone
    }

    @Override
    public void checkIfButtonReady() {
         setReady((MainMENU.LOBBY==MainMENU.LOCAL)|| p.getId()== ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }
}
