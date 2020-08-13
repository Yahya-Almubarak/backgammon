/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6;

import backgammon6.backgammonModel.Board;
import backgammon6.backgammonModel.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Programmer
 */
public class GameBoard {

    private final double width = 800;
    private final double numOfUnitsPerAccWidth = 1.8;
    private final double unit = (1 - 0.05) / (14 + numOfUnitsPerAccWidth) * width;
    private final double numOfUnitsPerHeight = 13;
    private final double height = numOfUnitsPerHeight * unit + (2.0 / 80) * width;
    private final double edgeWidth = width / 80;

    private final Color boardColor = Color.rgb(250, 196, 155);
    private final Color boardEdgeColor = Color.rgb(97, 0, 0);
    private final Color pointsColor1 = Color.rgb(98, 113, 57);
    private final Color pointsColor2 = Color.rgb(177, 50, 7);
    private final Color checkerColorForPlayer1 = Color.IVORY;
    private final Color checkerEdgeColorForPlayer1 = Color.BLACK;
    private final Color checkerColorForPlayer2 = Color.BLACK;
    private final Color checkerEdgeColorForPlayer2 = Color.WHITE;
 

    private final Font timerFont = new Font("Times New Roman", unit);
    private final Color timerTextColor = Color.VIOLET;

    private List<Dot> dots;
    private List<Group> pointsImageArray;
    private Group pointsImage;
    private List<Rectangle> pointRegionsArray;
    private Group barForPlayer1;
    private Group barForPlayer2;
    private Group accumulatorImageForPlayer1;
    private Group accumulatorImageForPlayer2;
    private Rectangle barForPlayer1Region;
    private Rectangle barForPlayer2Region;
    private Rectangle accumulatorImageForPlayer1Region;
    private Rectangle accumulatorImageForPlayer2Region;

    private Group diceGroup;
    private Circle innerCircleForDice1;
    private Circle innerCircleForDice2;
    private Circle outerCircleForDice1;
    private Circle outerCircleForDice2;
    private Group redoButtonGroup;
    private Text redoText;
    private Text remainingPointCounterPlayer1;
    private Text remainingPointCounterPlayer2;

