package app.map;

import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;

import java.util.List;

public class MapImpl extends Map {
    private static final float tileWidth = 64;

    private int width ;
    private int height ;

    public MapImpl(String file){}

    public MapImpl(int width, int height){
        world = new Tile[width][height];
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawFloor(int x, int y, int x2, int y2, RenderTarget target) {
        for (int i = x ; i < x2 && i < width ; ++i) {
            for (int j = y ; j < y2 && j < height ; ++j) {
                target.draw(this.getWorld()[i][j].getFloor());
            }
        }
    }

    @Override
    public void drawStruct(int x, int y, int x2, int y2, RenderTarget target) {
        for (int i = x ; i < x2 && i < width ; ++i) {
            for (int j = y ; j < y2 && j < height ; ++j) {
                target.draw(this.getWorld()[i][j].getStruct());
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
