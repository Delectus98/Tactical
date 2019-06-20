package app;


import Graphics.ConstShader;
import System.*;
import Graphics.FloatRect;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.network.ClientImpl;
import app.network.GameRegistration;
import app.network.ServerImpl;
import app.play.ClientGame;
import app.play.ServerGame;
import app.units.SoldierUnit;
import org.lwjgl.opengl.GL20;
import util.ResourceHandler;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(new VideoMode(500, 500), "Tactical Client");

        ResourceHandler.loadTexture("res/floor.png", "res/floor.png");
        ResourceHandler.loadTexture("res/wall.png", "res/wall.png");
        ResourceHandler.loadTexture("res/character.png", "character");
        ResourceHandler.loadTexture("res/ammo.png", "ammo");
        ResourceHandler.loadTexture("Sprites/FX/Explosion.png", "explosion");
        ResourceHandler.loadFont("res/font.ttf", 20,"default");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/oldMan.png", "oldMan");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/gurl.png", "gurl");
        ResourceHandler.loadTexture("Sprites/Characterrs/Animation/Walk/bigDude.png", "bigDude");

        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);


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
