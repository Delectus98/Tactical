package app;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    //
    private int id;
    //
    protected String name;
    protected List<Unite> unites;
    protected Team team;

    /**
     * Creates a player
     * @param name player's name
     */
    public Player(String name)
    {
        this.name = name;
        this.unites = new ArrayList<>();
    }

    /**
     * Adds unite to player
     * @param unite
     */
    public void addUnite(Unite unite)
    {
        unite.setId(unites.size());
        unite.setTeam(this.team);
        this.unites.add(unite);
    }

    /**
     * When player is added to player list we defines a unique id
     * @param id specified id
     */
    public final void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gives the unique id of the player
     * @return the unique id of the player
     */
    public final int getId()
    {
        return id;
    }

    /**
     * Gives name of the player
     * @return name of the player
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gives all unites of a player (even dead ones)
     * @return all unites of a player (even dead ones)
     */
    public List<Unite> getUnites()
    {
        return unites;
    }

    /**
     * Gives team of the player
     * @return team of the player
     */
    public Team getTeam()
    {
        return team;
    }

    /**
     * Defines a team
     * @param team specified team
     */
    public void setTeam(Team team)
    {
        this.team = team;
    }

    public void setName(String s) {
        this.name=s;
    }
}
