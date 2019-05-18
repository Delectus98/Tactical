package util;


import Graphics.Vector2i;

import app.Unite;
import app.map.Map;


import java.util.*;

public class Pathfinder {

    /*TODO voir comment réduire la zone à analyser, en réduisant la map entière à la zone globale de marche( en ignorant les blquages). attention, les bloquages ne font pas partie de la map qu'on a stockée. */
    private int width;
    private int height;
    List<Vector2i> mapList;

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
    public HashMap<Vector2i, Vector2i> possiblePath(Unite u) {

        int cost = 1; //à changer si besoin
        int distmax = u.getActionPoints() * cost;

        //Tableau des distances
        int[] dist = new int[mapList.size()];
        HashMap<Vector2i, Vector2i> pred = new HashMap<>();
        final boolean[] visited = new boolean[mapList.size()]; // all false initially
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        //depuis la position de l'unité
        dist[mapList.indexOf(u.getMapPosition())] = 0;//TODO Possible changement en passant par la correspondance unit.position vers mapList index

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);
            visited[next] = true;

            // The shortest path to next is dist[next] and via pred[next].
            Vector2i nil = new Vector2i(-1, -1);
            final ArrayList<Vector2i> n = parents(mapList.get(next));
            for (Vector2i vector2i : n) {
                final int v = mapList.indexOf(vector2i);
                final int d = dist[next] + 1;
                if (dist[v] > d) {
                    dist[v] = d;
                    if (dist[v] <= distmax) { //Vérification distance
                        pred.put(vector2i, mapList.get(next));
                    } else {
                        pred.put(vector2i, nil);//TODO à enlever? pour réduire la taille de la map.
                    }

                }

            }

        }
        return pred;  // (ignore pred[s]==0!)
    }


    public ArrayList Pathfind(HashMap<Vector2i, Vector2i> correspondance, Vector2i start, Vector2i goal) {
        final java.util.ArrayList path = new java.util.ArrayList();
        Vector2i current = goal;
        while (!found(current, start)) {
            path.add(0, current);
            current = correspondance.get(current);
        }
        path.add(0, start);
        return path;
    }

    //ANNEXES

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

    private ArrayList<Vector2i> parents(Vector2i v) {
        //int compteur=0;
        ArrayList<Vector2i> res = new ArrayList<>();
        for (int i = 0; i < mapList.size(); i++) {
            Vector2i current = mapList.get(i);
            if ((current.x == v.x - 1 || current.x == v.x + 1) && current.y == v.y)
                res.add(current);
        }
        return res;
    }

    private boolean found(Vector2i current, Vector2i goal) {
        return (current.x == goal.x && current.y == goal.y);
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