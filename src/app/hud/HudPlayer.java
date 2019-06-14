package app.hud;

import Graphics.FloatRect;
import Graphics.Sprite;
import Graphics.Text;
import Graphics.Vector2f;
import app.Game;
import app.Player;
import System.*;
import app.Unite;
import util.GameInput;
import util.MyPair;
import util.ResourceHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HudPlayer
{
    Game game;
    Player player;
    GameInput gameInput;
    HashMap<Unite, MyPair<Sprite, Text>> sprites;
    boolean selected;
    Unite selectedUnit;
    int test = 0;

    /*
    getBounds tu as le rectangle left top width height
setOrigin() (par défaut 0,0) tu le met setOrigin(hudBounds.w / 2.f, hudBounds.h/2.f)
puis setPositon(INPUT.getFrameRectangle().w/2,....)
     */

    public HudPlayer(Game game, Player player, GameInput gameInput)
    {
        this.game = game;
        this.player = player;
        this.gameInput = gameInput;
        this.sprites = new LinkedHashMap<>();
        this.createSprites();

    }

    public void draw(RenderTarget target)
    {
        for(HashMap.Entry<Unite, MyPair<Sprite, Text>> entry : sprites.entrySet())
        {
            target.draw(entry.getValue().getFst());
            target.draw(entry.getValue().getSnd());
        }
    }

    private void createSprites()
    {
        System.out.println("Making sprite list");
        //todo position adapté suivant la taille de la fenêtre
        float decLeft = 1;
        float decHeight = 10;
        for (Unite unit : player.getUnites())
        {
            Sprite tmp = new Sprite(unit.getSprite().getTexture());
            //todo use right values for spritesheets
            FloatRect r = unit.getSprite().getTextureRect();
            tmp.setTextureRect(r.l, r.t, r.w, r.h);
            //            imgUnit.setTextureRect(rect.l, rect.t, rect.w, rect.h);

            tmp.setPosition(decLeft, decHeight);
            Text hp = new Text(ResourceHandler.getFont("default"), "HP: " + unit.getHp());
            hp.setPosition(decLeft + 15, decHeight + 64);

            sprites.put(unit, new MyPair<>(tmp, hp));
            decHeight += 64 + 10;
        }
    }

    public void update(ConstTime time)
    {
        selected = false;
        if (gameInput.isLeftReleased())
        {
            Vector2f mouse = gameInput.getMousePositionOnHUD();
            for (HashMap.Entry<Unite, MyPair<Sprite, Text>> entry : sprites.entrySet())
            {
                if (entry.getValue().getFst().getBounds().contains(mouse.x, mouse.y))
                {
                    selectedUnit = entry.getKey().isDead() ? selectedUnit : entry.getKey();
                    selected = true;
                    break;
                }
            }
        }
        for(Unite unit : player.getUnites())
        {
            MyPair<Sprite, Text> value = sprites.get(unit);
            if (unit.isDead())
            {
                value.getSnd().setString("X DEAD X");
            } else
            {
                value.getSnd().setString("HP: " + unit.getHp());
            }
        }
    }

    public boolean isSelected()
    {
        return selected;
    }

    public Unite getSelectedUnit()
    {
        return selectedUnit;
    }


}
