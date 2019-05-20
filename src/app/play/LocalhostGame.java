package app.play;

import Graphics.*;
import System.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.actions.Action;
import app.map.Map;
import app.map.MapImpl;
import app.map.Tile;
import util.WindowUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class LocalhostGame extends Game {
    private static final float MAP_WIDTH_PERCENT = 0.50F;
    private static final float MAP_HEIGHT_PERCENT = 0.75F;

    private Camera2D[] mapCam;
    private Viewport[] mapViewports;
    private Camera2D[] hudCam;

    private Queue<Action> currentPlayerActions = new PriorityQueue<>();
    private boolean inAction = false;
    private Unite selectedUnite = null;
    private Keyboard keyboard;
    private Mouse mouse;
    private boolean menuEchap = false;
    private Texture spritesheet;

    private Set<Vector2i>[] visibles;

    public Set<Vector2i> getCurrentVisibles()
    {
        return visibles[currentPlayer];
    }

    static final int size = 40;
    public LocalhostGame(GLFWWindow window, Player p1, Player p2, Map map) throws IOException {
        // set up game context
        context = window;

        // set up inputs
        keyboard = new Keyboard(context);
        mouse = new Mouse(context);

        // init games values
        currentPlayer = 0;
        inAction = false;

        // creating map
        this.map = map;

        // set up players
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // set up camera
        mapCam = new Camera2D[2];
        mapViewports = new Viewport[2];
        hudCam = new Camera2D[2];

        mapCam[0] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        mapViewports[0] = new Viewport(new FloatRect(100,100, mapCam[0].getDimension().x, mapCam[0].getDimension().y));

        mapCam[1] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        mapViewports[1] = new Viewport(new FloatRect(mapCam[0].getDimension().x,0, mapCam[1].getDimension().x, mapCam[1].getDimension().y));
    }

    @Override
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % 2;
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
        if (mouse.isButtonPressed(Mouse.Button.Left)) {
            Vector2f pos = WindowUtils.mapCoordToPixel(mouse.getRelativePosition(), mapViewports[currentPlayer], mapCam[currentPlayer]);
            System.out.println(pos.x+":"+pos.y);

            int x = (int)(pos.x / 64.F);
            int y = (int)(pos.y / 64.F);

            if (x >= 0 && x < size && y >= 0 && y < size ) {
                if (super.map.getWorld()[x][y].getFloor().getBounds().contains(pos.x, pos.y))
                    super.map.getWorld()[x][y].getFloor().setFillColor(new Color((float) time.asSeconds(), (float) (Math.random()), (float) (Math.random())));
            }
        }

        //player 1 camera control
        if (keyboard.isKeyPressed(AZERTYLayout.UP_ARROW.getKeyID())) {
            mapCam[currentPlayer].move(new Vector2f(0, -300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.DOWN_ARROW.getKeyID())) {
            mapCam[currentPlayer].move(new Vector2f(0, 300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.LEFT_ARROW.getKeyID())) {
            mapCam[currentPlayer].move(new Vector2f(-300*(float) time.asSeconds(), 0));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.RIGHT_ARROW.getKeyID())) {
            mapCam[currentPlayer].move(new Vector2f(300*(float) time.asSeconds(), 0));
        }

        //player 2 camera control
        if (keyboard.isKeyPressed(AZERTYLayout.Z.getKeyID())) {
            mapCam[1].move(new Vector2f(0, -300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.S.getKeyID())) {
            mapCam[1].move(new Vector2f(0, 300*(float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.Q.getKeyID())) {
            mapCam[1].move(new Vector2f(-300*(float) time.asSeconds(), 0));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.D.getKeyID())) {
            mapCam[1].move(new Vector2f(300*(float) time.asSeconds(), 0));
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
            // on conserve l'ancienne View
            final Camera cam = target.getCamera();
            final Viewport vp = target.getViewport();

            // on applique nos view spécifique du joueur 1
            target.setViewport(mapViewports[0]);
            target.setCamera(mapCam[0]);
            map.draw(mapCam[0], target);

            // on applique nos view spécifique du joueur 2
            target.setViewport(mapViewports[1]);
            target.setCamera(mapCam[1]);
            map.draw(mapCam[1], target);

            // on affiche les unités
            Arrays.stream(players).forEach(p -> {
                if (p != null && !p.getUnites().isEmpty()) {
                    p.getUnites().get(0).draw(target);
                }
            }
            );
            // on remet l'ancienne view
            target.setCamera(cam);
            target.setViewport(vp);
        } else {
            //on affiche le menu echap
            target.clear();
        }
    }

    @Override
    public void handle(Event event) {
        if (event.type == Event.Type.RESIZE) {
            //hudCam[0].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));
            mapCam[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            mapViewports[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));

            mapCam[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            mapViewports[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            mapViewports[1].setTopLeftCorner(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, 0.f));

        } else if (event.type == Event.Type.KEYRELEASED && event.keyReleased == AZERTYLayout.ESCAPE.getKeyID()) {
            menuEchap = !menuEchap;
        }
    }
}
