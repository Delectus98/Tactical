package app;

import Graphics.*;
import System.*;
import app.animations.SpriteAnimation;
import app.map.Map;
import app.map.MapImpl;
import app.map.MapList;
import app.play.LocalhostGame;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;
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
            return sprite.getTexture();
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
        ResourceHandler.loadFont("res/font.ttf", 20, "default");

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

        ResourceHandler.loadShader("res/shader/default.vert", "res/shader/shining.frag", "shining");
        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);

        Map map = new MapImpl(MapList.DemoField);
        //Map map = new MapImpl(mapInfo);
        Player p1 = new Player("P1");
        //Unite unite = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        Unite unite = new SoldierUnit(Team.MAN);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);
        //Unite unite1 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        Unite unite1 = new SoldierUnit(Team.MAN);
        unite1.setTeam(Team.MAN);
        p1.addUnite(unite1);
        Player p2 = new Player("P2");
        //Unite unite2 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        Unite unite2 = new SoldierUnit(Team.APE);
        unite2.setTeam(Team.APE);
        p2.addUnite(unite2);
        //Unite unite3 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        Unite unite3 = new MarksmanUnit(Team.APE);
        unite3.setTeam(Team.APE);
        p2.addUnite(unite3);

        Game current = new LocalhostGame(window, p1, p2, map);

        //start game
        current.start();

        Clock clock = new Clock();

        //float milliseconds = 0;
        while (window.isOpen())
        {
            Time elapsed = clock.restart();

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
            window.clear();
            //Menu principal:

            //Game Menu:  game is running
            if (!current.isFinished())
            {
                current.update(elapsed);
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
