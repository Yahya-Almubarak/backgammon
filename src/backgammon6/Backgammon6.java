/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6;

import backgammon6.backgammonModel.Board;
import backgammon6.backgammonModel.Dice;
import backgammon6.backgammonModel.Move;
import backgammon6.backgammonModel.Player;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import static java.util.stream.Collectors.toList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Programmer
 */
public class Backgammon6 extends Application {

    private GameBoard gameBoard;
    private Group boardImage;
    private List<Group> pointsImageArray;
    private List<Dot> dots;

    private Board board;
    private Dice dice1;
    private Dice dice2;
    private Player player1;
    private Player player2;
    private List<Move> moves;
    private List<Integer> rolls;
    private List<Integer> playedRolls;
    private int leftDice;
    private int rightDice;
    private boolean dragFlag;
    private int pointNumToMoveFrom;
    private CheckerImage checkerToBeMoved;
    private CheckerImage movingChecker;
    private double movingCheckerX;
    private double movingCheckerY;
    private List<Rectangle> pointRegionsArray;
    private int currentPlayer;
    private Text remainingPointCounterPlayer1;
    private Text remainingPointCounterPlayer2;

    private Stack<State> states;

    @Override
    public void start(Stage primaryStage) {

        gameBoard = new GameBoard();
        boardImage = gameBoard.drawBoard();
        Scene scene = new Scene(boardImage);
        primaryStage.setTitle("Backgammon by Yahya Almubarak");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        startBoard();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void playerTurn(int playerNum, Dice dice1, Dice dice2, Board board, GameBoard gameBoard) {
        states.removeAllElements();
        gameBoard.getRedoButtonGroup().setVisible(false);

        currentPlayer = playerNum;
        int roll1 = dice1.getRoll();
        int roll2 = dice2.getRoll();
        // int roll1 = 4;
        // int roll2 = 5;
        leftDice = roll1;
        rightDice = roll2;
        playedRolls = new ArrayList<>();
        rolls = board.validRolls(playerNum, roll1, roll2, playedRolls);
        gameBoard.drawDicePair(playerNum, roll1, roll2);
        gameBoard.drawCircles(rolls, roll1, roll2);
        //moves = board.validMoves(playerNum, rolls);
        moves = board.validExtendedMoves(playerNum, rolls);
        if (rolls.isEmpty() || moves.isEmpty()) {
            gameBoard.getRedoButtonText().setText("Stop");
            gameBoard.getRedoButtonGroup().setVisible(true);
            currentPlayer = playerNum == 1 ? 2 : 1;
            final LocalTime firstTime = LocalTime.now();
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long seconds = Math.abs(Duration.between(LocalTime.now(), firstTime).getSeconds());

                    if (seconds >= 2) {
                        gameBoard.getRedoButtonGroup().setVisible(false);
                        playerTurn(currentPlayer, dice1, dice2, board, gameBoard);
                        this.stop();
                    }

                }
            }.start();

        }

    }

    public void actionForRedoButtonMouseClick(MouseEvent me) {

        if (gameBoard.getRedoButtonText().getText().equals(ButtonText.Start.toString())) {
            gameBoard.hideRedoButton();
            startGame();
        }

        if (gameBoard.getRedoButtonText().getText().equals(ButtonText.Redo.toString())) {
            popState();
        }

    }

    private void actionForMouseOnPointDragged(MouseEvent me) {
        Group pointImage = (Group) me.getSource();
        int pointNum = pointsImageArray.indexOf(pointImage);

        if (pointNum == 24) {
            pointNum = -10;
        }
        if (pointNum == 25) {
            pointNum = -20;
        }

        int playerNum = board.getPointOwner(pointNum);
        if (playerNum != currentPlayer) {
            return;
        }
        moves = board.validExtendedMoves(playerNum, rolls);
        if (!board.isPointMovable(pointNum, moves)) {
            return;
        }
        pointNumToMoveFrom = pointNum;
        dragFlag = true;

        // to turn dot color to red when ckecker is taken away from point
        if ((pointNum >= 0 && pointNum <= 23) && !pointImage.contains(me.getX(), me.getY())) {
            dots.get(pointNum).showDot(DotState.Red);
            dots.get(pointNum).setVisible(true);
        }
        int numberOfTopChecker;
        CheckerImage checkerImage = null;

        if (pointNum >= 0 && pointNum <= 23) {
            numberOfTopChecker = pointImage.getChildren().size() - 1;
            checkerImage = (CheckerImage) pointImage.getChildren().get(numberOfTopChecker);
        } else if (pointNum == -10) {
            numberOfTopChecker = gameBoard.getBarForPlayer1().getChildren().size() - 1;
            checkerImage = (CheckerImage) gameBoard.getBarForPlayer1().getChildren().get(numberOfTopChecker);
        } else if (pointNum == -20) {
            numberOfTopChecker = gameBoard.getBarForPlayer2().getChildren().size() - 1;
            checkerImage = (CheckerImage) gameBoard.getBarForPlayer2().getChildren().get(numberOfTopChecker);
        }

        //if (checker.contains(me.getX(), me.getY())) {
        if (checkerImage != null) {
            checkerImage.setVisible(false);
        }
        checkerToBeMoved = checkerImage;
        //CheckerImage movingChecker1;
        // }

        if (!(movingCheckerX == me.getX() && movingCheckerY == me.getY())) {
            if (movingChecker != null) {

                boardImage.getChildren().remove(movingChecker);

            }
            movingChecker = gameBoard.drawFloatingChecker(playerNum, me.getX(), me.getY());
            boardImage.getChildren().add(movingChecker);
            // movingChecker = movingChecker1;
            movingCheckerX = me.getX();
            movingCheckerY = me.getY();

        }

    }

    private void actionForMouseOnPointRealeased(MouseEvent me) {

        if (dragFlag == false) {
            return;
        }
        dragFlag = false;
        if (checkerToBeMoved == null) {
            return;
        }
        dots.stream().forEach((Dot d) -> d.showDot(DotState.Dim));
        checkerToBeMoved.setVisible(true);
        boardImage.getChildren().remove(movingChecker);

        int fromPointNum;
        int toPointNum;
        int playerNum;

        playerNum = checkerToBeMoved.getPlayerNum();
        //fromPointNum = checkerToBeMoved.getPointNum() - 1;
        fromPointNum = pointNumToMoveFrom;

        moves = board.validExtendedMoves(playerNum, rolls);

        for (toPointNum = 0; toPointNum <= 23; toPointNum++) {
            if (pointRegionsArray.get(toPointNum).contains(me.getX(), me.getY())) {
                break;
            }
        }

        if (gameBoard.getAccumulatorImageForPlayer1Region().contains(me.getX(), me.getY())) {
            toPointNum = -100;

        }

        if (gameBoard.getAccumulatorImageForPlayer2Region().contains(me.getX(), me.getY())) {
            toPointNum = -200;

        }
        final int toP = toPointNum; // final to lambda
        final int fromP = fromPointNum;
        Move move = null;
        List<Move> realMoves = moves.stream().filter(m -> ((m.getToPoint() == toP)
                || (toP == -100 && m.getToPoint() < 0 && m.getToPoint() >= - 6)
                || (toP == -200 && m.getToPoint() > 23 && m.getToPoint() <= 29))
                && (m.getFromPoint() == fromP)).collect(toList());
        if (realMoves.isEmpty()) {
            return;
        } else {
            if (realMoves.size() != 2) {
                move = realMoves.get(0);
            } else {
                if (realMoves.size() == 2) {
                    Move move1 = realMoves.get(0);
                    Move move2 = realMoves.get(1);
                    if (move1.getInvolvedRolls().size() <= 1) {
                        return;
                    }
                    if (move1.getInvolvedRolls().get(0) > move1.getInvolvedRolls().get(1)) {
                        move = move1;
                    } else {
                        move = move2;
                    }

                }
            }
        }
        //gameBoard.removeChecker(fromPointNum);
        //gameBoard.drawChecker(toPointNum + 1, playerNum);
        State state = new State(board, currentPlayer, rolls, playedRolls);

        // Move realMove = moves.stream().filter(m -> m.getFromPoint() == fromP && m.getToPoint() == toP
        //  ).collect(toList()).get(0);
        if (move != null && board.moveChecker(move)) {
            pushState(state);
            gameBoard.drawCheckers(board);

            List<Integer> involvedRolls = move.getInvolvedRolls();

            for (int i = 0; i < involvedRolls.size(); i++) {
                playedRolls.add(involvedRolls.get(i));
            }

            rolls = board.validRolls(playerNum, leftDice, rightDice, playedRolls);
            //   moves = board.validMoves(playerNum, rolls);
            moves = board.validExtendedMoves(playerNum, rolls);
            gameBoard.drawCircles(rolls, leftDice, rightDice);

        }

        if (!board.gameOver()) {
            if (rolls.isEmpty() || moves.isEmpty()) {
                currentPlayer = currentPlayer == 1 ? 2 : 1;
                playerTurn(currentPlayer, dice1, dice2, board, gameBoard);

            }
        } else {
            this.gameOver();
        }

        /*
        if (accumilatorImageForPlayer2.contains(me.getX(), me.getY()) && player2CanTearOff) {
            eraseChecker(fromPointNum);
            drawCheckerOnAccumilator(2);
            points.moveChecker(fromPointNum, -200);
        }*/
    }

    public void actionForMouseOnPointClicked(MouseEvent me) {

        Group pointImage = (Group) me.getSource();
        int pointNum = pointsImageArray.indexOf(pointImage);

        if (pointNum == 24) {
            pointNum = -10;
        } else if (pointNum == 25) {
            pointNum = -20;
        }

        int playerNum = board.getPointOwner(pointNum);
        if (playerNum != currentPlayer) {
            return;
        }

        rolls = board.validRolls(playerNum, leftDice, rightDice, playedRolls);

        if (rolls.isEmpty()) {
            return;
        }

        pointNumToMoveFrom = pointNum;
        dragFlag = false;
        State state = new State(board, currentPlayer, rolls, playedRolls);
        if (board.moveChecker(pointNum, rolls, playedRolls)) {
            pushState(state);
            gameBoard.drawCheckers(board);

            rolls = board.validRolls(playerNum, leftDice, rightDice, playedRolls);
            moves = board.validMoves(playerNum, rolls);

            //  moves = board.validExtendedMoves(playerNum, rolls);
            gameBoard.drawCircles(rolls, leftDice, rightDice);

        }

        if (!board.gameOver()) {
            if (rolls.isEmpty() || moves.isEmpty()) {
                currentPlayer = playerNum == 1 ? 2 : 1;
                playerTurn(currentPlayer, dice1, dice2, board, gameBoard);
            }
        } else {
            this.gameOver();
        }
    }

    public void actionForMouseOnPointEntered(MouseEvent me) {
        if (dragFlag == true) {
            return;
        }
        if (rolls.isEmpty()) {
            return;
        }
        dots.stream().forEach((Dot d) -> d.showDot(DotState.Dim));
        Group pointImage = (Group) me.getSource();
        int pointNum = pointsImageArray.indexOf(pointImage);

        if (pointNum == 24) {
            pointNum = -10;
        }
        if (pointNum == 25) {
            pointNum = -20;
        }

        int playerNum = board.getPointOwner(pointNum);
        if (playerNum != currentPlayer) {
            return;
        }

        List<Integer> acceptingPointsForThisPoint;
        moves = board.validExtendedMoves(playerNum, rolls);
        acceptingPointsForThisPoint = board.acceptingPointsFromMoves(pointNum, moves);
        if (acceptingPointsForThisPoint.isEmpty()) {
            return;
        }
        ListIterator<Integer> acceptingPointsIterator = acceptingPointsForThisPoint.listIterator();
        while (acceptingPointsIterator.hasNext()) {
            int index = acceptingPointsIterator.next();
            if (index == -100 || (index < 0 && index >= -6)) {
                index = 26;
            } else if (index == -200 || (index > 23 && index <= 29)) {
                index = 27;
            }
            dots.get(index).showDot(DotState.Green);
        }

    }

    public void actionForMouseOnPointExited(MouseEvent me) {
        if (dragFlag == true) {
            return;
        }
        //Platform.runLater(() -> dots.stream().forEach((Dot d) -> d.showDot(DotState.Dim)));
        dots.stream().forEach((Dot d) -> d.showDot(DotState.Dim));
        showDotsForMovables();
    }

    public void startBoard() {
        board = new Board();
        dice1 = new Dice();
        dice2 = new Dice();
        player1 = new Player(1, "Player_White");
        player2 = new Player(2, "Player_Black");

        playedRolls = new ArrayList<>();

        states = new Stack<>();

        gameBoard.drawCheckers(board);
        gameBoard.getRedoButtonGroup().setOnMouseClicked(me -> actionForRedoButtonMouseClick(me));
        pointsImageArray = gameBoard.getPointsImageArray();
        pointsImageArray.forEach((Group p) -> p.setOnMouseDragged(me -> actionForMouseOnPointDragged(me)));
        pointsImageArray.forEach((Group p) -> p.setOnMouseReleased(me -> actionForMouseOnPointRealeased(me)));
        pointsImageArray.forEach((Group p) -> p.setOnMouseClicked(me -> actionForMouseOnPointClicked(me)));
        pointsImageArray.forEach((Group p) -> p.setOnMouseEntered(me -> actionForMouseOnPointEntered(me)));
        pointsImageArray.forEach((Group p) -> p.setOnMouseExited(me -> actionForMouseOnPointExited(me)));
        dots = gameBoard.getDots();
        dots.stream().forEach((Dot d) -> d.showDot(DotState.Dim));
        pointRegionsArray = gameBoard.getPointRegionsArray();
        gameBoard.getBarForPlayer1Region().setOnMouseDragged(me -> actionForMouseOnPointDragged(me));
        gameBoard.getBarForPlayer1Region().setOnMouseClicked(me -> actionForMouseOnPointClicked(me));
        gameBoard.getAccumulatorImageForPlayer1Region().setOnMouseReleased(me -> actionForMouseOnPointRealeased(me));
        remainingPointCounterPlayer1 = gameBoard.getRemainingPointCounterPlayer1();
        remainingPointCounterPlayer2 = gameBoard.getRemainingPointCounterPlayer2();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                remainingPointCounterPlayer1.setText(Integer.toString(board.getRemainingPointCounter(1)));
                remainingPointCounterPlayer2.setText(Integer.toString(board.getRemainingPointCounter(2)));
            }
        }.start();
        gameBoard.hideDice();
        gameBoard.setRedoText(ButtonText.Start);
        gameBoard.showRedoButton();
        rolls = new ArrayList<>();
        moves = new ArrayList<>();
        //startGame();
    }

    private void startGame() {

        int roll1;
        int roll2;
        do {

            roll1 = dice1.getRoll();
            roll2 = dice2.getRoll();
        } while (roll1 == roll2);

        if (roll2 > roll1) {
            currentPlayer = 2;

        } else {
            currentPlayer = 1;

        }
        playerTurn(currentPlayer, dice1, dice2, board, gameBoard);

    }

    private void gameOver() {
        String winnerName = "";
        if (board.gameOver()) {
            if (board.isAccumulatorFull(1)) {
                winnerName = player1.getName();
            }
            if (board.isAccumulatorFull(2)) {
                winnerName = player2.getName();
            }
            int decision = gameBoard.showGameOverDialog(winnerName);
            if (decision == 1) {
                startBoard();
            }
            if (decision == 2 || decision == 0) {
                Platform.exit();
                System.exit(0);
            }
        }
    }

    private void showDotsForMovables() {
        int pointNum;
        for (int i = 0; i < dots.size(); i++) {
            pointNum = i;
            if (i == 24) {
                pointNum = -10;
            }
            if (i == 25) {
                pointNum = -20;
            }
            if (board.isPointMovable(pointNum, moves)) {
                dots.get(i).showDot(DotState.Yellow);
            }

        }
    }

    public void pushState(State state) {

        states.push(state);
        gameBoard.getRedoButtonText().setText(ButtonText.Redo.toString());
        gameBoard.getRedoButtonGroup().setVisible(true);
    }

    public void popState() {
        if (!states.isEmpty()) {
            State state = states.pop();
            board = state.getBoard();

            currentPlayer = state.getCurrentPlayer();
            rolls = state.getRolls();
            playedRolls = state.getPlayedRolls();
            gameBoard.drawCheckers(board);
            gameBoard.drawCircles(rolls, leftDice, rightDice);
            if (states.isEmpty()) {
                gameBoard.getRedoButtonGroup().setVisible(false);
            }
        }
    }

}
