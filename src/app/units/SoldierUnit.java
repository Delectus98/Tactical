package app.units;

import Graphics.*;
import app.Team;
import app.Unite;
import app.Weapon;
import System.*;
import app.weapon.Blade;
import app.weapon.Grenade;
import app.weapon.MachineGun;
import util.ResourceHandler;



public class SoldierUnit extends Unite
{
    public SoldierUnit(Team team)
    {
        this.hp = 25;
        this.fov = 10;
        this.maxActionPoints = 12;
        this.actionPoints = maxActionPoints;
        this.primary = new MachineGun();
        this.secondary = new Grenade();
        this.melee = new Blade();
        this.hpText = new Text(ResourceHandler.getFont("default"), "");
        hpText.setFillColor(Color.Green);

        //hptext.setScale(.4f,.4f);
        //todo dans le Main:    +   change Texture texture to ConstTexture texture;
        //ResourceHandler.loadTexture("Sprites/Characterrs/Walk/gurl.png", "gurl");
        //ResourceHandler.loadTexture("Sprites/Characterrs/Walk/bigDude.png", "bigDude");
        //puis load
        switch (team)
        {
            case APE:
                this.spritesheet = ResourceHandler.getTexture("gurl");
                this.avatarTexture = ResourceHandler.getTexture("gurlAvt");
                this.sprite = new Sprite(spritesheet);
                sprite.setTextureRect(0,0, 64, 64);
                break;
            case MAN :
                this.spritesheet = ResourceHandler.getTexture("bigDude");
                this.avatarTexture = ResourceHandler.getTexture("bigDudeAvt");
                this.sprite = new Sprite(spritesheet);
                sprite.setTextureRect(0,0, 64, 64);
                break;
        }
    this.team=team;
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
    public void removePA(short cost) {
        this.actionPoints-=cost;
    }

    @Override
    public short getMaximumPoints() {
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
    public ConstTexture getSpriteSheet() {
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
        if (isDead())
        {
            target.draw(sprite, ResourceHandler.getShader("grey"));
        }else
        {
            target.draw(this.sprite);
            hpText.setString(""+hp);
            hpText.setPosition(sprite.getPosition().x + 1, sprite.getPosition().y+36);
            target.draw(hpText);
        }
    }
}
