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


    private class defaultButton extends SpecialButton {

        private Player player;

        /**
         * Gives the player a default squad
         *
         * @param player
         */
        public defaultButton(Player player) {
            super("Default squad: 3 soldiers", new RectangleShape(MainMENU.window.getDimension().x / 2, MainMENU.window.getDimension().y / 2, 250, 50));
            this.shape.setFillColor(Color.Blue);
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

    private class AddUnitButton extends SpecialButton {
        Player p;
        Unite u;

        public AddUnitButton(Shape shape, Player player, Unite u) {//TODO if team==team,addunitbutton
            super(u.getClass().getName(), shape);//TODO unit name
            this.p = player;
            this.u = u;
            this.shape.setFillColor(Color.Red);
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
       //     MakeSquad.update(p);
        }

        @Override
        public void checkIfButtonReady() {

        }
    }

    private class DeleteUnit extends SpecialButton {
        private Unite unite;
        final Player p;
       // Vector2f ori;
        Sprite s;
        protected boolean isEmpty;

        public DeleteUnit(Shape shape, Unite unite, Player p /*, Vector2f origin */, Sprite sprite) {
         //   super("Delete \n" + p.getUnites().get(index).getClass().getName(), shape);
            super(unite.toString(),shape);
            this.p = p;
           this.unite=unite;
          //  this.ori = origin;
          //  shape.setPosition(origin.x + index * 80, origin.y);
            this.s = sprite;
            this.isEmpty = false;

        }

        public DeleteUnit(Shape shape, Unite unite, Player p /*, Vector2f origin*/) {
            super("", shape);
            this.p = p;
            this.unite=unite;
          //  this.ori = origin;
          //  shape.setPosition(origin.x + index * 80, origin.y);
            this.shape.setFillColor(Color.Black);
            this.isEmpty = true;

        }

        @Override
        protected void clickedIfReady() {
            if (!isEmpty) {
                int i=p.getUnites().indexOf(unite);
                p.getUnites().remove(i);
                DeleteUnit d = new DeleteUnit(shape, null, p /*, ori*/);
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
public Player player;
    public MakeSquad(int width, int height, Player player, int squadSlots) {

        super(width, height, "Squad Maker", MainMENU.LOBBY, 50, 50, new Vector2f(), new HashMap<>(), true);
        // this.getButtons().add(new defaultButton(player));
        ;
        this.player=player;
        int compatibleUnits=0;
        for (Unite u: availableUnits) {
            if (u.getTeam() == player.getTeam()) {
                getButtons().add(0, new AddUnitButton(new RectangleShape(WIDTH / 10 + (80 * (compatibleUnits % 5)), HEIGHT / 2 + 25 + (100 * (compatibleUnits / 5)), 64, 64), player, u));
                compatibleUnits++;
            }
        }

        for (int i = 0; i < squadSlots; i++) {
            if (i < player.getUnites().size()) {
                getButtons().add(new DeleteUnit(new RectangleShape(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/, 64, 64), player.getUnites().get(i), player /*, new Vector2f(WIDTH / 10, 300)*/, player.getUnites().get(i).getSprite()));

            } else {
                DeleteUnit d = new DeleteUnit(new RectangleShape(WIDTH / 10 + (80 * i /*% 5*/), 300/* +(100) * i / 5*/, 64, 64), null, player/*, new Vector2f(WIDTH / 10, 300)*/);
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
                    b.shape.setFillColor(Color.Black);
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                    b.shape.setFillColor(Color.Blue);
                    ktrue++;
                }

        }
    }

    protected void addUnitButton(Player player,Unite unite) {
        for (int i=0;i< menulist[MainMENU.MAKESQUAD].getButtons().size();i++) {
            MenuButton b=menulist[MainMENU.MAKESQUAD].getButtons().get(i);
            if (b instanceof DeleteUnit && ((DeleteUnit)b).isEmpty ){
                menulist[MainMENU.MAKESQUAD].getButtons().set(i, new DeleteUnit(new RectangleShape(0,0, 64, 64),unite,player,unite.getSprite() ));
            break;
            }
        }
    }

    //TODO algo de tri. fuque
    public static void update(Player player) {//réarrange les boutons selon le squad
        //unités joueur puis emplacements libres
        byte ktrue = 0;
        int kfalse = player.getUnites().size();

        for (MenuButton b : menulist[MainMENU.MAKESQUAD].getButtons()) {

            if (b instanceof DeleteUnit)
                if (((DeleteUnit) b).isEmpty) {
                   b.setPosition(WIDTH / 10 + 80 * kfalse, 300);
                    b.shape.setFillColor(Color.Black);
                    kfalse++;
                } else {
                    b.setPosition(WIDTH / 10 + ktrue * 80, 300);
                    b.shape.setFillColor(Color.Blue);
                    ktrue++;
                }

        }

    }


}
