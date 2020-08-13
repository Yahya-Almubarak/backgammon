/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.security.SecureRandom;

/**
 *
 * @author Programmer
 */
public class Dice {
    private int roll;
    private SecureRandom secureRandom;

    public Dice() {
        secureRandom = new SecureRandom();
        
    }
    

    public int getRoll() {
        roll = 1 + secureRandom.nextInt(6);
        return roll;
    }

    @Override
    public String toString() {
        return "Dice{" + "roll=" + roll + '}';
    }
}
