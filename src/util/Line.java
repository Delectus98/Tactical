package util;

import Graphics.Vector2f;
import Graphics.Vector2i;
import Graphics.Vector3f;
import app.Unite;
import app.map.Map;
import app.map.Tile;

import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Line
{
    /**
     * Calcule l'equation de la droite entre x et y
     *
     * @param start
     * @param end
     * @return Vector3f line où line.x est le facteur des x, line.y le facteur des y et line.z le Z de l'equation
     * α x + ß y = z
     */
    public static Vector3f computeLine(Vector2f start, Vector2f end)
    {
        float a = end.y - start.y;
        float b = start.x - end.x;
        float c = a * start.x + b * start.y;

        return new Vector3f(a, b, c);
    }

    /**
     * Verifie si une ligne passe entre 2 pts (pour verifier si la ligne passe dans une case d'obstacle
     *
     * @param line le Vector3f contenant l'equation de la ligne
     * @param a    le Vector2i du pt a
     * @param b    b
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
            for (int x = Integer.min(attacker.x, victim.x); x <= Integer.max(attacker.x, victim.x); x++)
            {
                for (int y = Integer.min(attacker.y, victim.y); y <= Integer.max(attacker.y, victim.y); y++)
                {
                    //System.out.println(x + " " + y);
                    if (validIndex(map.getWorld(), x, y))
                    {
                        //System.out.println("estObs " + map.getWorld()[x][y].isObstacle());
                        if (map.getWorld()[x][y].isObstacle())
                        {
                            Vector2f supLeftCorner = new Vector2f(x, y);
                            Vector2f lowRightCorner = new Vector2f(x + 1, y + 1);

                            Vector2f supRightCorner = new Vector2f(x, y + 1);
                            Vector2f lowLeftCorner = new Vector2f(x + 1, y);

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

    //todo
    public static boolean tileIsBetween2Lines(Vector2i tile, Vector3f line, Vector3f line1)
    {
        Vector2f[] corners = new Vector2f[]{
                new Vector2f(tile.x, tile.y),
                new Vector2f(tile.x, tile.y + 1),
                new Vector2f(tile.x + 1, tile.y),
                new Vector2f(tile.x + 1, tile.y + 1)};
        return Arrays.asList(corners).stream().allMatch(x -> pointBetweenTwoLines(x, line, line1));
    }

    public static boolean oneCornerBetween2Lines(Vector2i tile, Vector3f line, Vector3f line1)
    {
        Vector2f[] corners = new Vector2f[]{
                new Vector2f(tile.x, tile.y),
                new Vector2f(tile.x, tile.y + 1),
                new Vector2f(tile.x + 1, tile.y),
                new Vector2f(tile.x + 1, tile.y + 1)};
        return pointBetweenTwoLines(corners[0], line, line1)
                || pointBetweenTwoLines(corners[1], line, line1)
                || pointBetweenTwoLines(corners[2], line, line1)
                || pointBetweenTwoLines(corners[3], line, line1);
    }

    public static boolean pointBetweenTwoLines(Vector2f point, Vector3f lineSup, Vector3f lineInf)
    {
        float eq1 = point.x * lineSup.x + point.y * lineSup.y;
        float eq2 = point.x * lineInf.x + point.y * lineInf.y;

        return (eq1 >= lineSup.z && eq2 <= lineInf.z)
                || (eq1 <= lineSup.z && eq2 >= lineInf.z);

    }

    //Todo : all but supLeft & supRight ?
    public static List<Vector2i> getHidden(Unite unit, Map map)
    {
        ArrayList<Vector2i> hidden = new ArrayList<Vector2i>();
        HashMap<Vector2i, MyPair<Vector3f>> supLeft = new HashMap<>();
        HashMap<Vector2i, MyPair<Vector3f>> supRight = new HashMap<>();
        HashMap<Vector2i, MyPair<Vector3f>> lowLeft = new HashMap<>();
        HashMap<Vector2i, MyPair<Vector3f>> lowRight = new HashMap<>();
        Tile[][] world = map.getWorld();
        Vector2i pos = unit.getMapPosition();
        Vector2f startOfLines = new Vector2f(pos.x + .5f, pos.y + .5f);
        for (int i = pos.x - unit.getFov(); i < pos.x + unit.getFov(); i++)
        {
            for (int j = pos.y - unit.getFov(); j < pos.y + unit.getFov(); j++)
            {
                if (validIndex(world, i, j))
                {
                    if (world[i][j].isObstacle())
                    {
                        Vector3f fstLine, sndLine;
                        Vector2i tile = new Vector2i(i, j);
                        Vector2f coin1;
                        Vector2f coin2;
                        if (i <= pos.x) //PARTIE GAUCHE
                        {
                            if (j >= pos.y) //partie SUPERIEUR
                            {
                                if (i == pos.x) //on est dans le même colonne (HAUT)
                                {
                                    if (j - pos.y == 1) //on est collé à un obstacle
                                    {
                                        //on prends les coins superieurs
                                        coin1 = new Vector2f(i, j + 1);
                                        coin2 = new Vector2f(i + 1, j + 1);
                                    } else //l'obstacle n'est pas collé
                                    {
                                        //on prends les coins inferieurs
                                        coin1 = new Vector2f(i, j);
                                        coin2 = new Vector2f(i + 1, j);
                                    }
                                    //on peut ajouter a supLeft et supRight
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);

                                    supLeft.put(tile, new MyPair<>(fstLine, sndLine));
                                    supRight.put(tile, new MyPair<>(fstLine, sndLine));
                                } else if (j == pos.y) //on est sur la même ligne (GAUCHE)
                                {
                                    if (pos.x - i == 1) //obstacle collé
                                    {
                                        //on prends les coins gauches
                                        coin1 = new Vector2f(i, j);
                                        coin2 = new Vector2f(i, j + 1);
                                    } else //l'obstacle ne nous colle pas
                                    {
                                        //on prends les coins droits
                                        coin1 = new Vector2f(i + 1, j);
                                        coin2 = new Vector2f(i + 1, j + 1);
                                    }
                                    //on peut ajouter a supLeft et lowLeft
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);
                                    supLeft.put(tile, new MyPair<>(fstLine, sndLine));
                                    lowLeft.put(tile, new MyPair<>(sndLine, fstLine));
                                } else //on est dans la partie strict sup gauche
                                {
                                    //on prends les coins basGauche et supDroit
                                    coin1 = new Vector2f(i, j);
                                    coin2 = new Vector2f(i + 1, j + 1);
                                    //on ajoute dans supLeft uniquement
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);

                                    supLeft.put(tile, new MyPair<>(fstLine, sndLine));
                                }
                            } else //partie INFERIEUR
                            {
                                if (i == pos.x) //on est sur la même colonne (BAS)
                                {
                                    if (pos.y - j == 1) //l'obstace est collé
                                    {
                                        //on prend les coins inferieurs
                                        coin1 = new Vector2f(i, j);
                                        coin2 = new Vector2f(i + 1, j);
                                    } else //toujours même colonne, mais pas collé
                                    {
                                        //on prend les coins sup
                                        coin1 = new Vector2f(i, j + 1);
                                        coin2 = new Vector2f(i + 1, j + 1);
                                    }
                                    //on ajoute a lowLeft et lowRight
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);

                                    lowLeft.put(tile, new MyPair<>(fstLine, sndLine));
                                    lowRight.put(tile, new MyPair<>(fstLine, sndLine));
                                } else //partie strict inf gauche
                                {
                                    //on prends les coins sup gauche et bas droit
                                    coin1 = new Vector2f(i, j + 1);
                                    coin2 = new Vector2f(i + 1, j);
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);

                                    //on ajoute à lowLeft
                                    lowLeft.put(tile, new MyPair<>(fstLine, sndLine));
                                }
                            }
                        } else //Partie DROITE
                        {
                            if (j >= pos.y) //partie SUPERIEUR
                            {
                                if (j == pos.y) //même ligne (DROITE)
                                {
                                    if (i - pos.x == 1) //obst collé
                                    {
                                        //on prends les coins droits
                                        coin1 = new Vector2f(i + 1, j);
                                        coin2 = new Vector2f(i + 1, j + 1);
                                    } else
                                    {
                                        //les coins gauche
                                        coin1 = new Vector2f(i, j);
                                        coin2 = new Vector2f(i, j + 1);
                                    }
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);
                                    //on mets dans supRight et lowRight
                                    supRight.put(tile, new MyPair<>(fstLine, sndLine));
                                    lowRight.put(tile, new MyPair<>(fstLine, sndLine));
                                } else //partie strict haut droit
                                {
                                    //on prends les coins haut gauche et bas droit
                                    coin1 = new Vector2f(i, j + 1);
                                    coin2 = new Vector2f(i + 1, j);
                                    fstLine = computeLine(startOfLines, coin1);
                                    sndLine = computeLine(startOfLines, coin2);
                                    //on mets dans supRight
                                    supRight.put(tile, new MyPair<>(fstLine, sndLine));
                                }
                            } else //partie INFERIEUR
                            {
                                //strict inf, on prends les coins haut droit et bas gauche
                                coin1 = new Vector2f(i, j);
                                coin2 = new Vector2f(i + 1, j + 1);
                                fstLine = computeLine(startOfLines, coin1);
                                sndLine = computeLine(startOfLines, coin2);
                                //on mets dans lowRight
                                lowRight.put(tile, new MyPair<>(fstLine, sndLine));
                            }
                        }
                    }
                }
            }
        }
        //gérer les droites pour ne pas avoir de visibles illogiques
        //supLeft
        for (HashMap.Entry<Vector2i, MyPair<Vector3f>> entry : supLeft.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f> value = entry.getValue();
            for (HashMap.Entry<Vector2i, MyPair<Vector3f>> bis : supLeft.entrySet())
            {
                if (key != bis.getKey())
                {
                    //on regarde le point en bas à gauche // supgauche
                    Vector2f x = new Vector2f(bis.getKey().x, bis.getKey().y + 1);
                    if (pointBetweenTwoLines(x, value.getFst(), value.getSnd()))
                    {
                        supLeft.put(key, new MyPair<Vector3f>(value.getFst(), bis.getValue().getSnd()));
                    }
                }
            }
            for (int i = 0; i <= key.x; i++)
            {
                for (int j = key.y; j < world[i].length; j++)
                {
                    if (tileIsBetween2Lines(new Vector2i(i, j), supLeft.get(key).fst, supLeft.get(key).snd) && !(i == pos.x && j == pos.y))
                    {
                        if (!hidden.contains(new Vector2i(i, j)))
                        {
                            hidden.add(new Vector2i(i, j));
                        }
                    }
                }
            }
        }
        //supRight
        for (HashMap.Entry<Vector2i, MyPair<Vector3f>> entry : supRight.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f> value = entry.getValue();
            for (HashMap.Entry<Vector2i, MyPair<Vector3f>> bis : supRight.entrySet())
            {
                if (key != bis.getKey())
                {
                    //on regarde le point sup gauche
                    //todo le point inf gauche ?
                    Vector2f x = new Vector2f(bis.getKey().x, bis.getKey().y);
                    if (pointBetweenTwoLines(x, value.getFst(), value.getSnd()))
                    {
                        for (int i = bis.getKey().x; i < world.length; i++)
                        {
                            for (int j = bis.getKey().y; j < world[i].length; j++)
                            {
                                if (tileIsBetween2Lines(new Vector2i(i, j), supRight.get(key).fst, supRight.get(bis.getKey()).snd) && !(i == pos.x && j == pos.y))
                                {
                                    if (!hidden.contains(new Vector2i(i, j)))
                                    {
                                        hidden.add(new Vector2i(i, j));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i = key.x; i < world.length; i++)
            {
                for (int j = key.y; j < world[i].length; j++)
                {
                    if (tileIsBetween2Lines(new Vector2i(i, j), supRight.get(key).fst, supRight.get(key).snd) && !(i == pos.x && j == pos.y))
                    {
                        if (!hidden.contains(new Vector2i(i, j)))
                        {
                            hidden.add(new Vector2i(i, j));
                        }
                    }
                }
            }
        }

        //lowLeft
        for (HashMap.Entry<Vector2i, MyPair<Vector3f>> entry : lowLeft.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f> value = entry.getValue();
            for (HashMap.Entry<Vector2i, MyPair<Vector3f>> bis : lowLeft.entrySet())
            {
                if (key != bis.getKey())
                {
                    //on regarde le point sup à gauche
                    Vector2f x = new Vector2f(bis.getKey().x, bis.getKey().y+1);
                    if (pointBetweenTwoLines(x, value.getFst(), value.getSnd()))
                    {
                        for(int i = 0; i <= bis.getKey().x; i++)
                        {
                            for (int j = 0; j<= bis.getKey().y; j++)
                            {
                                if (tileIsBetween2Lines(new Vector2i(i, j), lowLeft.get(key).fst, lowLeft.get(bis.getKey()).snd)
                                && !hidden.contains(new Vector2i(i, j)))
                                {
                                    hidden.add(new Vector2i(i,j));
                                }
                            }
                        }
                        //todo
                    }
                }
            }
            for (int i = 0; i <= key.x; i++)
            {
                for (int j = 0; j <= key.y; j++)
                {
                    if (tileIsBetween2Lines(new Vector2i(i, j), lowLeft.get(key).fst, lowLeft.get(key).snd) && !(i == pos.x && j == pos.y))
                    {
                        if (!hidden.contains(new Vector2i(i, j)))
                        {
                            hidden.add(new Vector2i(i, j));
                        }
                    }
                }
            }
        }

        //lowRight
        for (HashMap.Entry<Vector2i, MyPair<Vector3f>> entry : lowRight.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f> value = entry.getValue();
            for (HashMap.Entry<Vector2i, MyPair<Vector3f>> bis : lowRight.entrySet())
            {
                if (key != bis.getKey())
                {
                    //on regarde le point bas à gauche
                    Vector2f x = new Vector2f(bis.getKey().x, bis.getKey().y);
                    if (pointBetweenTwoLines(x, value.getFst(), value.getSnd()))
                    {
                        lowRight.put(key, new MyPair<Vector3f>(value.getFst(), bis.getValue().getSnd()));
                    }
                }
            }
            for (int i = key.x; i < world.length; i++)
            {
                for (int j = 0; j <= key.y; j++)
                {
                    if (tileIsBetween2Lines(new Vector2i(i, j), lowRight.get(key).fst, lowRight.get(key).snd) && !(i == pos.x && j == pos.y))
                    {
                        if (!hidden.contains(new Vector2i(i, j)))
                        {
                            hidden.add(new Vector2i(i, j));
                        }
                    }
                }
            }
        }


        return hidden;
    }

    public static void main(String[] args)
    {
        HashMap<Integer, Integer> x = new HashMap<>();
        x.put(0, 1);
        x.put(1, 2);
        x.put(2, 3);
        for (HashMap.Entry<Integer, Integer> entry : x.entrySet())
        {
            int key = entry.getKey();
            int value = entry.getValue();
            for (HashMap.Entry<Integer, Integer> entry1 : x.entrySet())
            {
                if (entry1.getKey() != key)
                {
                    if (entry1.getValue() == 2)
                    {
                        x.put(1, value);
                    }
                }
            }
        }
        System.out.println(x);
    }

}