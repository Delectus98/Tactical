package app.play;


import Graphics.Vector2i;
import app.Game;
import app.actions.Action;
import app.network.ActionPacket;
import app.network.Packet;
import app.network.ServerImpl;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import System.*;
import app.network.TurnPacket;
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

    public ServerGame(ServerImpl server){
        this.server = server;
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
                    clientActions.add(ap.action);
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
