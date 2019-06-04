package util;

public class MyPair<Key, Value> //We created our own Pair because some members of the groupe didnt have them.
{
    Key fst;
    Value snd;

    public MyPair(Key fst, Value snd)
    {
        this.fst = fst;
        this.snd = snd;
    }

    public void setFst(Key fst)
    {
        this.fst = fst;
    }

    public void setSnd(Value snd)
    {
        this.snd = snd;
    }

    public Key getFst()
    {
        return fst;
    }

    public Value getSnd()
    {
        return snd;
    }

    public boolean equals(Object o)
    {
        if (o instanceof MyPair)
        {
            return this.fst.equals(((MyPair) o).fst) && this.snd.equals(((MyPair) o).snd);
        } else
        {
            return false;
        }
    }

    public String toString()
    {
        return "(" + fst.toString() + ", " + snd.toString() + ")";
    }
}


