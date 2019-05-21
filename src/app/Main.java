package app;

import Graphics.*;
import System.*;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.play.LocalhostGame;
import util.ResourceHandler;

import java.io.IOException;

public class Main
{

    private static class UniteTest extends Unite {
        public UniteTest(ConstTexture t, FloatRect rect){
            sprite = new Sprite(t);
            sprite.setTextureRect(rect.l, rect.t, rect.w, rect.h);
        }

        @Override
        public boolean isDead() {
            return false;
        }

        @Override
        public short getHp() {
            return 0;
        }

        @Override
        public short getFov() {
            return 0;
        }

        @Override
        public Weapon getPrimary() {
            return null;
        }

        @Override
        public Weapon getSecondary() {
            return null;
        }

        @Override
        public Weapon getMelee() {
            return null;
        }

        @Override
        public Sprite getSprite() {
            return sprite;
        }

        @Override
        public void seMapPosition(Vector2i coords) {
            super.position = coords.clone();
        }

        @Override
        public Vector2i getMapPosition() {
            return super.position;
        }

        @Override
        public void takeDamages(int amount) {
            hp = (short)Math.max(0, hp - amount);
        }

        @Override
        public short getActionPoints() {
            return 0;
        }

        @Override
        public void draw(RenderTarget target) {
            target.draw(sprite);
        }
    }

    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
        //Game current = new LocalhostGame(window);

        ResourceHandler.loadTexture("res/floor.png");
        ResourceHandler.loadTexture("res/wall.png");
        ResourceHandler.loadTexture("res/character.png");

        Map map = new MapImpl(MapList.Battlefield);
        Player p1 = new Player("P1");
        Unite unite = new UniteTest(ResourceHandler.getTexture("res/character.png"), new FloatRect(0,0,64,64));
        unite.getSprite().setPosition(64, 64);
        unite.getSprite().setZLayer(-0.15f);
        p1.addUnite(unite);
        Player p2 = new Player("P2");
        Unite unite2 = new UniteTest(ResourceHandler.getTexture("res/character.png"), new FloatRect(64,0,64,64));
        unite2.getSprite().setPosition(256, 128);
        p2.addUnite(unite2);
        Game current = new LocalhostGame(window, p1, p2, map);

        //start game
        current.start();

        Clock clock = new Clock();

        while (window.isOpen())
        {
            Event event;
            while ((event = window.pollEvents()) != null)
            {
                // updates window events (resize, keyboard text input, ...)
                current.handle(event);

                if (event.type == Event.Type.CLOSE)
                {
                    window.close();
                }
            }
            window.clear(Color.Cyan);
            //Menu principal:
            //TODO

            //Game Menu:  game is running
            if (!current.isFinished())
            {
                current.update(clock.restart());
                current.draw(window);
            }
            window.display();
        }
    }
    //Menu principale

    //Creer partie

    //Initialiser partie

    // ( start game If(!Finished))
    // car initialisation
    //Afficher map pour que p1 place ses joueurs

    //idem pour j2

    //commencer partie pour j1

    //plus initialisation
}
