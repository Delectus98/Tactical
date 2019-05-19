package app.map;

import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;

import java.util.List;

public class MapImpl extends Map {
    private static final float tileWidth = 64;

    private int width ;
    private int height ;

    public MapImpl(){}

    public MapImpl(int width, int height){
        world = new Tile[width][height];
        this.width = width;
        this.height = height;
    }

    public void loadFromMemory(String pattern) {}

    @Override
    public void draw(Camera2D camera, RenderTarget target) {
        // on obtient les coordonnées de la caméra
        Vector2f dim = camera.getDimension(); // dimension de la vue de la caméra
        Vector2f tlCorner = camera.getCenter().sum(dim.mul(-0.5f)); // coin gauche haut de la caméra

        // on cacule la zone rectangulaire où les tuiles doivent être affichées
        int x = Math.max(0,(int)(tlCorner.x / tileWidth)); // tuile la plus a gauche du point de vue de la caméra
        int y = Math.max(0,(int)(tlCorner.y / tileWidth)); // tuile la plus a gauche du point de vue de la caméra
        final int offset = 2; // sert pour afficher les cases mal concidérées
        int x2 = Math.max(0, (int)(tlCorner.x / tileWidth) + (int)(dim.x / tileWidth) + offset); // tuile la plus a droite affichée du point de vue de la caméra
        int y2 = Math.max(0, (int)(tlCorner.y / tileWidth) + (int)(dim.y / tileWidth) + offset); // tuile la plus en bas affichée du point de vue de la caméra

        // on affiche uniquement cette zone
        for (int i = x ; i < x2 && i < width ; ++i) {
            for (int j = y ; j < y2 && j < height ; ++j) {
                this.getWorld()[i][j].draw(target);
            }
        }
    }

    @Override
    public Tile[][] getWorld() {
        return world;
    }

    @Override
    public List<Vector2i> getSpawnPoints(int currentPlayer) {
        return spawnPoints[currentPlayer];
    }
}
