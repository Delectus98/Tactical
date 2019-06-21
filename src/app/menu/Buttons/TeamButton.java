package app.menu.Buttons;

import Graphics.Color;
import Graphics.Shape;
import app.Player;
import app.Team;
import app.Unite;

public class TeamButton extends SpecialButton {
    Player p;

    public TeamButton(Player player, Shape shape) {
        super("Switch Team",shape);
        this.p=player;
        update();
    }

    private void update() {
        if (p.getTeam() == Team.MAN) {
            this.shape.setFillColor(Color.Magenta);
        } else {
            this.shape.setFillColor(Color.Green);
        }
        for(Unite u:p.getUnites()){
            u.setTeam(p.getTeam());
        }
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

    }
}
