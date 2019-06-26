package app.network;

public class GameOverPacket extends Packet {
    public String reason;
    public boolean abandon;

    @Override
    public int getPriority() {
        return 0;
    }
}
