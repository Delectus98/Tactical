package app;

import java.util.List;

public abstract class Player
{
    protected String name;
    protected List<Unite> unites;

    public String getName()
    {
        return this.name;
    }

    public List<Unite> getUnites()
    {
        return unites;
    }
}
