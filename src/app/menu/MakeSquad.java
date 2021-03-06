package app.menu;

import Graphics.Sprite;
import Graphics.Vector2f;
import app.MainMENU;
import app.Player;
import app.Unite;
import app.menu.Buttons.MenuButton;
import app.menu.Buttons.SpecialButton;
import app.network.SquadPacket;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;

import java.util.HashMap;

import static app.MainMENU.*;

public class MakeSquad extends Menu {


    private class AddUnitButton extends SpecialButton {
        Player p;
        Unite u;

        AddUnitButton(Player player, Unite u) {//
            super("", new Sprite(u.getSprite().getTexture()));//
            this.p = player;
            this.u = u;
            //getSprite().setFillColor(new Color(0.6f, 0.6f, 0.6f));
        }

        @Override
        protected void clickedIfReady() {
            Unite toAdd = new SoldierUnit(p.getTeam());
            if (p.getUnites().size() < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getSquadCreationPoints())
                if (u instanceof SoldierUnit) {

                } else if (u instanceof MarksmanUnit) {
                    toAdd = new MarksmanUnit(p.getTeam());
                } else {
                    System.out.println("Erreur, type d'unité inconnu. default = soldier");
                }
            p.addUnite(toAdd);
            ((MakeSquad) menulist[MainMENU.MAKESQUAD]).addUnitButton(p, toAdd);
        }

        @Override
        public void checkIfButtonReady() {

        }
    }

    private class DeleteUnit extends SpecialButton {
        private Unite unite;
        final Player p;
        protected boolean isEmpty;

        DeleteUnit(Unite unite, Player p) {
            super("", new Sprite(unite.getSprite().getTexture()));
            this.p = p;
            getSprite().setTextureRect(0, 0, 64, 64);
            this.unite = unite;

            this.isEmpty = false;

        }

        DeleteUnit(Player p) {
            super("", Menu.newButtonSprite("squadSlot"));
            this.p = p;
            //this.getSprite().setFillColor(new Color(0.6f, 0.6f, 0.6f));
            this.isEmpty = true;

        }

        @Override
        protected void clickedIfReady() {
            if (!isEmpty) {
                p.getUnites().remove(unite);
                DeleteUnit d = new DeleteUnit(p);
                menulist[MainMENU.MAKESQUAD].getButtons().set(menulist[MainMENU.MAKESQUAD].getButtons().indexOf(this), d);

            }
        }

        @Override
        public void checkIfButtonReady() {

        }
    }

    private class okSquadButton extends SpecialButton {
        private final Player player;

        okSquadButton(String title, float x, float y, Player p) {
            super(title, Menu.newButtonSprite("menuSmall"));
            setPosition(x, y);
            this.player = p;
        }

        @Override
        protected void clickedIfReady() {
            if (LOBBY == HOST || LOBBY == JOIN) {
                SquadPacket squad = new SquadPacket();
                for (int i = 0; i < player.getUnites().size(); i++) {
                    if (player.getUnites().get(i) instanceof MarksmanUnit) {
                        squad.squad.add(1);
                    } else if (player.getUnites().get(i) instanceof SoldierUnit) {
                        squad.squad.add(0);
                    } else {
                        squad.squad.add(-1);
                    }


                }   ((OnlineLobby) menulist[LOBBY]).addToSend(squad);
            }
            currentMenu = LOBBY;

        }

        @Override
        public void checkIfButtonReady() {
            setReady(player.getUnites().size() <= ((Lobby) menulist[LOBBY]).getSquadCreationPoints());
        }

    }


    public static Player player;

    public MakeSquad(Player player, int squadSlots) {

        super("Squad Maker", MainMENU.LOBBY, new Vector2f(), new HashMap<>(), false);


        this.player = player;
        int compatibleUnits = 0;
        for (Unite u : availableUnits) {
            if (u.getTeam() == player.getTeam()) {
                AddUnitButton ad = new AddUnitButton(player, u);
                ad.setPosition(WIDTH / 10 + (80 * (compatibleUnits % 5)), HEIGHT / 2 + 25 + (100 * (compatibleUnits / 5)));
                getButtons().add(0, ad);
                ad.getSprite().setTextureRect(0, 0, 64, 64);
                compatibleUnits++;

            }
        }

        for (int i = 0; i < squadSlots; i++) {
            if (i < player.getUnites().size()) {
                DeleteUnit d = new DeleteUnit(player.getUnites().get(i), player);
                d.setPosition(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/);
                getButtons().add(d);

            } else {
                DeleteUnit d = new DeleteUnit(player/*, new Vector2f(WIDTH / 10, 300)*/);
                d.setPosition(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/);
                getButtons().add(d);
            }
//TODO attention si trop d'unités. max 5?
        }
        this.getButtons().add(new okSquadButton("ok", MainMENU.WIDTH - 100, MainMENU.HEIGHT - 40, player));


        byte ktrue = 0;
        int kfalse = player.getUnites().size();

        for (MenuButton b : getButtons()) {

            if (b instanceof DeleteUnit)
                if (((DeleteUnit) b).isEmpty) {
                    b.setPosition(WIDTH / 10 + 80 * kfalse, 300);
                    //b.getSprite().setFillColor(Color.Black);
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                    //b.getSprite().setFillColor(Color.Blue);
                    ktrue++;
                }

        }
    }

    private void addUnitButton(Player player, Unite unite) {
        for (int i = 0; i < menulist[MainMENU.MAKESQUAD].getButtons().size(); i++) {
            MenuButton b = menulist[MainMENU.MAKESQUAD].getButtons().get(i);
            if (b instanceof DeleteUnit && ((DeleteUnit) b).isEmpty) {
                menulist[MainMENU.MAKESQUAD].getButtons().set(i, new DeleteUnit(unite, player));
                break;
            }
        }
    }


    public static void update(Player player) {//réarrange les boutons selon le squad
        byte ktrue = 0;
        int kfalse = player.getUnites().size();

        for (MenuButton b : menulist[MainMENU.MAKESQUAD].getButtons()) {

            if (b instanceof DeleteUnit)
                if (((DeleteUnit) b).isEmpty) {
                    b.setPosition(WIDTH / 10 + 80 * kfalse, 300);
                   // b.getSprite().setFillColor(new Color(0.6f, 0.6f, 0.6f));
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                //   b.getSprite().setFillColor(Color.White);
                    ktrue++;
                }
        }
    }


}
