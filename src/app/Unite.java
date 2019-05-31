package app;

import Graphics.*;
import System.*;

public abstract class Unite
{
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

    public abstract boolean isDead();

    /**
     * Heal points of this unite
     * @return current remaining heal points
     */
    public abstract short getHp();

    /**
     * Field of view of this unite
     * @return
     */
    public abstract short getFov();

    public abstract Weapon getPrimary();

    public abstract Weapon getSecondary();

    public abstract Weapon getMelee();

    /**
     * Gives graphics component that is drawn by the Game
     * @return
     */
    public abstract Sprite getSprite();

    /**
     * Set Map Position into Tile coordinates
     * @param coords
     */
    public abstract void setMapPosition(Vector2i coords); // Map[x][y]

    public abstract void draw(RenderTarget target);

    /**
     * Gives Map Position into Tile coordinates
     * @return
     */
    public abstract Vector2i getMapPosition();

    /**
     * Hurt/Heal the unite
     * @param amount
     * @see Unite#getHp()
     * @see Unite#isDead()
     */
    public abstract void takeDamages(int amount);

    public abstract short getSparePoints();

    /**
     * Reset all action points to maximum.
     * Trigger specific unite effect.
     */
    public abstract void resetTurn(); //reset les pa et autres bonus suivant l'unite

    /**
     * Gives the spritesheet used by the unite
     * @return
     */
    public abstract ConstTexture getSpritesheet();

    /**
     * Defines a team for current unite
     * @param team
     */
    public abstract void setTeam(Team team); //selectionne le bon sprite sheet

    public abstract Team getTeam();

}
