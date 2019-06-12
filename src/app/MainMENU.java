package app;

import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.menu.Menu;
import System.*;
import Graphics.*;
import app.menu.MenuButton;
import app.play.LocalhostGame;
import util.ResourceHandler;

import java.io.IOException;
import java.util.ArrayList;

import static app.Main.*;

public class MainMENU {
    private static int WIDTH = 1280;
    private static int HEIGHT = 720;
    public static final int START = 0,
            GAMEMODE = 1, LOCAL = 2, ONLINE = 3,
            HOST = 4, JOIN = 5, MAKESQUAD = 6,
            LOBBY = 7, MAPCHOICE = 8, SCORE = 9,
            QUIT = 10, GAME = 11, LOADING = 12;

    //Warning; online host et join doivent rester à coté dans cet ordre, à coté. sinon bug au niveau de Lobby.
    public enum STATE {
        MENU,
        GAME
    }

    private static ArrayList<Menu> menulist = new ArrayList<>();
    public static STATE state = STATE.MENU;
    public static int currentMenu = START;//parmi la liste au dessus
    public static Game currentGame;

    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(new VideoMode(WIDTH, HEIGHT), "IsticLWJGL", WindowStyle.DEFAULT);
    /*RectangleShape shape = new RectangleShape(245,245, 10,10);
    shape.setFillColor(Color.Red);
    */
        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "res/character.png");
        ResourceHandler.loadFont("res/font.ttf",20, "default");

        Mouse mousse = new Mouse(window);
        Menu.init(menulist, WIDTH, HEIGHT);

        currentGame = bleh(window);
        Clock clock = new Clock();
        boolean isClicking = false;
        while (window.isOpen()) {
            ///gestion des évenements
            Event event;

            while ((event = window.pollEvents()) != null) {
                if (event.type == Event.Type.CLOSE) {
                    window.close();
                    System.exit(0);
                }
            }
            ///affichage menu
            window.clear(Color.Cyan);
            if (state == STATE.MENU) {
                //System.out.println(menulist.get(currentMenu).getTitle());
//DRAW BUTTONS
                for (MenuButton b : getCurrentMenu().getButtons()) {
                    window.draw(b.getShape());
                    //  window.draw(b.getText());
                }

//CHECK BUTTON CLICKED
                if (mousse.isButtonPressed(Mouse.Button.Left) && !isClicking) {

                    for (MenuButton b : getCurrentMenu().getButtons()) {//éléments à afficher du menu getcurrentmenu
                        if (b.collide(mousse.getRelativePosition())) {
                            b.clicked();
                            System.out.println("Menu: " + menulist.get(currentMenu).getTitle());
                        }
                    }
                }
                isClicking = mousse.isButtonPressed(Mouse.Button.Left);//TODO améliorer vers: clicker sur un élément et y aller si relache sur le même
            } else {//STATE = GAME
                if (!currentGame.isFinished()) {
                    currentGame.update(clock.restart());
                    currentGame.draw(window);
                }
            }
            window.display();


        }


    }


    private static Menu getCurrentMenu() {
        return menulist.get(currentMenu);
    }


    //TODO VIRER CETTE MERDE (tests) à refaire avec le système de lobby
    private static Game bleh(GLFWWindow window) throws IOException {


        Map map = new MapImpl(MapList.Battlefield3);
        Player p1 = new Player("P1");
        Unite unite = new UniteTest(ResourceHandler.getTexture("res/character.png"), new FloatRect(0, 0, 64, 64));
        unite.setMapPosition(new Vector2i(1, 1));
        unite.getSprite().setPosition(64, 64);
        p1.addUnite(unite);
        Player p2 = new Player("P2");
        Unite unite2 = new UniteTest(ResourceHandler.getTexture("res/character.png"), new FloatRect(64, 0, 64, 64));
        unite2.getSprite().setPosition(256, 128);
        unite2.setMapPosition(new Vector2i(4, 2));
        p2.addUnite(unite2);
        Game current = new LocalhostGame(window, p1, p2, map);

        //start game
        current.start();

        return current;
    }
}
