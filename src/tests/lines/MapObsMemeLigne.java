package tests.lines;

import Graphics.Vector2i;
import app.map.Map;
import app.map.Tile;
import System.*;

import java.util.List;

public class MapObsMemeLigne extends Map
{
    Tile sol = new Tile(null, null, null, null, false);
    Tile obs = new Tile(null, null, null, null, true);
    public MapObsMemeLigne()
    {
        this.world = new Tile[][]{
                {sol, sol, sol, sol, obs, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {obs, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, obs, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},

        };
    }

    @Override
    public void draw(Camera2D camera, RenderTarget target)
    {

    }

    @Override
    public Tile[][] getWorld()
    {
        return this.world;
    }

    @Override
    public List<Vector2i> getSpawnPoints(int currentPlayer)
    {
        return null;
    }
}
