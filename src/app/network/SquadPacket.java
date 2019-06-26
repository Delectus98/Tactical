package app.network;

import java.util.ArrayList;

public class SquadPacket extends Packet{

public ArrayList<Integer> squad = new ArrayList<>();
    @Override
    public int getPriority() {
        return 0;
    }
}
