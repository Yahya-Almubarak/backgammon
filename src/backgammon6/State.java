/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backgammon6;

import backgammon6.backgammonModel.Board;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Programmer
 */
public class State {
    
    private Board board;
    private int currentPlayer;
    private List<Integer> rolls;
    private List<Integer> playedRolls;

    public State(Board board, int currentPlayer, List<Integer> rolls, List<Integer> playedRolls) {
        this.board = board.clone();
        this.currentPlayer = currentPlayer;
        this.rolls = new ArrayList<>();
        for(int i = 0; i< rolls.size(); i++) {
            this.rolls.add(rolls.get(i).intValue());
        }
        this.playedRolls = new ArrayList<>();
        for(int i = 0; i< playedRolls.size(); i++) {
            this.playedRolls.add(playedRolls.get(i).intValue());
        }
    }

    
    
    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board.clone();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public void setRolls(List<Integer> rolls) {
       this.rolls = new ArrayList<>();
        for(int i = 0; i< rolls.size(); i++) {
            this.rolls.add(rolls.get(i).intValue());
        }
    }

    public List<Integer> getPlayedRolls() {
        return playedRolls;
    }

    public void setPlayedRolls(List<Integer> playedRolls) {
        this.playedRolls = new ArrayList<>();
        for(int i = 0; i< playedRolls.size(); i++) {
            this.playedRolls.add(playedRolls.get(i).intValue());
        }
    }

    
    

   
    
    
    
    
    
}
