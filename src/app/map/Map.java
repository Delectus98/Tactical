package app.map;

import com.sun.tools.javac.util.Pair;
import System.*;

import java.util.List;

public abstract class Map
{
    protected List<Pair<Integer, Integer>> spawnPlayer1;
    protected List<Pair<Integer, Integer>> spawnPlayer2;

    protected Tile[][] world;

    public abstract void draw(Camera2D camera, RenderTarget target);
}
