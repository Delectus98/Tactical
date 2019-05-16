package app;

import Graphics.Vector2i;
import app.map.Map;
import com.sun.tools.javac.util.Pair;

import System.*;

import java.util.List;

public abstract class Weapon
{
    int amunition; //-1 = infini, sinon utile pour armes speciales
    Pair<Integer, Integer> range;
    float[] precision; //= new float[range.fst - range.snd];
    int damagePerRounds;
    int rounds;
    int cout; //negatif = vide les PA si déjà déplacer; sinon val absolue

    public abstract int damageInflected(int distance); //calcul des degats d'apres la distance de tir
    public abstract boolean isInRange(int distance);

    public abstract int getAmunition(); //-1 = infini, sinon utile pour armes speciales

    public abstract void draw(RenderTarget target);

    public abstract void shot(Map map, List<Unite> unites, Vector2i start, Vector2i end); //toutes les unites
}
