package app;

import java.util.Scanner;

import lib.List;

/**
 * The class which contains the Main method to be called by the client to begin the game.
 * @author p13223165
 */
public class Play {

	/**
	 * Main method to begin the game. Creates the Scanner for input, then calls the 
	 * startNewGame() method of the Game class which returns the List of Tiles/Board
	 * which is ready to play. Once the game is played via the turn() method recursively
	 * calculating the number of turns, it prints out the results.
	 * @param args Command-line arguments. Not relevant in this context.
	 */
	public static void main(String[] args) {
		
		//Scanner for reading in input
		Scanner s = new Scanner(System.in);
		
		//Create a Game Board
		List<Tile> board = Game.startNewGame();
		
		//Play the game
		int j = turn(board, s);
		
		//Print Result
		System.out.println("You Succesfully completed the game in " + j + " moves");		

		
	}
	
	/**
	 * The method which represents each turn in a game. It uses recursive techniques to
	 * be able to also calculate how many turns each game took. It starts off by
	 * displaying the board, then checking if the game is complete.
	 * If not complete, then it reads the prompted input, and calls the relevant action on
	 * the passed list, however, recursively so it can calculate the turns without storing the
	 * value in a count variable, or using loop iterations.
	 * @param board The List of Tiles created by Game.startNewGame() representing the game board
	 * @param s The scanner to read input via console
	 * @return An integer recursively incremented for each 'turn' when the method is called.
	 */
	public static int turn(List<Tile> board, Scanner s) {
		//Display the board
		System.out.println(Game.display(board) + "\n\n");

		
		//Check if Complete
		if(Game.isComplete(board)) {
			return 0;
		}
		
		//Prompt the next moves
		System.out.println("u - Up\t\td - Down\tl - Left\tr - Right\n");
		System.out.print("Enter next move: ");
		
		//Read move and update board
		//Then display the board and recursively perform the next action
		switch (s.nextLine()) {
		case "u":
			board = Game.moveUp(board);
			return 1+turn(board, s);
		case "d":
			board = Game.moveDown(board);
			return 1+turn(board, s);
		case "l":
			board = Game.moveLeft(board);
			return 1+turn(board, s);
		case "r":
			board = Game.moveRight(board);
			return 1+turn(board, s);
		default:
			System.out.println("Incorrect move, please use only u, d, l and r");
			return 0+turn(board, s);
		}
		
		
	}
}
