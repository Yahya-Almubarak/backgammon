/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Programmer
 */
public class Board {

    List<Point> points;
    Point barForPlayer1;
    Point barForPlayer2;
    Point accumulatorForPlayer1;
    Point accumulatorForPlayer2;

    public Board() {
        barForPlayer1 = new Point(-10);
        barForPlayer1.setOwner(1);
        barForPlayer2 = new Point(-20);
        barForPlayer2.setOwner(2);
        accumulatorForPlayer1 = new Point(-100);
        accumulatorForPlayer1.setOwner(1);
        accumulatorForPlayer2 = new Point(-200);
        accumulatorForPlayer2.setOwner(2);
        points = distributeCheckers();

    }

    public boolean moveChecker(int fromPoint, int toPoint) {
        //moves checkers between somePoints and take care of moving bolled checker to bar;
        // it moves checkers to accumulator of player 2 if the toPoint exceeds 24 for player 2 
        // it moves checkers to accumulator of player 1 if the toPoint is between  -5 and 0  for player 1 
        // if the fromPoint = -10 then move from bar 1 and ensure that toPoint is in player2 home
        // if the fromPoint = -20 then move from bar 2 and ensure that toPoint is in player1 home
        // ensure that the move is legal and return true if the move is done
        if (isPointEmpty(fromPoint)) {
            return false;
        }

        int playerNum;

        Checker checker;
        Checker boll;
        if (fromPoint == -10) {
            playerNum = 1;
            if ((toPoint >= 18 && toPoint <= 23)
                    && !points.get(toPoint).isPointClosed(playerNum)) {
                checker = barForPlayer1.removeChecker();
                if (isOneChecker(toPoint) && playerNum != getPointOwner(toPoint)) {
                    boll = points.get(toPoint).removeChecker();  // one checker of opponent
                    barForPlayer2.addChecker(boll);
                }
                points.get(toPoint).addChecker(checker);
                return true;
            } else {
                return false;
            }

        } else if (fromPoint == -20) {
            playerNum = 2;
            if ((toPoint >= 0 && toPoint <= 5)
                    && !points.get(toPoint).isPointClosed(playerNum)) {
                checker = barForPlayer2.removeChecker();
                if (isOneChecker(toPoint) && playerNum != getPointOwner(toPoint)) {
                    boll = points.get(toPoint).removeChecker();  // one checker of opponent
                    barForPlayer1.addChecker(boll);
                }
                points.get(toPoint).addChecker(checker);
                return true;
            } else {
                return false;
            }
        }  // fromPoint != -10 , -20

        if ((fromPoint >= 0 && fromPoint <= 23) && (toPoint >= 0 && toPoint <= 23)) {
            playerNum = points.get(fromPoint).getOwner();

            if (playerNum == 1 && toPoint >= fromPoint) { // check for the right direction
                return false;
            }
            if (playerNum == 2 && toPoint <= fromPoint) {
                return false;
            }

            if (points.get(toPoint).isPointClosed(playerNum)) {
                return false;
            }

            checker = points.get(fromPoint).removeChecker();

            if (getPointOwner(toPoint) != playerNum) {
                if (isOneChecker(toPoint)) {
                    boll = points.get(toPoint).removeChecker();
                    if (playerNum == 1) {
                        barForPlayer2.addChecker(boll);
                    } else {
                        barForPlayer1.addChecker(boll);
                    }
                } //more than one checker of opponent's
            }// same Player

            points.get(toPoint).addChecker(checker);
            return true;
        }
        if ((fromPoint >= 0 && fromPoint <= 5) && ((toPoint < 0 && toPoint >= -6) || toPoint == -100)) {
            playerNum = points.get(fromPoint).getOwner();
            if (playerNum == 1 && isPlayerReadyToTearOff(playerNum)) {
                if (toPoint != -1) {
                    if (!isUpperPointsEmpty(fromPoint)) {
                        return false;
                    }
                }
                checker = points.get(fromPoint).removeChecker();
                accumulatorForPlayer1.addChecker(checker);
                return true;
            } else {
                return false;
            }
        }

        if ((fromPoint >= 18 && fromPoint <= 23) && ((toPoint > 23 && toPoint <= 29) || toPoint == -200)) {
            playerNum = points.get(fromPoint).getOwner();
            if (playerNum == 2 && isPlayerReadyToTearOff(playerNum)) {
                if (toPoint != 24) {
                    if (!isUpperPointsEmpty(fromPoint)) {
                        return false;
                    }
                }
                checker = points.get(fromPoint).removeChecker();
                accumulatorForPlayer2.addChecker(checker);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean moveChecker(Move move) {
        // facility method to help calling moveChecker( fromPoint, toPoint
        int toPoint = move.getToPoint();
        if (move.getToPoint() == -100) {
            toPoint = move.getFromPoint() - move.getInvolvedRolls().get(0);
        }
        if (move.getToPoint() == -200) {
            toPoint = move.getFromPoint() + move.getInvolvedRolls().get(0);
        }
        int sign = getPointOwner(move.getFromPoint()) == 1 ? -1 : +1;
        if (move.getInvolvedRolls().size() == 1) {

            return moveChecker(move.getFromPoint(), toPoint);
        }
        if (move.getInvolvedRolls().size() == 2) {
            int fromPoint1 = move.getFromPoint();
            int toPoint1 = fromPoint1 + sign * move.getInvolvedRolls().get(0);
            int fromPoint2 = toPoint1;
            int toPoint2 = fromPoint2 + sign * move.getInvolvedRolls().get(1);
            return moveChecker(fromPoint1, toPoint1) && moveChecker(fromPoint2, toPoint2);
        }
        if (move.getInvolvedRolls().size() == 3) {
            int fromPoint1 = move.getFromPoint();
            int toPoint1 = fromPoint1 + sign * move.getInvolvedRolls().get(0);
            int fromPoint2 = toPoint1;
            int toPoint2 = fromPoint2 + sign * move.getInvolvedRolls().get(1);
            int fromPoint3 = toPoint2;
            int toPoint3 = fromPoint3 + sign * move.getInvolvedRolls().get(2);
            return moveChecker(fromPoint1, toPoint1) && moveChecker(fromPoint2, toPoint2) && moveChecker(fromPoint3, toPoint3);
        }
        if (move.getInvolvedRolls().size() == 4) {
            int fromPoint1 = move.getFromPoint();
            int toPoint1 = fromPoint1 + sign * move.getInvolvedRolls().get(0);
            int fromPoint2 = toPoint1;
            int toPoint2 = fromPoint2 + sign * move.getInvolvedRolls().get(1);
            int fromPoint3 = toPoint2;
            int toPoint3 = fromPoint3 + sign * move.getInvolvedRolls().get(2);
            int fromPoint4 = toPoint3;
            int toPoint4 = fromPoint4 + sign * move.getInvolvedRolls().get(3);
            return moveChecker(fromPoint1, toPoint1) && moveChecker(fromPoint2, toPoint2)
                    && moveChecker(fromPoint3, toPoint3) && moveChecker(fromPoint4, toPoint4);
        }

        return false;

    }

    public boolean moveChecker(int fromPoint, List<Integer> rolls, List<Integer> playedRolls) {
        if (rolls.isEmpty()) {
            return false;
        }
        List<Move> moves;
        List<Move> suggestedMoves = new ArrayList<>();
        int playerNum = getPoint(fromPoint).getOwner();
        moves = validMoves(playerNum, rolls);

        if (!isPointMovable(fromPoint, moves)) {
            return false;
        }
        List<Integer> acceptingPointsForThisPoint = acceptingPointsFromMoves(fromPoint, moves, suggestedMoves);

        if (acceptingPointsForThisPoint.isEmpty()) {
            return false;
        }

        // Optional<Move> moveOptional = suggestedMoves.stream().reduce((m, n) -> {if(m.getRoll() >= n.getRoll())  return m; });
        Move move;

        Iterator<Move> suggestedMovesIterator = suggestedMoves.iterator();
        move = suggestedMovesIterator.next();
        int maxRoll = move.getRoll();
        while (suggestedMovesIterator.hasNext()) {
            Move tempMove = suggestedMovesIterator.next();
            if (tempMove.getRoll() > maxRoll) {
                move = tempMove;
                maxRoll = move.getRoll();
            }
        }

        //  Move move = moveOptional.get();
        if (moveChecker(move)) {

            Iterator<Integer> rollsIterator = rolls.iterator();
            while (rollsIterator.hasNext()) {
                if (rollsIterator.next() == move.getRoll()) {
                    rollsIterator.remove();
                    playedRolls.add(move.getRoll());
                    break;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public boolean moveCheckerToBar(int fromPoint) {
        // check the owner of point and decide which bar to move to
        int playerNum = points.get(fromPoint).getOwner();
        int toPoint = playerNum == 1 ? -10 : -20;
        return moveChecker(fromPoint, toPoint);
    }

    public boolean moveCheckerFromBar(int toPoint, int playerNum) {
        int fromPoint = playerNum == 1 ? -10 : -20;
        return moveChecker(fromPoint, toPoint);
    }

    public boolean moveCheckerToAccumilator(int fromPoint) {
        // deduce accumilator from point owner
        int playerNum = points.get(fromPoint).getOwner();
        int toPoint = playerNum == 1 ? -100 : -200;
        return moveChecker(fromPoint, toPoint);
    }

    public List<Move> validMoves(int playerNum, List<Integer> rolls) {
        return validMoves(playerNum, rolls, false);
    }

    public List<Move> validMoves(int playerNum, List<Integer> rolls, boolean extended) {

        Set<Move> movesSet1 = new HashSet<>();
        Set<Move> movesSet2 = new HashSet<>();
        Set<Move> movesSet3 = new HashSet<>();
        Set<Move> movesSet4 = new HashSet<>();
        Set<Move> movesSet;

        Set<Move> eXmovesSet1 = new HashSet<>();
        Set<Move> eXmovesSet2 = new HashSet<>();
        Set<Move> eXmovesSet3 = new HashSet<>();
        Set<Move> eXmovesSet4 = new HashSet<>();
        Set<Move> eXmovesSet;

        MoveTreeNode moveTreeParent = new MoveTreeNode();
        moveTreeParent.setParent(null);
        constructTree(moveTreeParent, this, playerNum, rolls);

        Iterator<MoveTreeNode> level1Iterator = moveTreeParent.getChildren();
        while (level1Iterator.hasNext()) {
            MoveTreeNode moveNode1 = level1Iterator.next();
            Move move1 = moveNode1.getElement();
            movesSet1.add(move1);
            eXmovesSet1.add(move1);
            Iterator<MoveTreeNode> level2Iterator = moveNode1.getChildren();

            while (level2Iterator.hasNext()) {
                movesSet2.add(move1);
                MoveTreeNode moveNode2 = level2Iterator.next();
                Move move2 = moveNode2.getElement();
                eXmovesSet2.add(move1);
                Move eXmove2 = null;
                if (move1 != null && move1.getToPoint() == move2.getFromPoint()) {
                    eXmove2 = new Move(move1.getFromPoint(), move2.getToPoint());
                    eXmove2.addRoll(move1.getRoll());
                    eXmove2.addRoll(move2.getRoll());
                    eXmovesSet2.add(eXmove2);
                }

                Iterator<MoveTreeNode> level3Iterator = moveNode2.getChildren();
                while (level3Iterator.hasNext()) {

                    movesSet3.add(move1);
                    MoveTreeNode moveNode3 = level3Iterator.next();
                    Move move3 = moveNode3.getElement();
                    eXmovesSet3.add(move1);
                    if (eXmove2 != null) {
                        eXmovesSet3.add(eXmove2);
                    }
                    Move eXmove3 = null;
                    if (eXmove2 != null && eXmove2.getToPoint() == move3.getFromPoint()) {
                        eXmove3 = new Move(eXmove2.getFromPoint(), move3.getToPoint());
                        eXmove3.addRoll(eXmove2.getInvolvedRolls());
                        eXmove3.addRoll(move3.getInvolvedRolls());

                        eXmovesSet3.add(eXmove3);
                    }
                    Iterator<MoveTreeNode> level4Iterator = moveNode3.getChildren();
                    while (level4Iterator.hasNext()) {
                        movesSet4.add(move1);
                        MoveTreeNode moveNode4 = level4Iterator.next();
                        Move move4 = moveNode4.getElement();
                        eXmovesSet4.add(move1);
                        if (eXmove2 != null) {
                            eXmovesSet4.add(eXmove2);
                        }
                        if (eXmove3 != null) {
                            eXmovesSet4.add(eXmove3);
                        }

                        if (eXmove3 != null && eXmove3.getToPoint() == move4.getFromPoint()) {
                            Move eXmove4;
                            eXmove4 = new Move(eXmove3.getFromPoint(), move4.getToPoint());
                            eXmove4.addRoll(eXmove3.getInvolvedRolls());
                            eXmove4.addRoll(move4.getInvolvedRolls());
                            eXmovesSet4.add(eXmove4);
                        }

                    }
                }
            }
        }

        if (!movesSet4.isEmpty()) {
            movesSet = movesSet4;
        } else if (!movesSet3.isEmpty()) {
            movesSet = movesSet3;
        } else if (!movesSet2.isEmpty()) {
            movesSet = movesSet2;
        } else if (!movesSet1.isEmpty()) {
            movesSet = movesSet1;
        } else {
            return null;
        }

        if (!eXmovesSet4.isEmpty()) {
            eXmovesSet = eXmovesSet4;
        } else if (!eXmovesSet3.isEmpty()) {
            eXmovesSet = eXmovesSet3;
        } else if (!eXmovesSet2.isEmpty()) {
            eXmovesSet = eXmovesSet2;
        } else if (!eXmovesSet1.isEmpty()) {
            eXmovesSet = eXmovesSet1;
        } else {
            return null;
        }
        List<Move> moves = new ArrayList<>();
        Iterator<Move> movesSetIterator;
        movesSetIterator = movesSet.iterator();
        while (movesSetIterator.hasNext()) {
            moves.add(movesSetIterator.next());
        }
        Iterator<Move> movesListIterator;
        movesListIterator = moves.iterator();
        while (movesListIterator.hasNext()) {
            Move move = movesListIterator.next();

            int toPoint = move.getToPoint();
            int fromPoint = move.getFromPoint();
            int roll = move.getRoll();
            if ((toPoint == -100) || (toPoint < 0 && toPoint >= -6)) {
                playerNum = 1;
                if ((isPlayerReadyToTearOff(playerNum) && isUpperPointsEmpty(fromPoint))
                        || ((isPlayerReadyToTearOff(playerNum) && !isUpperPointsEmpty(fromPoint) && (roll >= fromPoint + 1)))) {
                } else {
                    movesListIterator.remove();
                    eXmovesSet.remove(move);
                }
            }
        }

        List<Move> eXmoves = new ArrayList<>();
        Iterator<Move> eXmovesSetIterator = eXmovesSet.iterator();
        while (eXmovesSetIterator.hasNext()) {
            eXmoves.add(eXmovesSetIterator.next());
        }

        if (extended) {
            return eXmoves;
        } else {
            return moves;
        }

    }

    public void constructTree(MoveTreeNode moveTreeParent, Board board, int playerNum, List<Integer> rolls) {
        if (rolls == null || rolls.isEmpty()) {
            return;
        }
        Board board1;
        board1 = board.clone();
        if (!board1.isBarEmpty(playerNum)) {
            int p = playerNum == 1 ? -10 : -20;
            innerMethodForTreeConstruction(rolls, board1, p, moveTreeParent, playerNum);
        } else {
            for (int p = 0; p < 24; p++) {
                innerMethodForTreeConstruction(rolls, board1, p, moveTreeParent, playerNum);
            }
        }

    }

    private void innerMethodForTreeConstruction(List<Integer> rolls, Board board, int p, MoveTreeNode moveTreeParent, int playerNum) {
        Board board1;
        int roll;
        int fromPoint;
        int toPoint;
        Move move;
        int sign = playerNum == 1 ? -1 : +1;
        List<Integer> rolls1 = new ArrayList<>();
        for (int r = 0; r < rolls.size(); r++) {
            rolls1.add(rolls.get(r));
        }
        if (rolls1.size() == 1) {

            board1 = board.clone();
            roll = rolls1.get(0);
            fromPoint = p;
            if (!board1.isBarEmpty(playerNum)) {
                toPoint = playerNum == 1 ? 24 - roll : roll - 1;
            } else {
                toPoint = p + sign * roll;
            }
            move = new Move(fromPoint, toPoint);
            move.addRoll(roll);
            if (board1.moveChecker(move)) {
                MoveTreeNode moveNode = new MoveTreeNode();
                moveNode.setParent(moveTreeParent);
                moveNode.setElement(move);
                moveTreeParent.addChild(moveNode);
            }
        } else if (rolls1.size() == 2 && rolls1.get(0).intValue() != rolls1.get(1).intValue()) {
            List<Integer> rolls2 = new ArrayList<>();
            board1 = board.clone();
            roll = rolls1.get(0);
            fromPoint = p;
            if (!board1.isBarEmpty(playerNum)) {
                toPoint = playerNum == 1 ? 24 - roll : roll - 1;
            } else {
                toPoint = p + sign * roll;
            }
            move = new Move(fromPoint, toPoint);
            move.addRoll(roll);
            if (board1.moveChecker(move)) {
                MoveTreeNode moveNode = new MoveTreeNode();
                moveNode.setParent(moveTreeParent);
                moveNode.setElement(move);
                moveTreeParent.addChild(moveNode);
                rolls2.add(rolls1.get(1));
                constructTree(moveNode, board1, playerNum, rolls2);
            }
            rolls2 = new ArrayList<>();
            board1 = board.clone();
            roll = rolls1.get(1);
            fromPoint = p;
            if (!board1.isBarEmpty(playerNum)) {
                toPoint = playerNum == 1 ? 24 - roll : roll - 1;
            } else {
                toPoint = p + sign * roll;
            }
            move = new Move(fromPoint, toPoint);
            move.addRoll(roll);
            if (board1.moveChecker(move)) {
                MoveTreeNode moveNode = new MoveTreeNode();
                moveNode.setParent(moveTreeParent);
                moveNode.setElement(move);
                moveTreeParent.addChild(moveNode);
                rolls2.add(rolls1.get(0));
                constructTree(moveNode, board1, playerNum, rolls2);
            }
        } else {
            board1 = board.clone();
            roll = rolls1.get(0);
            fromPoint = p;
            if (!board1.isBarEmpty(playerNum)) {
                toPoint = playerNum == 1 ? 24 - roll : roll - 1;
            } else {
                toPoint = p + sign * roll;
            }
            move = new Move(fromPoint, toPoint);
            move.addRoll(roll);
            if (board1.moveChecker(move)) {
                MoveTreeNode moveNode = new MoveTreeNode();

                moveNode.setParent(moveTreeParent);
                moveNode.setElement(move);
                moveTreeParent.addChild(moveNode);
                rolls1 = rolls1.subList(1, rolls1.size());
                constructTree(moveNode, board1, playerNum, rolls1);
            }
        }
    }

    public List<Integer> validRolls(int playerNum, int dice1, int dice2, List<Integer> playedRolls) {
        Board board = this;
        List<Integer> rolls = new ArrayList<>();
        List<Integer> initialRolls = new ArrayList<>();
        if (dice1 != dice2) {
            if (playedRolls.isEmpty()) {
                initialRolls.add(dice1);
                initialRolls.add(dice2);
            } else if (playedRolls.size() == 1) {
                initialRolls.add(dice1 == playedRolls.get(0) ? dice2 : dice1);
            } else {
                return rolls;
            }
        }

        if (dice1 == dice2) {
            for (int r = playedRolls.size(); r < 4; r++) {
                initialRolls.add(dice1);
            }

        }
        if (initialRolls.isEmpty()) {
            return rolls;
        }

        List<Move> moves = this.validExtendedMoves(playerNum, initialRolls);
        if (moves == null || moves.isEmpty()) {
            return rolls;
        }
        if (dice1 != dice2) {
            if (initialRolls.size() == 1) {
                Iterator<Move> movesIterator = moves.iterator();
                while (movesIterator.hasNext()) {
                    if (movesIterator.next().getRoll() == initialRolls.get(0)) {
                        rolls.add(initialRolls.get(0));
                    }
                }
            } else {
                Iterator<Move> movesIterator = moves.iterator();
                while (movesIterator.hasNext()) {
                    Move move = movesIterator.next();
                    if (move.getInvolvedRolls().size() == 1) {
                        if (move.getInvolvedRolls().get(0).intValue() == initialRolls.get(0).intValue()
                                || move.getInvolvedRolls().get(0).intValue() == initialRolls.get(1).intValue()) {
                            rolls.add(move.getInvolvedRolls().get(0));
                        }

                    } else if ((move.getInvolvedRolls().get(0).intValue() == initialRolls.get(0).intValue()
                            && move.getInvolvedRolls().get(1).intValue() == initialRolls.get(1).intValue())
                            || (move.getInvolvedRolls().get(1).intValue() == initialRolls.get(0).intValue()
                            && move.getInvolvedRolls().get(0).intValue() == initialRolls.get(1).intValue())) {
                        rolls.add(move.getInvolvedRolls().get(0));
                        rolls.add(move.getInvolvedRolls().get(1));
                    }
                }
            }
            if (rolls.size() > 1) {
                rolls = rolls.stream().distinct().collect(toList());
            }
            return rolls;
        } else {// dice1 == dice2
            Move move;
            int numOfMoveableCheckers = 0;
            ListIterator<Move> iterator = moves.listIterator();
            while (iterator.hasNext()) {
                move = iterator.next();
                numOfMoveableCheckers += board.getPoint(move.getFromPoint()).getCheckers().size();
            }

            if (numOfMoveableCheckers >= 1 && playedRolls.size() <= 3) {
                rolls.add(dice1);
            }
            if (numOfMoveableCheckers >= 2 && playedRolls.size() <= 2) {
                rolls.add(dice1);
            }
            if (numOfMoveableCheckers >= 3 && playedRolls.size() <= 1) {
                rolls.add(dice1);
            }
            if (numOfMoveableCheckers >= 4 && playedRolls.isEmpty()) {
                rolls.add(dice1);
            }
            return rolls;
        }

    }

    public Point getPoint(int pointNum) {
        if (pointNum >= 0 && pointNum <= 23) {
            return this.getPoints().get(pointNum);
        } else if (pointNum == -10) {
            return barForPlayer1;
        } else if (pointNum == -20) {
            return barForPlayer2;
        } else if (pointNum == -100) {
            return accumulatorForPlayer1;
        } else if (pointNum == -200) {
            return accumulatorForPlayer2;
        } else {
            return null;
        }

    }

    public boolean isPointEmpty(int pointNum) {
        switch (pointNum) {
            case -10:
                return isBarEmpty(1);
            case -20:
                return isBarEmpty(2);
            default:
                return points.get(pointNum).isPointEmpty();
        }
    }

    public boolean isOneChecker(int pointNum) {
        return points.get(pointNum).isOneChecker();
    }

    public int getPointOwner(int pointNum) {
        switch (pointNum) {
            case -10:
            case -100:
                return 1;
            case -20:
            case -200:
                return 2;
            default:
                return points.get(pointNum).getOwner();
        }

    }

    public boolean isBarEmpty(int playerNum) {
        if (playerNum == 1) {
            return (barForPlayer1.isPointEmpty());
        } else {
            return (barForPlayer2.isPointEmpty());
        }
    }

    public boolean isPlayerReadyToTearOff(int playerNum) {
        switch (playerNum) {
            case 1:
                if (!barForPlayer1.isPointEmpty()) {
                    return false;
                }
                for (int i = 6; i < 24; i++) {
                    if (points.get(i).getOwner() == 1) {
                        return false;
                    }
                }
                return true;
            case 2:
                if (!barForPlayer2.isPointEmpty()) {
                    return false;
                }
                for (int i = 0; i < 18; i++) {
                    if (points.get(i).getOwner() == 2) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public boolean isUpperPointsEmpty(int PointNum) {
        int playerNum = getPoint(PointNum).getOwner();
        switch (playerNum) {
            case 1:
                if (!barForPlayer1.isPointEmpty()) {
                    return false;
                }
                for (int i = PointNum + 1; i < 24; i++) {
                    if (points.get(i).getOwner() == 1) {
                        return false;
                    }
                }
                return true;
            case 2:
                // player 2
                if (!barForPlayer2.isPointEmpty()) {
                    return false;
                }
                for (int i = 0; i < PointNum; i++) {
                    if (points.get(i).getOwner() == 2) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public List<Integer> acceptingPoints(int pointNum, List<Integer> rolls) {
        // give the somePoints that would accept legal move from this point according to the given rolls
        // accumulators are given the numbers -100 and -200 for players 1 and player 2 respectively

        int playeranum = this.getPoint(pointNum).getOwner();
        int sign = playeranum == 1 ? -1 : 1;
        int toPoint;
        Set<Integer> acceptingPointsSet = new HashSet<>();
        List<Integer> acceptingPoints = new ArrayList<>();

        if (isPointEmpty(pointNum)) {
            return acceptingPoints;
        }

        Iterator<Integer> rollsIterator = rolls.iterator();
        while (rollsIterator.hasNext()) {
            toPoint = pointNum + sign * rollsIterator.next();
            if (toPoint >= 0 && toPoint <= 23) {
                if (points.get(toPoint).isPointClosed(playeranum)) {
                    continue;
                }
            } else if (!isPlayerReadyToTearOff(playeranum)) {
                continue;
            }
            acceptingPointsSet.add(toPoint);
        }
        acceptingPoints = acceptingPointsSet.stream().collect(toList());
        return acceptingPoints;
    }

    public List<Integer> acceptingPointsFromMoves(int pointNum, List<Move> eXmoves) {
        List<Move> suggestedMoves = new ArrayList<>();
        return acceptingPointsFromMoves(pointNum, eXmoves, suggestedMoves);
    }

    public List<Integer> acceptingPointsFromMoves(int pointNum, List<Move> eXmoves, List<Move> suggestedMoves) {
        Set<Integer> acceptingPointsSet = new HashSet<>();
        Set<Move> suggestedMovesSet = new HashSet<>();
        Iterator<Move> eXmovesIterator = eXmoves.iterator();
        Iterator<Integer> involvedRollsIterator;
        Move eXmove;
        while (eXmovesIterator.hasNext()) {
            eXmove = eXmovesIterator.next();
            if (eXmove.getFromPoint() == pointNum) {
                acceptingPointsSet.add(new Integer(eXmove.getToPoint()));
                suggestedMovesSet.add(eXmove);
            }
        }
        List<Integer> acceptingPointsList = acceptingPointsSet.stream().collect(toList());
        suggestedMoves.addAll(suggestedMovesSet.stream().collect(toList()));
        return acceptingPointsList;

    }

    public boolean isAccumulatorFull(int playerNum) {
        if (playerNum == 1) {
            return accumulatorForPlayer1.getCheckers().size() == 15;
        } else {
            return accumulatorForPlayer2.getCheckers().size() == 15;
        }

    }

    private List<Point> distributeCheckers() {
        List<Point> somePoints = new ArrayList<>();

        for (int i = 1; i < 25; i++) {
            Point point = new Point(i);

            switch (i) {
                case 1:
                    addCheckersInitially(2, 2, point);
                    break;
                case 6:
                    addCheckersInitially(1, 5, point);
                    break;
                case 8:
                    addCheckersInitially(1, 3, point);
                    break;
                case 12:
                    addCheckersInitially(2, 5, point);
                    break;
                case 13:
                    addCheckersInitially(1, 5, point);
                    break;
                case 17:
                    addCheckersInitially(2, 3, point);
                    break;
                case 19:
                    addCheckersInitially(2, 5, point);
                    break;
                case 24:
                    addCheckersInitially(1, 2, point);

            }
            somePoints.add(point);
        }/*
        for (int i = 1; i < 25; i++) {
            Point point = new Point(i);

            switch (i) {
                case 1:
                    //  addCheckersInitially(1, 2, point);
                    break;
                case 2:
                    //   addCheckersInitially(1, 1, point);
                    break;
                case 3:
                    // addCheckersInitially(1, 1, point);
                    break;
                case 4:
                    //  addCheckersInitially(1, 3, point);
                    break;
                case 5:
                    //  addCheckersInitially(1, 6, point);
                    break;
                case 6:
                    //   addCheckersInitially(1, 4, point);
                    break;
                case 7:
                    // addCheckersInitially(1, 2, point);
                    break;
                case 8:
                    // addCheckersInitially(1, 2, point);
                    break;
                case 19:
                    addCheckersInitially(2, 3, point);
                    break;
                case 20:
                    addCheckersInitially(2, 2, point);
                    break;
                case 21:
                    addCheckersInitially(2, 1, point);
                    break;
                case 22:
                    addCheckersInitially(2, 2, point);
                    break;
                case 23:
                    addCheckersInitially(2, 1, point);
                    break;
                case 24:
                    addCheckersInitially(2, 11, point);
            }
            somePoints.add(point);
        }*/

        return somePoints;
    }

    private void addCheckersInitially(int playerNum, int checkersNo, Point point) {
        Checker checker;
        for (int i = 0; i < checkersNo; i++) {
            checker = new Checker(playerNum);
            point.addChecker(checker);
        }
    }

    public List<Move> validExtendedMoves(int playerNum, List<Integer> rolls) {
        return validMoves(playerNum, rolls, true);
    }

    public boolean gameOver() {
        return isAccumulatorFull(1) || isAccumulatorFull(2);
    }

    public boolean isPointMovable(int pointNum, List<Move> moves) {
        if (moves == null || moves.isEmpty()) {
            return false;
        }
        return moves.stream().anyMatch(m -> m.getFromPoint() == pointNum);

    }

    public boolean isPointMovableByRolls(int pointNum, List<Integer> rolls) {
        if (isPointEmpty(pointNum) || rolls == null || rolls.isEmpty()) {
            return false;
        }
        int playerNum = getPointOwner(pointNum);
        int sign = playerNum == 1 ? -1 : +1;
        boolean movable = false;
        for (int r = 0; r < rolls.size(); r++) {
            if (pointNum + sign * rolls.get(r) < 0 || pointNum + sign * rolls.get(r) > 23) {
                if (isPlayerReadyToTearOff(playerNum)) {
                    return true;
                }
            } else if (!getPoint(pointNum + sign * rolls.get(r)).isPointClosed(playerNum)) {
                movable = true;
            }
        }
        return movable;

    }

    public int getRemainingPointCounter(int playerNum) {
        int pointCounter = 0;
        for (int i = 0; i < 24; i++) {
            if (points.get(i).getOwner() == playerNum) {
                int pointValue = (playerNum == 1) ? i + 1 : 24 - i;
                pointCounter += points.get(i).getCheckers().size() * pointValue;
            }
        }
        if (playerNum == 1) {
            pointCounter += getBarForPlayer1().getCheckers().size() * 25;
        } else {
            pointCounter += getBarForPlayer2().getCheckers().size() * 25;
        }
        return pointCounter;
    }

    public boolean isHomeClosed(int playerNum) {
        if (playerNum == 1) {
            for (int i = 0; i < 6; i++) {
                if (!getPoint(i).isPointClosed(playerNum)) {
                    return false;
                }
            }
            return true;
        } else if (playerNum == 2) {
            for (int i = 18; i < 24; i++) {
                if (!getPoint(i).isPointClosed(playerNum)) {
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    public void printMoves(String s, List<Move> moves) {
        if (moves == null || moves.isEmpty()) {
            System.out.println(s + "   moves is null or empty");
        } else {
            for (int i = 0; i < moves.size(); i++) {
                System.out.println(s + " [" + (i + 1) + "] : from: " + moves.get(i).getFromPoint()
                        + " to: " + moves.get(i).getToPoint() + " roll: " + moves.get(i).getRoll());
            }
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    //// setter and getter
    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Point getBarForPlayer1() {
        return barForPlayer1;
    }

    public void setBarForPlayer1(Point barForPlayer1) {
        this.barForPlayer1 = barForPlayer1;
    }

    public Point getBarForPlayer2() {
        return barForPlayer2;
    }

    public void setBarForPlayer2(Point barForPlayer2) {
        this.barForPlayer2 = barForPlayer2;
    }

    public Point getAccumulatorForPlayer1() {
        return accumulatorForPlayer1;
    }

    public void setAccumulatorForPlayer1(Point accumulatorForPlayer1) {
        this.accumulatorForPlayer1 = accumulatorForPlayer1;
    }

    public Point getAccumulatorForPlayer2() {
        return accumulatorForPlayer2;
    }

    public void setAccumulatorForPlayer2(Point accumulatorForPlayer2) {
        this.accumulatorForPlayer2 = accumulatorForPlayer2;
    }

    @Override
    public Board clone() {

        Board board = new Board();

        List<Point> map = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            map.add(points.get(i).clone());
        }

        board.setPoints(map);
        board.setAccumulatorForPlayer1(accumulatorForPlayer1.clone());
        board.setAccumulatorForPlayer2(accumulatorForPlayer2.clone());
        board.setBarForPlayer1(barForPlayer1.clone());
        board.setBarForPlayer2(barForPlayer2.clone());
        return board;
    }

    @Override
    public String toString() {
        String s;
        s = "Board\n" + " points= \n";
        for (int i = 0; i < points.size(); i++) {
            s += points.get(i) + "\n";
        }
        s += "barForPlayer1= " + barForPlayer1 + ", barForPlayer2= " + barForPlayer2 + "\n";
        s += "accumulatorForPlayer1=" + accumulatorForPlayer1 + ", accumulatorForPlayer2=" + accumulatorForPlayer2;
        return s;
    }

}
