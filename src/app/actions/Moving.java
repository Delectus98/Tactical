package app.actions;

import Graphics.Vector2i;
import System.*;
import app.Game;
import app.Player;
import app.Unite;
import util.Pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Moving extends Action {
    private List<Vector2i> tilePath;
    private float duration = 0.f;
    private float elapsed = 0.f;
    private float speed = 1.f;
    private static float factor = 0.2f;

    public Moving()
    {
        // kryo constructor required
    }

    public Moving(Player p, Pathfinder finder, HashMap<Vector2i, Vector2i> possiblePaths, Unite unite, ArrayList<Unite> alliesUnites, ArrayList<Unite> enemyUnites, Vector2i target, float speed)
    {
        tilePath = finder.pathfind(possiblePaths, enemyUnites, alliesUnites, unite.getMapPosition(), target);

        duration = tilePath.size() * (1.f / speed);
        super.uniteId = unite.getId();
        super.playerId = p.getId();
    }

    @Override
    public void init(Game gameContext) {
        super.game = gameContext;
    }

    @Override
    public int getCost() {
        return tilePath.size() + (tilePath.isEmpty() ? (0):(1));
    }

    @Override
    public boolean isFinished() {
        return tilePath.isEmpty() ||(int)((elapsed / duration) * (speed)) >= tilePath.size();
    }

    @Override
    public void drawAboveHUD(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveEntity(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveStruct(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveFloor(RenderTarget target) {
        ;
    }

    @Override
    public void update(ConstTime time) {
        elapsed += time.asSeconds();

        Unite u = game.getPlayers()[playerId].getUnites().get(uniteId);
        if (!u.isDead() && tilePath.size() != 0) {
            Vector2i v = tilePath.get(Math.min(tilePath.size()-1, (int)((elapsed / duration) * (speed) )));
            u.getSprite().setPosition(v.x * 64, v.y * 64);
            u.setMapPosition(v);
        }
        game.updateFOG();
    }
}
