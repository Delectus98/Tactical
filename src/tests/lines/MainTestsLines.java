package tests.lines;

import Graphics.Vector2i;
import util.Line;

import java.util.Vector;

public class MainTestsLines
{
    static MapTestLineNoObs map = new MapTestLineNoObs();
    static MapObsMemeLigne ligne = new MapObsMemeLigne();

    public static void main(String[] args)
    {
        float x = Line.computePercentage(new Vector2i(0, 0), new Vector2i(9,9), map);
        assert x == 1.f : "Map sans obstacles, resultat != 1.0";
        //obs en 0 4
        x = Line.computePercentage(new Vector2i(0,0), new Vector2i(0, 9), ligne);
        System.out.println(x);

    }
}
