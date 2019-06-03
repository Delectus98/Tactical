package app.network;

import app.actions.Action;

public class ActionPacket extends Packet {
    public int uniteId;
    public int playerId;
    public Action action;

    @Override
    public int getPriority() {
        return 0;
    }
}
