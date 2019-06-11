package app;

import Graphics.*;
import System.*;
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
            return 10;
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

        @Override
        public short getMaximumPoints() {
            return 20;
        }

        @Override
        public short getSparePoints()
        {
            return 20;
        }

        @Override
        public void resetTurn()
        {

        }

        @Override
        public ConstTexture getSpriteSheet() {
            return null;
        }

        @Override
        public void setTeam(Team team)
        {

        }

        @Override
        public Team getTeam()
        {
            return null;
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
        ResourceHandler.loadFont("res/font.ttf", 20,"default");

        ConstShader shader = ResourceHandler.loadShader("res/shader/default.vert", "res/shader/grisant.frag", "grey");
        shader.bind();
        GL20.glUniform1f(shader.getUniformLocation("colorRatio"), 0.2f);

        /*MapInfo mapInfo = MapInfo.loadFromFile("Small Battlefield.build");
        MapInfo mapInfo = MapInfo.loadFromFile("Medium Battlefield.build");*/
        //MapInfo mapInfo = MapInfo.loadFromFile("Huge Battlefield.build");

        Map map = new MapImpl(MapList.Battlefield3);
        //Map map = new MapImpl(mapInfo);
        Player p1 = new Player("P1");
        Unite unite = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(0,0,64,64));
        unite.setMapPosition(new Vector2i(13, 12));
        unite.getSprite().setPosition(64*13, 64*12);
        unite.setTeam(Team.MAN);
        p1.addUnite(unite);
        Player p2 = new Player("P2");
        Unite unite2 = new UniteTest(ResourceHandler.getTexture("character"), new FloatRect(64,0,64,64));
        unite2.getSprite().setPosition(256, 128);
        unite2.setMapPosition(new Vector2i(4, 2));
        unite2.setTeam(Team.APE);
        p2.addUnite(unite2);
        Game current = new LocalhostGame(window, p1, p2, map);


        /*Weapon weapon = new Grenade();
        Impact impactTest = weapon.getImpactZone(new Vector2i(), new Vector2i(1,1), map);
        for (int i=0 ; i < impactTest.getTileCount() ; ++i) {
            Vector2i v =impactTest.getTileCoord(i);
            System.out.println(v);
            if (map.getWorld()[v.x][v.y].getFloor() != null) map.getWorld()[v.x][v.y].getFloor().setFillColor(Color.Blue);
            if (map.getWorld()[v.x][v.y].getStruct() != null) map.getWorld()[v.x][v.y].getStruct().setFillColor(Color.Blue);
        }*/

        Projectile projectile = new BulletProjectile("ammo", new FloatRect(32,0, 32,32), new Vector2f(400,850), new Vector2f(150,50));
        //start game
        current.start();

        Clock clock = new Clock();

        //float milliseconds = 0;
        while (window.isOpen())
        {
            Time elapsed = clock.restart();
            /*milliseconds += clock.getElapsed().asMilliseconds();
            ResourceHandler.getShader("grey").bind();
            GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), ((int)(milliseconds) % 1000) / 1000.f);*/

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

            projectile.update(elapsed);

            window.clear();
            //Menu principal:

            //Game Menu:  game is running
            if (!current.isFinished())
            {
                current.update(elapsed);
                current.draw(window);
                projectile.draw(window);
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
