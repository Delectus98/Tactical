package app.network;

import Graphics.Vector2f;
import Graphics.Vector2i;
import app.actions.Shooting;
import app.weapon.Impact;
import com.esotericsoftware.kryonet.EndPoint;


public class GameRegistration extends PacketRegistration {
    private GameRegistration(){}

    public static final GameRegistration instance = new GameRegistration();

    @Override
    public void register(EndPoint point) {
        //packets
        point.getKryo().register(PlayerPacket.class);
        point.getKryo().register(ActionPacket.class);

        //data
        point.getKryo().register(Vector2i.class);
        point.getKryo().register(Vector2f.class);
        point.getKryo().register(Impact.class);

        //actions
        point.getKryo().register(Shooting.class);
    }
}
