package app.map;

import Graphics.Vector2i;

import java.util.Collection;
import java.util.List;

//TODO Remove draw features to let Game draw itself
public abstract class Map
{
    protected List<Vector2i>[] spawnPoints;

    protected Tile[][] world;

    //TODO (removed?) public abstract void drawFloor(int x, int y, int x2, int y2, RenderTarget target);

    //TODO (removed?) public abstract void drawStruct(int x, int y, int x2, int y2, RenderTarget target);

    public abstract void setSpawnPoints(Collection<Vector2i> spawnPoints, int player);

    public abstract Tile[][] getWorld();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract List<Vector2i> getSpawnPoints(int currentPlayer);
}
