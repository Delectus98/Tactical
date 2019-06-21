package app.map;

import Graphics.FloatRect;

import java.io.*;

/**
 * Interface used by Map(s) to load tiles. It contains all information about how a map must be built
 */
public class MapInfo implements Serializable {
    public static class TileInfo implements Serializable {
        public boolean obstacle = false;
        public boolean enableFloor = false;
        public boolean enableStruct = false;
        public boolean spawn = false;
        public int spawnId = -1;
        public String textureFloor = "";
        public String textureStruct = "";
        transient public FloatRect floorRect = new FloatRect(0,0,0,0);
        transient public FloatRect structRect = new FloatRect(0,0,0,0);

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
        public TileInfo(String texture, FloatRect rect, int playerSpawn) {
            textureFloor = texture;
            floorRect = rect;
            enableFloor = true;
            enableStruct = false;
            obstacle = false;
            spawn = playerSpawn >= 0;
            spawnId = playerSpawn;
        }

        private static TileInfo readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            TileInfo info = null;
            info = (TileInfo) ois.readObject();
            info.floorRect = new FloatRect(ois.readFloat(), ois.readFloat(), ois.readFloat(), ois.readFloat());
            info.structRect = new FloatRect(ois.readFloat(), ois.readFloat(), ois.readFloat(), ois.readFloat());
            return info;
        }

        private void saveInto(ObjectOutputStream oos) throws IOException {
            oos.writeObject(this);
            oos.writeFloat(floorRect.l);
            oos.writeFloat(floorRect.t);
            oos.writeFloat(floorRect.w);
            oos.writeFloat(floorRect.h);

            oos.writeFloat(structRect.l);
            oos.writeFloat(structRect.t);
            oos.writeFloat(structRect.w);
            oos.writeFloat(structRect.h);
        }
    }
    public final String name;
    public final int width;
    public final int height;
    transient private TileInfo[][] info;

    /**
     * Create a map info with a name and default dimension
     * @param name map name
     * @param x width
     * @param y height
     */
    public MapInfo(String name, int x, int y) {
        this.name = name;
        this.width = x;
        this.height = y;

        this.info = new TileInfo[x][y];
    }

    /**
     * Sets up a info for a specified tile
     * @param x x tile coordinates
     * @param y y tile coordinates
     * @param info specified info
     */
    public void setTileInfo(int x, int y, TileInfo info) {
        this.info[x][y] = info;
    }

    /**
     * Gives a info according to a specified tile
     * @param x x tile coordinates
     * @param y y tile coordinates
     * @return
     */
    public TileInfo getTileInfo(int x, int y){
        return info[x][y];
    }

    /**
     * Loads a Map Info with a file that contains Serializable data
     * @param serializable file with Serializable data
     * @return
     */
    public static MapInfo loadFromFile(String serializable) {
        MapInfo info = null;

        ObjectInputStream ois = null;
        try {
            final FileInputStream fichier = new FileInputStream(serializable);
            ois = new ObjectInputStream(fichier);
            info = (MapInfo) ois.readObject();
            info.info = new TileInfo[info.width][info.height];
            for (int i = 0 ; i < info.width ; ++i) {
                for (int j = 0 ; j < info.height ; ++j) {
                    info.info[i][j] = TileInfo.readFrom(ois);
                }
            }

        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        return info;
    }

    /**
     *
     */
    public void save(){
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier = new FileOutputStream(name + ".build");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(this);
            for (int i = 0 ; i < width ; ++i) {
                for (int j = 0 ; j < height ; ++j) {
                    info[i][j].saveInto(oos);
                }
            }

        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
