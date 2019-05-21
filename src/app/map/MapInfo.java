package app.map;

import Graphics.ConstTexture;
import Graphics.FloatRect;
import Graphics.Texture;
import util.ResourceHandler;

public class MapInfo {
    public static class TileInfo {
        public boolean obstacle = false;
        public boolean enableFloor = false;
        public boolean enableStruct = false;
        public boolean spawn = false;
        public int spawnId = -1;
        public String textureFloor = "";
        public String textureStruct = "";
        public FloatRect floorRect = new FloatRect(0,0,0,0);
        public FloatRect structRect = new FloatRect(0,0,0,0);

        public TileInfo(String floor, FloatRect rectFloor, String struct, FloatRect rectStruct, boolean isObstacle) {
            textureFloor = floor;
            textureStruct = struct;
            floorRect = rectFloor;
            structRect = rectStruct;
            obstacle = isObstacle;
            enableFloor = true;
            enableStruct = true;
        }

        public TileInfo(String texture, FloatRect rect, boolean isObstacle) {
            if (isObstacle) {
                textureStruct = texture;
                structRect = rect;
                enableFloor = false;
                enableStruct = true;
                obstacle = true;
            } else {
                textureFloor = texture;
                floorRect = rect;
                enableFloor = true;
                enableStruct = false;
                obstacle = false;
            }
        }
        public TileInfo(String texture, FloatRect rect, boolean isSpawn, int playerSpawn) {
            textureFloor = texture;
            floorRect = rect;
            enableFloor = true;
            enableStruct = false;
            obstacle = false;
            spawn = playerSpawn >= 0;
            spawnId = playerSpawn;
        }
    }
    public final int width;
    public final int height;
    private TileInfo[][] info;

    public MapInfo(int x, int y) {
        width = x;
        height = y;

        info = new TileInfo[x][y];
    }

    public void setTileInfo(int x, int y, TileInfo info) {
        this.info[x][y] = info;
    }

    public TileInfo getTileInfo(int x, int y){
        return info[x][y];
    }

    /*public void build(Map map) {
        for (int i=0 ; i < height; ++i) {
            for (int j = 0; j < width ; ++j) {
                TileInfo ti = info[i][j];

                if (ti.enableFloor) {
                    ConstTexture floor = ResourceHandler.getTexture(ti.textureFloor);
                    if (ti.enableStruct) {
                        ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                        map.getWorld()[i][j] = new Tile(floor, ti.floorRect, struct, ti.structRect, ti.obstacle);
                    } else {
                        map.getWorld()[i][j] = new Tile(floor, ti.floorRect, false);
                    }

                }
                else if (ti.enableStruct) {
                    ConstTexture struct = ResourceHandler.getTexture(ti.textureStruct);
                    map.getWorld()[i][j] = new Tile(struct, ti.structRect, true);
                }

                if (map.getWorld()[i][j].getFloor() != null)  map.getWorld()[i][j].getFloor().setPosition(i * 64, j * 64);
                if (map.getWorld()[i][j].getStruct() != null) map.getWorld()[i][j].getStruct().setPosition(i * 64, j * 64);
            }
        }
    }*/
}
