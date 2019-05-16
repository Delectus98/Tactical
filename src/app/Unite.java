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

    abstract boolean isDead();

    abstract short getHp();

    abstract short getFov();

    abstract Weapon getPrimary();

    abstract Weapon getSecondary();

    abstract Weapon getMelee();

    abstract void setGraphicPosition(Vector2f position); // sprite,setPosition(...)

    abstract void seMapPosition(Vector2i coords); // Map[x][y]

    abstract void draw(RenderTarget target);

    abstract Vector2i getMapPosition();

    abstract void drawMoveRange(RenderTarget target);

    abstract void takeDamages(int amount);

}
