package app.map;


import Graphics.FloatRect;

import java.util.HashMap;


public class MapList {

    public static MapInfo Battlefield1;
    public static MapInfo Battlefield2;
    public static MapInfo Battlefield3;
    public static MapInfo Example1;
    public static MapInfo DemoField;
    public static MapInfo Casino;

    static {
        loadBattleField1();
        loadBattleField2();
        loadBattleField3();
        loadExample1();
        loadDemoField();
        loadCasino();
    }

    private static void loadBattleField1() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0, 0, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0, 0, 64, 64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64, 0, 64, 64), true));

        String[][] mapData = {
                {"W", "W", "W", "W", "W", "W", "W",},
                {"W", "F", "F", "F", "F", "F", "W",},
                {"W", "S1", "F", "M", "F", "S2", "W",},
                {"W", "S1", "F", "F", "F", "S2", "W",},
                {"W", "S1", "F", "M", "F", "S2", "W",},
                {"W", "F", "F", "F", "F", "F", "W",},
                {"W", "W", "W", "W", "W", "W", "W",},
        };

        Battlefield1 = new MapInfo("Small Battlefield", 7, 7);
        for (int i = 0; i < Battlefield1.width; ++i) {
            for (int j = 0; j < Battlefield1.height; ++j) {
                Battlefield1.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
        Battlefield1.setMiniature("bf1");
    }

    private static void loadBattleField2() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0, 0, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0, 0, 64, 64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64, 0, 64, 64), true));

        String[][] mapData = {
                {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
                {"W", "F", "F", "F", "S1", "S1", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "M", "M", "M", "F", "F", "W"},
                {"W", "M", "F", "F", "F", "F", "M", "F", "F", "W"},
                {"W", "F", "F", "M", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "M", "F", "M", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "M", "F", "F", "M", "W"},
                {"W", "F", "F", "M", "M", "M", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "S2", "S2", "F", "F", "F", "W"},
                {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
        };

        Battlefield2 = new MapInfo("Medium Battlefield", 10, 10);
        for (int i = 0; i < Battlefield2.width; ++i) {
            for (int j = 0; j < Battlefield2.height; ++j) {
                Battlefield2.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
        Battlefield2.setMiniature("bf2");
    }

    private static void loadBattleField3() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0, 0, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0, 0, 64, 64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64, 0, 64, 64), true));

        String[][] mapData = {
                {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "M", "F", "W"},
                {"W", "M", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "M", "F", "M", "W"},
                {"W", "F", "M", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "M", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "S1", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "F", "F", "F", "S2", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "M", "F", "F", "F", "M", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "M", "F", "F", "F", "M", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "M", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "S1", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "S2", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "M", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "M", "F", "F", "F", "F", "W"},
                {"W", "F", "M", "F", "F", "F", "F", "F", "M", "M", "F", "F", "F", "F", "M", "F", "F", "M", "F", "W"},
                {"W", "M", "F", "M", "F", "F", "M", "M", "M", "M", "M", "M", "F", "F", "F", "F", "M", "F", "M", "W"},
                {"W", "F", "M", "F", "F", "F", "M", "M", "F", "F", "M", "M", "F", "F", "F", "F", "F", "M", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "M", "F", "F", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "F", "F", "F", "F", "F", "F", "M", "F", "F", "M", "F", "F", "F", "F", "F", "F", "F", "F", "W"},
                {"W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W", "W"},};

        Battlefield3 = new MapInfo("Huge Battlefield", 42, 20);
        for (int i = 0; i < Battlefield3.width; ++i) {
            for (int j = 0; j < Battlefield3.height; ++j) {
                Battlefield3.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
        Battlefield3.setMiniature("bf3");
    }

    private static void loadExample1() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0, 0, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0, 0, 64, 64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64, 0, 64, 64), true));

        String[][] mapData = {
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "W", "F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
        };

        Example1 = new MapInfo("Medium Example", 20, 20);
        for (int i = 0; i < Example1.width; ++i) {
            for (int j = 0; j < Example1.height; ++j) {
                Example1.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
    }

    private static void loadDemoField() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("res/floor.png", new FloatRect(0, 0, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("res/floor.png", new FloatRect(64, 0, 64, 64), 1));
        infos.putIfAbsent("W", new MapInfo.TileInfo("res/wall.png", new FloatRect(0, 0, 64, 64), true));
        infos.putIfAbsent("M", new MapInfo.TileInfo("res/wall.png", new FloatRect(64, 0, 64, 64), true));


        String[][] mapData = {
                {"F", "F", "F", "F", "F", "F", "S1", "S1", "S1", "S1", "S1", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "W", "F", "F", "F", "S1", "S1", "S1", "F", "F", "F", "W", "F", "F", "F"},
                {"F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "W", "W", "W", "W", "W", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "W"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W"},
                {"W", "W", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"W", "W", "F", "F", "F", "W", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "W", "F", "W", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "W", "F", "F", "F", "W", "W"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W"},
                {"W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"W", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "W", "W", "W", "W", "W", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "W", "F", "F", "F", "F", "F", "F", "F", "W", "F", "F", "F", "F"},
                {"F", "F", "F", "W", "F", "F", "F", "S2", "S2", "S2", "F", "F", "F", "W", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "S2", "S2", "S2", "S2", "S2", "F", "F", "F", "F", "F", "F"}

        };

        DemoField = new MapInfo("Demo Battlefield", 23, 17);
        for (int i = 0; i < DemoField.width; ++i) {
            for (int j = 0; j < DemoField.height; ++j) {
                DemoField.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
        DemoField.setMiniature("demofield");


    }

    private static void loadCasino() {
        HashMap<String, MapInfo.TileInfo> infos = new HashMap<>();

        infos.putIfAbsent("F", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), false));
        infos.putIfAbsent("S1", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), 0));
        infos.putIfAbsent("S2", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), 1));
        infos.putIfAbsent("S", new MapInfo.TileInfo("tileset", new FloatRect(2*64+32, 64+32, 64, 64), true));

        infos.putIfAbsent("P", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), "furniture", new FloatRect(5*64, 5*64, 192, 128), true));
        infos.putIfAbsent("R", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), "furniture", new FloatRect(4*64, 0, 127, 128), true));
        infos.putIfAbsent("W", new MapInfo.TileInfo("tileset", new FloatRect(4*64+32, 1*64+32, 64, 64), "tileset", new FloatRect(14*64, 5*64, 64, 64), true));


        String[][] mapData = {
                {"F", "F", "F", "F", "F", "F", "S1", "S1", "S1", "S1", "S1", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "S1", "S1", "S1", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "S", "S", "S", "S", "S", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "S", "S", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "S", "S", "F", "R", "W", "W", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "P", "W", "W", "F", "F", "F", "W", "W", "W", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "W", "W", "W", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "R", "W", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "W", "W", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "S", "S", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "S", "S", "F", "S", "S", "S", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "S", "S", "F", "S", "S", "S", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "S", "S", "S", "S", "S", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "F", "S2", "S2", "S2", "F", "F", "F", "F", "F", "F", "F"},
                {"F", "F", "F", "F", "F", "F", "S2", "S2", "S2", "S2", "S2", "F", "F", "F", "F", "F", "F"}

        };

        Casino = new MapInfo("Demo Battlefield", 23, 17);
        for (int i = 0; i < Casino.width; ++i) {
            for (int j = 0; j < Casino.height; ++j) {
                Casino.setTileInfo(i, j, infos.get(mapData[i][j]));
            }
        }
        Casino.setMiniature("casino");

    }

}
