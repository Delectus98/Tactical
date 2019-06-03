package app.network;

public class TurnPacket extends Packet
{
    @Override
    public int getPriority() {
        return 0;
    }
}
