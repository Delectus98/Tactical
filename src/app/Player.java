package app;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    protected String name;
    protected List<Unite> unites;
    protected Team team;

    public Player(String name)
    {
        this.name = name;
        this.unites = new ArrayList<>();
    }

    public void addUnite(Unite unite)
    {
        this.unites.add(unite);
        unite.setTeam(this.team);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Unite> getUnites()
    {
        return unites;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

}
