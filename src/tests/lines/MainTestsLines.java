package tests.lines;

import Graphics.Vector2i;
import util.Line;

import java.util.Vector;

public class MainTestsLines
{
    static MapTestLineNoObs map = new MapTestLineNoObs();
    static MapObsMemeLigne ligne = new MapObsMemeLigne(); // (0,4) & (4,0) & (8,8)

    public static void main(String[] args)
    {
        float x = Line.computePercentage(new Vector2i(0, 0), new Vector2i(9,9), map);
        System.out.println(x);
        assert x == 1.f : "Map sans obstacles, resultat != 1.0";

        //obs en 0 4
        x = Line.computePercentage(new Vector2i(0,0), new Vector2i(0, 5), ligne);
        System.out.println(x);
        assert x == 0.f : "Map obs même ligne, res != 0";

        //obs en 4 0
        x = Line.computePercentage(new Vector2i(0,0), new Vector2i(9, 0), ligne);
        System.out.println(x);
        assert x == 0.f : "Map obs même col, res != 0";

        x = Line.computePercentage(new Vector2i(0,1), new Vector2i(9, 0), ligne);
        System.out.println(x);
        assert x == .5 : "(Col) Décaler de 1, et cible derriere obstacle(pas collé). Res != .5";

        x = Line.computePercentage(new Vector2i(1,0), new Vector2i(0, 5), ligne);
        System.out.println(x);
        assert x == .25 : " (Ligne) Décaler de 1, et cible derriere obstacle(collé). Res != .25";
        //todo : Run d'autres tests

        x = Line.computePercentage(new Vector2i(-1, -1), new Vector2i(0,0), ligne);
        System.out.println(x);
        assert x == -1 : "Unite oob and x != -1";

        x = Line.computePercentage(new Vector2i(7, 7), new Vector2i(9,9), ligne);
        System.out.println(x);
        assert x == 0 : "(diagonale) avec obst et x != 0";

        //.25 car un seul points ne croise pas le coin d'un obstacle d'où mecanique decalage dans les actions
        x = Line.computePercentage(new Vector2i(7, 8), new Vector2i(8,9), ligne);
        System.out.println(x);
        assert x == .25 : "Diagonale direct et unites collées au même obstacle; x != .25";

        x = Line.computePercentage(new Vector2i(1, 4), new Vector2i(1,6), ligne);
        System.out.println("1 a cote obs " + x);
    }
}
