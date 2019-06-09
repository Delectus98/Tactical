package app.network;


public class PlayerPacket extends Packet {
    public String name;
    public int id;

    @Override
    public int getPriority() {
        return 0;
    }
}