    public Group drawBoard() {
        Group group = new Group();

        Rectangle r1 = new Rectangle(0, 0, width, height);
        r1.setFill(boardEdgeColor);
        //r1.setStroke(boardStrokeColor);

        Rectangle r2 = new Rectangle(edgeWidth, edgeWidth, unit, 5 * unit);
        r2.setFill(boardColor);

        Rectangle r3 = new Rectangle(edgeWidth, height - 5 * unit - edgeWidth, unit, 5 * unit);
        r3.setFill(boardColor);

        Rectangle r4 = new Rectangle(width - edgeWidth - unit, edgeWidth, unit, 5 * unit);// accumulator 2
        r4.setFill(boardColor);

        Rectangle r5 = new Rectangle(width - edgeWidth - unit, height - edgeWidth - 5 * unit, unit, 5 * unit);
        r5.setFill(boardColor); // accumulator1

        Rectangle r6 = new Rectangle(2 * edgeWidth + unit, edgeWidth, 6 * unit, numOfUnitsPerHeight * unit);
        r6.setFill(boardColor);

        Rectangle r7 = new Rectangle(6 * edgeWidth + 8 * unit, edgeWidth, 6 * unit, numOfUnitsPerHeight * unit);
        r7.setFill(boardColor);

        Line l1 = new Line(width / 2, 0, width / 2, height);
        l1.setFill(Color.BLACK);

        Line l2 = new Line(0, height / 2, width, height / 2);
        l2.setFill(Color.BLACK);
        pointRegionsArray = new ArrayList<>();
        List<Rectangle> lowerPointRegionsArray = new ArrayList<>();
        List<Rectangle> upperPointRegionsArray = new ArrayList<>();
        Group triangles = new Group();
        for (int i = 0; i < 12; i++) {
            Polygon polygon = new Polygon();
            double displacement = 2 * edgeWidth + unit + (i > 5 ? 4 * edgeWidth + unit : 0);
            double lowerLeftX = displacement + i * unit;
            double lowerLeftY = height - edgeWidth;
            double lowerRightX = displacement + (i + 1) * unit;
            double lowerRightY = height - edgeWidth;
            double lowerTopX = displacement + (i * unit) + unit / 2;
            double lowerTopY = (height - edgeWidth) - 5 * unit;
            Double[] triangleHeads = {lowerLeftX, lowerLeftY, lowerRightX, lowerRightY, lowerTopX, lowerTopY};
            polygon.getPoints().addAll(triangleHeads);
            Paint paint;
            if (i % 2 == 0) {
                paint = pointsColor1;
            } else {
                paint = pointsColor2;
            }
            polygon.setFill(paint);
            triangles.getChildren().add(polygon);
            Rectangle pointRegion = new Rectangle(lowerLeftX, lowerTopY, unit, 5 * unit);
            lowerPointRegionsArray.add(pointRegion);
        }

        for (int i = 0; i < 12; i++) {
            Polygon polygon = new Polygon();
            double displacement = 2 * edgeWidth + unit + (i > 5 ? 4 * edgeWidth + unit : 0);
            double upperLeftX = displacement + i * unit;
            double upperLeftY = edgeWidth;
            double upperRightX = displacement + (i + 1) * unit;
            double upperRightY = edgeWidth;
            double upperTopX = displacement + (i * unit) + unit / 2;
            double upperTopY = edgeWidth + 5 * unit;
            Double[] triangleHeads = {upperLeftX, upperLeftY, upperRightX, upperRightY, upperTopX, upperTopY};
            polygon.getPoints().addAll(triangleHeads);
            Paint paint;
            if (i % 2 == 0) {
                paint = pointsColor2;
            } else {
                paint = pointsColor1;
            }
            polygon.setFill(paint);
            triangles.getChildren().add(polygon);
            Rectangle pointRegion = new Rectangle(upperLeftX, upperRightY, unit, 5 * unit);
            upperPointRegionsArray.add(pointRegion);
        }
        for (int i = 0; i < 12; i++) {
            pointRegionsArray.add(lowerPointRegionsArray.get(11 - i));
        }
        for (int i = 0; i < 12; i++) {
            pointRegionsArray.add(upperPointRegionsArray.get(i));
        }
        Group dotsGroup = drawDots();

        pointsImageArray = new ArrayList<>();
        pointsImage = new Group();
        for (int i = 0; i < 24; i++) {
            Group pointImage = new Group();
            pointsImageArray.add(pointImage);
            pointsImage.getChildren().add(pointImage);

        }
        barForPlayer1 = new Group();
        barForPlayer1Region = new Rectangle(width / 2 - 0.5 * unit, edgeWidth, unit, 5 * unit);
        barForPlayer2 = new Group();
        barForPlayer2Region = new Rectangle(width / 2 - 0.5 * unit, height - edgeWidth - 5 * unit, unit, 5 * unit);
        accumulatorImageForPlayer1 = new Group();
        accumulatorImageForPlayer1Region = new Rectangle(width - edgeWidth - unit, height - edgeWidth - 5 * unit,
                unit, 5 * unit);
        pointsImageArray.add(barForPlayer1);
        pointsImageArray.add(barForPlayer2);
        accumulatorImageForPlayer2 = new Group();
        accumulatorImageForPlayer2Region = new Rectangle(width - edgeWidth - unit, edgeWidth,
                unit, 5 * unit);

        diceGroup = new Group();
        innerCircleForDice1 = new Circle();
        innerCircleForDice2 = new Circle();
        outerCircleForDice1 = new Circle();
        outerCircleForDice2 = new Circle();

        redoButtonGroup = drawRedoButton(ButtonText.Roll.toString());

        group.getChildren().addAll(r1, r2, r3, r4, r5, r6, r7, l1, l2, triangles, dotsGroup, pointsImage, barForPlayer1, barForPlayer2, accumulatorImageForPlayer1, accumulatorImageForPlayer2);
        group.getChildren().addAll(diceGroup, innerCircleForDice1, innerCircleForDice2, outerCircleForDice1, outerCircleForDice2);
        group.getChildren().add(redoButtonGroup);
        
        double x = width - edgeWidth - unit/2 ;
        double y = height - edgeWidth - 5 * unit - unit/2;
        remainingPointCounterPlayer1 = new Text("167");
        Font font = new Font("Times New Roman", width / 40);
        remainingPointCounterPlayer1.setFont(font);
        remainingPointCounterPlayer1.setTextAlignment(TextAlignment.CENTER);
        remainingPointCounterPlayer1.setTextOrigin(VPos.TOP);
        x = x - remainingPointCounterPlayer1.prefWidth(height) / 2;
        y = y - remainingPointCounterPlayer1.prefHeight(width) / 2;
        remainingPointCounterPlayer1.setX(x );
        remainingPointCounterPlayer1.setY(y);
        remainingPointCounterPlayer1.setFill(Color.BEIGE);
        group.getChildren().add(remainingPointCounterPlayer1);
        
        x = width - edgeWidth - unit/2 ;
        y = edgeWidth + 5 * unit + unit/2;
        remainingPointCounterPlayer2 = new Text("167");
        remainingPointCounterPlayer2.setFont(font);
        remainingPointCounterPlayer2.setTextAlignment(TextAlignment.CENTER);
        remainingPointCounterPlayer2.setTextOrigin(VPos.TOP);
        x = x - remainingPointCounterPlayer2.prefWidth(height) / 2;
        y = y - remainingPointCounterPlayer2.prefHeight(width) / 2;
        remainingPointCounterPlayer2.setX(x );
        remainingPointCounterPlayer2.setY(y);
        remainingPointCounterPlayer2.setFill(Color.BEIGE);

        
        group.getChildren().add(remainingPointCounterPlayer2);
        return group;
    }

