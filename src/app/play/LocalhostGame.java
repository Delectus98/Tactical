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

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class LocalhostGame extends Game {
    private static final float MAP_WIDTH_PERCENT = 0.50F;
    private static final float MAP_HEIGHT_PERCENT = 0.75F;

    private Camera2D[] mapCam;
    private Viewport[] mapViewports;
    private Camera2D[] hudCam;

    private boolean isRunning = false;

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
        mapViewports[0] = new Viewport(new FloatRect(0,0, mapCam[0].getDimension().x, mapCam[0].getDimension().y));

        mapCam[1] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        mapViewports[1] = new Viewport(new FloatRect(mapCam[0].getDimension().x,0, mapCam[1].getDimension().x, mapCam[1].getDimension().y));
    }

    @Override
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % 2;
    }

    @Override
    public boolean isFinished() {
        return !isRunning && Arrays.stream(super.players).map(Player::getUnites).anyMatch(l -> l.stream().allMatch(Unite::isDead));
    }

    @Override
    public void start() {
        isRunning = true;
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

            if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                if (super.map.getWorld()[x][y].getFloor() != null && super.map.getWorld()[x][y].getFloor().getBounds().contains(pos.x, pos.y))
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
        if (keyboard.isKeyPressed(AZERTYLayout.END.getKeyID())){
            isRunning = false;
        }
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

    private void drawScreen(RenderTarget target, int player){
        // on applique nos view spécifique du joueur
        target.setViewport(mapViewports[player]);
        target.setCamera(mapCam[player]);

        // on obtient les coordonnées de la caméra
        Vector2f dim = mapCam[player].getDimension(); // dimension de la vue de la caméra
        Vector2f tlCorner = mapCam[player].getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra

        // on cacule la zone rectangulaire où les tuiles doivent être affichées
        int x = Math.max(0,(int)(tlCorner.x / 64.f)); // tuile la plus a gauche du point de vue de la caméra
        int y = Math.max(0,(int)(tlCorner.y / 64.f)); // tuile la plus a gauche du point de vue de la caméra
        final int offset = 2; // sert pour afficher les cases mal concidérées
        int x2 = Math.max(0, (int)(tlCorner.x / 64.f) + (int)(dim.x / 64.f) + offset); // tuile la plus a droite affichée du point de vue de la caméra
        int y2 = Math.max(0, (int)(tlCorner.y / 64.f) + (int)(dim.y / 64.f) + offset); // tuile la plus en bas affichée du point de vue de la caméra
        // on affiche la premiere couche
        // on affiche les unités des deux joueurs
        //glDisable(GL_DEPTH_TEST);
        map.drawFloor(x, y, x2, y2, target);
        Arrays.stream(players).forEach(p -> {if (p != null && !p.getUnites().isEmpty()) p.getUnites().get(0).draw(target);});

        // on affiche la seconde couche
        //map.drawStruct(x, y, x2, y2, target);
        // on affiche le brouillard de guerre

        /*for (int px = x ; px < x2 ; ++px) {
            for (int py = y ; py < y2 ; ++py) {
                if (!visibles[player].contains(new Vector2i(px, py))) {
                    //Sprite fog = new Sprite(textureWithFog);
                    //fog.setPosition(new Vector2f(px * 64.f, py * 64.f);
                    //target.draw(fog);
                }
            }
        }*/
    }

    @Override
    public void draw(RenderTarget target) {
        if (!menuEchap) {
            // on conserve l'ancienne View
            final Camera cam = target.getCamera();
            final Viewport vp = target.getViewport();

            // on affiche les deux écrans
            this.drawScreen(target, 0);
            this.drawScreen(target, 1);

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
