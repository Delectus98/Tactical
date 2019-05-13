package app;

abstract class Unite
{
    protected short hp;
    protected short fov;
    protected short actionPoints;
    protected Weapon primary;
    protected Weapon secondary;
    protected Weapon melee;

    abstract boolean isDead();

    abstract short getHp();

    abstract short getFov();

    abstract Weapon getPrimary();
    abstract Weapon getSecondary();
    abstract Weapon getMelee();

}
