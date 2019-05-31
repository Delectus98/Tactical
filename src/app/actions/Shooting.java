package app.actions;

import System.*;
import Graphics.*;
import app.Game;
import app.Unite;
import app.weapon.Impact;

import java.util.ArrayList;
import java.util.Arrays;

//TODO Need weapon information (projectiles, animation, effect, ...)
public class Shooting extends Action {
    // déroulement
    private transient Sprite projectile;
    private Vector2i firstPos;
    private Vector2i lastPos;
    private Vector2f trajectory;
    private Impact impact;
    private float duration;
    private float elapsed = 0;
    private int damage;
    private int cost;

    public Shooting()
    {
    }

    public Shooting(Vector2i p1, Vector2i p2, int damage, int cost, ConstTime duration)
    {
        this.damage = damage;
        this.cost = cost;

        this.duration = (float)duration.asSeconds();

        this.firstPos = p1;
        this.lastPos = p2;

        trajectory = new Vector2f(lastPos).sum(new Vector2f(firstPos).neg());
    }

    @Override
    public void build(Game context)
    {
        super.build(context);
        projectile = new Sprite();
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public boolean isFinished() {
        return elapsed >= duration;
    }

    @Override
    public void drawAboveHUD(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveEntity(RenderTarget target) {
        target.draw(projectile);
    }

    @Override
    public void drawAboveStruct(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveFloor(RenderTarget target) {
        ;
    }

    @Override
    public void update(ConstTime time) {
        elapsed += time.asSeconds();
        float percent = Math.min(Math.min(elapsed, duration) / duration, 1);
        projectile.setPosition(trajectory.mul(percent).x + firstPos.x * 64, trajectory.mul(percent).y + firstPos.y * 64);

        // si c'est fini
        if (isFinished()) {
            //alors on doit mettre des dégats aux unités concernées
            //super.game.getPlayers().
            ArrayList<Unite> all = new ArrayList<>();
            Arrays.stream(super.game.getPlayers()).forEach(p -> all.addAll(p.getUnites()));
            impact.apply(all);
        }
    }

}
