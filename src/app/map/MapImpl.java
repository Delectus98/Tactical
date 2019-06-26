package app.map;

import Graphics.ConstTexture;
import Graphics.Vector2i;
import util.ResourceHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapImpl extends Map {
    private static final float tileWidth = 64;

    private int width ;
    private int height ;
    private ConstTexture miniature;
    /**
     * Generates a map using MapInfo that contains data to creates Game Map
     * @param info map info use to load map
     */
    public MapImpl(MapInfo info){
        width = info.width;
        height = info.height;
        this.miniature=info.getMiniature();

        spawnPoints = new ArrayList[2];
        spawnPoints[0] = new ArrayList<>();
        spawnPoints[1] = new ArrayList<>();

        world = new Tile[width][height];

        for (int i=0 ; i < width; ++i) {
            for (int j = 0; j < height ; ++j) {
                MapInfo.TileInfo ti = info.getTileInfo(i, j);

                if (ti.enableFloor) {
                    ConstTexture floor = ResourceHandler.getTexture(ti.textureFloor);
                    if (ti.enableStruct) {
                        ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                        world[i][j] = new Tile(floor, ti.floorRect, struct, ti.structRect, ti.obstacle);
                    } else {
                        world[i][j] = new Tile(floor, ti.floorRect, false);
                    }

                    if (ti.spawn) spawnPoints[ti.spawnId].add(new Vector2i(i, j));
                }
                else if (ti.enableStruct) {
                    ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                    world[i][j] = new Tile(struct, ti.structRect, true);
                }

                if (world[i][j].getFloor() != null)  world[i][j].getFloor().setPosition(i * 64, j * 64);
                if (world[i][j].getStruct() != null) world[i][j].getStruct().setPosition(i * 64, j * 64);

                if(ti.spawnId!=-1){
                    spawnPoints[ti.spawnId].add(new Vector2i(i,j));
                }
            }

        }
    }

    /**
     * Sets a specific spawn area for specified player
     * @param spawnPoints
     * @param player
     */
    @Override
    public void setSpawnPoints(Collection<Vector2i> spawnPoints, int player) {
        super.spawnPoints[player].addAll(spawnPoints);
    }

    /**
     * Gives world's tiles contained inside map
     * @return
     */
    @Override
    public Tile[][] getWorld() {
        return world;
    }

    /**
     * Gives the total tiles on width side
     * @return
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Gives the total tiles on height side
     * @return
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Gives possible spawn points for a player for its own units
     * @param currentPlayer
     * @return
     */
    @Override
    public List<Vector2i> getSpawnPoints(int currentPlayer) {
        return spawnPoints[currentPlayer];
    }

    public ConstTexture getMiniature() {
        return miniature;
    }
}

/*@Override
public void drawFloor(int x, int y, int x2, int y2, RenderTarget target) {
    for (int i = x ; i < x2 && i < width ; ++i) {
        for (int j = y ; j < y2 && j < height ; ++j) {
            this.getWorld()[i][j].drawFloor(target);
        }
    }
}

@Override
public void drawStruct(int x, int y, int x2, int y2, RenderTarget target) {
    for (int i = x ; i < x2 && i < width ; ++i) {
        for (int j = y ; j < y2 && j < height ; ++j) {
            this.getWorld()[i][j].drawStruct(target);
        }
    }
}*/