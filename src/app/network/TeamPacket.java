package app.network;

import app.Team;

public class TeamPacket extends Packet {
    private String specie;

    public TeamPacket(Team t) {
        specie = t.equals(Team.APE) ? ("APE") : ("MAN");
    }

    public Team getTeam(){
        return specie.equals("APE") ? Team.APE : Team.MAN;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
