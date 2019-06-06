package app.actions;

import System.*;
import Graphics.*;
import app.Game;
import app.Unite;
import app.weapon.Impact;
import app.weapon.Projectile;
import jdk.nashorn.internal.objects.annotations.Constructor;

import java.util.ArrayList;
import java.util.Arrays;

//TODO Need weapon information (projectiles, animation, effect, ...)
public class Shooting extends Action {
    // déroulement
    private Impact impact;
    private Projectile projectile;
    private int cost;

    public Shooting()
    {
        // kryo constructor required
    }

    @Override
    public void init(Game context)
    {
        super.init(context);
        projectile.init();
    }

    public Shooting(Impact damage, Projectile projectile, int cost)
    {
        this.cost = cost;

        this.impact = damage;
        this.projectile = projectile;
    }



    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public boolean isFinished() {
        return projectile.hasFinishedHitting();
    }

    @Override
    public void drawAboveHUD(RenderTarget target) {
        ;
    }

    @Override
    public void drawAboveEntity(RenderTarget target) {
        projectile.draw(target);
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
        projectile.update(time);

        // si c'est fini
        if (projectile.hasFinishedHitting()) {
            //alors on doit mettre des dégats aux unités concernées
            //super.game.getPlayers().
            ArrayList<Unite> all = new ArrayList<>();
            Arrays.stream(super.game.getPlayers()).forEach(p -> all.addAll(p.getUnites()));
            impact.apply(all);
        }
    }

}
