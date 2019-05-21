package app.map;

import Graphics.ConstTexture;
import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;
import util.ResourceHandler;

import java.util.List;

public class MapImpl extends Map {
    private static final float tileWidth = 64;

    private int width ;
    private int height ;

    public MapImpl(MapInfo info){
        width = info.width;
        height = info.height;

        world = new Tile[width][height];

        //info.build(this);
        //or
        for (int i=0 ; i < height; ++i) {
            for (int j = 0; j < width ; ++j) {
                MapInfo.TileInfo ti = info.getTileInfo(i, j);

                if (ti.enableFloor) {
                    ConstTexture floor = ResourceHandler.getTexture(ti.textureFloor);
                    if (ti.enableStruct) {
                        ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                        world[i][j] = new Tile(floor, ti.floorRect, struct, ti.structRect, ti.obstacle);
                    } else {
                        world[i][j] = new Tile(floor, ti.floorRect, false);
                    }
                }
                else if (ti.enableStruct) {
                    ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                    world[i][j] = new Tile(struct, ti.structRect, true);
                }

                if (world[i][j].getFloor() != null)  world[i][j].getFloor().setPosition(i * 64, j * 64);
                if (world[i][j].getStruct() != null) world[i][j].getStruct().setPosition(i * 64, j * 64);
            }
        }
    }



    public MapImpl(int width, int height){
        world = new Tile[width][height];
        this.width = width;
        this.height = height;
    }

    @Override
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
    }

    @Override
    public Tile[][] getWorld() {
        return world;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<Vector2i> getSpawnPoints(int currentPlayer) {
        return spawnPoints[currentPlayer];
    }
}
