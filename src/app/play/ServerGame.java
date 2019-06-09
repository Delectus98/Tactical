package app.play;


import Graphics.FloatRect;
import Graphics.Vector2f;
import Graphics.Vector2i;
import app.Game;
import app.actions.Action;
import app.network.*;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import System.*;
import util.GameInput;
import util.MapUtil;

public class ServerGame extends Game {
    private static final int TCP_PORT = 25565;
    private static final int UDP_PORT = 25567;

    private final int localPlayer = 0;
    private ServerImpl server = null;
    private boolean running = false;

    private Queue<Action> clientActions = new PriorityQueue<>();
    private Action hostAction = null;

    private Set<Vector2i> visibles = new HashSet<>();

    private Camera2D mapCam;
    private Camera2D hudCam;
    private Viewport viewport;
    private GameInput input;
    private Mouse mouse;
    private Keyboard keyboard;

    public ServerGame(ServerImpl server, GLFWWindow context){
        this.context = context;
        this.server = server;

        mapCam = new Camera2D(new Vector2f(context.getDimension().x , context.getDimension().y));
        hudCam = new Camera2D(new Vector2f(context.getDimension().x , context.getDimension().y));
        viewport = new Viewport(new FloatRect(0, 0, mapCam.getDimension().x, mapCam.getDimension().y));

        mouse = new Mouse(context);
        keyboard = new Keyboard(context);

        input = new GameInput(mapCam, hudCam, viewport, mouse, keyboard);
    }

    @Override
    public void endTurn() {
        //server.send(new TurnPacket());
    }

    @Override
    public boolean isFinished() {
        return !running || !server.isRunning();
    }

    @Override
    public void start(){
        //server.send(new ReadyPacket());
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

    @Override
    public void update(ConstTime time) {
        if (server.isRunning()) {
            if (!server.isReceptionEmpty()) {
                Packet packet = server.received();
                if (packet instanceof TurnPacket) {

                } else if (packet instanceof ActionPacket) {
                    ActionPacket ap = (ActionPacket)packet;
                    ap.action.init(this);
                    clientActions.add(ap.action);
                } else if (packet instanceof ErrorPacket) {
                    System.out.println(((ErrorPacket)packet).msg);
                } else if (packet instanceof FatalErrorPacket) {
                    System.out.println(((FatalErrorPacket)packet).msg);
                    server.close();
                    running = false;
                }
            }
        }
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void draw(RenderTarget target) {

    }


}
