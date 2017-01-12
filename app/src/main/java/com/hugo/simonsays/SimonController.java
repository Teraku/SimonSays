/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugo.simonsays;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Hugo
 */
public class SimonController
{
    private SimonListener listener;
    
    private Simon simon;
    
    //This is used to remember which color in the sequence the user is on.
    private int index;
    
    private Timer timer;
    
    private Status status;

    /**
     * Time in milliseonds that each color should be displayed.
     */
    private float milliseconds;
    
    public SimonController(SimonListener listener)
    {
        this.listener = listener;
        
        this.status = Status.ATTRACT;
        
        this.timer = new Timer();

        this.milliseconds = 200;
    }
    
    public void startGame()
    {
        simon = new Simon();
        index = 0;
        
        listener.onGameStart();
        listener.onStopInput();
        
        status = Status.PLAYING;
        
        listener.displayColors(simon.getColors(), milliseconds);
    }
    
    public void stopGame()
    {
        status = Status.GAMEOVER;
        stopTimer();
        
        listener.onStopInput();
        listener.onGameStop();  
    }
    
    private void timeout()
    {
        status = Status.TIMEOUT;
        
        listener.onStopInput();
        listener.onGameTimeout();
    }
    
    public boolean inputColor(Color color)
    {
        if(status != Status.PLAYING)
        {
            return false;
        }
        
        boolean result = simon.checkColor(color, index);
        
        if(!result)
        {
            status = Status.GAMEOVER;
            stopGame();
            return false;
        }
        
        //Check if this was the last color in the sequence.
        if(index >= simon.getColors().size() - 1)
        {
            index = 0;
            listener.onStopInput();
            
            simon.addRandomColor();

            calculateButtonTime();
            
            listener.displayColors(simon.getColors(), milliseconds);
        }
        else
        {
            index += 1;
        }
        
        return true;
    }

    /**
     * Should be called by the listener, as soon as the listener is done displaying colors.
     */
    public void onColorsDisplayed()
    {
        listener.onStartInput();
        startTimer();
    }
    
    private void startTimer()
    {
        stopTimer();
        
        timer = new Timer();
        
        timer.schedule(new TimerTask(){
            
            @Override
            public void run()
            {
                timeout();
            }
            
        }, 10000);
    }
    
    private void stopTimer()
    {
        timer.cancel();
        timer.purge();
    }

    /**
     * Re-calculates the time that each button should be lit. The more colors, the faster each color is displayed.
     */
    private void calculateButtonTime()
    {
        milliseconds = 200 - (simon.getColors().size() * 5);

        if(milliseconds < 40)
        {
            milliseconds = 40;
        }
    }
}
