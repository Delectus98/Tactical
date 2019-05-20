package util;


import Graphics.Vector2i;

import app.Unite;
import app.map.Map;


import java.util.*;

public class Pathfinder {

    private int width;
    private int height;
    private List<Vector2i> mapList = new ArrayList<>();

    public Pathfinder(Map m) {
        this.width = m.getWorld().length;
        this.height = m.getWorld()[0].length;
        convertMap(m);
    }

    //PossiblePath => reachable Tiles

    /**
     * @param u L'unité en cours
     * @return une Hashmap de <Position, position pour y arriver>
     */
    public HashMap<Vector2i, Vector2i> possiblePath(Unite u, List<Unite> units, List<Vector2i> vectVisibles) {


        //Checkunits
//mapList => résultat de collision

        int cost = 1; //à changer si besoin
        int distmax = u.getActionPoints() * cost;
        ArrayList<Vector2i> tmpMapList = collisionVisible(units, vectVisibles);
        //Tableau des distances
        int[] dist = new int[tmpMapList.size()];
        HashMap<Vector2i, Vector2i> pred = new HashMap<>();
        final boolean[] visited = new boolean[tmpMapList.size()]; // all false initially
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        //depuis la position de l'unité
        dist[tmpMapList.indexOf(inMap(u.getMapPosition()))] = 0;

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);

            visited[next] = true;

            // Shortest path to next is dist[next] and via pred[next].
            //   Vector2i nil = new Vector2i(-1, -1);
            final ArrayList<Vector2i> n = parents(tmpMapList.get(next), tmpMapList);
            pred.put(u.getMapPosition(), u.getMapPosition()); // initialise la map d'accès sinon la position initiale est couplée à rien

            for (Vector2i vector2i : n) {
                final int v = tmpMapList.indexOf(vector2i);
                final int d = dist[next] + 1;
                if (dist[v] > d) {
                    dist[v] = d;
                    if (dist[v] <= distmax) { //Vérification distance
                        pred.put(vector2i, tmpMapList.get(next));
                    }/* else {
                        pred.put(vector2i, nil);//inutile, prend plus de ressources
                    }*/

                }

            }

        }
        // retirer de pred les clés associées à nil
        return pred;  // (ignore pred[s]==0!)
    }

    //à la fin, l'unité s'arrête si elle rencontre une unité. amélioration: considérer les aliés comme obstacles, et ennemis comme actuellement.
    public ArrayList<Vector2i> pathfind(HashMap<Vector2i, Vector2i> correspondance, ArrayList<Unite> units, Vector2i start, Vector2i goal) {

        final ArrayList<Vector2i> path = new ArrayList<>();


        //Check si goal appartient bien a maplist, et prend l'adresse du vecteur équivement à goal dans maplist
        Vector2i startInMap = inMap(start);
        Vector2i goalInMap = inMap(goal);
        if (startInMap == null || goalInMap == null) {
            System.out.println("Pathfind Vecteur non trouvé");
        } else if (!correspondance.keySet().contains(goalInMap)) {
            System.out.println("Objectif trop loin");
        } else {
            //Début de la recherche
            Vector2i current = goalInMap;
            while (!found(current, startInMap)) {
                path.add(0, current);
                current = correspondance.get(current);
            }
            path.add(0, startInMap);
        }

        return collisionInvisible(units, path);// path;
    }

    //ANNEXES


    //pour la détection dunités: dans pathfind prendre en arg la liste de visibilité du joueur et créer un maplist temporaire qui ne contient pas les vecteurs où se trouvent des ennemis
    private ArrayList<Vector2i> collisionVisible(List<Unite> units, List<Vector2i> lightzone) {//ici doit être placé la liste des unités ennemies et la zone de visibilité du joueur
//clone la mapList
        ArrayList<Vector2i> tmpMapList = new ArrayList<>(mapList);

        //intersection toRemove lightzine n units
        for (Unite ennemyunit : units)
            for (Vector2i v : lightzone)
                if (ennemyunit.getMapPosition().equals(v))
                    tmpMapList.remove(inMap(v));
        //tmpMapList.remove(mapList.indexOf(inMap(v)));

        return tmpMapList;
    }

    private ArrayList<Vector2i> collisionInvisible(List<Unite> units, ArrayList<Vector2i> path) {//ici doit être placé la liste des unités ennemies.
        Vector2i[] v = new Vector2i[units.size()];
        for (int i = 0; i < units.size(); i++) {
            v[i] = inMap(units.get(i).getMapPosition());
        }

        for (int i = 0; i < path.size(); i++) {
            //recherche
            for (Vector2i vect : v)
                if (path.get(i).equals(vect)) {
                    //  for (int j = 0; j < v.length; j++)
                    //      if (path.get(i).equals(v[j])) {
                    System.out.println("Unité ennemie trouvée, arrêt du mouvement en: (" + path.get(i).x + "," + path.get(i - 1).y + ")");
                    return new ArrayList<>(path.subList(0, i));
                }
            //rencontre

            //fin

        }
        return path;
    }

    /**
     * Convertit une map en tab vers une map en vecteurs en ignorant les Tiles bloquées
     *
     * @param m la map.
     */
    private void convertMap(Map m) {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!m.getWorld()[i][j].isObstacle()) {
                    Vector2i v = new Vector2i(i, j);
                    mapList.add(v);
                }
            }
        }
    }

    private ArrayList<Vector2i> parents(Vector2i v, ArrayList<Vector2i> tmpMapList) {
        //int compteur=0;
        ArrayList<Vector2i> res = new ArrayList<>();
        for (int i = 0; i < tmpMapList.size() && res.size() <= 4; i++) {
            Vector2i current = tmpMapList.get(i);
            if (((current.x == v.x - 1 || current.x == v.x + 1) && current.y == v.y) || (current.y == v.y - 1 || current.y == v.y + 1) && current.x == v.x) {
                res.add(current);
                // System.out.println(v.x+", "+v.y+"///"+current.x+" ,"+current.y);
            }
        }

        return res;
    }


    private boolean found(Vector2i current, Vector2i goal) {
        // System.out.println("Current:" + current.x+'/'+current.y);
        // System.out.println("Goal:" + goal.x + "/" + goal.y);
        return (current.x == goal.x && current.y == goal.y);
    }

    /**
     * @param v un vecteur à tester
     * @return L'équivelent dans la map du vecteur passé en paramètre
     */
    private Vector2i inMap(Vector2i v) {
        for (Vector2i vect : mapList)
            if (found(vect, v))
                return vect;

        System.out.println("Position (" + v.x + "," + v.y + " )non trouvée dans la map. Hauteur " + height + ", Largeur " + width);
        return null;


    }

    /**
     * Calcule l'indice de la Tile au moindre poids
     *
     * @param dist Tableau des distances
     * @param v    tableau des Tiles déja visitées
     * @return l'indice de la Tile au moindre poids parmi les non visités
     */
    private static int minVertex(int[] dist, boolean[] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices
        for (int i = 0; i < dist.length; i++) {
            if (!v[i] && dist[i] < x) {
                y = i;
                x = dist[i];
            }

        }
        return y;

    }

}