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
                    c.getInt(c.getColumnIndex("score"))
            ));
            c.moveToNext();
        }

        c.close();

        return scores;
    }

    public void insertHighScore(String name, int score)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("name", name);
        content.put("score", score);

        try {
            //Check if this name already exists
            Cursor c = db.rawQuery("SELECT rowid _id, * FROM highscores WHERE name = ?", new String[]{name});
            if(c == null || c.getCount() == 0)
            {
                db.insertOrThrow("highscores", null, content);
            }
            else
            {
                db.update("highscores", content, "name = ?", new String[]{name});
            }
        }
        catch(SQLException e){
            Log.e("ERROR_DB", e.getMessage(), e);
        }
    }

    public boolean isHighScore(String name, int score)
    {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT rowid _id, * FROM highscores WHERE name = ?";
        Cursor c = db.rawQuery(query, new String[]{name});

        //Check if the high scores DB does not yet have an entry for this user. If so, the new score will always be a highscore.
        if(c == null || c.getCount() == 0)
        {
            return true;
        }

        c.moveToLast();

        if(c.getInt(c.getColumnIndex("score")) < score)
        {
            c.close();
            return true;
        }
        else
        {
            c.close();
            return false;
        }
    }
}
