package app.map;


import Graphics.FloatRect;

import java.util.HashMap;


public class MapList {

   public static MapInfo Battlefield1;
   public static MapInfo Battlefield2;
   public static MapInfo Battlefield3;
   static {
       loadBattleField1();
       loadBattleField2();
       loadBattleField3();
   }

   private static void loadBattleField1(){
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

       Battlefield1 = new MapInfo("Small Battlefield",7,7);
       for (int i=0 ; i < Battlefield1.width ; ++i) {
           for (int j = 0; j < Battlefield1.height ; ++j) {
               Battlefield1.setTileInfo(i, j, infos.get(mapData[i][j]));
           }
       }
   }

    private static void loadBattleField2(){
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0,0,64,64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0,0,64,64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64,0,64,64), true));

        String[][] mapData = {
                {"W", "W", "W", "W", "W", "W", "W","W", "W", "W"},
                {"W", "F", "F", "F", "S1", "S1", "F","F", "F", "W"},
                {"W", "F", "F", "F", "M", "M", "M","F", "F", "W"},
                {"W", "M", "F", "F", "F", "F", "M","F", "F", "W"},
                {"W", "F", "F", "M", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "M", "F", "M", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "M", "F","F", "M", "W"},
                {"W", "F", "F", "M", "M", "M", "F","F", "F", "W"},
                {"W", "F", "F", "F", "S2", "S2", "F","F", "F", "W"},
                {"W", "W", "W", "W", "W", "W", "W","W", "W", "W"},
        };

        Battlefield2 = new MapInfo("Medium Battlefield", 10,10);
        for (int i=0 ; i < Battlefield2.width ; ++i) {
            for (int j = 0; j < Battlefield2.height ; ++j) {
                Battlefield2.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
    }

    private static void loadBattleField3(){
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0,0,64,64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64,0,64,64), true, 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0,0,64,64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64,0,64,64), true));

        String[][] mapData = {
                {"W", "W", "W", "W", "W", "W", "W","W", "W", "W", "W", "W", "W", "W", "W", "W", "W","W", "W", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "S1","F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "F", "F", "F","S2","F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "M", "M", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "M", "M", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","M", "M", "M", "M", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","M", "M", "M", "M", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","M", "M", "M", "M", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "F", "F", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "S1","F", "F", "F", "F","F", "F", "F", "F", "F", "F", "F", "F", "F", "F","S2","F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "F", "F", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "F", "F", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","F", "M", "M", "F", "F", "F", "F", "M", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","M", "M", "M", "M", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","M", "F", "F", "M", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M","F", "F", "F", "F", "M", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F","M", "F", "F", "M", "F", "F", "F", "F", "F", "F","F", "F", "W"},
                {"W", "W", "W", "W", "W", "W", "W","W", "W", "W", "W", "W", "W", "W", "W", "W", "W","W", "W", "W"},        };

        Battlefield3 = new MapInfo("Huge Battlefield",42,20);
        for (int i=0 ; i < Battlefield3.width ; ++i) {
            for (int j = 0; j < Battlefield3.height ; ++j) {
                Battlefield3.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
    }
}
