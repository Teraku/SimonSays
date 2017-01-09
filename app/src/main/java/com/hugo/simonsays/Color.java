/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hugo.simonsays;

/**
 *
 * @author Hugo
 */
public enum Color
{
    RED,
    GREEN,
    BLUE,
    YELLOW;
    
    public static Color getRandom()
    {
        return values()[(int)(Math.random() * values().length)];
    }
}
