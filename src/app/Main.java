package app;

import Graphics.*;
import System.*;
import app.animations.SpriteAnimation;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.play.LocalhostGame;
import app.weapon.*;
import org.lwjgl.opengl.GL20;
import util.ResourceHandler;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class Main
{

    public static class UniteTest extends Unite {
        private short pa = getMaximumPoints();

        public UniteTest(ConstTexture t, FloatRect rect){
            sprite = new Sprite(t);
            sprite.setTextureRect(rect.l, rect.t, rect.w, rect.h);

            super.primary = new CombatRifle();
            super.secondary = new Grenade();
            super.melee = new Blade();

            super.hp = 50;
        }

        @Override
        public boolean isDead() {
            return hp <= 0;
        }

        @Override
        public short getHp() {
            return hp;
        }

        @Override
        public short getFov() {
            return 10;
        }

        @Override
        public Weapon getPrimary() {
            return primary;
        }

        @Override
        public Weapon getSecondary() {
            return secondary;
        }

        @Override
        public Weapon getMelee() {
            return melee;
        }

        @Override
        public Sprite getSprite() {
            return sprite;
        }

        @Override
        public void setMapPosition(Vector2i coords) {
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

        public void removePA(short cost) {
            pa = (short)Math.max(0, pa - cost);
        }

        @Override
        public short getMaximumPoints() {
            return 20;
        }

        @Override
        public short getSparePoints()
        {
            return pa;
        }

        @Override
        public void resetTurn()
        {
            pa = getMaximumPoints();
        }

        @Override
        public ConstTexture getSpriteSheet() {
            return null;
        }

        @Override
        public void setTeam(Team team)
        {
            this.team = team;
        }

        @Override
        public Team getTeam()
        {
            return team;
        }

        @Override
        public void draw(RenderTarget target) {
            target.draw(sprite);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
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

        Map map = new MapImpl(MapList.Battlefield3);
        //Map map = new MapImpl(mapInfo);
        Player p1 = new Player("P1");
        Unite unite = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        unite.setMapPosition(new Vector2i(13, 12));
        unite.getSprite().setPosition(64*13, 64*12);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);
        Unite unite1 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        unite1.setMapPosition(new Vector2i(16, 12));
        unite1.getSprite().setPosition(64*16, 64*12);
        unite1.setTeam(Team.MAN);
        p1.addUnite(unite1);
        Player p2 = new Player("P2");
        Unite unite2 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        unite2.getSprite().setPosition(256, 128);
        unite2.setMapPosition(new Vector2i(4, 2));
        unite2.setTeam(Team.APE);
        p2.addUnite(unite2);
        Unite unite3 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        unite3.setMapPosition(new Vector2i(1, 12));
        unite3.getSprite().setPosition(64*1, 64*12);
        unite3.setTeam(Team.MAN);
        p2.addUnite(unite3);

        SpriteAnimation animation = new SpriteAnimation(ResourceHandler.getTexture("explosion"), new FloatRect(0,0,192,192), Time.seconds(1), 0, 15);
        Sprite sprite = new Sprite();
        animation.apply(sprite);

        Game current = new LocalhostGame(window, p1, p2, map);

        //start game
        current.start();

        Clock clock = new Clock();

        //float milliseconds = 0;
        while (window.isOpen())
        {
            Time elapsed = clock.restart();

          /*  animation.update(elapsed);
            animation.apply(sprite);

            sprite.rotate((float)elapsed.asSeconds());
            sprite.setOrigin(sprite.getBounds().w / 2.f, sprite.getBounds().h / 2.f);
            sprite.setPosition(192,192);*/
            Event event;
            while ((event = window.pollEvents()) != null)
            {
                // updates window events (resize, keyboard text input, ...)
                current.handle(event);

                if (event.type == Event.Type.CLOSE)
                {
                    ResourceHandler.free();
                    window.close();
                }
            }

          /*  if (animation.isLastImage())
                window.clear();
            else*/
                window.clear(Color.Red);
            //Menu principal:

            //Game Menu:  game is running
            if (!current.isFinished())
            {
                current.update(elapsed);
                current.draw(window);
            }
          //  window.draw(sprite);
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