    private Group drawDots() {
        Group dotsGroup = new Group();
        dots = new ArrayList<>();
        double x, y;
        double radius = edgeWidth / 3;
        Dot dot;

        for (int i = 0; i < 12; i++) {
            x = width - 2 * edgeWidth - unit - (i + 0.5) * unit - (i > 5 ? 4 * edgeWidth + unit : 0);
            y = height - edgeWidth / 2;
            dot = new Dot(x, y, radius, i + 1);
            dotsGroup.getChildren().add(dot);
            dots.add(dot);
        }

        for (int i = 0; i < 12; i++) {
            x = 2 * edgeWidth + unit + (i + 0.5) * unit + (i > 5 ? 4 * edgeWidth + unit : 0);
            y = edgeWidth / 2;
            dot = new Dot(x, y, radius, i);
            dotsGroup.getChildren().add(dot);
            dots.add(dot);
        }
        // player 1 bar index = 24
        x = width/ 2;
        y = height * 0.4  + edgeWidth;
        dot = new Dot(x, y, radius, -10);
        dotsGroup.getChildren().add(dot);
        dots.add(dot);
        
        // player 2 bar index = 25
        x = width/ 2;
        y = height * 0.6 - edgeWidth;
        dot = new Dot(x, y, radius, -20);
        dotsGroup.getChildren().add(dot);
        dots.add(dot);
        
        //player 1 accumulator index = 26
        x = width - edgeWidth - unit / 2;
        y = height - edgeWidth / 2;
        dot = new Dot(x, y, radius, -100);
        dotsGroup.getChildren().add(dot);
        dots.add(dot);

        // player 2 accuumulator index = 27
        x = width - edgeWidth - unit / 2;
        y = edgeWidth / 2;
        dot = new Dot(x, y, radius, -200);
        dotsGroup.getChildren().add(dot);
        dots.add(dot);

        return dotsGroup;
    }

