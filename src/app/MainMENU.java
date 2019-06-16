package app;

import Graphics.Color;
import Graphics.ConstShader;
import Graphics.FloatRect;
import Graphics.Vector2i;
import System.*;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.menu.Buttons.MenuButton;
import app.menu.Buttons.SpecialButton;
import app.menu.Menu;
import app.play.LocalhostGame;
import org.lwjgl.opengl.GL20;
import util.ResourceHandler;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class MainMENU {
    public static Map[] availableMaps;

    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    public static final int START = 0,
            GAMEMODE = 1, LOCAL = 2, ONLINE = 3,
            HOST = 4, JOIN = 5, MAKESQUAD = 6,
            MAPCHOICE = 7, SCORE = 9,
            QUIT = 10, GAME = 11, LOADING = 12;

    //public static int  LOBBY = 4;
    //Warning; online host et join doivent rester à coté dans cet ordre, à coté. sinon bug au niveau de LocalLobby.
    public enum STATE {
        MENU,
        GAME
    }

    public static int LOBBY = LOCAL;
    public static Menu[] menulist = new Menu[15];
    public static STATE state = STATE.MENU;
    public static int currentMenu = START;//parmi la liste au dessus
    public static Game currentGame;
    public static GLFWWindow window;

    public static void main(String[] args) throws IOException {
        window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
        glfwMaximizeWindow(window.getGlId());
        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "character");
        ResourceHandler.loadTexture("res/ammo.png", "ammo");
        ResourceHandler.loadTexture("Sprites/FX/Explosion.png", "explosion");
        ResourceHandler.loadFont("res/font.ttf", 20,"default");

        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);


        availableMaps = new Map[]{
                new MapImpl(MapList.Battlefield1),
                new MapImpl(MapList.Battlefield2),
                new MapImpl(MapList.Battlefield3),
                new MapImpl(MapList.Example1)
        };

        Mouse mousse = new Mouse(window);
        Clock clock = new Clock();
        boolean isClicking = false;
        Menu.init(menulist, window);


        while (window.isOpen()) {
            Time elapsed = clock.restart();


            Event event;
            while ((event = window.pollEvents()) != null) {
                // updates window events (resize, keyboard text input, ...)
                currentGame.handle(event);

                if (event.type == Event.Type.CLOSE) {
                    ResourceHandler.free();
                    window.close();
                }
            }

            window.clear(Color.Cyan);

//If STATE=MENU
            if (state == STATE.MENU) {
                window.draw(getCurrentMenu().getTitle());//TODO Getbackground

//DRAW BUTTONS
                for (MenuButton b : getCurrentMenu().getButtons()) {
                    if (b instanceof SpecialButton) {
                        ((SpecialButton) b).checkIfButtonReady();
                    }
                    window.draw(b.getShape());
                    window.draw(b.getText());
                }

//CHECK BUTTON CLICKED
                if (mousse.isButtonPressed(Mouse.Button.Left) && !isClicking) {

                    for (MenuButton b : getCurrentMenu().getButtons()) {//éléments à afficher du menu getcurrentmenu
                        if (b.collide(mousse.getRelativePosition())) {
                            b.clicked();
                            // System.out.println("Menu: " +getCurrentMenu().getTitle().getString());
                        }
                    }
                }
                isClicking = mousse.isButtonPressed(Mouse.Button.Left);//TODO améliorer vers: clicker sur un élément et y aller si relache sur le même
//STATE = GAME
            } else {
                if (!currentGame.isFinished())
                {
                    currentGame.update(elapsed);
                    currentGame.draw(window);
                }
            }
            window.display();


        }


    }


    private static Menu getCurrentMenu() {
        return menulist[currentMenu];
    }


    //TODO VIRER CETTE MERDE (tests) à refaire avec le système de lobby
    public static Game demo(GLFWWindow window) throws IOException {

        Map map = new MapImpl(MapList.Battlefield3);
        //Map map = new MapImpl(mapInfo);
        Player p1 = new Player("P1");
        Unite unite = new Main.UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        unite.setMapPosition(new Vector2i(13, 12));
        unite.getSprite().setPosition(64*13, 64*12);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);
        Unite unite1 = new Main.UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        unite1.setMapPosition(new Vector2i(16, 12));
        unite1.getSprite().setPosition(64*16, 64*12);
        unite1.setTeam(Team.MAN);
        p1.addUnite(unite1);
        Player p2 = new Player("P2");
        Unite unite2 = new Main.UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        unite2.getSprite().setPosition(256, 128);
        unite2.setMapPosition(new Vector2i(4, 2));
        unite2.setTeam(Team.APE);
        p2.addUnite(unite2);
        Unite unite3 = new Main.UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        unite3.setMapPosition(new Vector2i(1, 12));
        unite3.getSprite().setPosition(64*1, 64*12);
        unite3.setTeam(Team.MAN);
        p2.addUnite(unite3);

        return  new LocalhostGame(window, p1, p2, map);
    }
}
