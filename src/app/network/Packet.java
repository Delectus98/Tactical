package app.network;

/**
 * Interface used to send information threw network with emitter information
 */
public abstract class Packet {
    private transient boolean received = false;
    private transient String ip;
    private transient int port;

    public Packet(){}

    /**
     * When a Packet is received 'onReceive' save trace of the emitter
     * @param from emitter's ip address
     * @param port emitter's port
     */
    public final void onReceive(String from, int port) {
        this.ip = from;
        this.port = port;
        this.received = true;
    }

    /**
     * Gives emitter address.
     * @return emitter address.
     */
    public final String getEmitterAddress(){
        return ip;
    }

    /**
     * Gives emitter port.
     * @return emitter port.
     */
    public final int getEmitterPort(){
        return port;
    }

    /**
     * Gives the priority of the packet inside the packet treatment queue
     * @return the priority of the packet inside the packet treatment queue. 0 is default priority.
     */
    public abstract int getPriority();

    public static class Comparator implements java.util.Comparator<Packet> {
        @Override
        public int compare(Packet x, Packet y)
        {
            return Integer.compare(x.getPriority(), y.getPriority());
        }
    }
}
