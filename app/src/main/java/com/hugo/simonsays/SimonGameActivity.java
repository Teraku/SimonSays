package com.hugo.simonsays;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SimonGameActivity extends AppCompatActivity implements SimonListener {

    private SimonController simonGame;

    private Button greenButton;
    private Button redButton;
    private Button yellowButton;
    private Button blueButton;

    private TextView statusText;

    private Button startGameButton;

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
        blueButton.setTag(R.id.TAG_COLOR, Color.BLUE);

        statusText = (TextView) findViewById(R.id.statusTextView);

        this.simonGame = new SimonController(this);

        //Add listener to buttons
        View.OnClickListener simonButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simonGame.inputColor((Color) v.getTag(R.id.TAG_COLOR));
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
                simonGame.startGame();
                v.setVisibility(View.GONE);
            }
        });
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
                            Log.i("UIEvent", "Setting background color to press color");
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

                            Log.i("UIEvent", "Restoring color");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setBackgroundResource(originalColor);
                                }
                            });
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
}
