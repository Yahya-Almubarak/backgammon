/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6;

import javafx.scene.shape.Circle;

/**
 *
 * @author Programmer
 */
public class CheckerImage extends Circle{
    int playerNum;
    int PointNum;

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPointNum() {
        return PointNum;
    }

    public void setPointNum(int PointNum) {
        this.PointNum = PointNum;
    }
    public CheckerImage(){
        super();
        
    }
    public CheckerImage(int pointNum, int playerNum, double x, double y, double radius) {
        super(x, y, radius);
        this.setStrokeWidth(radius/80);
        this.PointNum = pointNum;
        this.playerNum = playerNum;
    }
    
}
