package com.hugo.simonsays;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.app.DialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.hugo.simonsays.Color.BLUE;

public class SimonGameActivity extends AppCompatActivity implements SimonListener {

    private SimonController simonGame;

    private ScoreDatabase highScoreDB;

    private Button greenButton;
    private Button redButton;
    private Button yellowButton;
    private Button blueButton;

    private TextView statusText;
    private TextView scoreText;

    private Button startGameButton;

    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);

        //Inflate buttons and keep reference
        greenButton = (Button) findViewById(R.id.green_button);
        greenButton.setTag(R.id.TAG_COLOR, Color.GREEN);

        redButton = (Button) findViewById(R.id.red_button);
        redButton.setTag(R.id.TAG_COLOR, Color.RED);

        yellowButton = (Button) findViewById(R.id.yellow_button);
        yellowButton.setTag(R.id.TAG_COLOR, Color.YELLOW);

        blueButton = (Button) findViewById(R.id.blue_button);
        blueButton.setTag(R.id.TAG_COLOR, BLUE);

        statusText = (TextView) findViewById(R.id.statusTextView);
        scoreText = (TextView) findViewById(R.id.currentScoreTextView);

        this.simonGame = new SimonController(this);

        //Add listener to buttons
        View.OnClickListener simonButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simonGame.inputColor((Color) v.getTag(R.id.TAG_COLOR));
                scoreText.setText("Score: " + Integer.toString(simonGame.getScore()));
            }
        };

        greenButton.setOnClickListener(simonButtonListener);
        redButton.setOnClickListener(simonButtonListener);
        yellowButton.setOnClickListener(simonButtonListener);
        blueButton.setOnClickListener(simonButtonListener);

        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNameDialog();
            }
        });

        this.highScoreDB = new ScoreDatabase(this);
    }

    //Create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.about_us) {
            //About us
            DialogFragment aUsFragment = new AboutUsFragment();
            aUsFragment.show(getFragmentManager(), "aboutusDialog");

            return true;

        } else if (id == R.id.action_setting) {
            //Leaderbord
            Intent lb_intent = new Intent(this, Leaderbord.class);
            startActivities(new Intent[]{lb_intent});
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameStart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText("Playing");
            }
        });
    }

    @Override
    public void onGameStop() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText("Game over! You made a mistake.");
                startGameButton.setVisibility(View.VISIBLE);
            }
        });

        checkHighScore(simonGame.getScore());
    }

    @Override
    public void onGameTimeout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText("Game over! You ran out of time.");
                startGameButton.setVisibility(View.VISIBLE);
            }
        });

        checkHighScore(simonGame.getScore());
    }

    @Override
    public void onStartInput() {
        greenButton.setClickable(true);
        redButton.setClickable(true);
        yellowButton.setClickable(true);
        blueButton.setClickable(true);
    }

    @Override
    public void onStopInput() {
        greenButton.setClickable(false);
        redButton.setClickable(false);
        yellowButton.setClickable(false);
        blueButton.setClickable(false);
    }

    @Override
    public void displayColors(final ArrayList<Color> colors, final float millisecondsPerColor) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                RunQueue displayColorQueue = new RunQueue();

                for (Color color : colors)
                {
                    final int originalColor;
                    final int pressedColor;

                    switch (color) {
                        case RED:
                            originalColor = R.drawable.custom_red_button;
                            pressedColor = R.drawable.custom_red_button_pressed;
                            break;
                        case GREEN:
                            originalColor = R.drawable.custom_green_button;
                            pressedColor = R.drawable.custom_green_button_pressed;
                            break;
                        case BLUE:
                            originalColor = R.drawable.custom_blue_button;
                            pressedColor = R.drawable.custom_blue_button_pressed;
                            break;
                        case YELLOW:
                            originalColor = R.drawable.custom_yellow_button;
                            pressedColor = R.drawable.custom_yellow_button_pressed;
                            break;
                        default:
                            throw new RuntimeException("Invalid color!");
                    }

                    final Button button = getButtonFromColor(color);

                    displayColorQueue.queue(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setBackgroundResource(pressedColor);
                                }
                            });

                            try {
                                Thread.sleep((long) millisecondsPerColor);
                            } catch (InterruptedException e) {

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setBackgroundResource(originalColor);
                                }
                            });

                            //Sleep for another 15 milliseconds, to give the buttons some "downtime".
                            //Otherwise, when the same color flashes twice in a row, the button will stay lit instead of flashing.
                            try {
                                Thread.sleep(15);
                            }
                            catch(InterruptedException e) {

                            }
                        }
                    });
                }

                //Notify the game controller that we have displayed all the colors.
                displayColorQueue.queue(new Runnable(){
                    @Override
                    public void run() {
                        simonGame.onColorsDisplayed();
                    }
                });

                displayColorQueue.run();
            }
        };

        thread.start();
    }

    /**
     * Get the corresponding Button from a color.
     *
     * @param color The Color to get the corresponding button from.
     * @return Button Returns a button which corresponds to the color parameter.
     */
    private Button getButtonFromColor(Color color) {
        switch (color) {
            case RED:
                return redButton;
            case GREEN:
                return greenButton;
            case BLUE:
                return blueButton;
            case YELLOW:
                return yellowButton;
            default:
                throw new RuntimeException("Unsupported color!");
        }
    }

    //Animation for the attract mode
    public void displayAnimation(){
        //Attractmode
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.attract_anim);
        greenButton.startAnimation(shake);
        redButton.startAnimation(shake);
        yellowButton.startAnimation(shake);
        blueButton.startAnimation(shake);
    }

    /**
     * Checks if a score is a new high score. If so, tells the database to insert/update the score, and shows a toast to the user.
     * @param score The score to check and possibly submit.
     * @param
     */
    private void checkHighScore(final int score)
    {
        if(highScoreDB.isHighScore(playerName, score))
        {
            highScoreDB.insertHighScore(playerName, score);
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(SimonGameActivity.this, "You got a new high score!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openNameDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your name");

        //Set up the text input for the user to enter their name
        final EditText nameInput = new EditText(this);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        nameInput.setHint("Name");
        builder.setView(nameInput);

        //Set up OK and cancel buttons
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerName = nameInput.getText().toString();
                startGameButton.setVisibility(View.GONE);
                simonGame.startGame();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}