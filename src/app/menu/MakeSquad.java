package app.menu;

import Graphics.*;
import app.MainMENU;
import app.Player;
import app.Team;
import app.Unite;
import app.menu.Buttons.MenuButton;
import app.menu.Buttons.SpecialButton;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;

import java.util.HashMap;

import static app.MainMENU.*;

public class MakeSquad extends Menu {



    private class AddUnitButton extends SpecialButton {
        Player p;
        Unite u;

        public AddUnitButton(Player player, Unite u) {//TODO if team==team,addunitbutton
            super("" , u.getSprite());//TODO unit name
            this.p = player;
            this.u = u;
            getSprite().setFillColor(new Color(169,169,169));
        }

        @Override
        protected void clickedIfReady() {
            Unite toAdd=new SoldierUnit(p.getTeam());
            if (p.getUnites().size() < ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getSquadCreationPoints())
                if (u instanceof SoldierUnit) {

                } else if (u instanceof MarksmanUnit) {
                   toAdd=new MarksmanUnit(p.getTeam());
                } else {
                    System.out.println("Erreur, type d'unité inconnu. default = soldier");
                }
            p.addUnite(toAdd);
            ((MakeSquad) menulist[MainMENU.MAKESQUAD]).addUnitButton(p,toAdd);
        }

        @Override
        public void checkIfButtonReady() {

        }
    }

    private class DeleteUnit extends SpecialButton {
        private Unite unite;
        final Player p;
        Sprite s;
        protected boolean isEmpty;

        public DeleteUnit(Unite unite, Player p) {
            super("" ,unite.getSprite());
            this.p = p;
           this.unite=unite;
            this.s = unite.getSprite();
            s.setFillColor(new Color(169,169,169));
            this.isEmpty = false;

        }

        public DeleteUnit(Player p ) {
            super("", Menu.newButtonSprite("squadSlot"));
            this.p = p;
            this.getSprite().setFillColor(new Color(169,169,169));
            this.isEmpty = true;

        }

        @Override
        protected void clickedIfReady() {
            if (!isEmpty) {
                int i=p.getUnites().indexOf(unite);
                p.getUnites().remove(i);
                DeleteUnit d = new DeleteUnit( p /*, ori*/);
                menulist[MainMENU.MAKESQUAD].getButtons().set(menulist[MainMENU.MAKESQUAD].getButtons().indexOf(this), d);
             //   MakeSquad.update(p);

            }
        }


        @Override
        public void checkIfButtonReady() {

        }
    }

    /**
     * Menu constructor
     *
     * @param width
     * @param height
     * @param player
     */
public static Player player;
    public MakeSquad(Player player, int squadSlots) {

        super(WIDTH, HEIGHT, "Squad Maker", MainMENU.LOBBY, 50, 50, new Vector2f(), new HashMap<>(), true);
        // this.getButtons().add(new defaultButton(player));
        ;
        this.player=player;
        int compatibleUnits=0;
        for (Unite u: availableUnits) {
            if (u.getTeam() == player.getTeam()) {
                AddUnitButton ad = new AddUnitButton(player, u);
                ad.setPosition(WIDTH / 10 + (80 * (compatibleUnits % 5)), HEIGHT / 2 + 25 + (100 * (compatibleUnits / 5)));
                getButtons().add(0,ad);
                compatibleUnits++;
            }
        }

        for (int i = 0; i < squadSlots; i++) {
            if (i < player.getUnites().size()) {
                DeleteUnit d = new DeleteUnit( player.getUnites().get(i),player);
              d.setPosition(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/);
                getButtons().add(d);

            } else {
                DeleteUnit d = new DeleteUnit(player/*, new Vector2f(WIDTH / 10, 300)*/);
                d.setPosition(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/);
                getButtons().add(d);
            }
//TODO attention si trop d'unités. max 5?
        }

        byte ktrue = 0;
        int kfalse = player.getUnites().size();

        for (MenuButton b : getButtons()) {

            if (b instanceof DeleteUnit)
                if (((DeleteUnit) b).isEmpty) {
                    b.setPosition(WIDTH / 10 + 80 * kfalse, 300);
                    b.getSprite().setFillColor(Color.Black);
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                    b.getSprite().setFillColor(Color.Blue);
                    ktrue++;
                }

        }
    }

    protected void addUnitButton(Player player,Unite unite) {
        for (int i=0;i< menulist[MainMENU.MAKESQUAD].getButtons().size();i++) {
            MenuButton b=menulist[MainMENU.MAKESQUAD].getButtons().get(i);
            if (b instanceof DeleteUnit && ((DeleteUnit)b).isEmpty ){
                menulist[MainMENU.MAKESQUAD].getButtons().set(i, new DeleteUnit(unite,player ));
            break;
            }
        }
    }


    public static void update(Player player) {//réarrange les boutons selon le squad
        //unités joueur puis emplacements libres
        byte ktrue = 0;
        int kfalse = player.getUnites().size();

        for (MenuButton b : menulist[MainMENU.MAKESQUAD].getButtons()) {

            if (b instanceof DeleteUnit)
                if (((DeleteUnit) b).isEmpty) {
                   b.setPosition(WIDTH / 10 + 80 * kfalse, 300);
                    b.getSprite().setFillColor(new Color(169,169,169));
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                    b.getSprite().setFillColor(Color.White);
                    ktrue++;
                }

        }

    }
    private class defaultButton extends SpecialButton {

        private Player player;

        /**
         * Gives the player a default squad
         *
         * @param player
         */
        public defaultButton(Player player) {
            super("Default squad: 3 soldiers", Menu.newButtonSprite("menuBig"));
            setPosition(WIDTH / 2, HEIGHT / 2);
            this.getSprite().setFillColor(Color.White);
            this.player = player;
        }

        @Override
        protected void clickedIfReady() {
            player.getUnites().clear();

            for (int i = 0; i < 3; i++) {
                Unite u = new SoldierUnit(player.getTeam());
                System.out.println(u.getSprite());
                player.addUnite(u);
                Vector2i v = new Vector2i((int) (Math.random() * ((Lobby) MainMENU.menulist[MainMENU.LOBBY]).getMap().getWorld()[0].length), (int) (Math.random() * MainMENU.currentGame.getMap().getWorld().length));
                u.setMapPosition(v);
                u.getSprite().setPosition(64 * v.x, 64 * v.y);
                u.setTeam((player.getTeam() == null) ? Team.MAN : player.getTeam());
            }
            MainMENU.currentMenu = MainMENU.LOBBY;

        }

        @Override
        public void checkIfButtonReady() {
            //setReady(true); //useless, true when created
        }
    }


}
