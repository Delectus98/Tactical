package app.play;


import Graphics.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.actions.Action;
import app.actions.ActionManager;
import app.hud.HudPlayer;
import app.hud.HudUnite;
import app.map.Map;
import app.network.*;

import java.util.*;

import System.*;
import org.lwjgl.opengl.GL20;
import util.GameInput;
import util.MapUtil;
import util.ResourceHandler;

public class ClientGame extends Game {
    private final int localPlayer;
    private ClientImpl client = null;
    private boolean running = false;

    private RectangleShape[] spawnIndicators;
    private int placedUnite = 0;
    private boolean initialized = false;
    private boolean spawnReady = false;


    private boolean serverIsActing = true;
    private Queue<Action> serverActions = new LinkedList<>();

    private boolean inAction = false;
    private Action currentAction = null;
    private ActionManager manager = null;

    private HudPlayer hudPlayer = null;
    private HudUnite hudUnite = null;
    private Unite selectedUnite = null;

    private Set<Vector2i> visibles = new HashSet<>();

    private Camera2D mapCam;
    private Camera2D hudCam;
    private Viewport viewport;
    private GameInput input;
    private Mouse mouse;
    private Keyboard keyboard;

    private Text nextTurn;

    private int currentPlayer = 0;

    public ClientGame(ClientImpl client, Player[] players, int localPlayer, Map map, GLFWWindow context){
        this.context = context;
        this.client = client;

        mapCam = new Camera2D(new Vector2f(context.getDimension().x , context.getDimension().y));
        hudCam = new Camera2D(new Vector2f(context.getDimension().x , context.getDimension().y));
        viewport = new Viewport(new FloatRect(0, 0, mapCam.getDimension().x, mapCam.getDimension().y));

        this.map = map;
        this.players = players;
        this.localPlayer = localPlayer;
        for (int i = 0 ; i < players.length ; ++i) players[i].setId(i);

        mouse = new Mouse(context);
        keyboard = new Keyboard(context);

        input = new GameInput(mapCam, hudCam, viewport, mouse, keyboard);

        hudPlayer = new HudPlayer(this, players[this.localPlayer], input);

        // ui
        nextTurn = new Text(ResourceHandler.getFont("default"), "Next Turn");
        nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        spawnIndicators = new RectangleShape[map.getSpawnPoints(localPlayer).size()];
        for (int i = 0 ; i < spawnIndicators.length ; ++i) {
            spawnIndicators[i] = new RectangleShape(50, 50);
            spawnIndicators[i].setFillColor(new Color(1.f,0.2f, 1.f, 0.3f));
            spawnIndicators[i].setOrigin(spawnIndicators[i].getBounds().w / 2.f,spawnIndicators[i].getBounds().h / 2.f);
        }
    }

    @Override
    public void endTurn() {
        inAction = false;
        manager = null;
        currentAction = null;
        hudUnite = null;
        selectedUnite = null;

        players[currentPlayer].getUnites().forEach(Unite::resetTurn);
        currentPlayer = (currentPlayer + 1) % players.length;
    }

    @Override
    public boolean isFinished() {
        return !running || !client.isRunning();
    }

    @Override
    public void start(){
        running = true;
    }

    @Override
    public Set<Vector2i> getCurrentVisibles() {
        return visibles;
    }

    @Override
    public void updateFOG() {
        visibles.clear();

        players[localPlayer].getUnites().forEach(u -> visibles.addAll(MapUtil.getVisibles(u, super.getMap())));
    }

    private void updateActionProgress(ConstTime time) {
        //déroulement des actions
        if (currentAction != null && !currentAction.isFinished()) {
            currentAction.update(time);
        } else {
            // il n'y a plus d'action alors on arrete la phase déroulement des actions
            inAction = false;
            currentAction = null;
            hudUnite.resetSelectedAction();
            manager = hudUnite.getSelectedAction();
            //endTurn();
        }
    }

