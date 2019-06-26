package app.network;


public class MapPacket extends Packet {
    public int index = 3;

    @Override
    public int getPriority() {
        return 0;
    }
}
