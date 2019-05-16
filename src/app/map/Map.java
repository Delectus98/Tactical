package app.map;

import com.sun.tools.javac.util.Pair;
import System.*;

import java.util.List;

public abstract class Map
{
    List<Pair<Integer, Integer>> spawnPlayer1;
    List<Pair<Integer, Integer>> spawnPlayer2;

    Tile[][] world;

    abstract void draw(Camera2D camera, RenderTarget target);
}
