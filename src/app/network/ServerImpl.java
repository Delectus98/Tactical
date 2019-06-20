package app.network;

import System.*;
import Graphics.Vector2i;
import app.actions.Shooting;
import app.weapon.Impact;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;


public class ServerImpl extends Listener {

    private PriorityQueue<Packet> packets = new PriorityQueue<>(new Packet.Comparator());
    private boolean running;
    protected final Server s;
    protected final int tcpPort;
    protected final int udpPort;

    public ServerImpl(int tcp, PacketRegistration registration) throws IOException {
        this.tcpPort = tcp;
        this.udpPort = -1;

        s = new Server();

        registration.register(s);

        s.start();
        s.bind(tcp);

        s.addListener(this);

        running = true;
    }

    public ServerImpl(int tcp, int udp, PacketRegistration registration) throws IOException {
        this.tcpPort = tcp;
        this.udpPort = udp;

        s = new Server();

        registration.register(s);

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
        Arrays.stream(s.getConnections()).forEach(Connection::close);
        s.close();
    }

    public synchronized void send(Object o) {
        s.sendToAllTCP(o);
    }

    public synchronized void send(Object o, int index) {
        if (0 <= index && index < this.getClientCount()) s.sendToTCP(index, o);
    }

    public synchronized int getClientCount(){
        return s.getConnections().length;
    }

    public synchronized Connection getClient(int i) {
        Connection[] t = s.getConnections();
        if (0<= i && i < t.length) return t[i];
        else return null;
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
        return packets.remove();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerImpl s = new ServerImpl(5002, GameRegistration.instance);
        System.out.println(PlayerPacket.class);
        System.out.println("Try to send msg!");
        PlayerPacket p = new PlayerPacket();
        p.name = "Player Name Test\n";
        PlayerPacket p2 = new PlayerPacket();
        p2.name = "Player Pas Name Test\n";
        //ActionPacket p2 = new ActionPacket();
        //p2.action = new Shooting(new Vector2i(0,0), new Vector2i(0,0), new Impact(),10, Time.seconds(0));

        while (s.isRunning()) {
            s.send(p);
            s.send(p2);

            Thread.sleep(1000);
        }
    }
}
