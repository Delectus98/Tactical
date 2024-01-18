package app.network;

//import com.esotericsoftware.kryonet.Client;
//import com.esotericsoftware.kryonet.Connection;
//import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.PriorityQueue;


public class ClientImpl {

    private PriorityQueue<Packet> packets = new PriorityQueue<>(new Packet.Comparator());
    private boolean running;
   //protected final Client c;
    protected final String ip;
    protected final int tcpPort;
    protected final int udpPort;

    public ClientImpl(String ip, int tcp, PacketRegistration registration) throws IOException {
        this(ip, tcp, -1, registration);
    }

    public ClientImpl(String ip, int tcp, int udp, PacketRegistration registration) throws IOException {
        this.ip = ip;
        this.tcpPort = tcp;
        this.udpPort = udp;

//        c = new Client();
//
//        registration.register(c);
//
//        c.start();
//        c.connect(1000, ip, tcp, udp);
//
//        c.addListener(this);

        running = true;
    }

    public synchronized boolean isRunning(){
        return running;
    }

    public synchronized void close() {
//        running = false;
//        c.close();
    }

    public synchronized void send(Object o) {
//        if (this.isRunning()) {
//            c.sendTCP(o);
//        }
    }

    public synchronized boolean isReceptionEmpty() {
        return packets.isEmpty();
    }

    public synchronized Packet received(){
        return packets.remove();
    }

//    @Override
//    public void connected(Connection connection) {
//        System.out.println("Connexion success");
//        running = true;
//    }
//
//    @Override
//    public synchronized void received(Connection connection, Object p) {
//        if (p instanceof Packet) {
//            ((Packet)p).onReceive(connection.getRemoteAddressTCP().getAddress().toString(), connection.getRemoteAddressTCP().getPort());
//            packets.add((Packet)p);
//        }
//    }
//
//    @Override
//    public void disconnected(Connection connection) {
//        System.out.println("Disconnection success");
//        this.close();
//    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientImpl c = new ClientImpl("localhost", 5002, GameRegistration.instance);

        System.out.println("Try to receive msg!");

        while (c.isRunning()) {
            //c.c.update(0);
            Thread.sleep(100);
            if (!c.isReceptionEmpty()) {
                Packet p = c.received();
                if (p instanceof PlayerPacket) {
                    System.out.println(((PlayerPacket) p).name);
                }
            }
        }
    }
}