package app;

import Graphics.*;
import System.*;

public abstract class Unite
{
    //
    private int id;
    //
    protected short hp;
    protected short fov;
    protected short actionPoints;
    protected short maxActionPoints;
    protected Vector2i position;
    protected Weapon primary;
    protected Weapon secondary;
    protected Weapon melee;
    protected Texture texture;
    protected Sprite sprite;
    protected ConstTexture spritesheet;
    protected Team team;

    /**
     * When unite is added to player unites we defines a unique id
     * @param id specified id
     */
    public final void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gives the unique id of the unite
     */
    public final int getId()
    {
        return id;
    }

    /**
     * Checks if unite is dead or not (hp <= 0)
     * @return true if unite is dead or not (hp <= 0) else true
     */
    public abstract boolean isDead();

    /**
     * Heal points of this unite
     * @return current remaining heal points
     */
    public abstract short getHp();

    /**
     * Field of view of this unite
     * @return Field of view of this unite
     */
    public abstract short getFov();

    /**
     * Gives the first weapon of the unite
     * @return first weapon
     */
    public abstract Weapon getPrimary();
    /**
     * Gives the first weapon of the unite
     * @return second weapon
     */
    public abstract Weapon getSecondary();
    /**
     * Gives the first weapon of the unite
     * @return melee weapon
     */
    public abstract Weapon getMelee();

    /**
     * Gives the graphics component of the unite that is drawn by the Game
     * @return the graphics component of the unite that is drawn by the Game
     */
    public abstract Sprite getSprite();

    /**
     * Set Map Position into Tile coordinates
     * @param coords
     */
    public abstract void setMapPosition(Vector2i coords); // Map[x][y]

    /**
     * Draws the unite on a render target.
     * @param target render target
     */
    public abstract void draw(RenderTarget target);

    /**
     * Gives Map Position into Tile coordinates
     * @return Map Position into Tile coordinates
     */
    public abstract Vector2i getMapPosition();

    /**
     * Hurt/Heal the unite
     * @param amount quantity of damage/heal
     * @see Unite#getHp()
     * @see Unite#isDead()
     */
    public abstract void takeDamages(int amount);

    /**
     * Gives count of maximum actions points of the unite
     * @return maximum action points
     */
    public abstract short getMaximumPoints();

    /**
     * Gives count of remaining actions points of the unite
     * @return remaining action points
     */
    public abstract short getSparePoints();

    /**
     * Reset all action points to maximum.
     * Trigger specific unite effect.
     */
    public abstract void resetTurn(); //reset les pa et autres bonus suivant l'unite

    /**
     * Gives the sprite sheet used by the unite
     * @return the sprite sheet used by the unite
     */
    public abstract ConstTexture getSpriteSheet();

    /**
     * Defines a team for current unite
     * @param team
     */
    public abstract void setTeam(Team team); //selectionne le bon sprite sheet

    /**
     * Gives the unite's team
     * @return unite's team
     */
    public abstract Team getTeam();

}
