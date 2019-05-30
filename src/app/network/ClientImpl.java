package app.network;

import Graphics.Vector2f;
import Graphics.Vector2i;
import app.actions.Running;
import app.actions.Shooting;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;


public class ClientImpl extends Listener {

    private boolean running;
    private Client c;
    private String ip;
    private int tcpPort;
    private int udpPort;

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
        c.getKryo().register(Running.class);

        c.start();
        c.connect(1000, ip, tcp, udp);

        c.addListener(this);

        running = true;
    }

    public boolean isRunning(){
        return running;
    }

    public void close() {
        running = false;
        c.close();
    }

    public void send(Object o) {
        c.sendTCP(o);
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connexion success");
        running = true;
    }

    @Override
    public void received(Connection connection, Object p) {
        if (p instanceof PlayerPacket) {
            System.out.println(((PlayerPacket) p).name);
        } else if (p instanceof ActionPacket) {
            System.out.println(((ActionPacket)p).actionId);
            if (((ActionPacket)p).action instanceof Shooting) {
                System.out.print("Shooting: ");
                System.out.println((((ActionPacket)p).action).getCost());
            } else if (((ActionPacket)p).action instanceof Running) {
                System.out.print("Running: ");
                System.out.println((((ActionPacket)p).action).getCost());

            } else {
                System.out.println("Else");
            }
        } else {
            System.out.println("Try to decode msg!");
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnexion success");
        this.close();
    }

    public static void main(String[] args) throws IOException {
        ClientImpl c = new ClientImpl("localhost", 5002, 5003);

        System.out.println("Try to receive msg!");

        while (c.isRunning()) {
            //c.c.update(0);
        }
    }
}