package app.network;

import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;
import app.actions.Shooting;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.PriorityQueue;


public class ServerImpl extends Listener {

    private PriorityQueue<Packet> packets = new PriorityQueue<>(new Packet.Comparator());
    private boolean running;
    protected final Server s;
    protected final int tcpPort;
    protected final int udpPort;

    public ServerImpl(int tcp, int udp) throws IOException {
        this.tcpPort = tcp;
        this.udpPort = udp;

        s = new Server();

        s.getKryo().register(PlayerPacket.class);
        s.getKryo().register(ActionPacket.class);
        s.getKryo().register(Vector2i.class);
        s.getKryo().register(Vector2f.class);
        s.getKryo().register(Shooting.class);

        s.start();
        s.bind(tcp, udp);

        s.addListener(this);

        running = true;
    }

    public synchronized boolean isRunning(){
        return running;
    }

    public synchronized void close() {
        running = false;
        s.close();
    }

    public synchronized void send(Object o) {
        s.sendToAllTCP(o);
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("connection:"+connection.getRemoteAddressTCP());
    }

    @Override
    public synchronized void received(Connection connection, Object p) {
        if (p instanceof Packet) {
            ((Packet)p).onReceive(connection.getRemoteAddressTCP().getAddress().toString(), connection.getRemoteAddressTCP().getPort());
            packets.add((Packet)p);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("disconnection:"+connection.getRemoteAddressTCP());
    }

    public synchronized boolean isReceptionEmpty() {
        return packets.isEmpty();
    }

    public synchronized Packet received(){
        return packets.peek();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerImpl s = new ServerImpl(5002, 5003);

        System.out.println("Try to send msg!");
        PlayerPacket p = new PlayerPacket();
        p.name = "Player Name Test";

        ActionPacket p2 = new ActionPacket();
        p2.action = new Shooting(new Vector2i(0,0), new Vector2i(0,0), 0,10, Time.seconds(0));

        while (s.isRunning()) {
            s.send(p);
            s.send(p2);
            Thread.sleep(1000);
        }
    }
}
