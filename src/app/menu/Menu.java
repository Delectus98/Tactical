package app.menu;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Text;
import Graphics.Vector2f;
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
    private int windowWidth;
    private int windoHeight;
    private Vector2f specialButtonOrigin;
    private Vector2f normalButtonOrigin;
    private int nbSpecialButton;

    public Menu(GLFWWindow window, String title, int parentMenuId, Vector2f normalButtonOrigin, Vector2f specialButtonOrigin, Vector2f titleposition, boolean backbutton) {
        this.title = new Text(ResourceHandler.getFont("default"), title);
        this.title.setPosition(titleposition.x, titleposition.y);
        this.windoHeight = window.getDimension().y;
        this.windowWidth = window.getDimension().x;
        this.normalButtonOrigin = normalButtonOrigin;
        this.specialButtonOrigin = specialButtonOrigin;


        if (backbutton)
            backbutton(parentMenuId, windowWidth, windoHeight, 150, 30);

    }

    /**
     * Menu constructor
     *
     * @param title           Displayed title of the menu
     * @param parentMenuId    where to go back if needed
     * @param buttonWidth     width of a button in this menu
     * @param buttonHeight    height of a button in this menu
     * @param buttonOrigin    origin of the buttonList
     * @param correspondances Correspondance between button and menu.
     */

    public Menu(int width, int height, String title, int parentMenuId, int buttonWidth, int buttonHeight, Vector2f buttonOrigin, HashMap<String, Integer> correspondances, boolean backbutton) {
        this.title = new Text(ResourceHandler.getFont("default"), title);
        this.title.setPosition(width / 10, height / 10);

        //For correspondance créer nouveau boutton avec la shape.
        for (String p : correspondances.keySet()) {

            MenuButton b = new NormalButton(p, correspondances.get(p),
                    new RectangleShape(buttonOrigin.x + buttonWidth / 2, buttonOrigin.y + buttonHeight / 2 + buttons.size() * 2 * buttonHeight, buttonWidth, buttonHeight));

           // b.getShape().setFillColor(Color.Blue);
            buttons.add(b);
        }

//RETURN BUTTON
        if (backbutton)
            backbutton(parentMenuId, width, height, buttonWidth, buttonHeight);

    }

    public void backbutton(int parentMenuId, int windowWidth, int windowHeight, int buttonWidth, int buttonHeight) {
        NormalButton n = new NormalButton("Go back", parentMenuId, new RectangleShape(0, windowHeight - buttonHeight, buttonWidth, buttonHeight));
        buttons.add(n);
        n.shape.setFillColor(Color.Blue);
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
            c.setPosition(specialButtonOrigin.x, specialButtonOrigin.y + nbSpecialButton * (10 + c.getShape().getBounds().h));
        } else {
            c.setPosition(normalButtonOrigin.x, normalButtonOrigin.y + (buttons.size() - nbSpecialButton) * (10 + c.getShape().getBounds().h));
        }
    }

    /**
     * Initialisation du menu, à utiliser Une (1) fois
     *
     * @param menulist la liste de menus à modifier
     * @param window window game
     */
    public static void init(Menu[] menulist, GLFWWindow window) throws IOException {
//Main
        //  Pair<String,Integer>[][]correspondance= new Pair[15][10];//paire nombre de menu/nombre de boutons max
//MAPCHOICE

        menulist[MainMENU.MAPCHOICE] = new MapMenu(window.getDimension().x, window.getDimension().y);

        HashMap<String, Integer> correspondance = new HashMap<>();
        correspondance.put("Start", MainMENU.GAMEMODE);
        menulist[MainMENU.START] = new Menu(window.getDimension().x, window.getDimension().y, "Welcome", MainMENU.START, 100, 40, new Vector2f(window.getDimension().x / 2, 100), correspondance, false);
        menulist[MainMENU.START].buttons.add(new QuitButton());
//GAMEMODE
        correspondance.clear();
        correspondance.put("Local game", MainMENU.LOCAL); //replaced by ToLobbyButton
        correspondance.put("Online game", MainMENU.ONLINE);
        menulist[MainMENU.GAMEMODE] = new Menu(window.getDimension().x, window.getDimension().y, "Chose a game mode", MainMENU.START, 100, 40, new Vector2f(window.getDimension().x / 2, 100), correspondance, true);


        ToLobbyButton t = new ToLobbyButton("Local game", (byte) 0);
        t.shape = menulist[MainMENU.GAMEMODE].buttons.get(1).getShape();
        t.setPosition(menulist[MainMENU.GAMEMODE].buttons.get(1).shape.getX(), menulist[MainMENU.GAMEMODE].buttons.get(1).shape.getY());
        menulist[MainMENU.GAMEMODE].buttons.set(1, t);


//LOCAL
       // menulist[MainMENU.LOCAL] =new LocalLobby(window,);
        menulist[MainMENU.LOCAL] = new LocalLobby(window.getDimension().x, window.getDimension().y, 100, 40);

//ONLINE
        correspondance.clear();
        correspondance.put("Join", MainMENU.JOIN);
        correspondance.put("Host", MainMENU.HOST);
        menulist[MainMENU.ONLINE] = new Menu(window.getDimension().x, window.getDimension().y, "Online", MainMENU.GAMEMODE, 100, 40, new Vector2f(window.getDimension().x / 2, 100), correspondance, true);
//HOST

        menulist[MainMENU.HOST] = null;// new LocalLobby(width, height, 100, 40, (byte) 1);

//JOIN

        menulist[MainMENU.JOIN] = null; //new LocalLobby(width, height, 100, 40, (byte) 2);


//Close          correspondance.clear();
        //Aussi, chaque menu ou presque aura des composants particuliers: champs à remplir, makesquad, select map ...

//MAKESQUAD

        menulist[MainMENU.MAKESQUAD] = null;



    }
}

