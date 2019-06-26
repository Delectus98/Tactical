package app;

import Graphics.Color;
import Graphics.ConstShader;
import System.*;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.menu.Buttons.MenuButton;
import app.menu.Buttons.SpecialButton;
import app.menu.Lobby;
import app.menu.MakeSquad;
import app.menu.Menu;
import app.sounds.Music;
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

    public static void main(String[] args) throws IOException {
        window = new GLFWWindow(new VideoMode(WIDTH, HEIGHT), "Tactical", WindowStyle.DEFAULT);

        Music.init();

        // glfwMaximizeWindow(window.getGlId());
        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "character");
        ResourceHandler.loadTexture("res/ammo.png", "ammo");
        ResourceHandler.loadTexture("Sprites/FX/Explosion.png", "explosion");
        ResourceHandler.loadFont("res/font.ttf", 20, "default");
        ResourceHandler.loadTexture("Sprites/Tiles/tileset.png", "tileset");
        ResourceHandler.loadTexture("Sprites/Tiles/furniture.png", "furniture");

        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/oldMan.png", "oldMan");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/otherChar.png", "otherChar");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/gurl.png", "gurl");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/bigDude.png", "bigDude");

        ResourceHandler.loadTexture("Sprites/UI/Character/bigDudeFace.png", "bigDudeAvt");
        ResourceHandler.loadTexture("Sprites/UI/Character/diffCharFace.png", "otherCharAvt");
        ResourceHandler.loadTexture("Sprites/UI/Character/gurlFace.png", "gurlAvt");
        ResourceHandler.loadTexture("Sprites/UI/Character/oldManFace.png", "oldManAvt");

        ResourceHandler.loadTexture("Sprites/UI/Character/faceFrame.png", "avtFrame");
        ResourceHandler.loadTexture("Sprites/UI/Character/faceBack.png", "avtBack");

        ResourceHandler.loadTexture("Sprites/UI/Buttons/shoot1.png", "shoot1");
        ResourceHandler.loadTexture("Sprites/UI/Buttons/shoot2.png", "shoot2");
        ResourceHandler.loadTexture("Sprites/UI/Buttons/knife.png", "knife");
        ResourceHandler.loadTexture("Sprites/UI/Buttons/skip.png", "skip");

        ResourceHandler.loadTexture("Sprites/UI/arr.png", "lower");
        ResourceHandler.loadTexture("Sprites/UI/uiBack.png", "uiBack");
        ResourceHandler.loadTexture("Sprites/UI/uiFrame.png", "backFrame");

        ResourceHandler.loadTexture("Sprites/Menu/Menubg.jpg", "menuBackground");
        ResourceHandler.loadTexture("Sprites/Menu/menuBig.png", "menuBig");
        ResourceHandler.loadTexture("Sprites/Menu/menuSmall.png", "menuSmall");
        ResourceHandler.loadTexture("Sprites/Menu/menuLarge.png", "menuLarge");
        ResourceHandler.loadTexture("Sprites/Menu/squadSlot.png", "squadSlot");

        ResourceHandler.loadTexture("Sprites/Menu/maps/bf1.jpg", "bf1");
        ResourceHandler.loadTexture("Sprites/Menu/maps/bf2.jpg", "bf2");
        ResourceHandler.loadTexture("Sprites/Menu/maps/bf3.jpg", "bf3");
        ResourceHandler.loadTexture("Sprites/Menu/maps/casino.jpg", "casino");
        ResourceHandler.loadTexture("Sprites/Menu/maps/demofield.jpg", "demofield");

        ResourceHandler.loadSound("res/sounds/sniper.wav", "sniper");
        ResourceHandler.loadSound("res/sounds/assault.wav", "assault");
        ResourceHandler.loadSound("res/sounds/grenade.wav", "grenade");//missing
        ResourceHandler.loadSound("res/sounds/uppercut.wav", "uppercut");
        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);


        availableMaps = new MapImpl[]{
                new MapImpl(MapList.Battlefield1),
                new MapImpl(MapList.Battlefield2),
                new MapImpl(MapList.Battlefield3),
                new MapImpl(MapList.DemoField),
                new MapImpl(MapList.Casino)
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
                if (currentGame != null)
                    currentGame.handle(event);//TODO si currengame=null

                if (event.type == Event.Type.CLOSE) {
                    ResourceHandler.free();
                    window.close();
                }
            }
            window.clear(Color.Black);

//If STATE=MENU
            if (state == STATE.MENU) {

                Music.updateMenuLoop();
//DRAW BUTTONS
                window.draw(getCurrentMenu().getSprite());
                window.draw(getCurrentMenu().getTitle());//TODO Getbackground
                for (MenuButton b : getCurrentMenu().getButtons()) {
                    if (b instanceof SpecialButton) {
                        ((SpecialButton) b).checkIfButtonReady();
                    }//todo getsprite
                    window.draw(b.getSprite());
                    window.draw(b.getText());

                }

//CHECK BUTTON CLICKED
                if (mousse.isButtonPressed(Mouse.Button.Left) && !isClicking) {

                    for (MenuButton b : getCurrentMenu().getButtons()) {//éléments à afficher du menu getcurrentmenu
                        if (b.collide(mousse.getRelativePosition())) {
                            b.clicked();
                        }
                    }
                    if (currentMenu == MAKESQUAD) {
                        ((MakeSquad) menulist[MAKESQUAD]).update(((MakeSquad) menulist[MAKESQUAD]).player);
                    }else if(currentMenu==LOBBY){
                      // menulist[LOBBY].update();
                    }
                }
                isClicking = mousse.isButtonPressed(Mouse.Button.Left);//TODO améliorer vers: clicker sur un élément et y aller si relache sur le même
//STATE = GAME
            } else {
                Music.updateLoop();
                if (!currentGame.isFinished()) {
                    currentGame.update(elapsed);
                    currentGame.draw(window);
                } else {
                    currentMenu = SCORE;
                    for (Player p : ((Lobby) menulist[LOBBY]).getPlayers())
                        p.getUnites().clear();
                    state = STATE.MENU;
                    window.setDimension(new VideoMode(1280, 720));
                    MainMENU.currentMenu = GAMEMODE;
                    Music.stopMusic();
                }
            }
            window.display();


        }


    }


    private static Menu getCurrentMenu() {
        return menulist[currentMenu];
    }


}
