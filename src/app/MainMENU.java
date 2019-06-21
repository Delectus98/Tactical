package app;

import Graphics.Color;
import Graphics.ConstShader;
import Graphics.Vector2i;
import System.*;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.menu.Buttons.MenuButton;
import app.menu.Buttons.SpecialButton;
import app.menu.MakeSquad;
import app.menu.Menu;
import app.play.LocalhostGame;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;
import org.lwjgl.opengl.GL20;
import util.ResourceHandler;

import java.io.IOException;

public class MainMENU {
    public static Map[] availableMaps;
    public static Unite[] availableUnits;

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

    public static Game demo(GLFWWindow window) throws IOException {

        Map map = new MapImpl(MapList.DemoField);
        //Map map = new MapImpl(mapInfo);
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");

        Unite unite = new SoldierUnit(Team.MAN);
        unite.setMapPosition(new Vector2i(1, 8));
        unite.getSprite().setPosition(64 * 1, 64 * 8);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);

        Unite unite0 = new SoldierUnit(Team.MAN);
        unite0.setMapPosition(new Vector2i(0, 8));
        unite0.getSprite().setPosition(64 * 0, 64 * 8);
        p1.addUnite(unite0);

        //player2
        Unite unite1 = new MarksmanUnit(Team.MAN);
        unite1.setMapPosition(new Vector2i(1, 9));
        unite1.getSprite().setPosition(64 * 1, 64 * 9);
        p1.addUnite(unite1);
        Unite unite2 = new SoldierUnit(Team.APE);
        unite2.setMapPosition(new Vector2i(22, 8));
        unite2.getSprite().setPosition(64 * 22, 64 * 8);
        p2.addUnite(unite2);

        Unite unite3 = new SoldierUnit(Team.APE);
        unite3.setMapPosition(new Vector2i(22, 9));
        unite3.getSprite().setPosition(64 * 22, 64 * 9);
        p2.addUnite(unite3);

        Unite unite4 = new MarksmanUnit(Team.APE);
        unite4.setMapPosition(new Vector2i(21, 8));
        unite4.getSprite().setPosition(64 * 21, 64 * 8);
        p2.addUnite(unite4);

        return new LocalhostGame(window, p1, p2, map);
    }

    public static void main(String[] args) throws IOException {
        window = new GLFWWindow(new VideoMode(WIDTH,HEIGHT), "Tactical", WindowStyle.DEFAULT);

       // glfwMaximizeWindow(window.getGlId());
        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "character");
        ResourceHandler.loadTexture("res/ammo.png", "ammo");
        ResourceHandler.loadTexture("Sprites/FX/Explosion.png", "explosion");
        ResourceHandler.loadFont("res/font.ttf", 20, "default");

        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/oldMan.png", "oldMan");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/otherChar.png", "otherChar");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/gurl.png", "gurl");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/bigDude.png", "bigDude");

        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);


        availableMaps = new Map[]{
                new MapImpl(MapList.Battlefield1),
                new MapImpl(MapList.Battlefield2),
                new MapImpl(MapList.Battlefield3),
                //new MapImpl(MapList.Example1),
                new MapImpl(MapList.DemoField)
        };
        availableUnits = new Unite[]{
                new SoldierUnit(Team.APE),
                new MarksmanUnit(Team.APE),
                new SoldierUnit(Team.MAN),
                new MarksmanUnit(Team.MAN)
        };

        Mouse mousse = new Mouse(window);
        Clock clock = new Clock();
        boolean isClicking = false;
        Menu.init(menulist, window);


        //RESIZE

        while (window.isOpen()) {
            Time elapsed = clock.restart();


            Event event;
            while ((event = window.pollEvents()) != null) {
                // updates window events (resize, keyboard text input, ...)
                if(currentGame!=null)
                    currentGame.handle(event);//TODO si currengame=null

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
                    }//todo getsprite
                    window.draw(b.getShape());
                    window.draw(b.getText());

                }

//CHECK BUTTON CLICKED
                if (mousse.isButtonPressed(Mouse.Button.Left) && !isClicking) {

                    for (MenuButton b : getCurrentMenu().getButtons()) {//éléments à afficher du menu getcurrentmenu
                        if (b.collide(mousse.getRelativePosition())) {
                            b.clicked();
                        }
                    }
                    if(currentMenu==MAKESQUAD){
                        ((MakeSquad)menulist[MAKESQUAD]).update(((MakeSquad)menulist[MAKESQUAD]).player);
                    }
                }
                isClicking = mousse.isButtonPressed(Mouse.Button.Left);//TODO améliorer vers: clicker sur un élément et y aller si relache sur le même
//STATE = GAME
            } else {
                if (!currentGame.isFinished()) {
                    currentGame.update(elapsed);
                    currentGame.draw(window);
                } else {
                    MainMENU.state = STATE.MENU;
                    window.setDimension(new VideoMode(1280,720));
                    MainMENU.currentMenu = GAMEMODE;
                }
            }
            window.display();


        }


    }


    private static Menu getCurrentMenu() {
        return menulist[currentMenu];
    }


}