    public void drawChecker(int pointNum, int playerNum) {
        Color color = null;
        Color edgeColor = null;
        if (playerNum == 1) {
            color = checkerColorForPlayer1;
            edgeColor = checkerEdgeColorForPlayer1;
        }
        if (playerNum == 2) {
            color = checkerColorForPlayer2;
            edgeColor = checkerEdgeColorForPlayer2;
        }
        if (pointNum >= 0 && pointNum <= 23) {
            int NumberOfCheckers = pointsImageArray.get(pointNum).getChildren().size();

            int i = pointNum > 11 ? pointNum - 12 : Math.abs(pointNum - 11);

            double displacement = 2 * edgeWidth + unit + (i > 5 ? 4 * edgeWidth + unit : 0);
            double x = displacement + (i + 0.5) * unit;
            double y;
            int squint = (NumberOfCheckers % 11) % 6;

            double tilt;
            if (NumberOfCheckers < 6) {
                tilt = 0;
            } else if (NumberOfCheckers < 11) {
                tilt = unit / 6;
            } else {
                tilt = unit / 3;
            }

            if (pointNum <= 11) {
                y = height - edgeWidth - unit / 2 - squint * unit - tilt;
            } else {
                y = edgeWidth + unit / 2 + squint * unit + tilt;
            }
            CheckerImage checker = new CheckerImage(pointNum, playerNum, x, y, unit / 2);
            checker.setFill(color);
            checker.setStroke(edgeColor);
            checker.setStrokeWidth(2);
            //checker.setOnMouseClicked(me -> actionForMouseClickOnChecker(me));
            //checker.setOnMouseDragged(me -> actionForMouseDraggedOnChecker(me));
            //checker.setOnMouseReleased(me -> actionForMouseRealese(me));

            pointsImageArray.get(pointNum).getChildren().add(checker);
        }

        if (pointNum == -100) {
            //drawCheckerOnAccumilator(1);
        }
        if (pointNum == -200) {
            //drawCheckerOnAccumilator(2);
        }

    }

    public void removeChecker(int pointNum) {
        int numOfCheckers = pointsImageArray.get(pointNum).getChildren().size();
        pointsImageArray.get(pointNum).getChildren().remove(numOfCheckers - 1);
    }

    public CheckerImage drawFloatingChecker(int playerNum, double x, double y) {
        Color color = null;
        Color edgeColor = null;
        if (playerNum == 1) {
            color = checkerColorForPlayer1;
            edgeColor = checkerEdgeColorForPlayer1;
        }
        if (playerNum == 2) {
            color = checkerColorForPlayer2;
            edgeColor = checkerEdgeColorForPlayer2;
        }

        CheckerImage checker = new CheckerImage(0, playerNum, x, y, unit / 2);
        checker.setFill(color);
        checker.setStroke(edgeColor);
        checker.setStrokeWidth(2);
        //checker.setOnMouseClicked(me -> actionForMouseClickOnChecker(me));
        //checker.setOnMouseDragged(me -> actionForMouseDraggedOnChecker(me));
        //checker.setOnMouseReleased(me -> actionForMouseRealese(me));
        return checker;

    }

    public void drawCheckerOnBar(Integer playerNum) {
        Group group;
        if (playerNum == 1) {
            group = barForPlayer1;
        } else {
            group = barForPlayer2;
        }

        Color color = null;
        Color edgeColor = null;
        if (playerNum == 1) {
            color = checkerColorForPlayer1;
            edgeColor = checkerEdgeColorForPlayer1;
        }
        if (playerNum == 2) {
            color = checkerColorForPlayer2;
            edgeColor = checkerEdgeColorForPlayer2;
        }

        int NumberOfCheckers = group.getChildren().size();

        double x = width / 2;
        double y;
        int squint = (NumberOfCheckers % 10) % 5;

        double tilt;
        if (NumberOfCheckers < 5) {
            tilt = 0;
        } else if (NumberOfCheckers < 10) {
            tilt = unit / 6;
        } else {
            tilt = unit / 3;
        }

        if (playerNum == 1) {
            y = height * 0.4 - unit / 2 - squint * unit - tilt;

        } else {
            y = height * 0.6 + unit / 2 + squint * unit + tilt;
        }

        int pointNum = playerNum == 1 ? -10 : -20;
        CheckerImage checker = new CheckerImage(pointNum, playerNum, x, y, unit / 2);
        checker.setFill(color);
        checker.setStroke(edgeColor);
        checker.setStrokeWidth(2);
        // checker.setOnMouseClicked(me -> actionForMouseClickOnChecker(me));
        // checker.setOnMouseDragged(me -> actionForMouseDraggedOnChecker(me));
        // checker.setOnMouseReleased(me -> actionForMouseRealese(me));
        group.getChildren().add(checker);

    }

