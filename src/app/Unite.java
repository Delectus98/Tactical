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

    public abstract short getHp();

    public abstract short getFov();

    public abstract Weapon getPrimary();

    public abstract Weapon getSecondary();

    public abstract Weapon getMelee();

    public abstract Sprite getSprite();

    public abstract void seMapPosition(Vector2i coords); // Map[x][y]

    public abstract void draw(RenderTarget target);

    public abstract Vector2i getMapPosition();

    public abstract void takeDamages(int amount);

    public abstract short getSparePoints();

    public abstract void resetTurn(); //reset les pa et autres bonus suivant l'unite

    public abstract ConstTexture getSpritesheet();

    public abstract void setTeam(Team team); //selectionne le bon sprite sheet

    public abstract Team getTeam();

}
