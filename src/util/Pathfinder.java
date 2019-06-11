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
        this.width = m.getWidth();
        this.height = m.getHeight();
        convertMap(m);
    }

    //PossiblePath => reachable Tiles

    /**
     * Sert à afficher la zone pù il est possible de se déplacer
     * @param u L'unité à déplacer
     * @param ennemyunits Liste des unités ennemies (gestion collisions)
     * @param vectVisibles Liste des positions visibles
     * @return Map<Position, position pour y accéder> de cases où il est possible de se déplacer.
     */
    public HashMap<Vector2i, Vector2i> possiblePath(Unite u, List<Unite> ennemyunits, List<Vector2i> vectVisibles) {

        int cost = 1; //à changer si besoin
        int distmax = u.getSparePoints() * cost;
        ArrayList<Vector2i> tmpMapList = collisionVisible(ennemyunits, vectVisibles);
        //Tableau des distances
        int[] dist = new int[tmpMapList.size()];
        HashMap<Vector2i, Vector2i> pred = new HashMap<>();
        final boolean[] visited = new boolean[tmpMapList.size()]; // all false initially
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
        }

        //depuis la position de l'unité
        int k=tmpMapList.indexOf(inMap(u.getMapPosition()));
        if(k>=0 &&k<dist.length){
            dist[k] = 0;}
        else {
            System.out.println("Erreur case de l'unité déja occupée ou inexdistante");
            return new HashMap<>();
        }

        for (int i = 0; i < dist.length; i++) {
            final int next = minVertex(dist, visited);

            if (next >= 0 && next <= visited.length) {
                visited[next] = true;
            } else {
                continue;
            }
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

    /**
     * Trouve le chemin le plus court entre deux positions si possible.
     *
     * @param correspondance Hashmap fournie par possiblePath
     * @param ennemyunits    liste des unités ennemies
     * @param allyunits      liste des unités alliées
     * @param start          position de départ ( généralement Unité.getMapPosition )
     * @param goal           objectif à atteindre
     * @return Arraylist des cases à traverser. la première est la case start.
     */
    public ArrayList<Vector2i> pathfind(HashMap<Vector2i, Vector2i> correspondance, ArrayList<Unite> ennemyunits, ArrayList<Unite> allyunits, Vector2i start, Vector2i goal) {

        final ArrayList<Vector2i> path = new ArrayList<>();


        //check si goal est sur la position d'une unité amie
        Vector2i startInMap = inMap(start);
        Vector2i goalInMap = inMap(goal);
        for (Unite v : allyunits)
            if (Objects.equals(inMap(v.getMapPosition()), inMap(goal))) {

                System.out.println("Case occupée par un allié");
                return path;
            }


        //Check si goal appartient bien a maplist, et prend l'adresse du vecteur équivement à goal dans maplist
        if (startInMap == null || goalInMap == null) {
            System.out.println("Pathfind: Position non trouvée");
        } else if (!correspondance.keySet().contains(goalInMap)) {
            System.out.println("Objectif trop loin, occupée par un ennemi ou un décors");

        } else {
            //Début de la recherche
            Vector2i current = goalInMap;
            //  while (!found(current, startInMap)) { //obsolète
            while (!current.equals(startInMap)) {
                path.add(0, current);
                current = correspondance.get(current);
            }
            path.add(0, startInMap);
        }

        return collisionInvisible(ennemyunits, path);// path;
    }

    //ANNEXES


    //pour la détection dunités: dans pathfind prendre en arg la liste de visibilité du joueur et créer un maplist temporaire qui ne contient pas les vecteurs où se trouvent des ennemis

    /**
     * Utilisé par possiblePath pour bloquer temporairement le déplacement vers une case ennemie
     *
     * @param ennemyunits liste unités ennemies
     * @param lightzone   liste cases visibles par le joueur
     * @return Arraylist équivalente à la map, positions ennemies visibles sont "Bloquées"
     */
    public ArrayList<Vector2i> collisionVisible(List<Unite> ennemyunits, List<Vector2i> lightzone) {
        //ici doit être placé la liste des unités ennemies et la zone de visibilité du joueur. clone la mapList
        ArrayList<Vector2i> tmpMapList = new ArrayList<>(mapList);

        //intersection toRemove lightzine n units
        for (Unite ennemyunit : ennemyunits)
            for (Vector2i v : lightzone)
                if (ennemyunit.getMapPosition().equals(v))
                    tmpMapList.remove(inMap(v));
        //tmpMapList.remove(mapList.indexOf(inMap(v)));

        return tmpMapList;
    }

    /**
     * Utiliser par pathfind, coupe la liste si l'unité doit passer par une case occupée
     *
     * @param ennemyunits liste unités ennemies
     * @param path        le chemin prévu
     * @return path raccourci si ennemi trouvé sur la trajectoire
     */
    private ArrayList<Vector2i> collisionInvisible(List<Unite> ennemyunits, ArrayList<Vector2i> path) {//ici doit être placé la liste des unités ennemies.
        Vector2i[] v = new Vector2i[ennemyunits.size()];
        for (int i = 0; i < ennemyunits.size(); i++) {
            v[i] = inMap(ennemyunits.get(i).getMapPosition());
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

    /* /**
     * Similaire à equals. Pourraît être supprimé
     * @param current
     * @param goal
     * @return

    private boolean found(Vector2i current, Vector2i goal) {
        // System.out.println("Current:" + current.x+'/'+current.y);
        // System.out.println("Goal:" + goal.x + "/" + goal.y);
        return current.equals(goal);
        //return (current.x == goal.x && current.y == goal.y);
    }*/

    /**
     * @param v un vecteur à tester
     * @return L'équivelent dans la map du vecteur passé en paramètre
     */
    private Vector2i inMap(Vector2i v) {
        for (Vector2i vect : mapList)
            if (vect.equals(v))
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