    private void updateCamera(ConstTime time) {
        //player 1 camera control
        Vector2f tlCorner0 = mapCam.getCenter().sum(mapCam.getDimension().mul(-0.5f));
        Vector2f brCorner0 = mapCam.getCenter().sum(mapCam.getDimension().mul(0.5f));
        if (mapCam.getDimension().y < map.getHeight() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.UP_ARROW.getKeyID())) {
                mapCam.move(new Vector2f(0, -300 * (float) time.asSeconds()));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.DOWN_ARROW.getKeyID())) {
                mapCam.move(new Vector2f(0, 300 * (float) time.asSeconds()));
            }
            //y correction
            if (tlCorner0.y < 0)
                mapCam.move(new Vector2f(0, -tlCorner0.y));
            if (brCorner0.y > map.getHeight() * 64)
                mapCam.move(new Vector2f(0, -(brCorner0.y - map.getHeight() * 64)));
        } else {
            //map is too small for y-coordinates
            mapCam.setCenter(new Vector2f(mapCam.getCenter().x, map.getHeight() * 64 / 2));
        }
        if (mapCam.getDimension().x < map.getWidth() * 64) {
            if (keyboard.isKeyPressed(AZERTYLayout.LEFT_ARROW.getKeyID())) {
                mapCam.move(new Vector2f(-300 * (float) time.asSeconds(), 0));
            }
            if (keyboard.isKeyPressed(AZERTYLayout.RIGHT_ARROW.getKeyID())) {
                mapCam.move(new Vector2f(300 * (float) time.asSeconds(), 0));
            }
            //x correction
            if (tlCorner0.x < 0)
                mapCam.move(new Vector2f(-tlCorner0.x, 0));
            if (brCorner0.x > map.getWidth() * 64)
                mapCam.move(new Vector2f(-(brCorner0.x - map.getWidth() * 64), 0));
        } else {
            //map is too small for x-coordinates
            mapCam.setCenter(new Vector2f(map.getWidth() * 64 / 2, mapCam.getCenter().y));
        }
    }

    private void updateUserInput(ConstTime time) {
        if (currentPlayer == localPlayer) {
            nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

            Vector2f mouseHud = input.getMousePositionOnHUD();

            if (input.isLeftReleased() && !serverIsActing && !inAction && nextTurn.getBounds().contains(mouseHud.x, mouseHud.y)) {
                client.send(new TurnPacket());
                endTurn();
            }

            hudPlayer.update(time);

            if (hudPlayer.isSelected()) {
                if (selectedUnite != hudPlayer.getSelectedUnit() && hudPlayer.getSelectedUnit() != null && !hudPlayer.getSelectedUnit().isDead()) {
                    selectedUnite = hudPlayer.getSelectedUnit();
                    //reset player HUD
                    // selection sur le HUD du joueur
                    hudUnite = new HudUnite(players[localPlayer], selectedUnite, input, this);
                    //hudUnite.setSelectedUnite(selectedUnite);
                    //reset action manager
                    manager = hudUnite.getSelectedAction();
                    // on veut voir l'unite
                    mapCam.setCenter(selectedUnite.getSprite().getPosition());
                }
            }

            if (hudUnite != null) {
                hudUnite.update(time);
                if (hudUnite.getSelectedAction() != manager) {
                    manager = hudUnite.getSelectedAction();
                }
            }

            if (manager != null && currentAction == null) {
                if ((hudUnite == null || !hudUnite.isClicked()) && (hudPlayer == null || !hudPlayer.isSelected())) {
                    manager.updatePreparation(time);
                    if (manager.isAvailable()) {
                        currentAction = manager.build();
                        ActionPacket ap = new ActionPacket();
                        ap.action = currentAction;
                        ap.uniteId = selectedUnite.getId();
                        ap.playerId = localPlayer;
                        client.send(ap);
                        currentAction.init(this);
                        selectedUnite.removePA((short) currentAction.getCost());
                        inAction = true;
                        manager = null;
                    }
                }
            }


            // la souris est dans le rectangle du jeu du bon joueur && qu'aucune action ne va se dérouler après alors on peut cliquer sur une unité
            if (input.isLeftReleased() && input.getFrameRectangle().contains(input.getMousePosition().x, input.getMousePosition().y)) {
                // selection d'unité sur le HUD

                // selection d'unité alliée sur la map
                players[localPlayer].getUnites().forEach(u -> {
                    if (!u.isDead() && u.getSprite().getBounds().contains(input.getMousePositionOnMap().x, input.getMousePositionOnMap().y)) {
                        selectedUnite = u;
                        //reset player HUD
                        // selection sur le HUD du joueur
                        hudUnite = new HudUnite(players[localPlayer], selectedUnite, input, this);
                        //hudUnite.setSelectedUnite(selectedUnite);
                        //reset action manager
                        manager = hudUnite.getSelectedAction();
                    }
                });

                // selection d'unité ennemie sur la map (sauf si une action va se produire)
                if (currentAction == null) {
                    for (int i = 0 ; i < players.length ; ++i) {
                        if (i != localPlayer) {
                            final int index = i;
                            players[i].getUnites().forEach(u -> {
                                if (!u.isDead() && u.getSprite().getBounds().contains(input.getMousePositionOnMap().x, input.getMousePositionOnMap().y)) {
                                    selectedUnite = u;
                                    //reset player HUD
                                    // selection sur le HUD du joueur
                                    hudUnite = new HudUnite(players[localPlayer], selectedUnite, input, this);
                                    //hudUnite.setSelectedUnite(selectedUnite);
                                    //reset action manager
                                    manager = hudUnite.getSelectedAction();
                                }
                            });
                        }
                    }
                }
            }

            if (selectedUnite != null && selectedUnite.isDead()) {
                selectedUnite = null;
                hudUnite = null;
                manager = null;
            }
        }
    }

    private void updateOtherActions(ConstTime time) {
        System.out.println("update other actions 1");
        if (!serverActions.isEmpty()) {
            System.out.println("update other actions 2 ");
            serverActions.peek().update(time);
            if (serverActions.peek().isFinished()) {
                System.out.println("update other actions 3 ");
                serverActions.remove();
            }
        }

        serverIsActing = !serverActions.isEmpty();
    }

    private void updatePlacement(ConstTime elapsed) throws RuntimeException {
        // exception throwing
        if (map.getSpawnPoints(localPlayer).size() < players[localPlayer].getUnites().size()) throw new RuntimeException("La map n'a pas assez de slot spawn");

        // update indicators
        for (int i = 0; i < spawnIndicators.length ; ++i) {
            // on obtient les coordonnées de la caméra
            Vector2f dim = mapCam.getDimension(); // dimension de la vue de la caméra
            Vector2f tlCorner = mapCam.getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra
            // on calcule le décallage si la carte est trop petite par rapport a l'écran
            Vector2f mapBorder = new Vector2f(Math.max(0, (dim.x - map.getWidth() * 64) / 2.f), Math.max(0, (dim.y - map.getHeight() * 64) / 2.f));
            // on cacule la zone rectangulaire où les tuiles doivent être affichées
            float x = Math.max(0, (tlCorner.x / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            float y = Math.max(0, (tlCorner.y / 64.f)); // tuile la plus a gauche du point de vue de la caméra
            float x2 = Math.max(0, (tlCorner.x / 64.f) + (dim.x / 64.f)); // tuile la plus a droite affichée du point de vue de la caméra
            float y2 = Math.max(0, (tlCorner.y / 64.f) + (dim.y / 64.f)); // tuile la plus en bas affichée du point de vue de la caméra

            Vector2i spawn = map.getSpawnPoints(localPlayer).get(i);
            Vector2f hudPos = new Vector2f(Math.min(dim.x - 32, (Math.min(x2, Math.max(x, spawn.x)) - x) * 64 + 32) + mapBorder.x, (Math.min(dim.y - 32, (Math.min(y2, Math.max(y, spawn.y)) - y) * 64 + 32)) + mapBorder.y);
            spawnIndicators[i].setPosition(hudPos.x, hudPos.y);
            float ratio = Math.min(1, 2.f * Math.max(0,(float)(1.0 / (1.0 + Math.exp(hudPos.add(tlCorner).sum(new Vector2f(spawn).mul(64.f).neg()).length() / 1000.f)))));

            if (players[localPlayer].getUnites().stream().anyMatch(u -> spawn.equals(u.getMapPosition()))) {
                spawnIndicators[i].setFillColor(new Color(ratio, 0, 0, 0.2f));
            } else {
                spawnIndicators[i].setFillColor(new Color(ratio, 0, 0, 1.f));
            }
        }

        // spawn unite selection
        if (input.getFrameRectangle().contains(input.getMousePosition().x, input.getMousePosition().y)) {
            Vector2f mouse = input.getMousePositionOnMap();
            Vector2i tile = new Vector2i((int) (mouse.x / 64.f), (int) (mouse.y / 64.f));
            //selectedSpawn.setPosition(tile.x * 64, tile.y * 64);
            Unite unite = players[localPlayer].getUnites().get(placedUnite);

            if (input.isLeftReleased() && map.getSpawnPoints(localPlayer).contains(tile) && players[localPlayer].getUnites().stream().noneMatch(u -> u != unite && tile.equals(u.getMapPosition()))) {
                unite.setMapPosition(new Vector2i(tile.x, tile.y));
                unite.getSprite().setPosition(tile.x * 64, tile.y * 64);
                SpawnPacket packet = new SpawnPacket();
                packet.playerId = localPlayer;
                packet.uniteId = placedUnite;
                packet.spawn = unite.getMapPosition();
                client.send(packet);
                placedUnite++;
            }

            if (placedUnite == players[localPlayer].getUnites().size()) {
                initialized = true;
                updateFOG();
                client.send(new ReadyPacket());
            }
        }
    }


    @Override
    public void update(ConstTime time) {
        nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        if (selectedUnite != null) selectedUnite.getSprite().setFillColor(Color.Red);

        this.updateCamera(time);


        if (initialized) {
            if (!spawnReady) {
                this.waitSpawnPacket();
            } else {
                if (serverIsActing) {
                    // si l'adversaire n'a pas encore fini ses actions
                    updateOtherActions(time);
                } else {
                    this.waitGamePacket();

                    if (inAction) {
                        this.updateActionProgress(time);
                    }
                    if (localPlayer == currentPlayer) {
                        this.updateUserInput(time);
                    }
                }
            }
        } else {
            this.updatePlacement(time);
        }


        input.reset();
    }

    private void waitSpawnPacket() {
        if (client.isRunning()) {
            if (!client.isReceptionEmpty()) {
                Packet packet = client.received();
                if (packet instanceof SpawnPacket) {
                    SpawnPacket sp = (SpawnPacket)packet;
                    players[sp.playerId].getUnites().get(sp.uniteId).setMapPosition(sp.spawn);
                    players[sp.playerId].getUnites().get(sp.uniteId).getSprite().setPosition(sp.spawn.x * 64, sp.spawn.y * 64);
                }
            }
        }

        spawnReady = Arrays.stream(players).allMatch(p -> p.getUnites().stream().allMatch(u -> u.getMapPosition() != null));
    }

    private void waitGamePacket() {
        if (client.isRunning()) {
            while (!client.isReceptionEmpty()) {
                System.out.println("Packet received");
                Packet packet = client.received();
                if (packet instanceof TurnPacket) {
                    if (currentPlayer == localPlayer)
                        throwErrorToServer("Not your turn you idiot but ok");
                    endTurn();
                    serverIsActing = true;
                } else if (packet instanceof ActionPacket) {
                    System.out.println("Action received");
                    // si on reçoit ce packet alors que c'est notre tour (bug)
                    if (currentPlayer == localPlayer) {
                        throwFatalErrorToServer("Not your turn [ActionPacket]");
                    } else {
                        ActionPacket ap = (ActionPacket) packet;
                        // on donne un context à l'action
                        ap.action.init(this);
                        serverActions.add(ap.action);
                    }
                } else if (packet instanceof ErrorPacket) {
                    System.out.println("Server issue: " + ((ErrorPacket) packet).msg);
                } else if (packet instanceof FatalErrorPacket) {
                    System.out.println("Server fatal issue: " + ((FatalErrorPacket) packet).msg);
                    client.close();
                    running = false;
                } else {
                    System.out.println("unknown received");
                }
            }
        }
    }


    @Override
    public void handle(Event event) {
        if (event.type == Event.Type.RESIZE) {
            mapCam.setDimension(new Vector2f(event.resizeX, event.resizeY));
            hudCam.setDimension(new Vector2f(event.resizeX, event.resizeY));
            hudCam.setCenter(new Vector2f(event.resizeX / 2.f, event.resizeY / 2.f));
            viewport.setDimension(new Vector2f(event.resizeX, event.resizeY));
        }

        input.update(event);
    }

    //déssine le sol (ce que voit la camera)
    private void drawMapFloor(int x, int y, int x2, int y2, RenderTarget target) {
        for (int i = x; i < x2 && i < map.getWidth(); ++i) {
            for (int j = y; j < y2 && j < map.getHeight(); ++j) {
                Sprite sp = map.getWorld()[i][j].getFloor();
                if (sp != null) {
                    final int xp = i, yp = j;
                    if (visibles.stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
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
    private void drawUnite(RenderTarget target) {
        //Arrays.stream(players).forEach(p -> {if (p != null && !p.getUnites().isEmpty()) p.getUnites().get(0).draw(target);});
        players[localPlayer].getUnites().forEach(u -> {
            if (u.isDead())
                target.draw(u.getSprite(), ResourceHandler.getShader("grey"));
            else target.draw(u.getSprite());
        });
        players[(localPlayer+1)%2].getUnites().forEach(u -> {
            if (visibles.stream().anyMatch(v -> v.equals( u.getMapPosition()))) {
                if (u.isDead())
                    target.draw(u.getSprite(), ResourceHandler.getShader("grey"));
                else target.draw(u.getSprite());
            }
        });
    }

    //déssine les structs (ce que voit la camera)
    private void drawMapStruct(int x, int y, int x2, int y2, RenderTarget target) {
        for (int i = x; i < x2 && i < map.getWidth(); ++i) {
            for (int j = 0; j < y2 && j < map.getHeight(); ++j) {
                Sprite sp = map.getWorld()[i][j].getStruct();
                if (sp != null) {
                    final int xp = i, yp = j;
                    if (visibles.stream().anyMatch(v2i -> v2i.x == xp && v2i.y == yp)) {
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
        // save previous view
        final Camera previousCam = target.getCamera();
        final Viewport previousViewport = target.getViewport();

        // game viewport
        target.setViewport(viewport);

        // map view
        target.setCamera(mapCam);

        // on obtient les coordonnées de la caméra
        Vector2f dim = mapCam.getDimension(); // dimension de la vue de la caméra
        Vector2f tlCorner = mapCam.getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra
        // on cacule la zone rectangulaire où les tuiles doivent être affichées
        int x = Math.max(0, (int) (tlCorner.x / 64.f)); // tuile la plus a gauche du point de vue de la caméra
        int y = Math.max(0, (int) (tlCorner.y / 64.f)); // tuile la plus a gauche du point de vue de la caméra
        final int offset = 2; // sert pour afficher les cases mal concidérées
        int x2 = Math.max(0, (int) (tlCorner.x / 64.f) + (int) (dim.x / 64.f) + offset); // tuile la plus a droite affichée du point de vue de la caméra
        int y2 = Math.max(0, (int) (tlCorner.y / 64.f) + (int) (dim.y / 64.f) + offset); // tuile la plus en bas affichée du point de vue de la caméra


        if (!initialized && currentPlayer == localPlayer) {
            ResourceHandler.getShader("grey").bind();
            GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), 1.0f);
        } else {
            ResourceHandler.getShader("grey").bind();
            GL20.glUniform1f(ResourceHandler.getShader("grey").getUniformLocation("colorRatio"), (!initialized) ? 0.2f : 0.05f);
        }


        // draw map
        drawMapFloor(x, y, x2, y2, target);
        if (currentPlayer == localPlayer && currentAction != null)
            currentAction.drawAboveFloor(target);
        if (currentPlayer == localPlayer && manager != null)
            manager.drawAboveFloor(target);
        drawUnite(target);
        if (currentPlayer == localPlayer && currentAction != null)
            currentAction.drawAboveEntity(target);
        if (currentPlayer == localPlayer && manager != null)
            manager.drawAboveEntity(target);
        drawMapStruct(x, y, x2, y2, target);
        if (currentPlayer == localPlayer && currentAction != null)
            currentAction.drawAboveStruct(target);
        if (currentPlayer == localPlayer && manager != null)
            manager.drawAboveStruct(target);

        // on affiche au niveau du hud
        target.setCamera(hudCam);

        if (initialized) {
            if (currentPlayer == localPlayer && currentAction != null)
                currentAction.drawAboveHUD(target);
            if (currentPlayer == localPlayer && manager != null)
                manager.drawAboveHUD(target);

            hudPlayer.draw(target);
            if (/*!inAction && */hudUnite != null && currentPlayer == localPlayer) {
                hudUnite.draw(target);
            }
            if (currentPlayer == localPlayer && !serverIsActing && !inAction) {
                target.draw(nextTurn);
            }
        } else {
            Arrays.stream(spawnIndicators).forEach(target::draw);
        }

        // set to previous view
        target.setViewport(previousViewport);
        target.setCamera(previousCam);
    }


    private void throwErrorToServer(String msg) {
        ErrorPacket p = new ErrorPacket();
        p.msg = msg;
        client.send(p);
    }

    private void throwFatalErrorToServer(String msg) {
        FatalErrorPacket p = new FatalErrorPacket();
        p.msg = msg;
        client.send(p);
        running = false;
    }


}