    public void drawCheckerOnAccumulator(int playerNum) {
        Group group;
        if (playerNum == 1) {
            group = accumulatorImageForPlayer1;
        } else {
            group = accumulatorImageForPlayer2;
        }

        Color color = null;
        Color edgeColor = null;
        if (playerNum == 1) {
            color = checkerColorForPlayer1;
            edgeColor = checkerEdgeColorForPlayer1;
        }
        if (playerNum == 2) {
            color = checkerColorForPlayer2;
            edgeColor = checkerEdgeColorForPlayer2;
        }

        int NumberOfCheckers = group.getChildren().size();
        double checkerHeight = 5 * unit / 15.0;
        double x = width - edgeWidth - unit;
        double y;

        if (playerNum == 1) {
            y = height - edgeWidth - 5 * unit + NumberOfCheckers * checkerHeight;

        } else {
            y = edgeWidth + 5 * unit - checkerHeight - NumberOfCheckers * checkerHeight;
        }

        Rectangle checker = new Rectangle(x, y, unit, checkerHeight);
        checker.setStroke(edgeColor);
        checker.setFill(color);
        checker.setArcWidth(width / 100);
        checker.setArcHeight(width / 160);

        group.getChildren().add(checker);

    }

    public void drawDicePair(int playerNum, int dice1, int dice2) {
        double x1 = 6 * edgeWidth + 8 * unit + 3 * unit - 0.8 * unit - width / 25;
        double x2 = 6 * edgeWidth + 8 * unit + 3 * unit + 0.8 * unit;
        double y1 = height * 0.5 - width / 50;
        double y2 = height * 0.5 - width / 50;
        Group group;

        group = drawDice(playerNum, dice1, x1, y1);
        diceGroup.getChildren().add(group);

        group = drawDice(playerNum, dice2, x2, y2);
        diceGroup.getChildren().add(group);

        x1 = x1 + width / 25 * 0.5;
        x2 = x2 + width / 25 * 0.5;
        y1 = height * 0.5;
        y2 = y1;
        double innerCircleRadius = width / 25 * 0.72;
        double outerCircleRadius = width / 25 * 0.81;

        Color color = Color.CYAN;
        innerCircleForDice1 = new Circle(x1, y1, innerCircleRadius);
        innerCircleForDice1.setFill(null);
        innerCircleForDice1.setStroke(color);
        innerCircleForDice1.setVisible(false);

        innerCircleForDice2 = new Circle(x2, y2, innerCircleRadius);
        innerCircleForDice2.setFill(null);
        innerCircleForDice2.setStroke(color);
        innerCircleForDice2.setVisible(false);

        outerCircleForDice1 = new Circle(x1, y1, outerCircleRadius);
        outerCircleForDice1.setFill(null);
        outerCircleForDice1.setStroke(color);
        outerCircleForDice1.setVisible(false);

        outerCircleForDice2 = new Circle(x2, y2, outerCircleRadius);
        outerCircleForDice2.setFill(null);
        outerCircleForDice2.setStroke(color);
        outerCircleForDice2.setVisible(false);

        diceGroup.getChildren().addAll(innerCircleForDice1, innerCircleForDice2, outerCircleForDice1, outerCircleForDice2);
        diceGroup.setVisible(true);
    }

