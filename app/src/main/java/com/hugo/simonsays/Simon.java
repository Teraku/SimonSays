/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugo.simonsays;

import java.util.*;

/**
 *
 * @author Danny
 */
public class Simon
{
    private ArrayList<Color> colors;
    
    private Random random;
    
    public Simon()
    {
        random = new Random();
        colors = new ArrayList<Color>();
        
        addRandomColor();
    }
    
    public boolean checkColor(Color color, int index)
    {
        return colors.get(index) == color;
    }
    
    public ArrayList<Color> getColors()
    {
        //Return a copy of the colors list, to prevent modification.
        return new ArrayList<Color>(colors);
    }
    
    public void addRandomColor()
    {
        colors.add(Color.getRandom());
    }
}
