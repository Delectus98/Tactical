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

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import System.*;
import util.GameInput;
import util.MapUtil;
import util.ResourceHandler;

public class ClientGame extends Game {
    private static final int TCP_PORT = 25565;
    private static final int UDP_PORT = 25567;

    private int localPlayer = 0;
    private ClientImpl client = null;
    private boolean running = false;

    private boolean serverEndHisTurn = true;
    private Queue<Action> clientActions = new PriorityQueue<>();

    private boolean inAction = false;
    private Action hostAction = null;
    private ActionManager hostManager = null;

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

        mouse = new Mouse(context);
        keyboard = new Keyboard(context);

        input = new GameInput(mapCam, hudCam, viewport, mouse, keyboard);

        updateFOG();

        hudPlayer = new HudPlayer(this, players[this.localPlayer], input);

        // ui
        nextTurn = new Text(ResourceHandler.getFont("default"), "Next Turn");
        nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);
    }

    @Override
    public void endTurn() {
        // try to change of turn
        client.send(new TurnPacket());
    }

    @Override
    public boolean isFinished() {
        return !running || !client.isRunning();
    }

    @Override
    public void start(){
        client.send(new ReadyPacket());
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

    private void updateActionProgress(ConstTime time) {
        //déroulement des actions
        if (hostAction != null && !hostAction.isFinished()) {
            hostAction.update(time);
        } else {
            // il n'y a plus d'action alors on arrete la phase déroulement des actions
            inAction = false;
            hostAction = null;
            hudUnite.resetSelectedAction();
            hostManager = hudUnite.getSelectedAction();
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
        nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        Vector2f mouseHud = input.getMousePositionOnHUD();

        if (input.isLeftReleased() && nextTurn.getBounds().contains(mouseHud.x, mouseHud.y)) {
            endTurn();
        }

        hudPlayer.update(time);

        if (hudPlayer.isSelected()) {
            if (selectedUnite != hudPlayer.getSelectedUnit() && !hudPlayer.getSelectedUnit().isDead()) {
                selectedUnite = hudPlayer.getSelectedUnit();
                //reset player HUD
                // selection sur le HUD du joueur
                hudUnite = new HudUnite(players[currentPlayer], selectedUnite, input, this);
                //hudUnite.setSelectedUnite(selectedUnite);
                //reset action manager
                hostManager = hudUnite.getSelectedAction();
            }
        }

        if (hudUnite != null) {
            hudUnite.update(time);
            if (hudUnite.getSelectedAction() != hostManager) {
                hostManager = hudUnite.getSelectedAction();
            }
        }

        if (hostManager != null) {
            if ((hudUnite == null || !hudUnite.isClicked()) && (hudPlayer == null || !hudPlayer.isSelected())) {
                hostManager.updatePreparation(time);
                if (hostManager.isAvailable()) {
                    hostAction = hostManager.build();
                    hostAction.init(this);
                    selectedUnite.removePA((short) hostAction.getCost());
                    inAction = true;
                    hostManager = null;
                }
            }
        }


        // la souris est dans le rectangle du jeu du bon joueur && qu'aucune action ne va se dérouler après alors on peut cliquer sur une unité
        if (input.isLeftReleased() && input.getFrameRectangle().contains(input.getMousePosition().x, input.getMousePosition().y)) {
            // selection d'unité sur le HUD

            // selection d'unité sur la map
            players[currentPlayer].getUnites().forEach(u -> {
                if (!u.isDead() && u.getSprite().getBounds().contains(input.getMousePositionOnMap().x, input.getMousePositionOnMap().y)) {
                    selectedUnite = u;
                    //reset player HUD
                    // selection sur le HUD du joueur
                    hudUnite = new HudUnite(players[currentPlayer], selectedUnite, input, this);
                    //hudUnite.setSelectedUnite(selectedUnite);
                    //reset action manager
                    hostManager = hudUnite.getSelectedAction();
                }
            });
        }

        if (selectedUnite != null && selectedUnite.isDead()) {
            selectedUnite = null;
            hudUnite = null;
            hostManager = null;
        }

    }

    private void updateOtherActions(ConstTime time) {
        if (!clientActions.isEmpty()) {
            clientActions.peek().update(time);
            if (clientActions.peek().isFinished()) {
                clientActions.remove();
            }
        }

        serverEndHisTurn = clientActions.isEmpty();
    }

    @Override
    public void update(ConstTime time) {
        nextTurn.setPosition(input.getFrameRectangle().w - nextTurn.getBounds().w - 10, 0);

        if (client.isRunning()) {
            if (!client.isReceptionEmpty()) {
                Packet packet = client.received();
                if (packet instanceof TurnPacket) {
                    // si on reçoit ce packet alors on change de tour (c'est le serveur qui décide)
                    if (currentPlayer != localPlayer) {
                        serverEndHisTurn = false;
                    }

                    currentPlayer = (currentPlayer + 1) % players.length;
                } else if (packet instanceof ActionPacket) {
                    // si on reçoit ce packet alors que c'est notre tour (bug)
                    if (currentPlayer == localPlayer) {
                        throwFatalErrorToServer("Not your turn [ActionPacket]");
                    } else {
                        ActionPacket ap = (ActionPacket) packet;
                        // on donne un context à l'action
                        ap.action.init(this);
                        clientActions.add(ap.action);
                    }
                } else if (packet instanceof ErrorPacket) {
                    System.out.println("Server issue: " + ((ErrorPacket) packet).msg);
                } else if (packet instanceof FatalErrorPacket) {
                    System.out.println("Server fatal issue: " + ((FatalErrorPacket) packet).msg);
                    client.close();
                    running = false;
                }
            }
        }

        this.updateCamera(time);

        if (serverEndHisTurn) {
            updateOtherActions(time);
        } else if (localPlayer == currentPlayer) {
            if (inAction) {
                this.updateActionProgress(time);
            } else {
                this.updateUserInput(time);
            }
        }


        input.reset();
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
            if (visibles.stream().anyMatch(v -> u.getMapPosition().equals(v))) {
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



        // draw map
        drawMapFloor(x, y, x2, y2, target);
        if (currentPlayer == localPlayer && hostAction != null)
            hostAction.drawAboveFloor(target);
        if (currentPlayer == localPlayer && hostManager != null)
            hostManager.drawAboveFloor(target);
        drawUnite(target);
        if (currentPlayer == localPlayer && hostAction != null)
            hostAction.drawAboveEntity(target);
        if (currentPlayer == localPlayer && hostManager != null)
            hostManager.drawAboveEntity(target);
        drawMapStruct(x, y, x2, y2, target);
        if (currentPlayer == localPlayer && hostAction != null)
            hostAction.drawAboveStruct(target);
        if (currentPlayer == localPlayer && hostManager != null)
            hostManager.drawAboveStruct(target);

        // on affiche au niveau du hud
        target.setCamera(hudCam);

        if (currentPlayer == localPlayer && hostAction != null)
            hostAction.drawAboveHUD(target);
        if (currentPlayer == localPlayer && hostManager != null)
            hostManager.drawAboveHUD(target);

        hudPlayer.draw(target);
        if (hudUnite != null && currentPlayer == localPlayer) {
            hudUnite.draw(target);
        }
        if (currentPlayer == localPlayer) {
            target.draw(nextTurn);
        }


        // set to previous view
        target.setViewport(previousViewport);
        target.setCamera(previousCam);
    }


}
