package app.map;

import Graphics.Vector2i;
import com.sun.tools.javac.util.Pair;
import System.*;

import java.util.List;

public abstract class Map
{
    protected List<Vector2i>[] spawnPoints;

    protected Tile[][] world;

    public abstract void draw(Camera2D camera, RenderTarget target);

    public abstract Tile[][] getWorld();

    public abstract List<Vector2i> getSpawnPoints(int currentPlayer);
}
