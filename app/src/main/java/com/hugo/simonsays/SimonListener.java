/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugo.simonsays;

import java.util.ArrayList;

/**
 *
 * @author Hugo
 */
public interface SimonListener
{
    void onGameStart();
    void onGameStop();
    void onGameTimeout();
    
    void onStartInput();
    void onStopInput();
    
    void displayColors(ArrayList<Color> colors, float millisecondsPerColor);

    void displayAnimation();
    void stopAnim();
}
