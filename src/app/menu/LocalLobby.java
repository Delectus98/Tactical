package app.menu;

import Graphics.Color;
import Graphics.Sprite;
import app.MainMENU;
import app.Player;
import app.Team;
import app.map.MapImpl;
import app.menu.Buttons.*;
import util.ResourceHandler;

public class LocalLobby extends Lobby {
    private Player[] playerlist = new Player[2];
    int slots = 0;


    /**
     * Menu constructor
     */
    public LocalLobby() {
        super("Game Local: Lobby", MainMENU.GAMEMODE);
        //Ajoute le bouton de lancement de jeu

        ReadyButton ready = new ReadyButton();
        ready.setPosition(MainMENU.WIDTH - ready.getSprite().getBounds().w, MainMENU.HEIGHT - ready.getSprite().getBounds().h);
        this.getButtons().add(ready);
        this.getButtons().add(new toMapButton(new Sprite(((MapImpl) getMap()).getMiniature())));//TODO miniature map


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

            slots=getButtons().size()-1;
            // TODO
            for (int k = 0; k < getSquadCreationPoints(); k++) {
                MenuButton d = new MenuButton("", new Sprite(ResourceHandler.getTexture("squadSlot"))) {
                };
                d.setPosition(64 + (80 * k), b.getSprite().getBounds().t + b.getSprite().getBounds().h / 4);
                getButtons().add(d);
            }//END TODO

        }


    }


    private class toMapButton extends SpecialButton {
        public toMapButton(Sprite sprite) {
            super("Chose Map", sprite);
            setPosition(MainMENU.WIDTH - getSprite().getBounds().w, 50 + MainMENU.HEIGHT / 10);
        }

        @Override
        protected void clickedIfReady() {
            for (MenuComponent m : MainMENU.menulist[MainMENU.MAPCHOICE].getButtons()) {
                if (m instanceof MapButton) {
                    // m.getSprite().setFillColor(new Color(169, 169, 169));
                    if (((MapButton) m).map == ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap()) {
                        m.getSprite().setFillColor(new Color(255, 255, 153));
                    }
                }
            }
            MainMENU.currentMenu = MainMENU.MAPCHOICE;
        }

        @Override
        public void checkIfButtonReady() {
            setReady((MainMENU.LOBBY == MainMENU.LOCAL) || MainMENU.LOBBY == MainMENU.HOST);
        }
    }

    @Override
    public void update() {
        //int nbplayers = players.length;
        for (MenuComponent b : getButtons()) {

            if (b instanceof toMapButton) {
                float x = b.getSprite().getBounds().l;
                float y = b.getSprite().getBounds().t;
                b.setSprite(new Sprite(((MapImpl) map).getMiniature()));
                b.getSprite().setPosition(x, y);
            } else if (b instanceof ReadyButton) {
                ((ReadyButton) b).checkIfButtonReady();
            } else if (b instanceof SquadButton) {
                if (((SquadButton) b).getPlayer().getUnites().size() > getSquadCreationPoints())
                    if (((SquadButton) b).getPlayer().getUnites().size() > getSquadCreationPoints() + 1) {
                        ((SquadButton) b).getPlayer().getUnites().subList(getSquadCreationPoints() + 1, ((SquadButton) b).getPlayer().getUnites().size()).clear();
                    }
            }
        }
    }
}
