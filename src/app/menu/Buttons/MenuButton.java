package app.menu.Buttons;


import Graphics.Sprite;
import Graphics.Vector2f;


public abstract class MenuButton extends MenuComponent {
    public MenuButton(String title, Sprite sprite ) {
        super(title,sprite);

    }

    public void clicked() {
        System.out.println(this.textZone.getString()); //TODO temporaire test
    }


    public boolean collide(Vector2f mousse) {
        return getSprite().getBounds().contains(mousse.x, mousse.y);
    }

}
