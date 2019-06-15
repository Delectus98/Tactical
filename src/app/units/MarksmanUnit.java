package app.units;

import Graphics.ConstTexture;
import Graphics.Sprite;
import Graphics.Vector2i;
import app.Team;
import app.Unite;
import app.Weapon;
import app.weapon.CombatRifle;
import System.*;
import app.weapon.Sniper;
import util.ResourceHandler;

public class MarksmanUnit extends Unite
{
    public MarksmanUnit(Team team)
    {
        this.hp = 20;
        this.fov = 10;
        this.maxActionPoints = 12;
        this.actionPoints = maxActionPoints;
        this.primary = new Sniper();
        this.secondary = new CombatRifle();
        //todo Melee aussi.
        this.melee = secondary;
        //TODO : Créer une arme de melee et la mettre
        this.melee = this.primary;
        //todo dans le Main:    +   change Texture texture to ConstTexture texture;
        //ResourceHandler.loadTexture("Sprites/Characterrs/Walk/oldMan.png", "oldMan");
        //ResourceHandler.loadTexture("Sprites/Characterrs/Walk/otherChar.png", "otherChar");
        //puis load
        switch (team)
        {
            case APE:
                this.spritesheet = ResourceHandler.getTexture("oldMan");
                this.sprite = new Sprite(spritesheet);
                sprite.setTextureRect(0, 0, 64, 64);
                break;
            case MAN:
                this.spritesheet = ResourceHandler.getTexture("otherChar");
                this.sprite = new Sprite(spritesheet);
                sprite.setTextureRect(0, 0, 64, 64);
                break;
        }
    }

    @Override
    public boolean isDead()
    {
        return this.getHp() <= 0;
    }

    @Override
    public short getFov()
    {
        return this.fov;
    }

    @Override
    public Weapon getPrimary()
    {
        return this.primary;
    }

    @Override
    public Weapon getSecondary()
    {
        return this.secondary;
    }

    @Override
    public Weapon getMelee()
    {
        return this.melee;
    }

    @Override
    public Sprite getSprite()
    {
        return this.sprite;
    }

    @Override
    public void setMapPosition(Vector2i coords)
    {
        this.position = coords;
    }

    @Override
    public Vector2i getMapPosition()
    {
        return this.position;
    }

    @Override
    public void takeDamages(int amount)
    {
        this.hp = (short) (this.hp - amount);
    }

    @Override
    public void removePA(short cost)
    {
        this.actionPoints -= cost;
    }

    @Override
    public short getMaximumPoints()
    {
        return maxActionPoints;
    }

    @Override
    public short getSparePoints()
    {
        return this.actionPoints;
    }

    @Override
    public void resetTurn()
    {
        this.actionPoints = this.maxActionPoints;
    }

    @Override
    public ConstTexture getSpriteSheet()
    {
        return this.spritesheet;
    }

    @Override
    public void setTeam(Team team)
    {
        this.team = team;
    }

    @Override
    public Team getTeam()
    {
        return this.team;
    }

    @Override
    public void draw(RenderTarget target)
    {
        target.draw(this.sprite);
    }
}

