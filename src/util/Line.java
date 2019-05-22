package util;

import Graphics.Vector2f;
import Graphics.Vector2i;
import Graphics.Vector3f;
import app.map.Map;
import app.map.Tile;

import java.util.Arrays;


public class Line
{
    /**
     * Calcule l'equation de la droite entre x et y
     *
     * @param start
     * @param end
     * @return Vector3f line où line.x est le facteur des x, line.y le facteur des y et line.z le Z de l'equation
     *          α x + ß y = z
     */
    public static Vector3f computeLine(Vector2f start, Vector2f end)
    {
        float a = end.y - start.y;
        float b = start.x - end.x;
        float c = a * start . x + b * start.y;

        return new Vector3f(a, b, c);
    }

    /**
     * Verifie si une ligne passe entre 2 pts (pour verifier si la ligne passe dans une case d'obstacle
     * @param line le Vector3f contenant l'equation de la ligne
     * @param a    le Vector2i du pt a
     * @param b                      b
     * @return true si la ligne est entre ces points
     */
    public static boolean lineIsBetween2pts(Vector3f line, Vector2f a, Vector2f b)
    {
        float c1 = line.x * a.x + line.y * a.y;
        float c2 = line.x * b.x + line.y * b.y;
        float c = line.z;

        return (c1 < c && c < c2) || (c2 < c && c < c1) || c == c1 || c == c2;
    }

    /**
     * Calcule le pourcentage de toucher entre l'attacker et la victime d'apres les obstacles de la map
     *
     * @param attacker
     * @param victim
     * @param map
     * @return float : where 1 = 100% and -1 means there was an error (attacker or victim out of bounds)
     */
    public static float computePercentage(Vector2i attacker, Vector2i victim, Map map)
    {
        //checks if attacker and victim are within the map
        if (!validIndex(map.getWorld(), attacker.x, attacker.y))
        {
            //Todo : Si ajout de loger : log

            // System.out.println("Attacker isn't within map range");
            return -1;
        }

        if (!validIndex(map.getWorld(), victim.x, victim.y))
        {
            //Todo : Si ajout de loger.. idem

            //System.out.println("Victim isn't within map range");
            return -1;
        }
        float ret = 1;
        Vector2f[] arr0, arr1;
        arr0 = new Vector2f[]{new Vector2f(attacker.x, attacker.y),
                new Vector2f(attacker.x, attacker.y + 1),
                new Vector2f(attacker.x + 1, attacker.y),
                new Vector2f(attacker.x + 1, attacker.y + 1)};
        arr1 = new Vector2f[]{new Vector2f(victim.x, victim.y),
                new Vector2f(victim.x, victim.y + 1),
                new Vector2f(victim.x + 1, victim.y),
                new Vector2f(victim.x + 1, victim.y + 1)};
//        System.out.print("\n[");
//        for (Vector2i v : Arrays.asList(arr0))
//        {
//            System.out.print("(" + v.x + "," + v.y + ")");
//        }
//        System.out.print("]\n[");
//        for (Vector2i v : Arrays.asList(arr1))
//        {
//            System.out.print("(" + v.x + "," + v.y + ")");
//        }
//        System.out.println("]");

        for (int i = 0; i < arr0.length; i++)
        {
            boolean obs = false;
            Vector3f line = computeLine(arr0[i], arr1[i]);
//            System.out.println(line.x + "x + " + line.y + " = " + line.z);

            //on regarde uniquement les cases entre attacker et victim pour calculer le degats
            // cela permet qu'une unité qui tire en ligne droite, tout en étant à coté d'un mur ne perde pas .5
            for(int x = Integer.min(attacker.x, victim.x); x <= Integer.max(attacker.x, victim.x); x++)
            {
                for (int y = Integer.min(attacker.y, victim.y); y <= Integer.max(attacker.y, victim.y); y++)
                {
                    //System.out.println(x + " " + y);
                    if(validIndex(map.getWorld(), x, y))
                    {
                        //System.out.println("estObs " + map.getWorld()[x][y].isObstacle());
                        if (map.getWorld()[x][y].isObstacle())
                        {
                            Vector2f supLeftCorner = new Vector2f(x, y);
                            Vector2f lowRightCorner = new Vector2f(x + 1, y + 1);

                            Vector2f supRightCorner = new Vector2f(x, y+1);
                            Vector2f lowLeftCorner = new Vector2f(x+1, y);

                            obs = lineIsBetween2pts(line, supLeftCorner, lowRightCorner)
                                    || lineIsBetween2pts(line, supRightCorner, lowLeftCorner);
                            if (obs)
                            {
                                break;
                            }
                        }
                    }
                }
                if (obs)
                {
                 ret -= .25;
                 break;
                }
            }
        }
        return ret;
    }

    /**
     * Verifie si les coordonnes x et y sont dans le tableau
     *
     * @param arr le tableau de la map
     * @param x
     * @param y
     * @return true si (x, y) appartient au tableau
     */
    public static boolean validIndex(Tile[][] arr, int x, int y)
    {
        return (0 <= x && x < arr.length) && (0 <= y && y < arr[x].length);
    }
}