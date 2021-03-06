package app.menu.Buttons;

import Graphics.Color;
import Graphics.Sprite;
import app.Player;
import app.Team;

public class TeamButton extends SpecialButton {
    Player p;

    public TeamButton(Player player, Sprite sprite) {
        super("Switch Team",sprite);
        this.p=player;
        update();
    }

    public void update() {
        if (p.getTeam() == Team.MAN) {
            this.getSprite().setFillColor(Color.Magenta);
        } else {
            this.getSprite().setFillColor(Color.Green);
        }
        /*for(Unite u:p.getUnites()){
            u.setTeam(p.getTeam());
        }*/
        p.getUnites().clear();
    }


    @Override
    protected void clickedIfReady() {
        if (p.getTeam() == Team.MAN) {
            p.setTeam(Team.APE);
        } else {
            p.setTeam(Team.MAN);
        }
        update();
    }

    @Override
    public void checkIfButtonReady() {
      //  setReady((MainMENU.LOBBY==MainMENU.LOCAL)|| p.getId()== ((OnlineLobby)MainMENU.menulist[MainMENU.LOBBY]).getMyPlayerId());
    }
}
