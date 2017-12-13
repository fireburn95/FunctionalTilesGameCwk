package app;

import java.util.Scanner;

import lib.List;

public class Play {

	public static void main(String[] args) {
		
		//Scanner for reading in input
		Scanner s = new Scanner(System.in);
		
		//Create a Game Board
		List<Tile> board = Game.startNewGame();
		
		//Play the game
		boolean j = turn(0, board, s);
		
		//If complete...
		if(j==true) {
			System.out.println("Thanks for playing!");
		}
		
		

		
	}
	
	public static boolean turn(int count, List<Tile> board, Scanner s) {
		//Display the board
		System.out.println(Game.display(board) + "\n\n--------------------\n");

		
		//Check if Complete
		if(Game.isComplete(board)) {
			System.out.println("You Succesfully completed the game in " + count + " moves");
			return true;
		}
		
		//Read move and update board
		//Then display the board and recursively perform the next action
		switch (s.nextLine()) {
		case "u":
			board = Game.moveUp(board);
			return turn(count+1, board, s);
		case "d":
			board = Game.moveDown(board);
			return turn(count+1, board, s);
		case "l":
			board = Game.moveLeft(board);
			return turn(count+1, board, s);
		case "r":
			board = Game.moveRight(board);
			return turn(count+1, board, s);
		default:
			System.out.println("Incorrect move, please use only u, d, l and r");
			return turn(count, board, s);
		}
		
		
	}
}
