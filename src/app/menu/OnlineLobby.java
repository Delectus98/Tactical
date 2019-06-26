package app.menu;

import app.MainMENU;

public class OnlineLobby extends Lobby {

    int myPlayerId;
    boolean isHost;

    public OnlineLobby(boolean isHost) {
        super(isHost?"Host Game":"Local Game", MainMENU.ONLINE);
        this.isHost=isHost;


    }

    @Override
   public void update(){
        if(isHost) {




        }else {


        }
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }
}
