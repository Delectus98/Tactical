package app.actions;

import Graphics.*;
import System.*;
import System.IO.AZERTYLayout;
import app.Game;
import app.Player;
import app.Unite;
import app.Weapon;
import util.GameInput;
import util.MapUtil;
import util.ResourceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShootingManager extends ActionManager
{
    // map
    private List<RectangleShape> rectangles;
    private RectangleShape touched;
    private RectangleShape selected;
    private List<Vector2i> selectable;
    private Weapon selectedWeapon = null;
    private int damage = 0;
    private Vector2i p1 = null;
    private Vector2i p2 = null;
    private GameInput input = null;
//    private boolean selectMode = true;
    // hud select weapon
//    private RectangleShape hudRect;
//    private Text fstWeapon;
//    private Text sndWeapon;
//    private Text thdWeapon;
    // hud back to selection
//    private Text resume;
//    private RectangleShape resumeBox;

    public ShootingManager(Player p, Unite user, Weapon weapon, Game game, GameInput input)
    {
        super(p, user, game);
        this.input = input;
        this.selectedWeapon = weapon;

        p1 = user.getMapPosition();

        selectable = MapUtil.getVisibles(user, game.getMap());
        selectable = selectable.stream().filter(v2i -> !game.getCurrentVisibles().contains(v2i)).collect(Collectors.toList());

        rectangles = new ArrayList<>();
        selectable.forEach(s ->
        {
            RectangleShape shape = new RectangleShape(s.x * 64, s.y * 64, 64, 64);
            shape.setFillColor(new Color(0.1f, 0.5f, 1.f, 0.6f));
            rectangles.add(shape);
        });

        touched = new RectangleShape(64, 64);
        touched.setPosition(p1.x * 64, p1.y * 64);

        selected = new RectangleShape(64, 64);
        selected.setFillColor(Color.Transparent);
        selected.setPosition(p1.x * 64, p1.y * 64);
    }

    @Override
    public void updatePreparation(ConstTime time)
    {
        Vector2f mouse = input.getMousePosition();

        if (true)
        {

            if (input.getFrameRectangle().contains(mouse.x, mouse.y))
            {
                mouse = input.getMousePositionOnMap();

                Vector2i tile = new Vector2i(mouse.mul(1.f / 64.f));

                if (input.isLeftReleased())
                {

                    if (!tile.equals(p1) && selectable.contains(tile))
                    {
                        p2 = tile;
                        selected.setFillColor(Color.White);
                        selected.setPosition(tile.x * 64, tile.y * 64);
                    }
                }

                if (selectable.contains(tile) && !tile.equals(p1))
                {
                    touched.setFillColor(new Color(0.9f, 0.01f, 0.01f, 0.8f));
                } else
                {
                    touched.setFillColor(new Color(0.09f, 0.1f, 0.9f, 0.8f));
                }

                touched.setPosition(tile.x * 64, tile.y * 64);

            }
        }
    }

    @Override
    public void drawAboveFloor(RenderTarget target)
    {

    }

    @Override
    public void drawAboveStruct(RenderTarget target)
    {
        for (RectangleShape rectangle : rectangles)
        {
            target.draw(rectangle);
        }
        target.draw(selected);
        target.draw(touched);
    }

    @Override
    public void drawAboveEntity(RenderTarget target)
    {
        ;
    }

    @Override
    public void drawAboveHUD(RenderTarget target)
    {

    }

    @Override
    public int getCost()
    {
        int cost = selectedWeapon.getCost();
        if (cost < 0)
            cost = super.unite.getSparePoints();

        return cost;
    }

    @Override
    public boolean isAvailable()
    {
        return (selectedWeapon.getAmmunition() > 0 || selectedWeapon.getAmmunition() == -1) && p2 != null /*&& selectedWeapon.isInRange((int)(p2.sum(p1.neg())).length())*/;
    }

    public boolean isPossible()
    {
        return selectedWeapon.getAmmunition() > 0 || selectedWeapon.getAmmunition() == -1;
    }

    @Override
    public Action build()
    {
        //selectedWeapon.getImpactZone(p1, p2, super.game.getMap()).chance(0.5f);
        return new Shooting(selectedWeapon.getImpactZone(p1, p2, super.game.getMap()), selectedWeapon.buildProjectile(p1, p2), selectedWeapon.getCost());
    }

    public Weapon getSelectedWeapon()
    {
        return selectedWeapon;
    }

}
