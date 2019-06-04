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
