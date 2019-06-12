package app.menu;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Vector2f;
import app.MainMENU;

import java.util.ArrayList;
import java.util.HashMap;


public class Menu {
    private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
    private String title;

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

    public Menu(int width, int height, String title, int parentMenuId, int buttonWidth, int buttonHeight, Vector2f buttonOrigin, HashMap<String, Integer> correspondances) {
        this.title = title;

        //For correspondance créer nouveau boutton avec la shape.
        for (String p : correspondances.keySet()) {

            MenuButton b = new NormalButton(p, correspondances.get(p),
                    new RectangleShape(buttonOrigin.x + buttonWidth / 2, buttonOrigin.y + buttonHeight / 2 + buttons.size() * 2 * buttonHeight, buttonWidth, buttonHeight));

            b.getShape().setFillColor(Color.Blue);
            buttons.add(b);
        }

//RETURN BUTTON
        MenuButton goBack = new NormalButton("Go back", parentMenuId, new RectangleShape(0, height - buttonHeight, buttonWidth, buttonHeight));
        goBack.getShape().setFillColor(Color.Blue);
        buttons.add(goBack);
    }


    public ArrayList<MenuButton> getButtons() {
        return buttons;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Initialisation du menu, à utiliser Une (1) fois
     *
     * @param menulist la liste de menus à modifier
     * @param width    paramètre de la fenêtre
     * @param height   paramètre de la fenêtre
     */
    public static void init(ArrayList<Menu> menulist, int width, int height) {
//Main
        //  Pair<String,Integer>[][]correspondance= new Pair[15][10];//paire nombre de menu/nombre de boutons max


        HashMap<String, Integer> correspondance = new HashMap<>();
        correspondance.put("Start", MainMENU.GAMEMODE);
        menulist.add(MainMENU.START, new Menu(width, height, "Welcome", MainMENU.START, 100, 40, new Vector2f(width / 2, 100), correspondance));

//GAMEMODE
        correspondance.clear();
        correspondance.put("Local game", MainMENU.LOCAL);
        correspondance.put("Online game", MainMENU.ONLINE);
        menulist.add(MainMENU.GAMEMODE, new Menu(width, height, "Chose a game mode", MainMENU.START, 100, 40, new Vector2f(width / 2, 100), correspondance));


//LOCAL
        correspondance.clear();
        menulist.add(MainMENU.LOCAL, new Lobby(width, height, 100, 40, (byte) 0));

//ONLINE
        correspondance.clear();
        correspondance.put("Join", MainMENU.JOIN);
        correspondance.put("Host", MainMENU.HOST);
        menulist.add(MainMENU.ONLINE, new Menu(width, height, "Online", MainMENU.GAMEMODE, 100, 40, new Vector2f(width / 2, 100), correspondance));
//HOST
        correspondance.clear();
        menulist.add(MainMENU.HOST, new Lobby(width, height, 100, 40, (byte) 1));

//JOIN
        correspondance.clear();
        menulist.add(MainMENU.JOIN, new Lobby(width, height, 100, 40, (byte) 2));


    }
//Close          correspondance.clear();
    //Aussi, chaque menu ou presque aura des composants particuliers: champs à remplir, makesquad, select map ...


}

