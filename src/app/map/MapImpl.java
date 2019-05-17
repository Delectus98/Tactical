package app.map;

import Graphics.Vector2f;
import System.*;
import Graphics.Vector2i;

import java.util.List;

public class MapImpl extends Map {
    private static final float tileWidth = 64;

    private int width ;
    private int height ;

    public MapImpl(int width, int height){
        world = new Tile[width][height];
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Camera2D camera, RenderTarget target) {
        Vector2f dim = camera.getDimension();
        Vector2f tlCorner = camera.getCenter().sum(dim.mul(-0.5f));


        int x = Math.max(0,(int)(tlCorner.x / tileWidth));
        int y = Math.max(0,(int)(tlCorner.y / tileWidth));

        int w = (int)(dim.x / tileWidth); // tuile en longueur affichée
        int h = (int)(dim.y / tileWidth); // tuile en hauteur affichée


        final int offset = 2; // sert pour afficher les cases mal concidérées
        for (int i = x ; i < x + w + offset && i < width ; ++i) {
            for (int j = y ; j < y + h + offset && j < height ; ++j) {
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
