package app.menu;

import app.Game;
import app.MainMENU;
import app.network.ClientImpl;

import java.util.Scanner;

public class OnlineLobby extends Lobby {

    int myPlayerId;
    boolean isHost;
    String ip = "0.0.0.0";

    public OnlineLobby(boolean isHost) {
        super(isHost ? "Host Game" : "Local Game", MainMENU.ONLINE);
        this.isHost = isHost;

        if (isHost) {


        } else {//isClient
//TextField ip:port

            ClientImpl client;

            //TODO TextField
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer IP ");
            ip = sc.nextLine();
            if (checkip(ip)) {

            }


        }
    }


    @Override
    public void update() {
        if (isHost) {
//check changes in team,name, squad,client Ready,is client connected
//send changes in team, name, squad, map,creationpoints,launch game


        } else {//isClient


        }
    }

    public int getMyPlayerId() {
        return myPlayerId;
    }

    /**
     * Verifie le format de l'ip
     *
     * @param ip string de l'ip en ipv4 ex:"127.0.0.1"
     * @return l'ip est valide
     **/
    private static boolean checkip(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public Game getGame() {
        return null;
    }
}
