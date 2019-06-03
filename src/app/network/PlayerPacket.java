package app.network;


public class PlayerPacket extends Packet {
    public String name;

    @Override
    public int getPriority() {
        return 0;
    }
}
