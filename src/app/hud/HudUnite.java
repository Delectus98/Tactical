package app.hud;

import Graphics.Sprite;
import Graphics.Text;
import app.Game;
import app.Player;
import app.Unite;
import System.*;
import app.actions.ActionManager;
import app.actions.ShootingManager;
import util.GameInput;
import util.ResourceHandler;

import java.io.IOException;


public class HudUnite
{
    Unite selected;
    Player player;
    GameInput input;
    Game game;

    Sprite imgUnit;
    Sprite back;
    Text hp;
    Text pa;
    boolean lowered;
    Sprite lower;

    //todo more actions for later
    //better graphics
    Text tirer;
    ActionManager selectedAction;

    private ActionManager tir;
    private ActionManager move;

    public HudUnite(Player player, Unite unite, GameInput input, Game game)
    {
        this.player = player;
        this.selected = unite;
        this.input = input;
        this.game = game;

        tir = new ShootingManager(selected, game, input);
        try
        {
            lower = new Sprite(ResourceHandler.loadTexture("res/hud/lower3232.png", "lower"));
            back = new Sprite(ResourceHandler.loadTexture("res/hud/fondHudUnite.png", "spriteeeee"));

            lower.setPosition(64, input.getFrameRectangle().h - 32);
            back.setPosition(lower.getPosition().x, input.getFrameRectangle().h - 128);

            imgUnit = new Sprite(unite.getSprite().getTexture());
            imgUnit.setTextureRect(0, 0, 64, 64);
            imgUnit.setPosition(back.getPosition().x + 10, back.getPosition().y + 10);

            hp = new Text(ResourceHandler.getFont("default"), "HP: " + unite.getHp());
            hp.setPosition(imgUnit.getPosition().x + 64 + 10, imgUnit.getPosition().y + 10);
            pa = new Text(ResourceHandler.getFont("default"), "PA: " + unite.getSparePoints());
            pa.setPosition(hp.getPosition().x, hp.getPosition().y + 25);

            tirer = new Text(ResourceHandler.getFont("default"), "A: TIRER");
            tirer.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

        } catch (IOException e0)
        {

        }
    }

    public void draw(RenderTarget target)
    {
        //todo position adaptée
        if (selected != null)
        {
            if (!lowered)
            {
                target.draw(back);
                target.draw(imgUnit);
                target.draw(hp);
                target.draw(pa);
                if (player.getUnites().contains(selected))
                {
                    if (tir.isAvailable())
                    {
                        target.draw(tirer);
                    }else
                    {
                        target.draw(tirer, ResourceHandler.getShader("grey"));
                    }
                }
            }
            target.draw(lower);
        }
    }

    //todo gérer selection actions
    //todo voir setSelectedUnite si ca convient
    public void update(ConstTime time)
    {
        if (input.isLeftReleased())
        {
            if (lower.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y))
            {
                this.lowered = !this.lowered;
            } else if (tirer.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)
                    && !lowered && tir.isAvailable())
            {
                //on passe en action de tire
                selectedAction = tir;
                //on reduit le hud
                this.lowered = true;
            }
        }
    }

    public void setSelectedUnite(Unite unit)
    {
        this.selected = unit;
        imgUnit.setTexture(unit.getSprite().getTexture(), false);
    }

    public ActionManager getSelectedAction()
    {
        return selectedAction;
    }

    //Si on annule/fini l'action de tir, on reset la value de selectedAction
    public void resetSelectedAction()
    {
        this.selectedAction = null; //or MovingManager
    }
}
