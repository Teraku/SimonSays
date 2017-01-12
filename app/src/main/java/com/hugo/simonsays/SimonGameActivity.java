package com.hugo.simonsays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.DialogFragment;

public class SimonGameActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);

    }

    //Create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if(id == R.id.about_us){
            //About us
            DialogFragment aUsFragment = new AboutUsFragment();
            aUsFragment.show(getFragmentManager(), "aboutusDialog");

            return true;
        }else if(id == R.id.action_setting){
            //Leaderbord

        }

        return super.onOptionsItemSelected(item);
    }

}
