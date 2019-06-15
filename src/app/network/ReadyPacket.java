package app.network;

/**
 * ReadPacket's task is to tell to another user the local user is ready to play
 */
public class ReadyPacket extends Packet {
    @Override
    public int getPriority() {
        return 0;
    }
}
