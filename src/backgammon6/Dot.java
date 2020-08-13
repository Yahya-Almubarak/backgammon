/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Programmer
 */
public class Dot extends Circle {

    int dotNum;

    public Dot(double x, double y, double radius, int dotNum) {
        super(x, y, radius);
        this.setFill(Color.YELLOW);
        this.dotNum = dotNum;
    }

    public void showDot(DotState state) {
        switch (state) {

            case Red:
                this.setFill(Color.RED);

                this.setVisible(true);
                break;
            case Dim:

                this.setFill(Color.rgb(97, 0, 0));
                this.setVisible(true);
                break;
            case Green:

                this.setFill(Color.GREENYELLOW);
                this.setVisible(true);
                break;
            case Yellow:
                this.setFill(Color.rgb(249, 249, 108));

                this.setVisible(true);

        }
    }

}
