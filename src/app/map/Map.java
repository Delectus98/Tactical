package app.map;

import Graphics.Vector2i;
import System.*;

import java.util.Collection;
import java.util.List;

public abstract class Map
{
    protected List<Vector2i>[] spawnPoints;

    protected Tile[][] world;

    public abstract void drawFloor(int x, int y, int w, int h, RenderTarget target);

    public abstract void drawStruct(int x, int y, int w, int h, RenderTarget target);

    public abstract void setSpawnPoints(Collection<Vector2i> spawnPoints, int player);

    public abstract Tile[][] getWorld();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract List<Vector2i> getSpawnPoints(int currentPlayer);
}
