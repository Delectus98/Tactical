package app.menu.Buttons;

import app.MainMENU;
import app.Player;
import app.menu.Lobby;
import app.menu.Menu;
import app.menu.OnlineLobby;
import app.network.PlayerPacket;
import util.TextInput;

public class RenamePlayer extends SpecialButton {
    private Player p;
    String tmp;


    public RenamePlayer(Player player) {
        super("Rename", Menu.newButtonSprite("menuSmall"));
        this.p = player;


    }

    private void changeName() {
        p.setName(tmp);
        if (MainMENU.LOBBY != MainMENU.LOCAL) {
            PlayerPacket pk = new PlayerPacket();
            pk.name = p.getName();
            pk.id = p.getId();
            ((OnlineLobby) MainMENU.menulist[MainMENU.LOBBY]).addToSend(pk);
        }
    }

    @Override
    protected void clickedIfReady() {
        ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).toUpdate = this;

//New text zone
    }

    @Override
    public void checkIfButtonReady() {
        setReady((MainMENU.LOBBY == MainMENU.LOCAL) || p.getId() == ((OnlineLobby) MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }

    @Override
    public void update() {
        if (isEditing) {

            if (TextInput.isTextEntered()) {
                tmp += TextInput.getChar();
            } else if (TextInput.isBackspaceEntered()) {
                if (tmp.length() > 0)
                    tmp = tmp.substring(0, tmp.length() - 1);
            } else if (TextInput.isReturnCarriageEntered()) {
                changeName();
                ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).toUpdate = null;
                isEditing = false;

            }
            p.setName(tmp);
        } else {
            tmp = "";
            isEditing = true;
        }


    }
}
