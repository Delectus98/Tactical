package app.network;

import Graphics.Vector2f;
import Graphics.Vector2i;
import app.actions.Shooting;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.PriorityQueue;


public class ClientImpl extends Listener{

    private PriorityQueue<Packet> packets = new PriorityQueue<>(new Packet.Comparator());
    private boolean running;
    protected final Client c;
    protected final String ip;
    protected final int tcpPort;
    protected final int udpPort;

    public ClientImpl(String ip, int tcp, int udp) throws IOException {
        this.ip = ip;
        this.tcpPort = tcp;
        this.udpPort = udp;

        c = new Client();

        c.getKryo().register(PlayerPacket.class);
        c.getKryo().register(ActionPacket.class);
        c.getKryo().register(Vector2i.class);
        c.getKryo().register(Vector2f.class);
        c.getKryo().register(Shooting.class);

        c.start();
        c.connect(1000, ip, tcp, udp);

        c.addListener(this);

        running = true;
    }

    public synchronized boolean isRunning(){
        return running;
    }

    public synchronized void close() {
        running = false;
        c.close();
    }

    public synchronized void send(Object o) {
        if (this.isRunning()) {
            c.sendTCP(o);
        }
    }

    public synchronized boolean isReceptionEmpty() {
        return packets.isEmpty();
    }

    public synchronized Packet received(){
        return packets.peek();
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connexion success");
        running = true;
    }

    @Override
    public synchronized void received(Connection connection, Object p) {
        if (p instanceof Packet) {
            packets.add((Packet)p);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnexion success");
        this.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientImpl c = new ClientImpl("localhost", 5002, 5003);

        System.out.println("Try to receive msg!");

        while (c.isRunning()) {
            //c.c.update(0);
            System.out.println("Client is running");
            Thread.sleep(100);
        }
    }
}