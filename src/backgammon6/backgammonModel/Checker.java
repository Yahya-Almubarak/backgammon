/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

/**
 *
 * @author Programmer
 */
public class Checker implements Cloneable{
    
    private int owner; 

    public Checker(int owner) {
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
    
    public Checker clone() {
        Checker checker = new Checker(0);
        checker.setOwner(this.getOwner());
        return checker;
    }
}
