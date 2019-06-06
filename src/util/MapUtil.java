package util;

import Graphics.Vector2f;
import Graphics.Vector2i;
import Graphics.Vector3f;
import app.Unite;
import app.map.Map;
import app.map.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static util.Line.*;

public class MapUtil
{
    //Todo : Opti / refac
    // --> Retirer les obstacles qui sont déjà strictement compris entre les droites d'un autre
    //

    /**
     * Used to update the fog of war based on the unit's fov
     *
     * @param unit
     * @param map
     * @return the list of tile that are hidden in a 10*10 square around the unit
     */
    public static List<Vector2i> getHidden(Unite unit, Map map)
    {
        return getHidden(unit.getMapPosition(), unit.getFov(), map);
    }

    public static List<Vector2i> getVisibles(Unite unit, Map map)
    {
        return getVisibles(unit.getMapPosition(), unit.getFov(), map);
    }

    public static List<Vector2i> getHidden(Vector2i tile, int fov, Map map)
    {
        ArrayList<Vector2i> hidden = new ArrayList<Vector2i>();
        LinkedHashMap<Vector2i, MyPair<Vector3f, Vector3f>> supLeft = new LinkedHashMap<>();
        LinkedHashMap<Vector2i, MyPair<Vector3f, Vector3f>> supRight = new LinkedHashMap<>();
        LinkedHashMap<Vector2i, MyPair<Vector3f, Vector3f>> lowLeft = new LinkedHashMap<>();
        LinkedHashMap<Vector2i, MyPair<Vector3f, Vector3f>> lowRight = new LinkedHashMap<>();
        Tile[][] world = map.getWorld();
        Vector2i pos = tile;
        Vector2f startOfLines = new Vector2f(pos.x + .5f, pos.y + .5f);

        //AJOUT DES OBSTACLES ET DE LEURS DROITES DANS LES HASHMAPS
        for (int i = pos.x - fov; i < pos.x + fov; i++)
        {
            for (int j = pos.y - fov; j < pos.y + fov; j++)
            {
                Vector2i current = new Vector2i(i, j);
                Vector2f coin1;
                Vector2f coin2;
                Vector3f line1;
                Vector3f line2;
                if (validIndex(world, i, j) && world[i][j].isObstacle())
                {

                    /*
                    sens horaire
                    supLeft : bas Gauche, haut Droit
                    supRight : haut Gauche, bas Droit
                    lowRight : haut droit, bas gauche
                    lowLeft : bas droit, haut gauche
                     */
                    if (i <= pos.x) //GAUCHE
                    {
                        if (i == pos.x) //Même colonne
                        {
                            if (j - pos.y >= 1) //mur AU DESSUS de nous (supLeft, supRight)
                            {
                                if (j - pos.y == 1) //on est collé au mur
                                {
                                    //cas particulier : les coins supérieurs
                                    coin1 = new Vector2f(i, j + 1);
                                    coin2 = new Vector2f(i + 1, j);

                                } else //on est pas collé
                                {
                                    //les coins inférieurs
                                    coin1 = new Vector2f(i, j);
                                    coin2 = new Vector2f(i + 1, j);
                                }
                                line1 = computeLine(startOfLines, coin1);
                                line2 = computeLine(startOfLines, coin2);

                                supLeft.put(current, new MyPair(line1, line2));
                                supRight.put(current, new MyPair(line1, line2));

                            } else if (pos.y - j >= 1) //mur EN DESSOUS de nous (lowLeft, lowRight)
                            {
                                if (pos.y - j == 1) //on est collé au mur
                                {
                                    //cas particulier : les coins inferieurs
                                    coin1 = new Vector2f(i + 1, j);
                                    coin2 = new Vector2f(i, j);

                                } else //on est pas collé
                                {
                                    coin1 = new Vector2f(i + 1, j + 1);
                                    coin2 = new Vector2f(i, j + 1);
                                }
                                line1 = computeLine(startOfLines, coin1);
                                line2 = computeLine(startOfLines, coin2);

                                lowLeft.put(current, new MyPair(line1, line2));
                                lowRight.put(current, new MyPair(line1, line2));

                            }
                        } else //STRICTE GAUCHE
                        {
                            if (pos.y == j) //obstacle sur la GAUCHE (supLeft, lowLeft)
                            {
                                if (pos.x - i == 1)//on est collé au mur
                                {
                                    //cas particulier : on prend les coins gauche
                                    coin1 = new Vector2f(i, j);
                                    coin2 = new Vector2f(i, j + 1);

                                } else //on est pas collé au mur
                                {
                                    //on prend les coins droit
                                    coin1 = new Vector2f(i + 1, j);
                                    coin2 = new Vector2f(i + 1, j + 1);

                                }
                                line1 = computeLine(startOfLines, coin1);
                                line2 = computeLine(startOfLines, coin2);

                                supLeft.put(current, new MyPair(line1, line2));
                                lowLeft.put(current, new MyPair(line1, line2));

                            } else if (j > pos.y) // partie SUPERIEUR (STRICTE) (supLeft)
                            {
                                //coins bas gauche et haut droit
                                coin1 = new Vector2f(i, j);
                                coin2 = new Vector2f(i + 1, j + 1);

                                line1 = computeLine(startOfLines, coin1);
                                line2 = computeLine(startOfLines, coin2);

                                supLeft.put(current, new MyPair(line1, line2));

                            } else //partie INFERIEUR (STRICTE) (lowLeft)
                            {
                                //coins bas droit, et haut gauche
                                coin1 = new Vector2f(i + 1, j);
                                coin2 = new Vector2f(i, j + 1);

                                line1 = computeLine(startOfLines, coin1);
                                line2 = computeLine(startOfLines, coin2);

                                lowLeft.put(current, new MyPair(line1, line2));

                            }
                        }
                    } else //DROITE
                    {
                        if (j == pos.y) //obstacle sur la DROITE (supRight, lowRight)
                        {
                            if (i - pos.x == 1) //collé au mur
                            {
                                //cas particulier : coins droit
                                coin1 = new Vector2f(i + 1, j + 1);
                                coin2 = new Vector2f(i + 1, j);

                            } else //pas collé
                            {
                                //coins gauche
                                coin1 = new Vector2f(i, j + 1);
                                coin2 = new Vector2f(i, j);
                            }
                            line1 = computeLine(startOfLines, coin1);
                            line2 = computeLine(startOfLines, coin2);

                            supRight.put(current, new MyPair(line1, line2));
                            lowRight.put(current, new MyPair(line1, line2));
                        } else if (j < pos.y) //inférieur STRICTE (lowRight)
                        {
                            //haut droit et bas gauche
                            coin1 = new Vector2f(i + 1, j + 1);
                            coin2 = new Vector2f(i, j);

                            line1 = computeLine(startOfLines, coin1);
                            line2 = computeLine(startOfLines, coin2);

                            lowRight.put(current, new MyPair(line1, line2));
                        } else //supérieur STRICTE (supRight)
                        {
                            //haut gauche et bas droit
                            coin1 = new Vector2f(i, j + 1);
                            coin2 = new Vector2f(i + 1, j);

                            line1 = computeLine(startOfLines, coin1);
                            line2 = computeLine(startOfLines, coin2);

                            supRight.put(current, new MyPair(line1, line2));
                        }
                    }
                }
            }
        } //FIN AJOUT DANS HASHMAPS

        //supLeft
        for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> entry : supLeft.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f, Vector3f> value = entry.getValue();
            int a = pos.x - key.x;
            int b = key.y - pos.y;
            //System.out.println("==================================================");
            //System.out.println("KEY: " + key);

            for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> bis : supLeft.entrySet())
            {
                //System.out.println("    bKEY : " + bis.getKey());
                Vector2f coin, coin1;
                if (a > b)
                {
                    //haut droit
                    coin = new Vector2f(bis.getKey().x + 1, bis.getKey().y + 1);
                    //bas gauche
                    coin1 = new Vector2f(bis.getKey().x, bis.getKey().y);

                    if (pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && (bis.getKey().x >= key.x || bis.getKey().y < key.y))
                    {
                        supLeft.put(key, new MyPair<>(bis.getValue().fst, value.snd));
                        value = supLeft.get(key);
                    }
                } else if (a < b)
                {
                    //bas gauche
                    coin = new Vector2f(bis.getKey().x, bis.getKey().y);
                    //haut droit
                    coin1 = new Vector2f(bis.getKey().x + 1, bis.getKey().y + 2);

                    if (pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && (bis.getKey().y <= key.y || bis.getKey().x > key.x))
                    {
                        supLeft.put(key, new MyPair<>(value.fst, bis.getValue().snd));
                        value = supLeft.get(key);
                    }

                } else // a == b
                {
                    int a2 = pos.x - bis.getKey().x, b2 = bis.getKey().y - pos.y;
                    //haut droit
                    coin = new Vector2f(bis.getKey().x + 1, bis.getKey().y + 2);
                    //bas gauche
                    coin1 = new Vector2f(bis.getKey().x, bis.getKey().y);
                    if (a2 > b2 && bis.getKey().y <= key.y && bis.getKey().x <= key.x && pointBetweenTwoLines(coin, value.fst, value.snd)
                            && !pointBetweenTwoLines(coin1, value.fst, value.snd))
                    {
                        supLeft.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = supLeft.get(key);
                    } else if (a2 < b2 && bis.getKey().x >= key.x &&
                            pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        supLeft.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = supLeft.get(key);
                    } //si == on s'en fout
                }

            }
            for (int i = pos.x - fov; i <= key.x; i++)
            {
                for (int j = key.y; j <= pos.y + fov /*todo : ou pos.y + fov ???*/; j++)
                {
                    if (validIndex(world, i, j) && tileIsBetween2Lines(new Vector2i(i, j), value.getFst(), value.getSnd())
                            && !(i == key.x && j == key.y))
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
        for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> entry : lowLeft.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f, Vector3f> value = entry.getValue();
            int a = pos.x - key.x;
            int b = pos.y - key.y;
            for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> bis : lowLeft.entrySet())
            {
                Vector2f coin, coin1;
                if (a > b)
                {
                    //sup gauche et bas droit
                    coin = new Vector2f(bis.getKey().x, bis.getKey().y + 1);
                    coin1 = new Vector2f(bis.getKey().x + 1, bis.getKey().y);
                    if ((bis.getKey().x >= key.x || bis.getKey().y > key.y)
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        lowLeft.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = lowLeft.get(key);
                    }
                } else if (a < b)
                {
                    //coin bas droit et sup gauche
                    coin = new Vector2f(bis.getKey().x + 1, bis.getKey().y);
                    coin1 = new Vector2f(bis.getKey().x, bis.getKey().y + 1);
                    if ((bis.getKey().y >= key.y || bis.getKey().y < key.y)
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        lowLeft.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = lowLeft.get(key);
                    }
                } else //a == b
                {
                    //coin bas droit et sup gauche
                    coin = new Vector2f(bis.getKey().x + 1, bis.getKey().y);
                    coin1 = new Vector2f(bis.getKey().x, bis.getKey().y + 1);
                    if (bis.getKey().y >= key.y
                            && pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        lowLeft.put(key, new MyPair<>(value.getFst(), bis.getValue().getSnd()));
                        value = lowLeft.get(key);
                    } else if (bis.getKey().x >= key.x
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        lowLeft.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = lowLeft.get(key);
                    }
                }
            }
            //todo ou utiliser pos.x/y - fov au lieu de 0
            for (int i = pos.x - fov; i <= key.x; i++)
            {
                for (int j = pos.y - fov; j <= key.y; j++)
                {
                    if (validIndex(world, i, j) && tileIsBetween2Lines(new Vector2i(i, j), value.getFst(), value.getSnd())
                            && !(i == key.x && j == key.y))
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
        for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> entry : supRight.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f, Vector3f> value = entry.getValue();
            int a = key.x - pos.x;
            int b = key.y - pos.y;
            for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> bis : supRight.entrySet())
            {
                //supG
                Vector2f coin = new Vector2f(bis.getKey().x, bis.getKey().y + 1);
                //infD
                Vector2f coin1 = new Vector2f(bis.getKey().x + 1, bis.getKey().y);

                if (a < b)
                {
                    if ((bis.getKey().y <= key.y || bis.getKey().x < key.x)
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        supRight.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = supRight.get(key);
                    }
                } else if (a > b)
                {
                    if (bis.getKey().y <= key.y
                            && pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        supRight.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = supRight.get(key);
                    }
                } else //a == b
                {
                    if (bis.getKey().y <= key.y
                            && pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        supRight.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = supRight.get(key);
                    } else if (bis.getKey().x <= key.x
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        supRight.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = supRight.get(key);
                    }
                }
            }
            //todo ou utiliser pos.x/y - fov au lieu de 0
            for (int i = key.x; i <= pos.x + fov; i++)
            {
                for (int j = key.y; j <= pos.y + fov; j++)
                {
                    if (validIndex(world, i, j) && tileIsBetween2Lines(new Vector2i(i, j), value.getFst(), value.getSnd())
                            && !(i== key.x && j == key.y))
                    {
                        if (!hidden.contains(new Vector2i(i, j)))
                        {
                            hidden.add(new Vector2i(i, j));
                        }
                    }
                }
            }
        }

        //todo lowRight
        for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> entry : lowRight.entrySet())
        {
            Vector2i key = entry.getKey();
            MyPair<Vector3f, Vector3f> value = entry.getValue();
            int a = key.x - pos.x;
            int b = pos.y - key.y;
            for (HashMap.Entry<Vector2i, MyPair<Vector3f, Vector3f>> bis : lowRight.entrySet())
            {
                //sup droit
                Vector2f coin = new Vector2f(bis.getKey().x + 1, bis.getKey().y + 1);
                //bas gauche
                Vector2f coin1 = new Vector2f(bis.getKey().x, bis.getKey().y);
                if (a < b) //!!! low
                {
                    if ((bis.getKey().y >= key.y || bis.getKey().x < key.x)
                            && pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        lowRight.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = lowRight.get(key);
                    }
                } else if (a > b) //!!! sup
                {
                    if ((bis.getKey().x <= key.x || bis.getKey().y > key.y)
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        lowRight.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = lowRight.get(key);
                    }
                } else //a == b
                {
                    if (bis.getKey().x <= key.x
                            && pointBetweenTwoLines(coin, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin1, value.getFst(), value.getSnd()))
                    {
                        lowRight.put(key, new MyPair(value.getFst(), bis.getValue().getSnd()));
                        value = lowRight.get(key);
                    } else if (bis.getKey().y >= key.y
                            && pointBetweenTwoLines(coin1, value.getFst(), value.getSnd())
                            && !pointBetweenTwoLines(coin, value.getFst(), value.getSnd()))
                    {
                        lowRight.put(key, new MyPair(bis.getValue().getFst(), value.getSnd()));
                        value = lowRight.get(key);
                    }
                }
            }
            //todo change world.length / 0
            for (int i = key.x; i <= pos.x + fov; i++)
            {
                for (int j = pos.y - fov; j <= key.y; j++)
                {
                    if (validIndex(world, i, j) && tileIsBetween2Lines(new Vector2i(i, j), value.getFst(), value.getSnd())
                            && !(i == key.x && j == key.y))
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

    public static List<Vector2i> getVisibles(Vector2i tile, int fov, Map map)
    {
        Vector2i pos = tile;
        ArrayList<Vector2i> ret = new ArrayList<>();
        List<Vector2i> hidden = getHidden(tile, fov, map);
        int var = 0;
        for (int i = pos.x - fov; i <= pos.x + fov; i++)
        {
            if (i == pos.x)
            {
                var = fov;
            }
            for (int j = pos.y - var; j <= pos.y + var; j++)
            {
                if (validIndex(map.getWorld(), i, j))
                {
                    if (!hidden.contains(new Vector2i(i, j)))
                    {
                        ret.add(new Vector2i(i, j));
                    }
                }
            }
            if (i < pos.x)
            {
                var++;
            } else
            {
                var--;
            }
        }
        return ret;
    }
}
