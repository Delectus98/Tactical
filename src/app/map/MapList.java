package app.map;


import Graphics.ConstTexture;
import Graphics.FloatRect;
import util.ResourceHandler;

import java.util.HashMap;

public class MapList {

   public static MapInfo Battlefield;
   static {
       loadBattleField();
   }

   private static void loadBattleField(){
       HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

       infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0,0,64,64), false));
       infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 0));
       infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 1));
       infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0,0,64,64), true));
       infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64,0,64,64), true));

       String[][] mapData = {
               {"W", "W", "W", "W", "W", "W", "W",},
               {"W", "F", "F", "F", "F", "F", "W",},
               {"W", "S1", "F", "M", "F", "S2", "W",},
               {"W", "S1", "F", "F", "F", "S2", "W",},
               {"W", "S1", "F", "M", "F", "S2", "W",},
               {"W", "F", "F", "F", "F", "F", "W",},
               {"W", "W", "W", "W", "W", "W", "W",},
       };

       Battlefield = new MapInfo(7,7);
       for (int i=0 ; i < Battlefield.width ; ++i) {
           for (int j = 0; j < Battlefield.height ; ++j) {
                Battlefield.setTileInfo(i, j, infos.get(mapData[i][j]));
           }
       }
   }
}
