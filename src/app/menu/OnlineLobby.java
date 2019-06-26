package app.menu;

import Graphics.Sprite;
import System.VideoMode;
import app.*;
import app.map.MapImpl;
import app.menu.Buttons.ReadyButton;
import app.menu.Buttons.RenamePlayer;
import app.menu.Buttons.SquadButton;
import app.menu.Buttons.TeamButton;
import app.network.*;
import app.play.ClientGame;
import app.play.ServerGame;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static app.MainMENU.window;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class OnlineLobby extends Lobby {

    int myPlayerId;
    boolean isHost;
    String ip = "0.0.0.0";
    public Listener listener;
    private Player[] playerlist = new Player[2];
   public boolean isClientReady = false;
    boolean isClientHere = false;


    public OnlineLobby(boolean isHost) throws IOException {

        super(isHost ? "Host Game" : "Local Game", MainMENU.ONLINE);
        setPlayers(playerlist);
        this.isHost = isHost;

        //READYBUTTON
        ReadyButton ready = new ReadyButton();
        ready.setPosition(MainMENU.WIDTH - ready.getSprite().getBounds().w, MainMENU.HEIGHT - ready.getSprite().getBounds().h);
        this.getButtons().add(ready);
        //MAPBUTTON
        this.getButtons().add(new toMapButton(new Sprite(((MapImpl) getMap()).getMiniature())));

//HOST MODE
        if (isHost) {
            myPlayerId = 0;
            try {
                ServerImpl server = new ServerImpl(25565, GameRegistration.instance);
                server.setClientLimit(1);
                this.listener = server;


//INI player 1
                playerlist[0] = new Player("Player1");
                playerlist[0].setTeam(Team.MAN);
                playerlist[0].setId(0);
                SquadButton b = new SquadButton(getPlayers()[0], Menu.newButtonSprite("menuLarge"));
                b.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b.getSprite().getBounds().l);
                getButtons().add(b);


                RenamePlayer rename = new RenamePlayer(getPlayers()[0]);
                rename.setPosition(b.getSprite().getBounds().l + 15 + b.getSprite().getBounds().w, b.getSprite().getBounds().t);
                getButtons().add(rename);


                TeamButton t = new TeamButton(getPlayers()[0], Menu.newButtonSprite("menuSmall"));
                t.setPosition(rename.getSprite().getBounds().l, rename.getSprite().getBounds().t + rename.getSprite().getBounds().h + 10);
                getButtons().add(t);
//INI player 2
                playerlist[1] = new Player("Waiting for player 2...");
                playerlist[1].setTeam(Team.MAN);
                playerlist[1].setId(1);

                SquadButton b2 = new SquadButton(getPlayers()[1], Menu.newButtonSprite("menuLarge"));

                b2.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b2.getSprite().getBounds().l + 15 + b2.getSprite().getBounds().h + b2.getSprite().getBounds().l);
                getButtons().add(b2);


                RenamePlayer rename2 = new RenamePlayer(getPlayers()[1]);
                rename2.setPosition(b2.getSprite().getBounds().l + 15 + b2.getSprite().getBounds().w, b2.getSprite().getBounds().t);
                getButtons().add(rename2);


                TeamButton t2 = new TeamButton(getPlayers()[1], Menu.newButtonSprite("menuSmall"));
                t2.setPosition(rename2.getSprite().getBounds().l, rename2.getSprite().getBounds().t + rename2.getSprite().getBounds().h + 10);
                getButtons().add(t2);
                t2.setReady(false);


            } catch (IOException e) {
                e.printStackTrace();
            }


//CLIENT MODE
        } else {//isClient

            myPlayerId = 1;
            ClientImpl client = null;
            this.listener = client;
            //TODO TextField ip:port
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer IP ");
            ip = sc.nextLine();
            if (checkip(ip)) {

                //get player list
                //get map
                //get squadcreationpoints


            } else {
                System.out.println("ip incorrecte");
                System.exit(0);
            }
            client = new ClientImpl(ip, 25565, GameRegistration.instance);


        }
    }


    @Override
    public void update() throws IOException {
        if (isHost && isClientHere) {//reception of a package
            if (((ServerImpl) listener).getClientCount() == 1) {

//check changes in team,name, squad,client Ready,is client connected
//send changes in team, name, squad, map,creationpoints,launch game
                for (int k = 0; k < 10; k++)
                    if (!((ServerImpl) listener).isReceptionEmpty()) {
                        Packet p = ((ServerImpl) listener).received();
                        if (p instanceof PlayerPacket) {
                            playerlist[1].setName(((PlayerPacket) p).name);

                        } else if (p instanceof SquadPacket) {
                            playerlist[1].getUnites().clear();//reset unités du joueur
                            for (int i = 0; i < ((SquadPacket) p).squad.size(); i++) {
                                Unite u;
                                switch (((SquadPacket) p).squad.get(i)) {
                                    case 0:
                                        u = new SoldierUnit(players[1].getTeam());
                                        break;
                                    case 1:
                                        u = new MarksmanUnit(playerlist[1].getTeam());
                                        break;
                                    default:
                                        System.out.println("erreur packet");
                                        u = new SoldierUnit(Team.MAN);
                                        break;
                                }
                                playerlist[1].addUnite(u);
                            }
                        } else if (p instanceof ReadyPacket) {
                            isClientReady = !isClientReady;
                        } else {
                            System.out.println("Paquet étrange" + p.getClass());
                        }

                    }
            } else if (isHost && !isClientHere) {//First contact, send stuff
                if (((ServerImpl) listener).getClientCount() == 1) {
                    isClientHere = true;
                    //playerHost info
                    PlayerPacket p = new PlayerPacket();
                    p.name = playerlist[0].getName();
                    p.id = 0;
                    ((ServerImpl) listener).send(p);
                    //Map
                    MapPacket m = new MapPacket();
                    m.index = getMapIndex();
                    ((ServerImpl) listener).send(m);
                    //Units
                    ArrayList<Integer> squad = new ArrayList<>();
                    for (Unite u : playerlist[0].getUnites()) {
                        int type = 0;
                        if (u instanceof MarksmanUnit) {
                            type = 1;
                        } else if (u instanceof SoldierUnit) {
                            type = 0;
                        } else {
                            type = -1;
                        }
                        squad.add(type);
                        SquadPacket sq = new SquadPacket();
                        sq.squad=squad;
                    }
                    isClientHere = true;
                }

            }
        } else {//isClient
            for (int k = 0; k < 10; k++)
                if (!((ServerImpl) listener).isReceptionEmpty()) {
                    Packet p = ((ServerImpl) listener).received();
                    if (p instanceof PlayerPacket) {
                        playerlist[0].setName(((PlayerPacket) p).name);
//                        playerlist[0].setId(((PlayerPacket) p).id);
                        //                      playerlist[0].setTeam(playerlist[0].getTeam() == Team.MAN ? Team.APE : Team.MAN);
                    } else if (p instanceof SquadPacket) {
                        for (int i = 0; i < ((SquadPacket) p).squad.size(); i++) {
                            Unite u;
                            switch (((SquadPacket) p).squad.get(i)) {
                                case 0:
                                    u = new SoldierUnit(players[0].getTeam());
                                    break;
                                case 1:
                                    u = new MarksmanUnit(playerlist[0].getTeam());
                                    break;
                                default:
                                    System.out.println("erreur packet");
                                    u = new SoldierUnit(Team.MAN);
                                    break;
                            }
                            playerlist[0].addUnite(u);
                        }
                    } else if (p instanceof ReadyPacket) {
                        // isClientReady = !isClientReady;
                            MainMENU.state = MainMENU.STATE.GAME;

                            window.setDimension(VideoMode.getDesktopMode());
                            glfwMaximizeWindow(window.getGlId());

                            if (getGame() != null) {
                                MainMENU.currentGame =getGame();
                                MainMENU.currentGame.start();
                            } else {
                                System.out.println("readybutton: game Not ready");
                            }
                    } else if (p instanceof MapPacket) {
                        this.setMap(MainMENU.availableMaps[((MapPacket) p).index]);

                    } else {
                        System.out.println("Paquet étrange" + p.getClass());
                    }
                }
        }

        if (isClientReady) startgame();
    }

    private void startgame() {
        //todo copier depuis readybutton
        //MainMENU.currentGame = new Game(); etc
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
        if (MainMENU.LOBBY == MainMENU.JOIN) {
            return new ClientGame((ClientImpl) listener, getPlayers(), myPlayerId, getMap(), MainMENU.window);
        } else if ((MainMENU.LOBBY == MainMENU.HOST)) {
            return new ServerGame((ServerImpl) listener, getPlayers(), myPlayerId, getMap(), MainMENU.window);
        } else {
            System.out.println("Shouldnt happend: online lobby");
            return null;
        }
    }
}
