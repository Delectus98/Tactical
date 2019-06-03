package app.map;

import Graphics.Vector2i;

import java.util.Collection;
import java.util.List;


public abstract class Map
{
    protected List<Vector2i>[] spawnPoints;

    protected Tile[][] world;

    /**
     * Sets a default spawn points for a specific player
     * @param spawnPoints list of available tiles
     * @param player specified player
     */
    public abstract void setSpawnPoints(Collection<Vector2i> spawnPoints, int player);

    /**
     * Gives the world tiles
     * @return the world tiles
     */
    public abstract Tile[][] getWorld();

    /**
     * Gives the width of the world in tiles
     * @return the width of the world in tiles
     */
    public abstract int getWidth();
    /**
     * Gives the height of the world in tiles
     * @return the height of the world in tiles
     */
    public abstract int getHeight();

    /**
     * Gives a list of available spawn points for a specific player
     * @param currentPlayer specified player
     * @return a list of available spawn points for a specific player
     */
    public abstract List<Vector2i> getSpawnPoints(int currentPlayer);
}
