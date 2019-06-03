package util;

public class MyPair<T> //We created our own Pair because some members of the groupe didnt have them.
{
    T fst;
    T snd;

    public MyPair(T fst, T snd)
    {
        this.fst = fst;
        this.snd = snd;
    }

    public void setFst(T fst)
    {
        this.fst = fst;
    }

    public void setSnd(T snd)
    {
        this.snd = snd;
    }

    public T getFst()
    {
        return fst;
    }

    public T getSnd()
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


