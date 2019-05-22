package tests.lines;

import Graphics.Vector2i;
import app.map.Map;
import app.map.Tile;
import System.*;

import java.util.Collection;
import java.util.List;

public class MapTestLineNoObs extends Map
{
    Tile sol = new Tile(false);
    Tile obs = new Tile(true);
    public MapTestLineNoObs()
    {
        this.world = new Tile[][]{
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},
                {sol, sol, sol, sol, sol, sol, sol, sol, sol, sol},

        };
    }

    public void drawFloor(int x, int y, int w, int h, RenderTarget target)
    {

    }

    public void drawStruct(int x, int y, int w, int h, RenderTarget target)
    {
    }

    public void setSpawnPoints(Collection<Vector2i> spawnPoints, int player)
    {
    }

    public Tile[][] getWorld()
    {
        return this.world;
    }

    public int getWidth()
    {
        return 0;
    }

    public int getHeight()
    {
        return 0;
    }


    public List<Vector2i> getSpawnPoints(int currentPlayer)
    {
        return null;
    }
}