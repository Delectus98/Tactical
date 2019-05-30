package app.network;

import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;
import app.actions.Running;
import app.actions.Shooting;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.Set;


public class ServerImpl extends Listener {

    private boolean running;
    private Server s;
    private int tcpPort;
    private int udpPort;

    public ServerImpl(int tcp, int udp) throws IOException {
        this.tcpPort = tcp;
        this.udpPort = udp;

        s = new Server();


        s.getKryo().register(PlayerPacket.class);
        s.getKryo().register(ActionPacket.class);
        s.getKryo().register(Vector2i.class);
        s.getKryo().register(Vector2f.class);
        s.getKryo().register(Shooting.class);
        s.getKryo().register(Running.class);

        s.start();
        s.bind(tcp, udp);

        s.addListener(this);

        running = true;
    }

    public boolean isRunning(){
        return running;
    }

    public void close() {
        running = false;
        s.close();
    }

    public void send(Object o) {
        s.sendToAllTCP(o);
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("connection:"+connection.getRemoteAddressTCP());
    }

    @Override
    public void received(Connection connection, Object p) {

    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("disconnection:"+connection.getRemoteAddressTCP());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerImpl s = new ServerImpl(5002, 5003);

        System.out.println("Try to send msg!");
        PlayerPacket p = new PlayerPacket();
        p.name = "Player Name Test";

        ActionPacket p2 = new ActionPacket();
        p2.action = new Shooting(new Vector2i(0,0), new Vector2i(0,0), 0,10, Time.seconds(0));
        //p2.action = new Running();

        while (s.isRunning()) {
            s.send(p);
            s.send(p2);
            Thread.sleep(1000);
        }
    }
}
