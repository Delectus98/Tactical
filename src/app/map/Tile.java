package app.map;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Texture;
import System.*;

public class Tile
{
    //prend une texture en param
    Sprite floor;
    Sprite struct;
    boolean isObstacle;

    public Tile(Texture texture, FloatRect rect) //sans struct <=> !isObstacle
    {
        //todo
        floor = new Sprite(texture);
        floor.setTextureRect(rect.l, rect.t, rect.w, rect.h);

        isObstacle = false;
    }

    public Tile(Texture floor, FloatRect rectFloor, Texture struct, FloatRect rectStruct, boolean isObstacle)
    {
        this.floor = new Sprite(floor);
        this.floor.setTextureRect(rectFloor.l, rectFloor.t, rectFloor.w, rectFloor.h);

        this.struct = new Sprite(struct);
        this.struct.setTextureRect(rectStruct.l, rectStruct.t, rectStruct.w, rectStruct.h);

        this.isObstacle = isObstacle;
    }

    public void draw(RenderTarget target)
    {
        if (floor != null) target.draw(floor);
        if (struct != null) target.draw(struct);
    }

    public Sprite getFloor()
    {
        return floor;
    }

    public Sprite getStruct()
    {
        return struct;
    }

    public boolean isObstacle()
    {
        return isObstacle;
    }
}
