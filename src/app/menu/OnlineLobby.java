package app.menu;

import Graphics.Sprite;
import System.VideoMode;
import app.*;
import app.map.MapImpl;
import app.menu.Buttons.*;
import app.network.*;
import app.play.ClientGame;
import app.play.ServerGame;
import app.units.MarksmanUnit;
import app.units.SoldierUnit;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static app.MainMENU.*;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;

public class OnlineLobby extends Lobby {

    private int myPlayerId;
    private boolean isHost;
    private String ip = "0";
    public Listener listener;
    private Player[] playerlist = new Player[2];
    public boolean isClientReady = false;
    private boolean isClientHere = false;
    private List<Packet> toSend = new LinkedList<>();


    public OnlineLobby(boolean isHost) throws IOException, InterruptedException {

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
                playerlist[1].setTeam(Team.APE);
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
                ((ServerImpl)listener).close();
            }


//CLIENT MODE
        } else {//isClient
            myPlayerId = 1;
            ClientImpl client = null;
            this.listener = client;
/*
            //TODO IPField ip:port
            Scanner sc = new Scanner(System.in);
            System.out.println("Entrer IP :");
            ip = sc.nextLine();
            */
            playerlist[1] = new Player("Player2");
            playerlist[1].setId(1);
            playerlist[1].setTeam(Team.APE);
            playerlist[0] = new Player("Host");
            playerlist[0].setTeam(Team.MAN);
            playerlist[0].setId(0);
            ip= ((OnlineMenu)menulist[ONLINE]).getIP();

              try{  listener = new ClientImpl(ip, 25565, GameRegistration.instance);
              if(!((ClientImpl)listener).isRunning()){
                  System.out.println("listener is not running");
              }
              }
              catch (Exception e){
                      e.printStackTrace();
                  ((ClientImpl)listener).close();
              }
                PlayerPacket p = new PlayerPacket();
                p.name = playerlist[myPlayerId].getName();
                p.id = myPlayerId;
            //    ((ClientImpl) listener).send(p);
                addToSend(p);
                int i=0;
            while (((ClientImpl) listener).isReceptionEmpty() && i<10000) {
//wait
                i++;
            }
            update();

            for (i = 0; i < getPlayers().length; i++) {
                SquadButton b = new SquadButton(getPlayers()[i], Menu.newButtonSprite("menuLarge"));
                b.setPosition(20, 50 + MainMENU.HEIGHT / 10 + b.getSprite().getBounds().l + i * (15 + b.getSprite().getBounds().h + b.getSprite().getBounds().l));
                getButtons().add(b);


                RenamePlayer rename = new RenamePlayer(getPlayers()[i]);
                rename.setPosition(b.getSprite().getBounds().l + 15 + b.getSprite().getBounds().w, b.getSprite().getBounds().t);
                getButtons().add(rename);


                TeamButton t = new TeamButton(getPlayers()[i], Menu.newButtonSprite("menuSmall"));
                t.setPosition(rename.getSprite().getBounds().l, rename.getSprite().getBounds().t + rename.getSprite().getBounds().h + 10);
                getButtons().add(t);
                if (i == 0)
                    t.setReady(false);
/*
            slots=getButtons().size()-1;

            for (int k = 0; k < getSquadCreationPoints(); k++) {
                MenuButton d = new MenuButton("", new Sprite(ResourceHandler.getTexture("squadSlot"))) {
                };
                d.setPosition(64 + (80 * k), b.getSprite().getBounds().t + b.getSprite().getBounds().h / 4);
                getButtons().add(d);
            }//END TODO
*/
            }
//faire les boutons.
        }
    }


    @Override
    public void update() throws InterruptedException, IOException {
        if(this.toUpdate!=null)
            toUpdate.update();

        if ((!isHost &&!isClientHere)|| (isHost && !isClientHere && ((ServerImpl) listener).getClientCount() == 1)) {
            firstContact();
        } else if (isHost && isClientHere) {
            getPackets();
        } else if (!isHost) {
            getPackets();
        }

        //update buttons
        for (MenuComponent b : getButtons()) {
            if (b instanceof toMapButton) {//update miniature
                float x = b.getSprite().getBounds().l;
                float y = b.getSprite().getBounds().t;
                b.setSprite(new Sprite(((MapImpl) getMap()).getMiniature()));
                b.getSprite().setPosition(x, y);
            } else if (b instanceof ReadyButton) {//check ready
                ((ReadyButton) b).checkIfButtonReady();
            } else if (b instanceof SquadButton)
                for (int i = ((SquadButton) b).getPlayer().getUnites().size() - 1; i >= getSquadCreationPoints(); i--)
                    ((SquadButton) b).getPlayer().getUnites().remove(i);
        }
        sendToSend();

    }

    private void getPackets() {
        for (int k = 0; k < 5; k++)
            if (!isHost) {//isClient
                if (!((ClientImpl) listener).isReceptionEmpty()) {
                    Packet p = ((ClientImpl) listener).received();
                    if (p instanceof PlayerPacket) {
                        playerlist[((PlayerPacket) p).id].setName(((PlayerPacket) p).name);
//                        playerlist[0].setId(((PlayerPacket) p).id);
                        //                      playerlist[0].setTeam(playerlist[0].getTeam() == Team.MAN ? Team.APE : Team.MAN);
                    } else if (p instanceof SquadPacket) {
                        playerlist[1 - myPlayerId].getUnites().clear();
                        for (int i = 0; i < ((SquadPacket) p).squad.size(); i++) {
                            Unite u;
                            switch (((SquadPacket) p).squad.get(i)) {
                                case 0:
                                    u = new SoldierUnit(players[1-myPlayerId].getTeam());
                                    break;
                                case 1:
                                    u = new MarksmanUnit(playerlist[1-myPlayerId].getTeam());
                                    break;
                                default:

                                    u = new SoldierUnit(Team.MAN);
                                    break;
                            }
                            playerlist[1 - myPlayerId].addUnite(u);
                        }
                    } else if ( p instanceof MapPacket) {
                        this.setMap(((MapPacket) p).index);

                    } else if (p instanceof ReadyPacket) {
                        startgame();
                    } else {
                        System.out.println("Paquet étrange " + p.getClass());
                    }
                }


            }else {//if isHost
                if (!((ServerImpl) listener).isReceptionEmpty()) {
                    Packet p = ((ServerImpl) listener).received();
                    if (p instanceof PlayerPacket) {
                        playerlist[((PlayerPacket) p).id].setName(((PlayerPacket) p).name);
//                        playerlist[0].setId(((PlayerPacket) p).id);
                        //                      playerlist[0].setTeam(playerlist[0].getTeam() == Team.MAN ? Team.APE : Team.MAN);
                    } else if (p instanceof SquadPacket) {
                        playerlist[1 - myPlayerId].getUnites().clear();
                        for (int i = 0; i < ((SquadPacket) p).squad.size(); i++) {
                            Unite u;
                            switch (((SquadPacket) p).squad.get(i)) {
                                case 0:
                                    u = new SoldierUnit(players[1-myPlayerId].getTeam());
                                    break;
                                case 1:
                                    u = new MarksmanUnit(playerlist[1-myPlayerId].getTeam());
                                    break;
                                default:
                                    u = new SoldierUnit(Team.MAN);
                                    break;
                            }
                            playerlist[1 - myPlayerId].addUnite(u);
                        }
                    } else if (p instanceof ReadyPacket) {//&&isHost
                        isClientReady = !isClientReady;

                    } else {
                        System.out.println("Paquet étrange" + p.getClass());
                    }
                }

            }
    }

    private void startgame() {
ClientGame g =(ClientGame) getGame();
        if (g != null) {
            MainMENU.state = MainMENU.STATE.GAME;
            MainMENU.currentGame = g;
            MainMENU.currentGame.start();
            window.setDimension(VideoMode.getDesktopMode());
            glfwMaximizeWindow(window.getGlId());
        }
    }

    private void firstContact() throws InterruptedException {
        toSend.clear();
        this.isClientHere = true;
        //send player infos
        PlayerPacket p = new PlayerPacket();
        p.name = playerlist[getMyPlayerId()].getName();
        p.id = getMyPlayerId();
        addToSend(p);

        if (isHost) {//if host, send map and squad
            //send map
            MapPacket m = new MapPacket();
            m.index = getMapIndex();
            addToSend(m);

            //send units
            ArrayList<Integer> squad = new ArrayList<>();
            SquadPacket sq = new SquadPacket();

            for (Unite u : playerlist[0].getUnites()) {
                int type;
                if (u instanceof MarksmanUnit) {
                    type = 1;
                } else if (u instanceof SoldierUnit) {
                    type = 0;
                } else {
                    type = -1;
                }
                squad.add(type);
            }
            sq.squad = squad;
            addToSend(sq);
        }
        sendToSend();
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
            }if (ip.equals("localhost"))
                return true;

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
        if (!isHost) {
            return new ClientGame((ClientImpl) listener, getPlayers(), myPlayerId, getMap(), MainMENU.window);
        } else {
            return new ServerGame((ServerImpl) listener, getPlayers(), myPlayerId, getMap(), MainMENU.window);
        }
    }

    public void sendToSend() throws InterruptedException {
        if(toSend.size()>0)
        for (Packet p : toSend) {
            if (isHost) {
                ((ServerImpl) listener).send(p);
            } else {
                ((ClientImpl) listener).send(p);
            }
            Thread.sleep(30);
        }
        toSend.clear();
    }

    public void addToSend(Packet packet) {
        toSend.add(packet);
    }
}
