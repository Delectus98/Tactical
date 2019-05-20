package app.play;

import Graphics.*;
import System.*;
import app.Unite;
import app.Weapon;

public class TestUnite extends Unite {
    private Shape shape;
    private Vector2i tile;

    public TestUnite(){
        shape = new RectangleShape(64, 64);
        shape.setFillColor(Color.Blue);
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public short getHp() {
        return 0;
    }

    @Override
    public short getFov() {
        return 0;
    }

    @Override
    public Weapon getPrimary() {
        return null;
    }

    @Override
    public Weapon getSecondary() {
        return null;
    }

    @Override
    public Weapon getMelee() {
        return null;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void seMapPosition(Vector2i coords) {
        tile = coords.clone();
    }

    @Override
    public Vector2i getMapPosition() {
        return tile;
    }

    @Override
    public void takeDamages(int amount) {

    }

    @Override
    public short getActionPoints() {
        return 0;
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(shape);
    }
}
