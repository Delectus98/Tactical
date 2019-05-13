package app;

import com.sun.tools.javac.util.Pair;

abstract class Weapon
{
    int amunition; //-1 = infini, sinon utile pour armes speciales
    Pair<Integer, Integer> range;
    float[] precision; //= new float[range.fst - range.snd];
    int damagePerRounds;
    int rounds;

    abstract int damageInflected(int distance); //calcul des degats d'apres la distance de tir
    abstract boolean isInRange(int distance);

    abstract int getAmunition(); //-1 = infini, sinon utile pour armes speciales
}
