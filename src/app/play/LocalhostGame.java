package app.play;

import Graphics.Color;
import Graphics.FloatRect;
import Graphics.Texture;
import Graphics.Vector2f;
import System.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.actions.Action;
import app.map.MapImpl;
import app.map.Tile;

import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class LocalhostGame extends Game {
    private Camera2D[] mapPlayerCam;
    private Camera2D[] hudPlayerCam;

    private Queue<Action> currentPlayerActions = new PriorityQueue<>();
    private boolean inAction = false;
    private Keyboard keyboard;
    private Mouse mouse;
    private boolean menuEchap = false;
    private Texture spritesheet;


    public LocalhostGame(GLFWWindow window) throws IOException {
        context = window;

        keyboard = new Keyboard(context);
        mouse = new Mouse(context);

        currentPlayer = 0;
        inAction = false;

        spritesheet = new Texture("res/spritesheet.png");

        final int size = 40;
        map = new MapImpl(size, size);

        for (int i = 0 ; i < size ; ++i) {
            for (int j = 0 ; j < size ; ++j) {
                //map.getWorld()[i][j] = new Tile(ResourceHandler.getTexture("floor"), );
                map.getWorld()[i][j] = new Tile(spritesheet, new FloatRect(0,0, 64, 64));
                map.getWorld()[i][j].getFloor().setPosition(i * 64, j * 64);
                map.getWorld()[i][j].getFloor().setFillColor(new Color(i / (float)size, j / (float)size, 1));
                //map.getWorld()[i][j].getS().setPosition(i * 64, j * 64);
            }
        }

        mapPlayerCam = new Camera2D[2];
        hudPlayerCam = new Camera2D[2];

        mapPlayerCam[0] = new Camera2D(new Vector2f(
                context.getDimension())//.mul(0.5f)
        );
        //mapPlayerCam[0].setZoom(0.05f);
        //mapPlayerCam[0].setCenter(new Vector2f(size * 64 / 2.f, size * 64 / 2.f));

        //hudPlayerCam[1] = new Camera2D(new Vector2f(context.getDimension()).mul(0.5f));
        //hudPlayerCam[1].setCenter(new Vector2f(size * 64 / 2.f, size * 64 / 2.f));
    }

    @Override
    public void endTurn() {
        switch (currentPlayer) {
            case 0: currentPlayer = 1; break;
            case 1: currentPlayer = 0; break;
        }
    }

    @Override
    public boolean isFinished() {
        return Arrays.stream(super.players).map(Player::getUnites).anyMatch(l -> l.stream().allMatch(Unite::isDead));
    }

    private void updateActionProgress(ConstTime time){
        //déroulement des actions
        if (!currentPlayerActions.isEmpty()) {
            // il reste des actions alors on doit les faires

            // on met a jour la derniere actions
            currentPlayerActions.element().update(time);
            // si elle se termine alors on a la supprime
            if (currentPlayerActions.element().isFinished()) {
                currentPlayerActions.remove();
            }
        } else {
            // il n'y a plus d'action alors on arrete la phase déroulement des actions
            inAction = false;
            endTurn();
        }
    }
    private void updateUserInput(ConstTime time){


        //camera user moves
        if (keyboard.isKeyPressed(AZERTYLayout.UP_ARROW.getKeyID())) {
            mapPlayerCam[currentPlayer].move(new Vector2f(0, -300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.DOWN_ARROW.getKeyID())) {
            mapPlayerCam[currentPlayer].move(new Vector2f(0, 300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.LEFT_ARROW.getKeyID())) {
            mapPlayerCam[currentPlayer].move(new Vector2f(-300*(float) time.asSeconds(), 0));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.RIGHT_ARROW.getKeyID())) {
            mapPlayerCam[currentPlayer].move(new Vector2f(300*(float) time.asSeconds(), 0));
        }
    }
    private void updateEscapeMenu(ConstTime time) {

    }

    @Override
    public void update(ConstTime time) {
        if (!menuEchap) {
            if (inAction) {
                updateActionProgress(time);
            } else {
                updateUserInput(time);
            }
        } else {
            updateEscapeMenu(time);
        }
    }

    @Override
    public void draw(RenderTarget target) {
        if (!menuEchap) {
            //target.setCamera(hudPlayerCam[0]);
            //target.draw(hud1);
            Camera cam = target.getCamera();

            target.setCamera(mapPlayerCam[0]);
            map.draw(mapPlayerCam[0], target);

            /*target.setCamera(hudPlayerCam[1]);
            //target.draw(hud1);
            target.setCamera(mapPlayerCam[1]);
            map.draw(mapPlayerCam[1], target);*/

            target.setCamera(cam);
        } else {
            //on affiche le menu echap
            target.clear();
        }
    }

    @Override
    public void handle(Event event) {
        if (event.type == Event.Type.RESIZE) {
            //hudPlayerCam[0].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));
            mapPlayerCam[0].setDimension(new Vector2f(event.resizeX, event.resizeY));


            /*hudPlayerCam[1].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));
            mapPlayerCam[1].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));*/
        } else if (event.type == Event.Type.KEYRELEASED && event.keyReleased == AZERTYLayout.ESCAPE.getKeyID()) {
            menuEchap = !menuEchap;
        }
    }
}
