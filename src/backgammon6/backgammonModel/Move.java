/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Programmer
 * Handles the points numbers to move checkers between.
 * If the move is to accumulator of player 1 toPoint = -100.
 * If the move is to accumulator of player 2 toPoint = -200.
 */
public class Move {
    private int fromPoint;
    private int toPoint;
    private int roll;
    private List<Integer> involvedRolls;
    
    public Move(int fromPoint, int toPoint) {
        involvedRolls = new ArrayList<>();
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
    }
    
    public Move(int fromPoint, int toPoint, int roll) {
        this(fromPoint, toPoint);
        this.roll = roll;
        this.involvedRolls.add(roll);
    }
    
    public void addRoll(int roll) {
        this.roll += roll;
        involvedRolls.add(roll);
    }
    
    public void addRoll(List<Integer> rolls) {
        this.roll += rolls.stream().reduce(Math::addExact).get();
        involvedRolls.addAll(rolls);
    }
    public int getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(int fromPoint) {
        this.fromPoint = fromPoint;
    }

    public int getToPoint() {
        return toPoint;
    }

    public void setToPoint(int toPoint) {
        this.toPoint = toPoint;
    }

    public int getRoll() {
        return this.roll;
    }

    public void setRoll(int roll) {
        this.involvedRolls.add(roll);
        this.roll = roll;
    }

    public List<Integer> getInvolvedRolls() {
        return involvedRolls;
    }

    public void setInvolvedRolls(List<Integer> involvedRolls) {
        this.involvedRolls = involvedRolls;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Move other = (Move) obj;
        if (this.fromPoint != other.fromPoint) {
            return false;
        }
        if (this.toPoint != other.toPoint) {
            return false;
        }
        if(this.getRoll() != other.getRoll()) {
            return false;
        }
        if (this.getInvolvedRolls().size() != other.getInvolvedRolls().size()) {
            return false;
        }
        if (this.getInvolvedRolls().size() == 1) {
            if(this.getInvolvedRolls().get(0).intValue() != other.getInvolvedRolls().get(0).intValue()) {
                return false;
            }
        }
        if (this.getInvolvedRolls().size() == 2) {
            if(this.getInvolvedRolls().get(0).intValue() != other.getInvolvedRolls().get(0).intValue()
                    || this.getInvolvedRolls().get(1).intValue() != other.getInvolvedRolls().get(1).intValue()) {
                return false;
            }
        }
        if (this.getInvolvedRolls().size() == 3) {
            if(this.getInvolvedRolls().get(0).intValue() != other.getInvolvedRolls().get(0).intValue()
                    || this.getInvolvedRolls().get(1).intValue() != other.getInvolvedRolls().get(1).intValue()
                    || this.getInvolvedRolls().get(2).intValue() != other.getInvolvedRolls().get(2).intValue()) {
                return false;
            }
        }
        if (this.getInvolvedRolls().size() == 4) {
            if(this.getInvolvedRolls().get(0).intValue() != other.getInvolvedRolls().get(0).intValue()
                    || this.getInvolvedRolls().get(1).intValue() != other.getInvolvedRolls().get(1).intValue()
                    || this.getInvolvedRolls().get(2).intValue() != other.getInvolvedRolls().get(2).intValue()
                    || this.getInvolvedRolls().get(3).intValue() != other.getInvolvedRolls().get(3).intValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Move{" + "fromPoint=" + fromPoint + ", toPoint=" + toPoint + ", roll=" + roll + ", involvedRolls=" + involvedRolls + '}';
    }
    
    
}
