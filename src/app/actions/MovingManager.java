package app.actions;

import Graphics.*;
import System.*;
import app.Game;
import app.Player;
import app.Unite;
import org.lwjgl.opengl.GL20;
import util.GameInput;
import util.Pathfinder;
import util.ResourceHandler;

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
    private Text costMsg;
    private GameInput input;
    private List<Vector2i> tilePath = new ArrayList<>();

    private ArrayList<Unite> allies;
    private ArrayList<Unite> enemies;

    private float totalElapsed = 0;

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
        allies = new ArrayList<>();
        Arrays.stream(game.getPlayers()).filter(p2 -> p2 == p).map(Player::getUnites).forEach((c) -> {
            allies.addAll(c);
            allies.removeIf(Unite::isDead);
        });
        enemies = new ArrayList<>();
        Arrays.stream(game.getPlayers()).filter(p2 -> p2 != p).map(Player::getUnites).forEach((c) -> {
            enemies.addAll(c);
            enemies.removeIf(Unite::isDead);
        });
        ArrayList<Vector2i> visibles = new ArrayList<>(game.getCurrentVisibles());
        possiblePaths = finder.possiblePath(user, Math.max(0, user.getSparePoints() - 2), enemies, visibles);

        availableTiles = new ArrayList<>();

        possiblePaths.forEach((v1, v2) -> {
            Shape s = new RectangleShape(v1.x*64, v1.y*64, 64, 64);
            s.setFillColor(new Color(0.5f,0.5f,0.5f,0.3f));
            if (!v1.equals(user.getMapPosition())) availableTiles.add(s);
        });

        touched = new RectangleShape(user.getMapPosition().x*64,user.getMapPosition().y*64,64,64);
        touched.setFillColor(new Color(0.f, 1.f, 0.2f, 0.5f));

        costMsg = new Text(ResourceHandler.getFont("default"), "0");
        costMsg.setScale(1.4f, 1.f);
        costMsg.setPosition(user.getMapPosition().x*64,user.getMapPosition().y*64);
        costMsg.setFillColor(Color.Green);
    }

    @Override
    public void updatePreparation(ConstTime time) {
        totalElapsed += time.asSeconds();

        if (input.getFrameRectangle().contains(input.getMousePosition().x, input.getMousePosition().y)) {
            Vector2i tile = new Vector2i((int)(input.getMousePositionOnMap().x / 64), (int)(input.getMousePositionOnMap().y / 64));
            if (new ArrayList<>(possiblePaths.keySet()).contains(tile)) {
                touched.setPosition(tile.x * 64, tile.y * 64);
                costMsg.setPosition(tile.x * 64, tile.y * 64);
                if (!possiblePaths.isEmpty()) {
                    tilePath = finder.pathfindFullList(possiblePaths, allies, unite.getMapPosition(), new Vector2i(touched.getPosition().mul(1.f/64.f)));
                    availableTiles.forEach(t -> {
                        if (!tilePath.contains(new Vector2i(t.getPosition().mul(1.f/64.f))))
                            t.setFillColor(new Color(0.5f,0.5f,0.5f,0.3f));
                        else
                            t.setFillColor(new Color(0.5f,1.f,0.5f,0.4f));
                    });
                }

                costMsg.setString(this.getCost()+"");
                costMsg.setFillColor(new Color((float)Math.exp(1.f - (float)this.getCost() / (float)unite.getSparePoints()), 0.5f - ((float)this.getCost() / (float)unite.getSparePoints()), 0.F, 1.f));

                if (input.isLeftReleased()) {
                    target = tile;
                }
            }
        }
    }

    @Override
    public void drawAboveFloor(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveStruct(RenderTarget target) {
        ConstShader shining = ResourceHandler.getShader("shining");
        shining.bind();
        GL20.glUniform1f(shining.getUniformLocation("elapsed"), totalElapsed*70);
        GL20.glUniform1i(shining.getUniformLocation("modulus"), 64);
        GL20.glUniform1f(shining.getUniformLocation("shining"), 5);
        availableTiles.forEach(s -> target.draw(s, shining));
        target.draw(touched, shining);
        target.draw(costMsg, shining);
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
        return Math.max(0,tilePath.size()) + (tilePath.isEmpty() ? (0):(1));
    }

    @Override
    public boolean isAvailable() {
        return target != null && player.getUnites().stream().noneMatch(u -> !u.isDead() && u.getMapPosition().equals(target));
    }

    @Override
    public Action build() {
        //TODO unite.removePA(this.getCost());
        Action action = new Moving(super.player, finder, possiblePaths, super.unite, allies, enemies, target, 85.f);
        action.init(game);
        return action;
    }
}
