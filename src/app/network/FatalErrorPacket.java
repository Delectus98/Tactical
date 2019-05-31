package app.network;

/**
 * Packet used to send fatal error message that close the connexion
 * @see ErrorPacket
 */
public class FatalErrorPacket extends Packet {
    public String msg;

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }
}
