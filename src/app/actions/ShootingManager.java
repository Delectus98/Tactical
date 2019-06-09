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

public class ShootingManager extends ActionManager {
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
    private boolean selectMode = true;
    // hud select weapon
    private RectangleShape hudRect;
    private Text fstWeapon;
    private Text sndWeapon;
    private Text thdWeapon;
    // hud back to selection
    private Text resume;
    private RectangleShape resumeBox;

    public ShootingManager(Player p, Unite user, Game game, GameInput input) {
        super(p, user, game);
        this.input = input;

        p1 = user.getMapPosition();

        selectable = MapUtil.getVisibles(user, game.getMap());
        selectable = selectable.stream().filter(v2i -> !game.getCurrentVisibles().contains(v2i)).collect(Collectors.toList());

        rectangles = new ArrayList<>();
        selectable.forEach(s -> {
            RectangleShape shape = new RectangleShape(s.x*64, s.y*64, 64, 64);
            shape.setFillColor(new Color(0.1f,0.5f, 1.f, 0.6f));
            rectangles.add(shape);
        });

        touched = new RectangleShape(64, 64);
        touched.setPosition(p1.x * 64, p1.y * 64);

        selected = new RectangleShape(64, 64);
        selected.setFillColor(Color.Transparent);
        selected.setPosition(p1.x * 64, p1.y * 64);

        hudRect = new RectangleShape(500,500,500, 500);
        fstWeapon = new Text(ResourceHandler.getFont("default"), /*super.unite.getPrimary().toString()*/"1. First Weapon");
        sndWeapon = new Text(ResourceHandler.getFont("default"), /*super.unite.getPrimary().toString()*/"2. Second Weapon");
        thdWeapon = new Text(ResourceHandler.getFont("default"), /*super.unite.getPrimary().toString()*/"3. Third Weapon");
        resume = new Text(ResourceHandler.getFont("default"), /*super.unite.getPrimary().toString()*/"Resume Selection");
        resume.setPosition(20, 50);
        resumeBox = new RectangleShape(20, 50, resume.getBounds().w, resume.getBounds().h);
        resumeBox.setFillColor(new Color(0.5f,0.5f,0.5f,0.9f));
    }

