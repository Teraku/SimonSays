package com.hugo.simonsays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class Leaderbord extends AppCompatActivity {

    private ArrayList<Score> score;
    private ListView scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrore_row);

        //Init database
        ScoreDatabase sc_db = new ScoreDatabase(this);

        //getting scores
        this.score = sc_db.getHighScores();
        
        ScoreAdapter sc_adapter = new ScoreAdapter(this, score);
        scoreList = (ListView)findViewById(R.id.scoreList);

        //Init and setting the header for the layout
        View header = getLayoutInflater().inflate(R.layout.leaderbord_header, null);
        scoreList.addHeaderView(header);

        scoreList.setAdapter(sc_adapter);

    }

}
