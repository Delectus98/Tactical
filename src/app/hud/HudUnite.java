package app.hud;

import Graphics.Sprite;
import Graphics.Text;
import app.Game;
import app.Player;
import app.Unite;
import System.*;
import app.actions.ActionManager;
import app.actions.MovingManager;
import app.actions.ShootingManager;
import util.GameInput;
import util.ResourceHandler;

import java.io.IOException;


public class HudUnite
{
    private Unite selected;
    private Player player;
    private GameInput input;
    private Game game;

    private Sprite imgUnit;
    private Sprite back;
    private Text hp;
    private Text pa;
    private boolean lowered;
    private Sprite lower;

    private boolean clicked;

    //todo more actions for later
    //better graphics
    private ActionManager selectedAction;

    private ShootingManager tir1;
    private Text tirer1;
    private ShootingManager tir2;
    private Text tirer2;
    private ShootingManager tir3;
    private Text tirer3;
    private ActionManager move;

    public HudUnite(Player player, Unite unite, GameInput input, Game game)
    {
        this.player = player;
        this.selected = unite;
        this.input = input;
        this.game = game;

        if (player.getUnites().contains(selected))
        {
            tir1 = new ShootingManager(player, selected, selected.getPrimary(), game, input);
            tir2 = new ShootingManager(player, selected, selected.getSecondary(), game, input);
            tir3 = new ShootingManager(player, selected, selected.getMelee(), game, input);
            move = new MovingManager(player, selected, game, input);
            selectedAction = move;
        }

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

            tirer1 = new Text(ResourceHandler.getFont("default"), "1: TIRER");
            tirer1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

            tirer2 = new Text(ResourceHandler.getFont("default"), "2:TIRER");
            tirer2.setPosition(tirer1.getPosition().x, tirer1.getPosition().y + 64);

            tirer3 = new Text(ResourceHandler.getFont("default"), "3:TIRER");
            tirer3.setPosition(tirer2.getPosition().x, tirer2.getPosition().y + 64);

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
                    if (tir1.isAvailable())
                    {
                        target.draw(tirer1);
                    } else
                    {
                        target.draw(tirer1, ResourceHandler.getShader("grey"));
                    }
                    if (tir2.isAvailable())
                    {
                        target.draw(tirer2);
                    } else
                    {
                        target.draw(tirer2, ResourceHandler.getShader("grey"));
                    }
                    if (tir3.isAvailable())
                    {
                        target.draw(tirer3);
                    } else
                    {
                        target.draw(tirer3, ResourceHandler.getShader("grey"));
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
                clicked = true;
            } else if (tirer1.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)
                    && !lowered && tir1.isPossible())
            {
                //on passe en action de tire
                selectedAction = tir1;
                //on reduit le hud
                this.lowered = true;
                System.out.println("SELECTED TIR1");
                clicked = true;
            }else if (tirer2.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)
                    && !lowered && tir2.isPossible())
            {
                selectedAction = tir2;
                this.lowered = true;
                clicked = true;
            } else if (tirer3.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)
                    && !lowered && tir3.isPossible())
            {
                selectedAction = tir3;
                this.lowered = true;
                clicked = true;
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

    public boolean isClicker()
    {
        return clicked;
    }
}