    @Override
    public void updatePreparation(ConstTime time) {
        Vector2f mouse = input.getMousePosition();
        if (selectMode) {
            selectMode = false;

            hudRect.setPosition(input.getFrameRectangle().w / 2.f - hudRect.getBounds().w /2.f, input.getFrameRectangle().h / 2.f - hudRect.getBounds().h /2.f);
            fstWeapon.setPosition(input.getFrameRectangle().w / 2.f - fstWeapon.getBounds().w /2.f, input.getFrameRectangle().h / 2.f - 100);
            sndWeapon.setPosition(input.getFrameRectangle().w / 2.f - sndWeapon.getBounds().w /2.f, input.getFrameRectangle().h / 2.f);
            thdWeapon.setPosition(input.getFrameRectangle().w / 2.f - thdWeapon.getBounds().w /2.f, input.getFrameRectangle().h / 2.f + 100);
            fstWeapon.setFillColor(Color.White);
            sndWeapon.setFillColor(Color.White);
            thdWeapon.setFillColor(Color.White);

            if (input.getKeyboard().isKeyPressed(AZERTYLayout.PAD_1.getKeyID()) || input.getKeyboard().isKeyPressed(AZERTYLayout.NUM_1.getKeyID())) {
                selectedWeapon = super.unite.getPrimary();
            } else if (input.getKeyboard().isKeyPressed(AZERTYLayout.PAD_2.getKeyID()) || input.getKeyboard().isKeyPressed(AZERTYLayout.NUM_2.getKeyID())) {
                selectedWeapon = super.unite.getSecondary();
            } else if (input.getKeyboard().isKeyPressed(AZERTYLayout.PAD_3.getKeyID()) || input.getKeyboard().isKeyPressed(AZERTYLayout.NUM_3.getKeyID())) {
                selectedWeapon = super.unite.getMelee();
            }

            else if (input.isLeftReleased() && fstWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                selectedWeapon = super.unite.getPrimary();
            } else if (input.isLeftReleased() && sndWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                selectedWeapon = super.unite.getSecondary();
            } else if (input.isLeftReleased() && thdWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                selectedWeapon = super.unite.getMelee();
            } else {
                selectMode = true;
            }


            if (fstWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                fstWeapon.move(10,0);
                if (input.isLeftPressed()) fstWeapon.setFillColor(Color.Yellow);
                else fstWeapon.setFillColor(Color.Green);
            } else if (sndWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                sndWeapon.move(10,0);
                if (input.isLeftPressed()) sndWeapon.setFillColor(Color.Yellow);
                else sndWeapon.setFillColor(Color.Green);
            } else if (thdWeapon.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                thdWeapon.move(10,0);
                if (input.isLeftPressed()) thdWeapon.setFillColor(Color.Yellow);
                else thdWeapon.setFillColor(Color.Green);
            }

            hudRect.setPosition(input.getFrameRectangle().w / 2.f, input.getFrameRectangle().h / 2.f);
            hudRect.setOrigin(hudRect.getBounds().w / 2.f, hudRect.getBounds().h / 2.f);

            if (input.getFrameRectangle().contains(mouse.x, mouse.y) && hudRect.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                hudRect.setFillColor(new Color(0.5f,0.5f, 0.5f, 0.9f));
            } else {
                hudRect.setFillColor(new Color(0.5f,0.5f, 0.5f, 0.5f));
            }
        }

        if (!selectMode) {
            if (input.getKeyboard().isKeyPressed(AZERTYLayout.HOME.getKeyID())) {
                selectMode = true;
            } else {
                // la souris est dans la fenetre et viewport du joueur
                if (input.getFrameRectangle().contains(mouse.x, mouse.y)) {
                    mouse = input.getMousePositionOnMap();

                    Vector2i tile = new Vector2i(mouse.mul(1.f / 64.f));

                    if (resume.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                        if (input.isLeftPressed()) resume.setFillColor(Color.Yellow);
                        else resume.setFillColor(Color.Green);
                    } else {
                        resume.setFillColor(Color.White);
                    }

                    if (input.isLeftReleased()) {

                        if (resume.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePositionOnHUD().y)) {
                            selectMode = true;
                        } else if (!tile.equals(p1) && selectable.contains(tile)) {
                            p2 = tile;
                            selected.setFillColor(Color.White);
                            selected.setPosition(tile.x * 64, tile.y * 64);
                        }
                    }

                    if (selectable.contains(tile) && !tile.equals(p1)) {
                        touched.setFillColor(new Color(0.9f, 0.01f, 0.01f, 0.8f));
                    } else {
                        touched.setFillColor(new Color(0.09f, 0.1f, 0.9f, 0.8f));
                    }

                    touched.setPosition(tile.x * 64, tile.y * 64);
                }
            }
        }
    }

    @Override
    public void drawAboveFloor(RenderTarget target)
    {
        ;
    }

    @Override
    public void drawAboveStruct(RenderTarget target)
    {
        if (!selectMode) for (RectangleShape rectangle : rectangles) {
            target.draw(rectangle);
        }
        if (!selectMode) target.draw(selected);
        if (!selectMode) target.draw(touched);
    }

    @Override
    public void drawAboveEntity(RenderTarget target)
    {
        ;
    }

    @Override
    public void drawAboveHUD(RenderTarget target)
    {
        if (selectMode) target.draw(hudRect);
        if (selectMode) target.draw(thdWeapon);
        if (selectMode) target.draw(fstWeapon);
        if (selectMode) target.draw(sndWeapon);
        if (!selectMode) target.draw(resumeBox);
        if (!selectMode) target.draw(resume);
    }

    @Override
    public int getCost() {
        int cost = selectedWeapon.getCost();
        if (cost < 0)
            cost = super.unite.getSparePoints();

        return cost;
    }

    @Override
    public boolean isAvailable() {
        return selectedWeapon != null && (selectedWeapon.getAmmunition() > 0 || selectedWeapon.getAmmunition() == -1) && p2 != null /*&& selectedWeapon.isInRange((int)(p2.sum(p1.neg())).length())*/;
    }

    @Override
    public Action build()
    {
        //selectedWeapon.getImpactZone(p1, p2, super.game.getMap()).chance(0.5f);
        return new Shooting(selectedWeapon.getImpactZone(p1, p2, super.game.getMap()), selectedWeapon.buildProjectile(p1, p2), selectedWeapon.getCost());
    }

}
