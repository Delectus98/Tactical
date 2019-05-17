package app;

import Graphics.Sprite;
import Graphics.Texture;
import Graphics.Vector2f;
import Graphics.Vector2i;
import System.*;

public abstract class Unite
{
    protected short hp;
    protected short fov;
    protected short actionPoints;
    protected Vector2i position;
    protected Weapon primary;
    protected Weapon secondary;
    protected Weapon melee;
    protected Texture texture;
    protected Sprite sprite;

    public abstract boolean isDead();

    public abstract short getHp();

    public abstract short getFov();

    public abstract Weapon getPrimary();

    public abstract Weapon getSecondary();

    public abstract Weapon getMelee();

    public abstract void setGraphicPosition(Vector2f position); // sprite,setPosition(...)

    public abstract void seMapPosition(Vector2i coords); // Map[x][y]

    public abstract void draw(RenderTarget target);

    public abstract Vector2f getGraphicPosition();

    public abstract Vector2i getMapPosition();

    public abstract void takeDamages(int amount);

    public abstract short getActionPoints();

}
