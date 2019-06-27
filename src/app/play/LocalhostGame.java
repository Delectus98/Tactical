package app.play;

import Graphics.*;
import System.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.actions.Action;
import app.actions.ActionManager;
import app.hud.HudGameMenu;
import app.hud.HudPlayer;
import app.hud.HudUnite;
import app.map.Map;
import org.lwjgl.opengl.GL20;
import util.GameInput;
import util.MapUtil;
import util.ResourceHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class LocalhostGame extends Game {
    private static final float MAP_WIDTH_PERCENT = 0.50F;
    private static final float MAP_HEIGHT_PERCENT = 1;

    private Camera2D[] mapCam;
    private Viewport[] viewports;
    private Camera2D[] hudCam;

    private boolean initialized = false;
    private int placedUnite = 0;
    private int playerInitialized = 0;
    private RectangleShape selectedSpawn;
    private RectangleShape[] spawnIndicators;

    private boolean isRunning = false;

    private Action currentAction = null;
    private ActionManager manager = null;
    private boolean inAction = false;
    private Unite selectedUnite = null;
    private Keyboard keyboard;
    private Mouse mouse;

    private HudPlayer[] hudPlayer;
    private GameInput[] inputs;
    private HudUnite hudUnite = null;

    private HudGameMenu gameMenu[];

    private Set<Vector2i>[] visibles;

    private Text nextTurn;

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
        currentPlayer = 1;
        inAction = false;

        // set up visibles
        visibles = new Set[2];
        visibles[0] = new HashSet<>();
        visibles[1] = new HashSet<>();

        // creating map
        this.map = map;

        // set up players
        p1.setId(0);
        p2.setId(1);
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

        inputs = new GameInput[2];
        inputs[0] = new GameInput(mapCam[0], hudCam[0], viewports[0], mouse, keyboard);
        inputs[1] = new GameInput(mapCam[1], hudCam[1], viewports[1], mouse, keyboard);

        hudPlayer = new HudPlayer[2];
        hudPlayer[0] = new HudPlayer(this, p1, inputs[0]);
        hudPlayer[1] = new HudPlayer(this, p2, inputs[1]);

        // ui
        nextTurn = new Text(ResourceHandler.getFont("default"), "Next Turn");
        nextTurn.setPosition(inputs[currentPlayer].getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        spawnIndicators = new RectangleShape[map.getSpawnPoints(currentPlayer).size()];
        for (int i = 0 ; i < spawnIndicators.length ; ++i) {
            spawnIndicators[i] = new RectangleShape(50, 50);
            spawnIndicators[i].setFillColor(new Color(1.f,0.2f, 1.f, 0.3f));
            spawnIndicators[i].setOrigin(spawnIndicators[i].getBounds().w / 2.f,spawnIndicators[i].getBounds().h / 2.f);
        }

        gameMenu = new HudGameMenu[2];
        gameMenu[0] = new HudGameMenu();
        gameMenu[1] = new HudGameMenu();
    }

    @Override
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % 2;

        inAction = false;
        manager = null;
        currentAction = null;
        hudUnite = null;

        players[currentPlayer].getUnites().forEach(Unite::resetTurn);
    }

    @Override
    public boolean isFinished() {
        return !isRunning || Arrays.stream(super.players).map(Player::getUnites).anyMatch(l -> l.stream().allMatch(Unite::isDead));
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
            if (hudUnite != null) hudUnite.resetSelectedAction();
            if (hudUnite != null) manager = hudUnite.getSelectedAction();
            //endTurn();
        }
    }

    private void updatePlacement(ConstTime elapsed) throws RuntimeException {
        // exception throwing
        if (map.getSpawnPoints(currentPlayer).size() < players[currentPlayer].getUnites().size()) throw new RuntimeException("La map n'a pas assez de slot spawn");

        // update indicators
        for (int i = 0; i < spawnIndicators.length ; ++i) {
            // on obtient les coordonnées de la caméra
            Vector2f dim = mapCam[currentPlayer].getDimension(); // dimension de la vue de la caméra
            Vector2f tlCorner = mapCam[currentPlayer].getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra
            // on calcule le décallage si la carte est trop petite par rapport a l'écran
            Vector2f mapBorder = new Vector2f(Math.max(0, (dim.x - map.getWidth() * 64) / 2.f), Math.max(0, (dim.y - map.getHeight() * 64) / 2.f));
            // on cacule la zone rectangulaire où les tuiles doivent être affichées
            float x = Math.max(0, ((tlCorner.x) / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            float y = Math.max(0, ((tlCorner.y) / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            float x2 = Math.max(0, ((tlCorner.x) / 64.f) + (dim.x / 64.f)); // tuile la plus a droite affichée du point de vue de la caméra
            float y2 = Math.max(0, ((tlCorner.y) / 64.f) + (dim.y / 64.f)); // tuile la plus en bas affichée du point de vue de la caméra

            Vector2i spawn = map.getSpawnPoints(currentPlayer).get(i);
            Vector2f hudPos = new Vector2f(Math.min(dim.x - 32, (Math.min(x2, Math.max(x, spawn.x)) - x) * 64 + 32) + mapBorder.x, (Math.min(dim.y - 32, (Math.min(y2, Math.max(y, spawn.y)) - y) * 64 + 32)) + mapBorder.y);
            spawnIndicators[i].setPosition(hudPos.x, hudPos.y);
            float ratio = Math.min(1, 2.f * Math.max(0,(float)(1.0 / (1.0 + Math.exp(hudPos.add(tlCorner).sum(new Vector2f(spawn).mul(64.f).neg()).length() / 1000.f)))));

            if (players[currentPlayer].getUnites().stream().anyMatch(u -> spawn.equals(u.getMapPosition()))) {
                spawnIndicators[i].setFillColor(new Color(ratio, 0, 0, 0.2f));
            } else {
                spawnIndicators[i].setFillColor(new Color(ratio, 0, 0, 1.f));
            }
        }

        // spawn unite selection
        if (inputs[currentPlayer].getFrameRectangle().contains(inputs[currentPlayer].getMousePosition().x, inputs[currentPlayer].getMousePosition().y)) {
            Vector2f mouse = inputs[currentPlayer].getMousePositionOnMap();
            Vector2i tile = new Vector2i((int) (mouse.x / 64.f), (int) (mouse.y / 64.f));
            //selectedSpawn.setPosition(tile.x * 64, tile.y * 64);
            Unite unite = players[currentPlayer].getUnites().get(placedUnite);

            if (inputs[currentPlayer].isLeftReleased() && map.getSpawnPoints(currentPlayer).contains(tile) && players[currentPlayer].getUnites().stream().noneMatch(u -> u != unite && tile.equals(u.getMapPosition()))) {
                unite.setMapPosition(new Vector2i(tile.x, tile.y));
                unite.getSprite().setPosition(tile.x * 64, tile.y * 64);
                placedUnite++;
            }

            if (placedUnite == players[currentPlayer].getUnites().size()) {
                if (playerInitialized != players.length) {
                    // not all player placed their unites
                    currentPlayer = (currentPlayer + 1) % players.length;
                    playerInitialized++;
                    placedUnite = 0;
                }

                if (playerInitialized == players.length) {
                    // all players placed their unites
                    initialized = true;
                    // change indicators
                    updateFOG();
                }
            }
        }
    }

    private void updateCamera(ConstTime time) {
        float seconds = (float)time.asSeconds();

        //player 1 camera control
        Vector2f tlCorner0 = mapCam[currentPlayer].getCenter().sum(mapCam[currentPlayer].getDimension().mul(-0.5f));
        Vector2f brCorner0 = mapCam[currentPlayer].getCenter().sum(mapCam[currentPlayer].getDimension().mul(0.5f));
        if (mapCam[currentPlayer].getDimension().y < map.getHeight() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.UP_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(0, -300 * seconds));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.DOWN_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(0, 300 * seconds));
            }
            //y correction
            if (tlCorner0.y < 0)
                mapCam[currentPlayer].move(new Vector2f(0, -tlCorner0.y));
            if (brCorner0.y > map.getHeight() * 64)
                mapCam[currentPlayer].move(new Vector2f(0, -(brCorner0.y - map.getHeight() * 64)));
        } else {
            //map is too small for y-coordinates
            mapCam[currentPlayer].setCenter(new Vector2f(mapCam[currentPlayer].getCenter().x, map.getHeight() * 64 / 2));
        }
        if (mapCam[currentPlayer].getDimension().x < map.getWidth() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.LEFT_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(-300 * seconds, 0));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.RIGHT_ARROW.getKeyID())) {
                mapCam[currentPlayer].move(new Vector2f(300 * seconds, 0));
            }
            //x correction
            if (tlCorner0.x < 0)
                mapCam[currentPlayer].move(new Vector2f(-tlCorner0.x, 0));
            if (brCorner0.x > map.getWidth() * 64)
                mapCam[currentPlayer].move(new Vector2f(-(brCorner0.x - map.getWidth() * 64), 0));
        } else {
            //map is too small for x-coordinates
            mapCam[currentPlayer].setCenter(new Vector2f(map.getWidth() * 64 / 2, mapCam[currentPlayer].getCenter().y));
        }
    }

    private void updateUserInput(ConstTime time) {
        nextTurn.setPosition(inputs[currentPlayer].getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        Vector2f mouseHud = inputs[currentPlayer].getMousePositionOnHUD();

        if (inputs[currentPlayer].isLeftReleased() && nextTurn.getBounds().contains(mouseHud.x, mouseHud.y)) {
            endTurn();
        }

        hudPlayer[currentPlayer].update(time);
        hudPlayer[(currentPlayer+1)%2].update(time);

        if (hudPlayer[currentPlayer].isSelected()) {
            if (selectedUnite != hudPlayer[currentPlayer].getSelectedUnit() && hudPlayer[currentPlayer].getSelectedUnit() != null && !hudPlayer[currentPlayer].getSelectedUnit().isDead()) {
                selectedUnite = hudPlayer[currentPlayer].getSelectedUnit();
                //reset player HUD
                // selection sur le HUD du joueur
                hudUnite = new HudUnite(players[currentPlayer], selectedUnite, inputs[currentPlayer], this);
                //hudUnite.setSelectedUnite(selectedUnite);
                //reset action manager
                manager = hudUnite.getSelectedAction();
                // on veut voir l'unite
                mapCam[currentPlayer].setCenter(selectedUnite.getSprite().getPosition());
            }
        }

        if (hudUnite != null) {
            hudUnite.update(time);
            if (hudUnite.getSelectedAction() != manager) {
                manager = hudUnite.getSelectedAction();
            }
        }

        if (manager != null &&currentAction == null) {
            if ((hudUnite == null || !hudUnite.isClicked()) && (hudPlayer[currentPlayer] == null || !hudPlayer[currentPlayer].isSelected())) {
                manager.updatePreparation(time);
                if (manager.isAvailable()) {
                    currentAction = manager.build();
                    currentAction.init(this);
                    selectedUnite.removePA((short) currentAction.getCost());
                    inAction = true;
                    manager = null;
                }
            }
        }


        // la souris est dans le rectangle du jeu du bon joueur && qu'aucune action ne va se dérouler après alors on peut cliquer sur une unité
        if (inputs[currentPlayer].isLeftReleased() && inputs[currentPlayer].getFrameRectangle().contains(inputs[currentPlayer].getMousePosition().x, inputs[currentPlayer].getMousePosition().y)) {
            // selection d'unité sur le HUD

            // selection d'unité alliée sur la map
            players[currentPlayer].getUnites().forEach(u -> {
                    if (!u.isDead() && u.getSprite().getBounds().contains(inputs[currentPlayer].getMousePositionOnMap().x, inputs[currentPlayer].getMousePositionOnMap().y)) {
                        selectedUnite = u;
                        //reset player HUD
                        // selection sur le HUD du joueur
                        hudUnite = new HudUnite(players[currentPlayer], selectedUnite, inputs[currentPlayer], this);
                        //hudUnite.setSelectedUnite(selectedUnite);
                        //reset action manager
                        manager = hudUnite.getSelectedAction();
                    }
                });

            // selection d'unité ennemie sur la map (sauf si une action va se produire)
            if (currentAction == null) {
                Arrays.stream(players).filter(p -> p != players[currentPlayer]).forEach(lu -> lu/*players[currentPlayer]*/.getUnites().forEach(u -> {
                            if (!u.isDead() && u.getSprite().getBounds().contains(inputs[currentPlayer].getMousePositionOnMap().x, inputs[currentPlayer].getMousePositionOnMap().y)) {
                                selectedUnite = u;
                                //reset player HUD
                                // selection sur le HUD du joueur
                                hudUnite = new HudUnite(players[currentPlayer], selectedUnite, inputs[currentPlayer], this);
                                //reset action manager
                                manager = hudUnite.getSelectedAction();
                            }
                        })
                );
            }
        }

        if (selectedUnite != null && selectedUnite.isDead()) {
            selectedUnite = null;
            hudUnite = null;
            manager = null;
        }

    }

    @Override
    public void updateFOG() {
        visibles[0].clear();
        visibles[1].clear();

        //visibles[0] :
        for (Unite unit : players[0].getUnites())
        {
            if (!unit.isDead()) visibles[0].addAll(MapUtil.getVisibles(unit, map));
        }

        //visibles[1] :
        for (Unite unit : players[1].getUnites())
        {
            if (!unit.isDead()) visibles[1].addAll(MapUtil.getVisibles(unit, map));
        }
    }

    @Override
    public void update(ConstTime time) {
        updateCamera(time);

        gameMenu[currentPlayer].update(inputs[currentPlayer]);
        if (gameMenu[currentPlayer].isAttemptingToEscape())
            isRunning = false;

        if (initialized) {
            if (inAction) {
                updateActionProgress(time);
            } else {
                updateUserInput(time);
            }
        } else {
            updatePlacement(time);
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
                    if (initialized) {
                        final int xp = i, yp = j;
                        if (visibles[player].stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
                            target.draw(sp);
                        } else {
                            //on grisonne la zone
                            target.draw(sp, ResourceHandler.getShader("grey"));
                        }

                    } else {
                        if (map.getSpawnPoints(player).contains(new Vector2i(i, j))) {
                            target.draw(sp);
                        } else {
                            //on grisonne la zone
                            target.draw(sp, ResourceHandler.getShader("grey"));
                        }
                    }
                }
            }
        }
    }

    //déssine les unités visibles
    private void drawUnite(RenderTarget target, int player) {
        //Arrays.stream(players).forEach(p -> {if (p != null && !p.getUnites().isEmpty()) p.getUnites().get(0).draw(target);});
        //Arrays.stream(players).forEach(p -> {if (p != null && !p.getUnites().isEmpty()) p.getUnites().get(0).draw(target);});
        // on dessine les morts avant les vivants
        players[player].getUnites().forEach(u -> {if (u.isDead()) u.draw(target);});
        players[(player + 1) % 2].getUnites().forEach(u -> {
            if (visibles[player].stream().anyMatch(v -> u.getMapPosition().equals(v))) {
                if (u.isDead()) u.draw(target);
            }
        });
        players[player].getUnites().forEach(u -> {if (!u.isDead()) u.draw(target);});
        players[(player + 1) % 2].getUnites().forEach(u -> {
            if (visibles[player].stream().anyMatch(v -> u.getMapPosition().equals(v))) {
                if (!u.isDead()) u.draw(target);
            }
        });
    }

    //déssine les structs (ce que voit la camera)
    private void drawMapStruct(int x, int y, int x2, int y2, RenderTarget target, int player) {
        for (int i = x; i < x2 && i < map.getWidth(); ++i) {
            for (int j = 0; j < y2 && j < map.getHeight(); ++j) {
                Sprite sp = map.getWorld()[i][j].getStruct();
                if (sp != null) {
                    if (initialized) {
                        final int xp = i, yp = j;
                        //if (visibles[player].stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
                            target.draw(sp);
                        //} else {
                            //on grisonne la zone
                        //    target.draw(sp, ResourceHandler.getShader("grey"));
                        //}

                    } else {
                        if (map.getSpawnPoints(player).contains(new Vector2i(i, j))) {
                            target.draw(sp);
                        } else {
                            //on grisonne la zone
                            target.draw(sp, ResourceHandler.getShader("grey"));
                        }
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
            if (!initialized && currentPlayer == 0) {
                ResourceHandler.getShader("grey").bind();
                GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), 1.0f);
            } else {
                ResourceHandler.getShader("grey").bind();
                GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), (!initialized) ? 0.2f : 0.05f);
            }

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
            if (currentAction != null)
                currentAction.drawAboveFloor(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveFloor(target);
            drawUnite(target, 0);
            if (currentAction != null)
                currentAction.drawAboveEntity(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveEntity(target);
            drawMapStruct(x, y, x2, y2, target, 0);
            if (currentAction != null)
                currentAction.drawAboveStruct(target);
            if (currentPlayer == 0 && manager != null)
                manager.drawAboveStruct(target);

            // on affiche au niveau du hud
            target.setCamera(hudCam[0]);

            if (initialized) {
                if (currentAction != null)
                    currentAction.drawAboveHUD(target);
                if (currentPlayer == 0 && manager != null)
                    manager.drawAboveHUD(target);

                hudPlayer[0].draw(target);
                if (/*!inAction && */hudUnite != null && currentPlayer == 0) {
                    hudUnite.draw(target);
                }
                if (currentPlayer == 0) {
                    target.draw(nextTurn);
                }
            } else {
                if (currentPlayer == 0) Arrays.stream(spawnIndicators).forEach(target::draw);
            }

            gameMenu[0].draw(target);
        }

        ///draw second player screen
        {
            if (!initialized && currentPlayer == 1) {
                ResourceHandler.getShader("grey").bind();
                GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), 1.f);
            } else {
                ResourceHandler.getShader("grey").bind();
                GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), (!initialized) ? 0.2f : 0.05f);
            }

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
            if (currentAction != null)
                currentAction.drawAboveFloor(target);
            if (currentPlayer == 1 && manager != null)
                manager.drawAboveFloor(target);
            drawUnite(target, 1);
            if (currentAction != null)
                currentAction.drawAboveEntity(target);
            if (currentPlayer == 1 && manager != null)
                manager.drawAboveEntity(target);
            drawMapStruct(x, y, x2, y2, target, 1);
            if (currentAction != null)
                currentAction.drawAboveStruct(target);
            if (currentPlayer == 1 && manager != null)
                manager.drawAboveStruct(target);


            // on affiche au niveau du hud
            target.setCamera(hudCam[1]);

            if (initialized) {
                if (currentAction != null)
                    currentAction.drawAboveHUD(target);
                if (currentPlayer == 1 && manager != null)
                    manager.drawAboveHUD(target);

                hudPlayer[1].draw(target);
                if (/*!inAction && */hudUnite != null && currentPlayer == 1) {
                    hudUnite.draw(target);
                }

                if (currentPlayer == 1) {
                    target.draw(nextTurn);
                }
            } else {
                if (currentPlayer == 1) Arrays.stream(spawnIndicators).forEach(target::draw);
            }

            gameMenu[1].draw(target);
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
        if (event.type == Event.Type.RESIZE) {
            //hudCam[0].setDimension(new Vector2f(event.resizeX / 2.f, event.resizeY));
            mapCam[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            hudCam[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            hudCam[0].setCenter(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT / 2.f, event.resizeY * MAP_HEIGHT_PERCENT / 2.f));
            viewports[0].setTopLeftCorner(new Vector2f(0.f, 0.f));
            viewports[0].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));

            mapCam[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            hudCam[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            hudCam[1].setCenter(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT / 2.f, event.resizeY * MAP_HEIGHT_PERCENT / 2.f));
            viewports[1].setDimension(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, event.resizeY * MAP_HEIGHT_PERCENT));
            viewports[1].setTopLeftCorner(new Vector2f(event.resizeX * MAP_WIDTH_PERCENT, 0.f));
        }

        inputs[0].update(event);
        inputs[1].update(event);

        gameMenu[currentPlayer].handle(event);

    }

}
