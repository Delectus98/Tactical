package app.actions;

import Graphics.*;
import System.*;
import app.Game;
import app.Player;
import app.Unite;
import util.GameInput;
import util.Pathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MovingManager extends ActionManager {
    private Pathfinder finder;
    private HashMap<Vector2i, Vector2i> possiblePaths;
    private Vector2i target = null;
    private List<Shape> availableTiles;
    private RectangleShape touched;
    private Vector2i selected = null;
    private GameInput input;

    private ArrayList<Unite> all;
    private ArrayList<Unite> enemies;

    /**
     * Creates an Action Manager
     *
     * @param user launcher
     * @param game context
     */
    public MovingManager(Player p, Unite user, Game game, GameInput input) {
        super(p, user, game);

        this.input = input;

        finder = new Pathfinder(game.getMap());
        all = new ArrayList<>();
        Arrays.stream(game.getPlayers()).map(Player::getUnites).forEach(all::addAll);
        enemies = new ArrayList<>();
        Arrays.stream(game.getPlayers()).filter(p2 -> p2 != p).map(Player::getUnites).forEach(enemies::addAll);
        System.out.println("enemies:"+enemies.size());
        ArrayList<Vector2i> visibles = new ArrayList<>(game.getCurrentVisibles());
        possiblePaths = finder.possiblePath(user, enemies, visibles);
        System.out.println("paths:"+possiblePaths.size());
        System.out.println("paths:"+possiblePaths);

        availableTiles = new ArrayList<>();

        possiblePaths.forEach((v1, v2) -> {
            Shape s = new RectangleShape(v1.x*64, v1.y*64, 64, 64);
            s.setFillColor(new Color(0.3f,1.0f,1.f,0.5f));
            availableTiles.add(s);
        });

        touched = new RectangleShape(user.getMapPosition().x*64,user.getMapPosition().x*64,64,64);
    }


    @Override
    public void updatePreparation(ConstTime time) {
        if (input.getFrameRectangle().contains(input.getMousePosition().x, input.getMousePosition().y)) {
            Vector2i tile = new Vector2i((int)(input.getMousePositionOnMap().x / 64), (int)(input.getMousePositionOnMap().y / 64));
            if (new ArrayList<>(possiblePaths.keySet()).contains(tile)) {
                touched.setPosition(tile.x * 64, tile.y * 64);
            }
            if (input.isLeftReleased()) {
                selected = tile;
            }
        }
    }

    @Override
    public void drawAboveFloor(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveStruct(RenderTarget target) {
        availableTiles.forEach(target::draw);
        target.draw(touched);
    }

    @Override
    public void drawAboveEntity(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveHUD(RenderTarget target) {
        ;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public Action build() {
        return new Moving(super.player, finder, possiblePaths, super.unite, all, enemies, target, 85.f);
    }
}