    private Group drawDice(int playerNum, int dice, double x, double y) {
        double diceWidth = width / 25;
        double dotRadius = diceWidth / 8;
        Color diceColor;
        Color dotColor;
        if (playerNum == 1) {
            diceColor = Color.CORNSILK;
            dotColor = Color.HOTPINK;
        } else {
            diceColor = Color.CRIMSON;
            dotColor = Color.DARKBLUE;
        }
        if (dice == 6 || dice == 5) {
            dotRadius *= 0.9;
        }
        Group group = new Group();
        Rectangle r = new Rectangle(x, y, diceWidth, diceWidth);
        r.setFill(diceColor);
        r.setArcWidth(8);
        r.setArcHeight(8);
        group.getChildren().add(r);

        Circle d1 = new Circle(x + diceWidth * 0.5, y + diceWidth * 0.5, dotRadius); // center dot for 1, 3, 5
        d1.setFill(dotColor);

        Circle d2 = new Circle(x + diceWidth * 0.25, y + diceWidth * 0.25, dotRadius); // upper left corner dot for 2, 3, 4, 5, 6
        d2.setFill(dotColor);

        Circle d3 = new Circle(x + diceWidth * 0.75, y + diceWidth * 0.25, dotRadius); // upper right dot for 4, 5, 6
        d3.setFill(dotColor);

        Circle d4 = new Circle(x + diceWidth * 0.25, y + diceWidth * 0.75, dotRadius); // lower left dot for 5, 6, 4
        d4.setFill(dotColor);

        Circle d5 = new Circle(x + diceWidth * 0.75, y + diceWidth * 0.75, dotRadius); // lower right dot for 2, 3, 5, 6, 4
        d5.setFill(dotColor);

        Circle d6 = new Circle(x + diceWidth * 0.25, y + diceWidth * 0.5, dotRadius); // midle left dot for 6
        d6.setFill(dotColor);

        Circle d7 = new Circle(x + diceWidth * 0.75, y + diceWidth * 0.5, dotRadius); // midle right dot for 6
        d7.setFill(dotColor);

        switch (dice) {
            case 6:
                group.getChildren().addAll(new Circle[]{d2, d3, d4, d5, d6, d7});
                break;
            case 5:
                group.getChildren().addAll(new Circle[]{d1, d2, d3, d4, d5});
                break;
            case 4:
                group.getChildren().addAll(new Circle[]{d2, d3, d4, d5});
                break;
            case 3:
                group.getChildren().addAll(new Circle[]{d1, d2, d5});
                break;
            case 2:
                group.getChildren().addAll(new Circle[]{d2, d5});
                break;
            case 1:
                group.getChildren().add(d1);

        }

        return group;

    }

    public void hideDice() {
        if (diceGroup == null) {
            return;
        }
        diceGroup.setVisible(false);
    }

    public void drawCircles(List<Integer> rolls, int leftDice, int rightDice) {

        innerCircleForDice1.setVisible(false);
        innerCircleForDice2.setVisible(false);
        outerCircleForDice1.setVisible(false);
        outerCircleForDice2.setVisible(false);

        if (rolls.isEmpty()) {
            return;
        }
        if (rolls.size() == 1) {
            if (rolls.get(0) == leftDice) {
                innerCircleForDice1.setVisible(true);
            } else {
                innerCircleForDice2.setVisible(true);
            }
        }
        if (rolls.size() == 2) {
            innerCircleForDice1.setVisible(true);
            innerCircleForDice2.setVisible(true);
        }

        if (rolls.size() == 3) {
            innerCircleForDice1.setVisible(true);
            innerCircleForDice2.setVisible(true);
            outerCircleForDice1.setVisible(true);
        }

        if (rolls.size() == 4) {
            innerCircleForDice1.setVisible(true);
            innerCircleForDice2.setVisible(true);
            outerCircleForDice1.setVisible(true);
            outerCircleForDice2.setVisible(true);
        }
    }

    public void drawCheckers(Board board) {
        List<Point> points = board.getPoints();
        int numOfCheckers;
        int playerNum;
        for (int i = 0; i < pointsImageArray.size(); i++) {
            numOfCheckers = pointsImageArray.get(i).getChildren().size();
            pointsImageArray.get(i).getChildren().remove(0, numOfCheckers);
        }

        numOfCheckers = barForPlayer1.getChildren().size();
        barForPlayer1.getChildren().remove(0, numOfCheckers);

        numOfCheckers = barForPlayer2.getChildren().size();
        barForPlayer2.getChildren().remove(0, numOfCheckers);

        numOfCheckers = accumulatorImageForPlayer1.getChildren().size();
        accumulatorImageForPlayer1.getChildren().remove(0, numOfCheckers);

        numOfCheckers = accumulatorImageForPlayer2.getChildren().size();
        accumulatorImageForPlayer2.getChildren().remove(0, numOfCheckers);

        for (int i = 0; i < points.size(); i++) {
            numOfCheckers = points.get(i).getCheckers().size();
            playerNum = points.get(i).getOwner();
            for (int j = 0; j < numOfCheckers; j++) {
                drawChecker(i, playerNum);
            }
        }

        numOfCheckers = board.getBarForPlayer1().getCheckers().size();
        playerNum = 1;
        for (int j = 0; j < numOfCheckers; j++) {
            drawCheckerOnBar(playerNum);
        }

        numOfCheckers = board.getBarForPlayer2().getCheckers().size();
        playerNum = 2;
        for (int j = 0; j < numOfCheckers; j++) {
            drawCheckerOnBar(playerNum);
        }

        numOfCheckers = board.getAccumulatorForPlayer1().getCheckers().size();
        playerNum = 1;
        for (int j = 0; j < numOfCheckers; j++) {
            drawCheckerOnAccumulator(playerNum);
        }

        numOfCheckers = board.getAccumulatorForPlayer2().getCheckers().size();
        playerNum = 2;
        for (int j = 0; j < numOfCheckers; j++) {
            drawCheckerOnAccumulator(playerNum);
        }

    }

