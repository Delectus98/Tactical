package app.menu.Buttons;


import Graphics.Shape;
import Graphics.Vector2f;


public abstract class MenuButton extends MenuComponent {
    public MenuButton(String title, Shape shape) {
        super(title,shape);

    }

    public void clicked() {
        System.out.println(this.textZone.getString()); //TODO temporaire test
    }


    public boolean collide(Vector2f mousse) {
        return shape.getBounds().contains(mousse.x, mousse.y);
    }

}
