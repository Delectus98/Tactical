package app.weapon;

import Graphics.Vector2i;
import app.Unite;
import util.MyPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area where damage/heal will be applied
 */
public final class Impact
{
    private List<MyPair<Vector2i, Integer>> impact;

    public Impact()
    {
        impact = new ArrayList<>();
    }

    /**
     * Adds a impact on a specific tile
     * @param tile specified tile
     * @param damage damage to the tile
     */
    public void add(Vector2i tile, int damage)
    {
        impact.add(new MyPair<>(tile, damage));
    }

    /**
     * Creates an impact zone using a list that contains all tiles associated with a damage
     * @param impact list with pairs of tile pos and damage
     */
    public Impact(List<MyPair<Vector2i, Integer>> impact)
    {
        this.impact = impact;
    }

    /**
     * Gives the total of tiles impacted
     * @return total of tiles impacted
     */
    public int getTileCount() {
        return impact.size();
    }

    /**
     * Gives the coordinates of a tile using index
     * @param i index
     * @return coordinates of a tile using index
     */
    public Vector2i getTileCoord(int i) {
        return impact.get(i).getFst();
    }

    /**
     * Gives the damage associated to a tile using index
     * @param i index
     * @return damage associated to a tile using index
     */
    public int getTileDamage(int i) {
        return impact.get(i).getSnd();
    }

    /**
     * Applies damage/heal to all unites is the area of the impact
     * @param all all unites
     */
    public final void apply(List<Unite> all)
    {
        for (Unite unite : all) {
            impact.forEach(i -> {
                if (i.getFst().equals(unite.getMapPosition())) {
                    unite.takeDamages(i.getSnd());
                }
            });
        }
    }
}
