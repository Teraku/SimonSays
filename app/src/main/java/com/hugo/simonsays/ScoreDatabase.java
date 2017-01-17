package com.hugo.simonsays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by Hugo on 29-11-2016.
 */

public class ScoreDatabase extends SQLiteAssetHelper
{
    private static String DATABASE_NAME = "scores.db";
    private static int DATABASE_VERSION = 1;

    public ScoreDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<Score> getHighScores()
    {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT rowid _id, * FROM highscores ORDER BY score DESC, id ASC LIMIT 10;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        ArrayList<Score> scores = new ArrayList<Score>();
        while(c.isAfterLast() == false)
        {
            scores.add(new Score(
                    c.getString(c.getColumnIndex("name")),
                    c.getInt(c.getColumnIndex("score")),
                    c.getInt(c.getColumnIndex("id"))
            ));
            c.moveToNext();
        }

        return scores;
    }

    public void insertHighScore(String name, int score)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("name", name);
        content.put("score", score);

        try {
            db.insertOrThrow("highscores", null, content);
        }
        catch(SQLException e){
            Log.e("ERROR_DB", e.getMessage(), e);
        }
    }

    public boolean isHighScore(int score)
    {
        SQLiteDatabase db= getReadableDatabase();

        String query = "SELECT rowid _id, * FROM highscores ORDER BY score DESC, id ASC LIMIT 10";
        Cursor c = db.rawQuery(query, null);

        //Check if the high scores DB has less than 10 entries. If so, a score will always be a high score.
        if(c == null || c.getCount() < 10)
        {
            return true;
        }

        c.moveToLast();

        if(c.getInt(c.getColumnIndex("score")) < score)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
