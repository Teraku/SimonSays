package com.hugo.simonsays;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Danny on 17-1-2017.
 */

public class ScoreAdapter extends ArrayAdapter<Score>{

    private ArrayList<Score> scores;
    private Activity activity;
    private LayoutInflater inflater;

    public ScoreAdapter(Activity activity, ArrayList<Score> scores) {
        super(activity,R.layout.activity_leaderbord ,scores);
        //super(context, scores);
        this.activity = activity;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View lbView, ViewGroup parent) {
        
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (lbView == null)
            lbView = inflater.inflate(R.layout.activity_leaderbord, parent, false);


        TextView name = (TextView)lbView.findViewById(R.id.name);
        TextView score = (TextView)lbView.findViewById(R.id.score);
        TextView rank = (TextView)lbView.findViewById(R.id.rank);

        Score sc = scores.get(position);

        rank.setText(Integer.toString(sc.getRank()));
        name.setText(sc.getName());
        score.setText(Integer.toString(sc.getScore()));

        return lbView;

    }


}
