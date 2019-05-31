package app.play;

import Graphics.*;
import System.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.actions.Action;
import app.actions.ActionManager;
import app.actions.ShootingManager;
import app.map.Map;
import util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class LocalhostGame extends Game {
    private static final float MAP_WIDTH_PERCENT = 0.50F;
    private static final float MAP_HEIGHT_PERCENT = 1;

    private Camera2D[] mapCam;
    private Viewport[] viewports;
    private Camera2D[] hudCam;

    private boolean isRunning = false;

    private Action currentAction = null;
    private ActionManager manager = null;
    private boolean inAction = false;
    private Unite selectedUnite = null;
    private Keyboard keyboard;
    private Mouse mouse;

    private GameInput inputs[];

    private Set<Vector2i>[] visibles;

    public Set<Vector2i> getCurrentVisibles() {
        return visibles[currentPlayer];
    }

    public LocalhostGame(GLFWWindow window, Player p1, Player p2, Map map) throws IOException {
        // set up game context
        context = window;

        // set up inputs
        keyboard = new Keyboard(context);
        mouse = new Mouse(context);

        // init games values
        currentPlayer = 0;
        inAction = false;

        // set up visibles
        visibles = new Set[2];
        visibles[0] = new HashSet<>();
        visibles[1] = new HashSet<>();

        // creating map
        this.map = map;

        // set up players
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // set up camera
        mapCam = new Camera2D[2];
        viewports = new Viewport[2];
        hudCam = new Camera2D[2];

        mapCam[0] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        hudCam[0] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        viewports[0] = new Viewport(new FloatRect(0, 0, mapCam[0].getDimension().x, mapCam[0].getDimension().y));

        mapCam[1] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        hudCam[1] = new Camera2D(new Vector2f(context.getDimension().x * MAP_WIDTH_PERCENT, context.getDimension().y * MAP_HEIGHT_PERCENT));
        viewports[1] = new Viewport(new FloatRect(mapCam[0].getDimension().x, 0, mapCam[1].getDimension().x, mapCam[1].getDimension().y));

        updateFOG();

        inputs = new GameInput[2];
        inputs[0] = new GameInput(mapCam[0], hudCam[0], viewports[0], mouse, keyboard);
        inputs[1] = new GameInput(mapCam[1], hudCam[1], viewports[1], mouse, keyboard);

        manager = new ShootingManager(p1.getUnites().get(0), this, inputs[0]);

    }

    @Override
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % 2;

        players[currentPlayer].getUnites().forEach(Unite::resetTurn);
    }

    @Override
    public boolean isFinished() {
        return !isRunning && Arrays.stream(super.players).map(Player::getUnites).anyMatch(l -> l.stream().allMatch(Unite::isDead));
    }

    @Override
    public void start() {
        isRunning = true;
    }

    private void updateActionProgress(ConstTime time) {
        //déroulement des actions
        if (currentAction != null && !currentAction.isFinished()) {
            currentAction.update(time);
        } else {
            // il n'y a plus d'action alors on arrete la phase déroulement des actions
            inAction = false;
            currentAction = null;
            endTurn();
        }
    }

    private void updateCamera(ConstTime time) {
        //player 1 camera control
        Vector2f tlCorner0 = mapCam[0].getCenter().sum(mapCam[0].getDimension().mul(-0.5f));
        Vector2f brCorner0 = mapCam[0].getCenter().sum(mapCam[0].getDimension().mul(0.5f));
        if (mapCam[0].getDimension().y < map.getHeight() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.UP_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(0, -300 * (float) time.asSeconds()));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.DOWN_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(0, 300 * (float) time.asSeconds()));
            }
            //y correction
            if (tlCorner0.y < 0)
                mapCam[0].move(new Vector2f(0, -tlCorner0.y));
            if (brCorner0.y > map.getHeight() * 64)
                mapCam[0].move(new Vector2f(0, -(brCorner0.y - map.getHeight() * 64)));
        } else {
            //map is too small for y-coordinates
            mapCam[0].setCenter(new Vector2f(mapCam[0].getCenter().x, map.getHeight() * 64 / 2));
        }
        if (mapCam[0].getDimension().x < map.getWidth() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.LEFT_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(-300 * (float) time.asSeconds(), 0));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.RIGHT_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(300 * (float) time.asSeconds(), 0));
            }
            //x correction
            if (tlCorner0.x < 0)
                mapCam[0].move(new Vector2f(-tlCorner0.x, 0));
            if (brCorner0.x > map.getWidth() * 64)
                mapCam[0].move(new Vector2f(-(brCorner0.x - map.getWidth() * 64), 0));
        } else {
            //map is too small for x-coordinates
            mapCam[0].setCenter(new Vector2f(map.getWidth() * 64 / 2, mapCam[0].getCenter().y));
        }

        //player 2 camera control
        if (keyboard.isKeyPressed(AZERTYLayout.Z.getKeyID())) {
            mapCam[1].move(new Vector2f(0, -300 * (float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.S.getKeyID())) {
            mapCam[1].move(new Vector2f(0, 300 * (float) time.asSeconds()));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.Q.getKeyID())) {
            mapCam[1].move(new Vector2f(-300 * (float) time.asSeconds(), 0));
        }
        if (keyboard.isKeyPressed(AZERTYLayout.D.getKeyID())) {
            mapCam[1].move(new Vector2f(300 * (float) time.asSeconds(), 0));
        }
    }

    private void updateUserInput(ConstTime time) {
        manager.updatePreparation(time);

        if (mouse.isButtonPressed(Mouse.Button.Left)) {
            Vector2f pos = WindowUtils.mapCoordToPixel(mouse.getRelativePosition(), viewports[currentPlayer], mapCam[currentPlayer]);
            System.out.println(pos.x + ":" + pos.y);

            int x = (int) (pos.x / 64.F);
            int y = (int) (pos.y / 64.F);

            if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                if (super.map.getWorld()[x][y].getFloor() != null && super.map.getWorld()[x][y].getFloor().getBounds().contains(pos.x, pos.y))
                    super.map.getWorld()[x][y].getFloor().setFillColor(new Color((float) time.asSeconds(), (float) (Math.random()), (float) (Math.random())));
            }
        }

        final int speed = 300;
        if (keyboard.isKeyPressed(AZERTYLayout.PAD_8.getKeyID())) {
            players[currentPlayer].getUnites().forEach(u -> u.getSprite().move(0, -speed * (float) time.asSeconds()));
            updateFOG();
        }
        if (keyboard.isKeyPressed(AZERTYLayout.PAD_5.getKeyID())) {
            players[currentPlayer].getUnites().forEach(u -> u.getSprite().move(0, speed * (float) time.asSeconds()));
            updateFOG();
        }
        if (keyboard.isKeyPressed(AZERTYLayout.PAD_4.getKeyID())) {
            players[currentPlayer].getUnites().forEach(u -> u.getSprite().move(-speed * (float) time.asSeconds(), 0));
            updateFOG();
        }
        if (keyboard.isKeyPressed(AZERTYLayout.PAD_6.getKeyID())) {
            players[currentPlayer].getUnites().forEach(u -> u.getSprite().move(speed * (float) time.asSeconds(), 0));
            updateFOG();
        }

        players[currentPlayer].getUnites().forEach(u -> u.setMapPosition(new Vector2i(u.getSprite().getPosition().mul(1.f / 64.f))));

    }

    @Override
    public void updateFOG() {
        visibles[0].clear();
        visibles[1].clear();

        //visibles[0] :
        for (Unite unit : players[0].getUnites())
        {
            visibles[0].addAll(MapUtil.getVisibles(unit, map));
        }

        //visibles[1] :
        for (Unite unit : players[1].getUnites())
        {
            visibles[1].addAll(MapUtil.getVisibles(unit, map));
        }
    }

    @Override
    public void update(ConstTime time) {
        updateCamera(time);

        if (inAction) {
            updateActionProgress(time);
        } else {
            updateUserInput(time);
        }

        inputs[0].reset();
        inputs[1].reset();
    }

    //déssine le sol (ce que voit la camera)
    private void drawMapFloor(int x, int y, int x2, int y2, RenderTarget target, int player) {
        for (int i = x; i < x2 && i < map.getWidth(); ++i) {
            for (int j = y; j < y2 && j < map.getHeight(); ++j) {
                Sprite sp = map.getWorld()[i][j].getFloor();
                if (sp != null) {
                    final int xp = i, yp = j;
                    if (visibles[player].stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
                        target.draw(sp);
                    } else {
                        //on grisonne la zone
                        target.draw(sp, ResourceHandler.getShader("grey"));
                    }
                }
            }
        }
    }

    //déssine les unités visibles
    private void drawUnite(RenderTarget target, int player) {
        //Arrays.stream(players).forEach(p -> {if (p != null && !p.getUnites().isEmpty()) p.getUnites().get(0).draw(target);});
        players[player].getUnites().forEach(u -> {
            if (u.isDead())
                target.draw(u.getSprite(), ResourceHandler.getShader("grey"));
            else target.draw(u.getSprite());
        });
        players[(player + 1) % 2].getUnites().forEach(u -> {
            if (visibles[player].stream().anyMatch(v -> u.getMapPosition().equals(v))) {
                if (u.isDead())
                    target.draw(u.getSprite(), ResourceHandler.getShader("grey"));
                else target.draw(u.getSprite());
            }
        });
    }

    //déssine les structs (ce que voit la camera)
    private void drawMapStruct(int x, int y, int x2, int y2, RenderTarget target, int player) {
        for (int i = x; i < x2 && i < map.getWidth(); ++i) {
            for (int j = 0; j < y2 && j < map.getHeight(); ++j) {
                Sprite sp = map.getWorld()[i][j].getStruct();
                if (sp != null) {
                    final int xp = i, yp = j;
                    if (visibles[player].stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
                        target.draw(sp);
                    } else {
                        target.draw(sp, ResourceHandler.getShader("grey"));
                    }
                }
            }
        }
    }

    @Override
    public void draw(RenderTarget target) {
        // on conserve l'ancienne View
        final Camera cam = target.getCamera();
        final Viewport vp = target.getViewport();

        ///draw first player screen
        {
            // on applique nos view spécifique du joueur
            target.setViewport(viewports[0]);
            // on affiche au niveau de la map
            target.setCamera(mapCam[0]);

            // on obtient les coordonnées de la caméra
            Vector2f dim = mapCam[0].getDimension(); // dimension de la vue de la caméra
            Vector2f tlCorner = mapCam[0].getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra
            // on cacule la zone rectangulaire où les tuiles doivent être affichées
            int x = Math.max(0, (int) (tlCorner.x / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            int y = Math.max(0, (int) (tlCorner.y / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            final int offset = 2; // sert pour afficher les cases mal concidérées
            int x2 = Math.max(0, (int) (tlCorner.x / 64.f) + (int) (dim.x / 64.f) + offset); // tuile la plus a droite affichée du point de vue de la caméra
            int y2 = Math.max(0, (int) (tlCorner.y / 64.f) + (int) (dim.y / 64.f) + offset); // tuile la plus en bas affichée du point de vue de la caméra

            // draw map
            drawMapFloor(x, y, x2, y2, target, 0);
            if (currentPlayer == 0 && currentAction != null)
                currentAction.drawAboveFloor(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveFloor(target);
            drawUnite(target, 0);
            if (currentPlayer == 0 && currentAction != null)
                currentAction.drawAboveEntity(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveEntity(target);
            drawMapStruct(x, y, x2, y2, target, 0);
            if (currentPlayer == 0 && currentAction != null)
                currentAction.drawAboveStruct(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveStruct(target);

            // on affiche au niveau du hud
            target.setCamera(hudCam[0]);

            if (currentPlayer == 0 && currentAction != null)
                currentAction.drawAboveHUD(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveHUD(target);
        }

        ///draw second player screen
        {
            // on applique nos view spécifique du joueur
            target.setViewport(viewports[1]);
            // on affiche au niveau de la map
            target.setCamera(mapCam[1]);

            // on obtient les coordonnées de la caméra
            Vector2f dim = mapCam[1].getDimension(); // dimension de la vue de la caméra
            Vector2f tlCorner = mapCam[1].getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra
            // on cacule la zone rectangulaire où les tuiles doivent être affichées
            int x = Math.max(0, (int) (tlCorner.x / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            int y = Math.max(0, (int) (tlCorner.y / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            final int offset = 2; // sert pour afficher les cases mal concidérées
            int x2 = Math.max(0, (int) (tlCorner.x / 64.f) + (int) (dim.x / 64.f) + offset); // tuile la plus a droite affichée du point de vue de la caméra
            int y2 = Math.max(0, (int) (tlCorner.y / 64.f) + (int) (dim.y / 64.f) + offset); // tuile la plus en bas affichée du point de vue de la caméra

            // draw map
            drawMapFloor(x, y, x2, y2, target, 1);
            if (currentPlayer == 1 && currentAction != null)
                currentAction.drawAboveFloor(target);
            drawUnite(target, 1);
            if (currentPlayer == 1 && currentAction != null)
                currentAction.drawAboveEntity(target);
            drawMapStruct(x, y, x2, y2, target, 0);
            if (currentPlayer == 1 && currentAction != null)
                currentAction.drawAboveStruct(target);
            if (currentPlayer == 1 && currentAction != null)
                currentAction.drawAboveHUD(target);

            // on affiche au niveau du hud
            target.setCamera(hudCam[1]);
        }

        // on remet l'ancienne view
        target.setCamera(cam);
        target.setViewport(vp);
    }


    /**
     * Certains evenements systemes peuvent interesser le jeu.
     * @param event
     */
    @Override
    public void handle(Event event) {
        inputs[0].update(event);
        inputs[1].update(event);

        if (event.type == Event.Type.RESIZE) {
            //hudCam[0].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));
            mapCam[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            viewports[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));

            mapCam[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            viewports[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            viewports[1].setTopLeftCorner(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, 0.f));
        }
    }

}
