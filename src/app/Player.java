package app;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    protected String name;
    protected List<Unite> unites;

    public Player(String name)
    {
        this.name = name;
        this.unites = new ArrayList<>();
    }

    public void addUnite(Unite unite)
    {
        this.unites.add(unite);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Unite> getUnites()
    {
        return unites;
    }
}
