package app.menu.Buttons;

import System.Event;
import System.IO.AZERTYLayout;
import System.Keyboard;
import app.MainMENU;
import app.Player;
import app.menu.Menu;
import app.menu.OnlineLobby;
import app.network.PlayerPacket;
import util.TextInput;

public class RenamePlayer extends SpecialButton{
    private Player p;

    public RenamePlayer(Player player) {
        super("Rename", Menu.newButtonSprite("menuSmall"));
        this.p=player;


    }

    private void changeName(){
String s="";
        Keyboard keyboard = new Keyboard(MainMENU.window);
        Event event;
        TextInput tx = new TextInput();
        while (!keyboard.isKeyPressed(AZERTYLayout.RETURN.getKeyID())) {
            if(tx.hasBeenUpdated())
                if(keyboard.isKeyPressed(AZERTYLayout.PAD_RETURN.getKeyID())){
                    s=s.substring(0,s.length()-2);}//!!null
                else{
                    s+=s+tx.getChar();
                    tx.reset();
                }
            p.setName(s);
        }
       /* String s;
        //TODO TextField
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer nouveau nom: ");
        s=sc.nextLine();*/
        //p.setName(s);
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
