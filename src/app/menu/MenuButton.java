package app.menu;


import Graphics.FontFamily;
import Graphics.Shape;
import Graphics.Text;
import Graphics.Vector2f;

import java.io.IOException;


public abstract class MenuButton {
    public String text = "null";
    protected Shape shape;
    protected Text textZone;


    public MenuButton(String title, Shape shape) {
        this.text = title;
        this.shape = shape;
        //  textZone.setPosition(shape.getX(),shape.getY());;
        //  textZone.setString(title);
    }

    public void clicked() {
        System.out.println(this.text); //TODO temporaire test
    }

    public Shape getShape() {
        return shape;
    }

    public Shape getText() {
        return textZone;
    }

    public boolean collide(Vector2f mousse) {
        return shape.getBounds().contains(mousse.x, mousse.y);
    }

}