    public Group drawRedoButton(String text) {
        redoButtonGroup = new Group();
        double x = 6 * edgeWidth + 11 * unit;
        double y = 0.5 * height;
        redoText = new Text(text);

        Font font = new Font("Times New Roman", width / 30);
        redoText.setFont(font);
        redoText.setTextAlignment(TextAlignment.CENTER);
        redoText.setTextOrigin(VPos.TOP);
        x = x - redoText.prefWidth(height) / 2;
        y = y - redoText.prefHeight(width) / 2;
        redoText.setX(x);
        redoText.setY(y);

        redoText.setFill(Color.RED);
        redoText.setStroke(Color.BLUEVIOLET);

        Rectangle r = new Rectangle(x - redoText.prefWidth(-1) * 0.07, y, redoText.prefWidth(-1) * (1 + 0.28), redoText.prefHeight(-1));
        r.setArcWidth(5);
        r.setArcHeight(5);
        r.setFill(Color.YELLOW);
        r.setStroke(Color.RED);

        redoButtonGroup.getChildren().addAll(r, redoText);

        redoButtonGroup.setVisible(false);
        //redoText.setOnMouseClicked(me -> actionForRedoRollButtonClicked(me)); 
        return redoButtonGroup;

    }

    public void showRedoButton() {
        redoButtonGroup.setVisible(true);
    }

    public void hideRedoButton() {
        redoButtonGroup.setVisible(false);
    }

    public void setRedoText(ButtonText text) {
        redoText.setText(text.toString());
    }

    public Group getRedoButtonGroup() {
        return redoButtonGroup;
    }

    public Text getRedoButtonText() {
        return redoText;
    }

    public List<Group> getPointsImageArray() {
        return pointsImageArray;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public List<Rectangle> getPointRegionsArray() {
        return pointRegionsArray;
    }

    public Group getBarForPlayer1() {
        return barForPlayer1;
    }

    public Group getBarForPlayer2() {
        return barForPlayer2;
    }

    public Group getAccumulatorImageForPlayer1() {
        return accumulatorImageForPlayer1;
    }

    public Group getAccumulatorImageForPlayer2() {
        return accumulatorImageForPlayer2;
    }

    public Rectangle getBarForPlayer1Region() {
        return barForPlayer1Region;
    }

    public Rectangle getBarForPlayer2Region() {
        return barForPlayer2Region;
    }

    public Rectangle getAccumulatorImageForPlayer1Region() {
        return accumulatorImageForPlayer1Region;
    }

    public Rectangle getAccumulatorImageForPlayer2Region() {
        return accumulatorImageForPlayer2Region;
    }

    public Text getRemainingPointCounterPlayer1() {
        return remainingPointCounterPlayer1;
    }

    public Text getRemainingPointCounterPlayer2() {
        return remainingPointCounterPlayer2;
    }
    
    

    public int showGameOverDialog(String playerName) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Backgammon");
        alert.setHeaderText("Player " + playerName + " won!");
        alert.setContentText("Play again");

        ButtonType buttonTypeOne = new ButtonType("Play again");
        ButtonType buttonTypeTwo = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            return 1;
        } else if (result.get() == buttonTypeTwo) {
            return 2;

        } else {
            return 0;
        }
    }

}
