package app.weapon;

import Graphics.Vector2i;
import app.Unite;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area where damage/heal will be applied
 */
public final class Impact
{
    private List<Integer> damages;
    private List<Vector2i> areas;

    public Impact()
    {
        damages = new ArrayList<>();
        areas = new ArrayList<>();
    }

    /**
     * Adds a impact on a specific tile
     * @param tile specified tile
     * @param damage damage to the tile
     */
    public void add(Vector2i tile, int damage)
    {
        damages.add(damage);
        areas.add(tile);
    }

    /**
     * Gives the total of tiles impacted
     * @return total of tiles impacted
     */
    public int getTileCount() {
        return damages.size();
    }

    /**
     * Gives the coordinates of a tile using index
     * @param i index
     * @return coordinates of a tile using index
     */
    public Vector2i getTileCoord(int i) {
        return areas.get(i);
    }

    /**
     * Gives the damage associated to a tile using index
     * @param i index
     * @return damage associated to a tile using index
     */
    public int getTileDamage(int i) {
        return damages.get(i);
    }

    /**
     * Reduce the damage according to an accuracy
     * @param percent accuracy percent
     * @return
     */
    public Impact reduceAccuracy(float percent) {
        for (int i = 0 ; i < damages.size() ; ++i) {
            damages.set(i, (int)(damages.get(i) * percent));
        }

        return this;
    }

    /**
     * Reduce the chance
     * @param percent
     * @return
     */
    public Impact chance(float percent) {
        for (int i = 0 ; i < damages.size() ; ++i) {
            int impacted = Math.random() <= percent ? 1 : 0;
            damages.set(i, (damages.get(i) * impacted));
        }

        return this;
    }

    /**
     * Applies damage/heal to all unites is the area of the impact
     * @param all all unites
     */
    public final void apply(List<Unite> all)
    {
        for (Unite unite : all) {
            for (int i = 0 ; i < damages.size() ; ++i) {
                if (areas.get(i).equals(unite.getMapPosition())) {
                    unite.takeDamages(damages.get(i));
                }
            }
        }
    }
}
