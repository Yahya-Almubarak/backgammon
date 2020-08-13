/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Programmer
 */
public class Point {

    private List<Checker> checkers;
    private int owner; // zero if point is empty;
    private int pointNum;

    public Point(int pointNum) {
        checkers = new ArrayList<>();
        owner = 0;
        this.pointNum = pointNum;
    }

    public boolean isPointEmpty() {
        if (checkers.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addChecker(Checker checker) {
        if (checker == null) {
            return false;
        }
        if (checkers.add(checker)) {
            this.setOwner(checker.getOwner());
            return true;
        } else {
            return false;
        }

    }

    public Checker removeChecker() {
        if (checkers.isEmpty()) {
            return null;
        }
        Checker checker = checkers.remove(checkers.size() - 1);
        if (checkers.isEmpty()) {
            setOwner(0);
        }
        return checker;

    }

    public boolean isOneChecker() {
        if (checkers.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPointClosed() {
        if (checkers.size() >= 2) {
            return true;
        } else {
            return false;
        }

    }
    
    public boolean isPointClosed(int playerNum) {
        if (getOwner() != playerNum && isPointClosed()) {
            return true;
        } else {
            return false;
        }

    }
// setter and getters

    public List<Checker> getCheckers() {
        return checkers;
    }

    public void setCheckers(List<Checker> checkers) {
        this.checkers = checkers;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public Point clone() {
        Point point = new Point(0);
        point.setOwner(this.getOwner());
        point.setPointNum(this.getPointNum());
        List<Checker> checkersCopy = new ArrayList<>(); // to be checked if it makes shollow copy or deep one
        for (int i = 0; i < checkers.size(); i++) {
            checkersCopy.add(checkers.get(i).clone());
        }
        point.setCheckers(checkersCopy);
        return point;
    }

    @Override
    public String toString() {
        return "Point{" + " pointNum= " + pointNum + " owner= " + owner + " Number of checkers= " + checkers.size() + " }";
    }

}
