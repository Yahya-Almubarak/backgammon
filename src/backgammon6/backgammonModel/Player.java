/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6.backgammonModel;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author Programmer
 */
public class Player {

    int playerNum;
    String name;

    public Player(int playerNum, String name) {
        this.playerNum = playerNum;
        this.name = name;
    }

    public void play(Dice dice1, Dice dice2, Board board) {
        int roll1 = dice1.getRoll();
        int roll2 = dice2.getRoll();
        play(roll1, roll2, board);

    }
    
    public void play(int roll1, int roll2, Board board) {
        Scanner scanner = new Scanner(System.in);
        SecureRandom random = new SecureRandom();
        List<Integer> rolls = new ArrayList<>();
        List<Integer> playedRolls = new ArrayList<>();
        List<Move> moves;
        Move move;
        int moveSelected;
        
        //System.out.println("roll1 : " + roll1 + " roll2 :" + roll2);
        
        rolls = board.validRolls(playerNum, roll1, roll2, playedRolls);
        
        
       
        
        while(!rolls.isEmpty()) {
        moves = board.validMoves(playerNum, rolls);
       // ListIterator  rollsIterator =rolls.listIterator();
        
        /*
        for(int i =0; i < moves.size(); i++){
            System.out.println("move[" + (i+1) +"] : from: " + moves.get(i).getFromPoint() + 
                    " to: " + moves.get(i).getToPoint() + " roll: " + moves.get(i).getRoll());
        }
        */
            moveSelected = random.nextInt(moves.size());
         //   System.out.println("move selected : " + moveSelected);
            move = moves.get(moveSelected);
            
         
       /* while(rollsIterator.hasNext()) {
            int roll = (Integer)rollsIterator.next();
            if(move.getRoll()== roll) {
                rollsIterator.remove();
                break;
            }
        } */
        //System.out.println("rolls  after : " + rolls);
        
          board.moveChecker(move);   
          playedRolls.add(move.getRoll());
          rolls = board.validRolls(playerNum, roll1, roll2, playedRolls);
        }
            
        
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
