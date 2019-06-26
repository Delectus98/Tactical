package app.menu.Buttons;

import Graphics.Color;
import Graphics.Shape;
import Graphics.Sprite;
import Graphics.Text;
import util.ResourceHandler;

public class MenuComponent {

    //  public Shape shape;
    protected Text textZone;
    // private ConstTexture texture;
    String txt;
    private Sprite sprite;


    public MenuComponent(String title, Sprite sprite) {
        if (sprite.getTexture() == null) {
            System.out.println(title + sprite.getTexture());
        } else {
            this.sprite = sprite;
            this.textZone = new Text(ResourceHandler.getFont("default"), title);
            setPosition(sprite.getBounds().l, sprite.getBounds().t);
            //sprite.setFillColor(Color.White);
            sprite.setTextureRect(sprite.getBounds().l, sprite.getBounds().t, sprite.getBounds().w, sprite.getBounds().h);
            textZone.setFillColor(Color.Black);
txt=title;
        }
    }

    /* public Shape getShape() {
         return sprite;
     }
 */
    public Shape getText() {
        return textZone;
    }

    public void setPosition(float x, float y) {

        this.sprite.setPosition(x, y);
        this.textZone.setPosition(x, y);

    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public String getString() {return txt;}

}
