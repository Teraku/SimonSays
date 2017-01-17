package com.hugo.simonsays;

/**
 * Created by Hugo on 17-1-2017.
 */

public class Score
{
    private String name;
    private int score;
    private int rank;

    public Score(String name, int score, int rank)
    {
        this.name = name;
        this.score = score;
        this.rank = rank;
    }

    public String getName()
    {
        return this.name;
    }

    public int getScore()
    {
        return this.score;
    }

    public int getRank(){
        return this.rank;
    }
}
