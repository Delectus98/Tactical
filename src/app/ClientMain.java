package app;


import Graphics.ConstShader;
import System.*;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.network.ClientImpl;
import app.network.GameRegistration;
import app.play.ClientGame;
import app.units.SoldierUnit;
import org.lwjgl.opengl.GL20;
import util.ResourceHandler;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(new VideoMode(500, 500), "Tactical Client");
        window.setFrameRateLimit(40);

        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "character");
        ResourceHandler.loadTexture("res/ammo.png", "ammo");

        ResourceHandler.loadTexture("Sprites/FX/Explosion.png", "explosion");

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

        ResourceHandler.loadFont("res/font.ttf", 20, "default");

        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);

        ResourceHandler.loadSound("res/sounds/sniper.wav", "sniper");
        ResourceHandler.loadSound("res/sounds/assault.wav", "assault");
   //     ResourceHandler.loadSound("res/sounds/grenade.wav", "grenade");
   //     ResourceHandler.loadSound("res/sounds/uppercut.wav", "uppercut");


        Player p1 = new Player("P1");
        Unite unite = new SoldierUnit(Team.MAN);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);
        Unite unite1 = new SoldierUnit(Team.MAN);
        unite1.setTeam(Team.MAN);
        p1.addUnite(unite1);
        Player p2 = new Player("P2");
        Unite unite2 = new SoldierUnit(Team.APE);
        unite2.setTeam(Team.APE);
        p2.addUnite(unite2);
        Unite unite3 = new SoldierUnit(Team.APE);
        unite3.setTeam(Team.APE);
        p2.addUnite(unite3);

        ClientImpl client;
        Map map = new MapImpl(MapList.DemoField);
        try {
            client = new ClientImpl("localhost", 25565, GameRegistration.instance);

            Thread.sleep(1000);

            Game game = new ClientGame(client, new Player[]{p1, p2}, 1, map, window);

            //start game
            game.start();

            Clock clock = new Clock();

            //float milliseconds = 0;
            while (window.isOpen())
            {
                Time elapsed = clock.restart();

                Event event;
                while ((event = window.pollEvents()) != null)
                {
                    // updates window events (resize, keyboard text input, ...)
                    game.handle(event);

                    if (event.type == Event.Type.CLOSE)
                    {
                        ResourceHandler.free();
                        window.close();
                        client.close();
                        System.exit(0);
                    }
                }
                window.clear();
                //Menu principal:

                //Game Menu:  game is running
                if (!game.isFinished())
                {
                    game.update(elapsed);
                    game.draw(window);
                }
                window.display();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
