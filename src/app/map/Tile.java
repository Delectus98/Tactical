package app.map;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Texture;

public class Tile
{
    //prend une texture en param
    Sprite floor;
    Sprite struct;
    boolean isObstacle;

    public Tile(Texture texture, FloatRect rect) //sans struct <=> !isObstacle
    {
        //todo
    }

    public Tile(Texture floor, FloatRect rectFloor, Texture struct, FloatRect rectStruct, boolean isObstacle)
    {

    }

}
