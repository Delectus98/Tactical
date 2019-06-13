package app.menu.Buttons;

import Graphics.Color;
import Graphics.Shape;
import Graphics.Text;
import util.ResourceHandler;

public class MenuComponent {

    public Shape shape;
    protected Text textZone;


    public MenuComponent(String title, Shape shape) {

        this.shape = shape;
        this.textZone= new Text(ResourceHandler.getFont("default"),title);
        setPosition(shape.getX(), shape.getY());
        shape.setFillColor(Color.Blue);
        textZone.setFillColor(Color.White);
    }

    public Shape getShape() {
        return shape;
    }

    public Shape getText() {
        return textZone;
    }

    public void setPosition(float x, float y){
        this.shape.setPosition(x,y);
        this.textZone.setPosition(x,y);
    }
}
