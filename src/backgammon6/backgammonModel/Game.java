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
public class Game {
    Board board = new Board();
    Dice dice1 = new Dice();
    Dice dice2 = new Dice();
    Player player1 = new Player(1, "Yahya");
    Player player2 = new Player(2, "John");
    
    public void startGame() {
        
        int roll1;
        int roll2;
        do {
            roll1 = dice1.getRoll();
            roll2 = dice2.getRoll();
        } while (roll1 == roll2);

        if (roll1 > roll2) {
            player1.play(dice1, dice2, board);
        }
        while(!gameOver()){
         player2.play(dice1, dice2, board);
         player1.play(dice1, dice2, board);
         
        }
        if(board.isAccumulatorFull(1)) {
            System.out.println("Player " + player1.name + " wins");
        } else if(board.isAccumulatorFull(2)){
            System.out.println("Player " + player2.name + " wins");
        } else {
            System.out.println("problem");
            
        }
    }
    
    public boolean gameOver() {
        if(board.isAccumulatorFull(1) || board.isAccumulatorFull(2)) {
            return true;
        } else {
            return false;
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Dice getDice1() {
        return dice1;
    }

    public void setDice1(Dice dice1) {
        this.dice1 = dice1;
    }

    public Dice getDice2() {
        return dice2;
    }

    public void setDice2(Dice dice2) {
        this.dice2 = dice2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
    
    
}
