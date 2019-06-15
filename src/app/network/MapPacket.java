package app.network;


public class MapPacket extends Packet {
    public String file = "";

    @Override
    public int getPriority() {
        return 0;
    }
}
