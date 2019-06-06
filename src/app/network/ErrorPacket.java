package app.network;

/**
 * Packet used to send error message without closing the connexion
 * @see FatalErrorPacket
 */
public class ErrorPacket extends Packet {
    public String msg;

    @Override
    public int getPriority() {
        return 0;
    }
}
