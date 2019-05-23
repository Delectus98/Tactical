package app.map;

import Graphics.ConstTexture;
import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Texture;
import System.*;

//TODO Remove draw features to let Game draw itself
public class Tile
{
    //prend une texture en param
    Sprite floor = null;
    Sprite struct = null;
    boolean isObstacle;

    public Tile(ConstTexture texture, FloatRect rect, boolean isObstacle) //sans struct <=> !isObstacle
    {
        if (!isObstacle) {
            floor = new Sprite(texture);
            floor.setTextureRect(rect.l, rect.t, rect.w, rect.h);
        } else {
            struct = new Sprite(texture);
            struct.setTextureRect(rect.l, rect.t, rect.w, rect.h);
        }

        this.isObstacle = isObstacle;
    }

    public Tile(ConstTexture floor, FloatRect rectFloor, ConstTexture struct, FloatRect rectStruct, boolean isObstacle)
    {
        this.floor = new Sprite(floor);
        this.floor.setTextureRect(rectFloor.l, rectFloor.t, rectFloor.w, rectFloor.h);

        this.struct = new Sprite(struct);
        this.struct.setTextureRect(rectStruct.l, rectStruct.t, rectStruct.w, rectStruct.h);

        this.isObstacle = isObstacle;
    }

    //pour les testes sans affichage.
    public Tile(boolean isObstacle)
    {
        this.isObstacle = isObstacle;
    }

    public Sprite getFloor()
    {
        return floor;
    }

    public Sprite getStruct()
    {
        return struct;
    }

    /*public void drawFloor(RenderTarget target) {
        if (floor != null) target.draw(floor);
    }

    public void drawStruct(RenderTarget target) {
        if (struct != null) target.draw(struct);
    }
    */
    public boolean isObstacle()
    {
        return isObstacle;
    }
}
