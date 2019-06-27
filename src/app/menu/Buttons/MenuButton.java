package app.menu.Buttons;


import Graphics.Sprite;
import Graphics.Vector2f;

import java.io.IOException;


public abstract class MenuButton extends MenuComponent {
    MenuButton(String title, Sprite sprite) {
        super(title,sprite);

    }

    public void clicked() throws IOException, InterruptedException {
        System.out.println(this.textZone.getString()); //TODO temporaire test
    }


    public boolean collide(Vector2f mousse) {
        return getSprite().getBounds().contains(mousse.x, mousse.y);
    }

}
