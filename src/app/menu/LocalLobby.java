package app.menu;

import Graphics.Sprite;
import app.Game;
import app.MainMENU;
import app.Player;
import app.Team;
import app.map.MapImpl;
import app.menu.Buttons.*;
import app.play.LocalhostGame;

import java.io.IOException;

public class LocalLobby extends Lobby {
    private Player[] playerlist = new Player[2];
    //  int slots = 0;


    /**
     * Menu constructor
     */
    public LocalLobby() {
        super("Game Local: Lobby", MainMENU.GAMEMODE);
        //Ajoute le bouton de lancement de jeu

        ReadyButton ready = new ReadyButton();
        ready.setPosition(MainMENU.WIDTH - ready.getSprite().getBounds().w, MainMENU.HEIGHT - ready.getSprite().getBounds().h);
        this.getButtons().add(ready);
        this.getButtons().add(new toMapButton(new Sprite(((MapImpl) getMap()).getMiniature())));


        playerlist[0] = new Player("Player1");
        playerlist[0].setTeam(Team.MAN);
        playerlist[1] = new Player("Player2");
        playerlist[1].setTeam(Team.APE);
        setPlayers(playerlist);


        for (int i = 0; i < getPlayers().length; i++) {
            SquadButton b = new SquadButton(getPlayers()[i], Menu.newButtonSprite("menuLarge"));
            b.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b.getSprite().getBounds().l + i * (15 + b.getSprite().getBounds().h + b.getSprite().getBounds().l));
            getButtons().add(b);


            RenamePlayer rename = new RenamePlayer(getPlayers()[i]);
            rename.setPosition(b.getSprite().getBounds().l + 15 + b.getSprite().getBounds().w, b.getSprite().getBounds().t);
            getButtons().add(rename);


            TeamButton t = new TeamButton(getPlayers()[i], Menu.newButtonSprite("menuSmall"));
            t.setPosition(rename.getSprite().getBounds().l, rename.getSprite().getBounds().t + rename.getSprite().getBounds().h + 10);
            getButtons().add(t);
/*
            slots=getButtons().size()-1;
            // TODO
            for (int k = 0; k < getSquadCreationPoints(); k++) {
                MenuButton d = new MenuButton("", new Sprite(ResourceHandler.getTexture("squadSlot"))) {
                };
                d.setPosition(64 + (80 * k), b.getSprite().getBounds().t + b.getSprite().getBounds().h / 4);
                getButtons().add(d);
            }//END TODO
*/
        }
    }

    @Override
    public Game getGame() throws IOException {
        if (getPlayers()[0].getUnites().size() > 0 && getPlayers()[1].getUnites().size() > 0 && getPlayers()[0].getUnites().size() <= getSquadCreationPoints() && getPlayers()[1].getUnites().size() <= getSquadCreationPoints() && getMap() != null) {

            return new LocalhostGame(MainMENU.window, getPlayers()[0], getPlayers()[1], getMap());
        } else {
            return null;
        }
    }

    @Override
    public void update() throws IOException, InterruptedException {
        if(this.toUpdate!=null)
            toUpdate.update();
        //int nbplayers = players.length;
        for (MenuComponent b : getButtons()) {
            if (b instanceof toMapButton) {//update miniature
                float x = b.getSprite().getBounds().l;
                float y = b.getSprite().getBounds().t;
                b.setSprite(new Sprite(((MapImpl) getMap()).getMiniature()));
                b.getSprite().setPosition(x, y);
            } else if (b instanceof ReadyButton) {//check ready
                ((ReadyButton) b).checkIfButtonReady();
            } else if (b instanceof SquadButton)
                for (int i = ((SquadButton) b).getPlayer().getUnites().size() - 1; i >= getSquadCreationPoints(); i--)
                    ((SquadButton) b).getPlayer().getUnites().remove(i);
        }
    }
}
