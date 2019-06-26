package app.network;

import Graphics.Vector2f;
import Graphics.Vector2i;
import app.actions.Moving;
import app.actions.Shooting;
import app.weapon.BladeProjectile;
import app.weapon.BlastingProjectile;
import app.weapon.BulletProjectile;
import app.weapon.Impact;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;


public class GameRegistration extends PacketRegistration {
    private GameRegistration(){}

    public static final GameRegistration instance = new GameRegistration();

    @Override
    public void register(EndPoint point) {
        //packets
        point.getKryo().register(PlayerPacket.class);
        point.getKryo().register(ActionPacket.class);
        point.getKryo().register(ErrorPacket.class);
        point.getKryo().register(FatalErrorPacket.class);
        point.getKryo().register(TurnPacket.class);
        point.getKryo().register(TeamPacket.class);
        point.getKryo().register(SpawnPacket.class);
        point.getKryo().register(ReadyPacket.class);
        point.getKryo().register(GameOverPacket.class);
        point.getKryo().register(MapPacket.class);
        point.getKryo().register(SquadPacket.class);

        //data
        point.getKryo().register(ArrayList.class, new ArrayListSerializer());
        point.getKryo().register(String.class);
        point.getKryo().register(Vector2i.class);
        point.getKryo().register(Vector2f.class);
        point.getKryo().register(Impact.class);
        point.getKryo().register(BulletProjectile.class);
        point.getKryo().register(BladeProjectile.class);
        point.getKryo().register(BlastingProjectile.class);

        //actions
        point.getKryo().register(Shooting.class);
        point.getKryo().register(Moving.class);
    }
}
