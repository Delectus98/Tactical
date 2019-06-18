package app.hud;

import Graphics.FloatRect;
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
    private Sprite cancelAction;

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
        FloatRect rect = unite.getSprite().getTextureRect();

        if (player.getUnites().contains(selected))
        {
            tir1 = new ShootingManager(player, selected, game, input, selected.getPrimary());
            tir2 = new ShootingManager(player, selected, game, input, selected.getSecondary());
            tir3 = new ShootingManager(player, selected, game, input, selected.getMelee());
            move = new MovingManager(player, selected, game, input);
            selectedAction = move;
        }

        try
        {
            cancelAction = new Sprite(ResourceHandler.loadTexture("res/hud/lower3232.png", "cancel"));
            lower = new Sprite(ResourceHandler.loadTexture("Sprites/UI/arr.png", "lower"));
            lower.setTextureRect(32, 0, 32, 32);
            back = new Sprite(ResourceHandler.loadTexture("res/hud/fondHudUnite.png", "spriteeeee"));

            lower.setPosition(64, input.getFrameRectangle().h - 32);
            back.setPosition(lower.getPosition().x, input.getFrameRectangle().h - 128);
            cancelAction.setPosition(lower.getPosition().x + back.getBounds().w, input.getFrameRectangle().h - 32);

            imgUnit = new Sprite(unite.getSprite().getTexture());
            imgUnit.setTextureRect(rect.l, rect.t, rect.w, rect.h);
            imgUnit.setPosition(back.getPosition().x + 10, back.getPosition().y + 10);

            hp = new Text(ResourceHandler.getFont("default"), "HP: " + unite.getHp());
            hp.setPosition(imgUnit.getPosition().x + 64 + 10, imgUnit.getPosition().y + 10);
            pa = new Text(ResourceHandler.getFont("default"), "PA: " + unite.getSparePoints());
            pa.setPosition(hp.getPosition().x, hp.getPosition().y + 25);

            tirer1 = new Text(ResourceHandler.getFont("default"), "1:TIRER");
            tirer1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

            tirer2 = new Text(ResourceHandler.getFont("default"), "2:TIRER");
            tirer1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

            tirer3 = new Text(ResourceHandler.getFont("default"), "3:TIRER");
            tirer3.setPosition(tirer2.getPosition().x, tirer2.getPosition().y + 25);

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
            if (!(selectedAction instanceof MovingManager))
            {
                target.draw(cancelAction);
            }

        }
    }

    //todo gérer selection actions
    //todo voir setSelectedUnite si ca convient
    public void update(ConstTime time)
    {
        lower.setPosition(64, input.getFrameRectangle().h - 32);
        back.setPosition(lower.getPosition().x, input.getFrameRectangle().h - 128);
        imgUnit.setPosition(back.getPosition().x + 10, back.getPosition().y + 10);
        hp.setPosition(imgUnit.getPosition().x + 64 + 10, imgUnit.getPosition().y + 10);
        pa.setPosition(hp.getPosition().x, hp.getPosition().y + 25);

        tirer1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);
        tirer2.setPosition(tirer1.getPosition().x, tirer1.getPosition().y + 25);
        tirer3.setPosition(tirer2.getPosition().x, tirer2.getPosition().y + 25);

        clicked = false;

        pa.setString("PA: " + selected.getSparePoints());
        hp.setString("HP: " + selected.getHp());
        //we check if the unit is part of the player's units
        if (input.isLeftReleased() && player.getUnites().contains(selected))
        {
            clicked = back.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered;

            if (lower.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y))
            {
                this.lowered = !this.lowered;
                if (lowered)
                {
                    lower.setTextureRect(0,0,32,32);
                }else
                {
                    lower.setTextureRect(32,0,32,32);
                }
                clicked = true;
            } else if (tirer1.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                //on passe en action de tire
                selectedAction = tir1;
                //on reduit le hud
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            } else if (tirer2.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                selectedAction = tir2;
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            } else if (tirer3.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                selectedAction = tir3;
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            } else if (cancelAction.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)
            )
            {
                resetSelectedAction();
                clicked = true;
                this.lowered = false;
            }
        }
    }

    public void setSelectedUnite(Unite unit)
    {
        this.selected = unit;
        imgUnit.setTexture(unit.getSprite().getTexture(), false);
        FloatRect r = unit.getSprite().getTextureRect();
        imgUnit.setTextureRect(r.l, r.t, r.w, r.h);
    }

    public ActionManager getSelectedAction()
    {
        return selectedAction;
    }

    //Si on annule/fini l'action de tir, on reset la value de selectedAction
    public void resetSelectedAction()
    {
        this.selectedAction = null; //or MovingManager
        tir1 = new ShootingManager(player, selected, game, input, selected.getPrimary());
        tir2 = new ShootingManager(player, selected, game, input, selected.getSecondary());
        tir3 = new ShootingManager(player, selected, game, input, selected.getMelee());
        move = new MovingManager(player, selected, game, input);
        selectedAction = move;
    }

    public boolean isClicked()
    {
        return clicked;
    }
}
