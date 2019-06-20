package app.network;

import Graphics.Vector2i;

public class SpawnPacket extends Packet {
    public int playerId;
    public int uniteId;
    public Vector2i spawn;

    @Override
    public int getPriority() {
        return 0;
    }
}
