package app.menu;

import Graphics.*;
import app.MainMENU;
import app.menu.Buttons.*;
import util.ResourceHandler;
import System.GLFWWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Menu {
    private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
    private Text title;
    private Vector2f specialButtonOrigin;
    private Vector2f normalButtonOrigin;
    private int nbSpecialButton;
    private ConstTexture texture;
    private Sprite sprite;

    public Menu(GLFWWindow window, String title, int parentMenuId, Vector2f normalButtonOrigin, Vector2f specialButtonOrigin, Vector2f titleposition, boolean backbutton) throws IOException {
        this.title = new Text(ResourceHandler.getFont("default"), title);
        this.texture = ResourceHandler.getTexture("menuBackground");
        this.sprite = new Sprite(texture);

        this.title.setPosition(titleposition.x, titleposition.y);
        this.normalButtonOrigin = normalButtonOrigin;
        this.specialButtonOrigin = specialButtonOrigin;


        if (backbutton)
            backbutton(parentMenuId);

    }

    /**
     * Menu constructor
     *
     * @param title           Displayed title of the menu
     * @param parentMenuId    where to go back if needed
     * @param buttonOrigin    origin of the buttonList
     * @param correspondances Correspondance between button and menu.
     */

    public Menu(String title, int parentMenuId, Vector2f buttonOrigin, HashMap<String, Integer> correspondances, boolean backbutton) {
        this.title = new Text(ResourceHandler.getFont("default"), title);
        this.title.setPosition(MainMENU.WIDTH / 10, MainMENU.HEIGHT / 10);
        this.texture = ResourceHandler.getTexture("menuBackground");
        this.sprite = new Sprite(texture);
        //For correspondance créer nouveau boutton avec la shape.
        for (String p : correspondances.keySet()) {

            MenuButton b = new NormalButton(p, correspondances.get(p),
                    newButtonSprite("menuSmall"));
            b.setPosition(buttonOrigin.x - b.getSprite().getBounds().l / 2, buttonOrigin.y + buttons.size() * (15 + b.getSprite().getBounds().h));
            buttons.add(b);
        }

//RETURN BUTTON
        if (backbutton)
            backbutton(parentMenuId);

    }

    public void backbutton(int parentMenuId) {
        NormalButton n = new NormalButton("Go back", parentMenuId, newButtonSprite("menuSmall"));
        n.setPosition(0, MainMENU.HEIGHT - sprite.getBounds().h);
        buttons.add(n);
    }

    public ArrayList<MenuButton> getButtons() {
        return buttons;
    }

    public Text getTitle() {
        return title;
    }

    public void addComponent(boolean special, MenuComponent c) {
        if (special) {
            nbSpecialButton++;
            c.setPosition(specialButtonOrigin.x, specialButtonOrigin.y + nbSpecialButton * (10 + c.getSprite().getBounds().h));
        } else {
            c.setPosition(normalButtonOrigin.x, normalButtonOrigin.y + (buttons.size() - nbSpecialButton) * (10 + c.getSprite().getBounds().h));
        }
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public static Sprite newButtonSprite(String textureName) {
        return new Sprite(ResourceHandler.getTexture(textureName));
    }

    /**
     * Initialisation du menu, à utiliser Une (1) fois
     *
     * @param menulist la liste de menus à modifier
     * @param window   window game
     */


    public static void init(Menu[] menulist, GLFWWindow window) throws IOException {
//Main
        //  Pair<String,Integer>[][]correspondance= new Pair[15][10];//paire nombre de menu/nombre de boutons max
//MAPCHOICE

        menulist[MainMENU.MAPCHOICE] = new MapMenu();

        HashMap<String, Integer> correspondance = new HashMap<>();
        correspondance.put("Start", MainMENU.GAMEMODE);
        menulist[MainMENU.START] = new Menu("Welcome", MainMENU.START, new Vector2f(window.getDimension().x / 2, 100), correspondance, false);
        menulist[MainMENU.START].buttons.add(new QuitButton());
//GAMEMODE
        correspondance.clear();

        correspondance.put("Local game", MainMENU.LOCAL); //replaced by ToLobbyButton
        correspondance.put("Online game", MainMENU.ONLINE);
        menulist[MainMENU.GAMEMODE] = new Menu("Chose a game mode", MainMENU.START, new Vector2f(window.getDimension().x / 2, 100), correspondance, true);

//LOCAL
        ToLobbyButton t = new ToLobbyButton("Local game", (byte) 0);
        t.setSprite(menulist[MainMENU.GAMEMODE].buttons.get(1).getSprite());
        t.setPosition(menulist[MainMENU.GAMEMODE].buttons.get(1).getSprite().getX(), menulist[MainMENU.GAMEMODE].buttons.get(1).getSprite().getY());
        menulist[MainMENU.GAMEMODE].buttons.set(1, t);

//ONLINE
        correspondance.clear();
        correspondance.put("Host", MainMENU.HOST);
        correspondance.put("Join", MainMENU.JOIN);

        menulist[MainMENU.ONLINE] = new Menu("Online", MainMENU.GAMEMODE, new Vector2f(MainMENU.WIDTH / 2, 100), correspondance, true);
//HOST

        ToLobbyButton host = new ToLobbyButton("Host game", (byte) 1);
        host.setSprite(menulist[MainMENU.ONLINE].buttons.get(0).getSprite());
        host.setPosition(menulist[MainMENU.ONLINE].buttons.get(0).getSprite().getX(), menulist[MainMENU.ONLINE].buttons.get(0).getSprite().getY());
        menulist[MainMENU.ONLINE].buttons.set(0, host);

//JOIN

        ToLobbyButton join = new ToLobbyButton("Join game", (byte) 2);
        join.setSprite(menulist[MainMENU.ONLINE].buttons.get(1).getSprite());
        join.setPosition(menulist[MainMENU.ONLINE].buttons.get(1).getSprite().getX(), menulist[MainMENU.ONLINE].buttons.get(1).getSprite().getY());
        menulist[MainMENU.ONLINE].getButtons().set(1, join);


//MAKESQUAD

        menulist[MainMENU.MAKESQUAD] = null;

//SCORE
        correspondance.clear();
        correspondance.put("Main menu", MainMENU.GAMEMODE);
        correspondance.put("Back to lobby", MainMENU.LOBBY);
        menulist[MainMENU.SCORE] = new Menu("Game Over", MainMENU.GAMEMODE, new Vector2f(MainMENU.WIDTH / 2, 100), correspondance, false);

    }

    public void update() {
    }
}

