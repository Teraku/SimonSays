package com.hugo.simonsays;

/**
 * Created by Hugo on 17-1-2017.
 */

public class Score
{
    private String name;
    private int score;

    public Score(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName()
    {
        return this.name;
    }

    public int getScore()
    {
        return this.score;
    }
}
