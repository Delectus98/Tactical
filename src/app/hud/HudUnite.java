package app.hud;

import Graphics.Color;
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
    private Sprite avtFrame;
    private Sprite avtBack;
    private Sprite back;
    private Sprite backFrame;
    private Text hp;
    private Text pa;
    private boolean lowered;
    private Sprite lower;
    private Sprite cancelAction;

    private boolean clicked;

    //todo more actions for later
    //better graphics
    private ActionManager selectedAction;

    private ShootingManager action1;
    private Sprite shoot1;
    private Text cost1;
    private ShootingManager action2;
    private Sprite shoot2;
    private Text cost2;
    private ShootingManager action3;
    private Sprite shoot3;
    private Text cost3;
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
            action1 = new ShootingManager(player, selected, game, input, selected.getPrimary());
            cost1 = new Text(ResourceHandler.getFont("default"), ""+action1.getCost());
            cost1.setFillColor(Color.Blue);
            action2 = new ShootingManager(player, selected, game, input, selected.getSecondary());
            cost2 = new Text(ResourceHandler.getFont("default"), ""+action2.getCost());
            cost2.setFillColor(Color.Blue);
            action3 = new ShootingManager(player, selected, game, input, selected.getMelee());
            cost3 = new Text(ResourceHandler.getFont("default"), ""+action3.getCost());
            cost3.setFillColor(Color.Blue);
            move = new MovingManager(player, selected, game, input);
            selectedAction = move;
        }
            cancelAction = new Sprite(ResourceHandler.getTexture("skip"));
            lower = new Sprite(ResourceHandler.getTexture("lower"));
            lower.setTextureRect(32, 0, 32, 32);
            back = new Sprite(ResourceHandler.getTexture("uiBack"));

            backFrame = new Sprite(ResourceHandler.getTexture("backFrame"));
            avtFrame = new Sprite(ResourceHandler.getTexture("avtFrame"));
            avtBack = new Sprite(ResourceHandler.getTexture("avtBack"));

            lower.setPosition(64, input.getFrameRectangle().h - 32);
            back.setPosition(0, 0);
            cancelAction.setPosition(lower.getPosition().x + back.getBounds().w, input.getFrameRectangle().h - 32);

            imgUnit = new Sprite(unite.getAvatarTexture());
            //imgUnit.setTextureRect(rect.l, rect.t, rect.w, rect.h);
            imgUnit.setPosition(back.getPosition().x + 10, back.getPosition().y + 10);

            hp = new Text(ResourceHandler.getFont("default"), "HP: " + unite.getHp());
            hp.setFillColor(Color.Red);
            hp.setPosition(imgUnit.getPosition().x + 64 + 10, imgUnit.getPosition().y + 10);
            pa = new Text(ResourceHandler.getFont("default"), "PA: " + unite.getSparePoints());
            pa.setFillColor(Color.Blue);
            pa.setPosition(hp.getPosition().x, hp.getPosition().y + 25);

            shoot1 = new Sprite(ResourceHandler.getTexture("shoot1"));
            shoot1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

            shoot2 = new Sprite(ResourceHandler.getTexture("shoot2"));
            shoot1.setPosition(hp.getPosition().x + 64, hp.getPosition().y);

            shoot3 = new Sprite(ResourceHandler.getTexture("knife"));
            shoot3.setPosition(shoot2.getPosition().x, shoot2.getPosition().y + 25);

    }

    public void draw(RenderTarget target)
    {
        //todo position adaptée
        if (selected != null)
        {
            if (!lowered)
            {
                target.draw(back);
                target.draw(backFrame);
                target.draw(avtBack);
                target.draw(imgUnit);
                target.draw(avtFrame);
                target.draw(hp);
                target.draw(pa);
                if (player.getUnites().contains(selected))
                {
                    if (action1.isAvailable())
                    {
                        target.draw(shoot1);
                        target.draw(cost1);
                    } else
                    {
                        target.draw(shoot1, ResourceHandler.getShader("grey"));
                        target.draw(cost1);
                    }
                    if (action2.isAvailable())
                    {
                        target.draw(shoot2);
                        target.draw(cost2);
                    } else
                    {
                        target.draw(shoot2, ResourceHandler.getShader("grey"));
                        target.draw(cost2);
                    }
                    if (action3.isAvailable())
                    {
                        target.draw(shoot3);
                        target.draw(cost3);
                    } else
                    {
                        target.draw(shoot3, ResourceHandler.getShader("grey"));
                        target.draw(cost3);
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
        back.setPosition(lower.getPosition().x+32, input.getFrameRectangle().h - 128);
        backFrame.setPosition(back.getPosition().x, back.getPosition().y);

        imgUnit.setPosition(back.getPosition().x + 10, back.getPosition().y + 10);
        avtBack.setPosition(imgUnit.getPosition().x, imgUnit.getPosition().y);
        avtFrame.setPosition(imgUnit.getPosition().x, imgUnit.getPosition().y);

        hp.setPosition(imgUnit.getPosition().x + 128 + 10, imgUnit.getPosition().y + 5);
        pa.setPosition(hp.getPosition().x, hp.getPosition().y + 25);

        shoot1.setPosition(hp.getPosition().x - 10, hp.getPosition().y + 61);
        cost1.setPosition(shoot1.getPosition().x + 38, shoot1.getPosition().y + 4);
        shoot2.setPosition(shoot1.getPosition().x + 60, shoot1.getPosition().y);
        cost2.setPosition(shoot2.getPosition().x + 38, shoot2.getPosition().y + 4);
        shoot3.setPosition(shoot2.getPosition().x + 60, shoot2.getPosition().y);
        cost3.setPosition(shoot3.getPosition().x + 38, shoot3.getPosition().y + 4);

        cancelAction.setPosition(lower.getPosition().x, lower.getPosition().y);

        clicked = false;

        pa.setString("PA: " + selected.getSparePoints());
        hp.setString("HP: " + selected.getHp());
        //we check if the unit is part of the player's units
        if (input.isLeftReleased() && player.getUnites().contains(selected))
        {
            clicked = back.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered;

            if (cancelAction.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y))
            {
                resetSelectedAction();
                clicked = true;
                this.lowered = false;
            }else if (lower.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y))
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
            } else if (shoot1.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                //on passe en action de actione
                selectedAction = action1;
                //on reduit le hud
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            } else if (shoot2.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                selectedAction = action2;
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            } else if (shoot3.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y) && !lowered)
            {
                selectedAction = action3;
                this.lowered = true;
                lower.setTextureRect(0,0,32,32);
                clicked = true;
            }
        }
    }

    public void setSelectedUnite(Unite unit)
    {
        this.selected = unit;
        imgUnit.setTexture(unit.getAvatarTexture(), false);
        resetSelectedAction();
    }

    public ActionManager getSelectedAction()
    {
        return selectedAction;
    }

    //Si on annule/fini l'action de tir, on reset la value de selectedAction
    public void resetSelectedAction()
    {
        action1 = new ShootingManager(player, selected, game, input, selected.getPrimary());
        cost1 = new Text(ResourceHandler.getFont("default"), ""+action1.getCost());
        cost1.setFillColor(Color.Blue);
        action2 = new ShootingManager(player, selected, game, input, selected.getSecondary());
        cost2 = new Text(ResourceHandler.getFont("default"), ""+action2.getCost());
        cost2.setFillColor(Color.Blue);
        action3 = new ShootingManager(player, selected, game, input, selected.getMelee());
        cost3 = new Text(ResourceHandler.getFont("default"), ""+action3.getCost());
        cost3.setFillColor(Color.Blue);
        move = new MovingManager(player, selected, game, input);
        selectedAction = move;
    }

    public boolean isClicked()
    {
        return clicked;
    }
}
