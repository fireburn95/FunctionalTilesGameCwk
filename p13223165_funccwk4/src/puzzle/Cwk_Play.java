package puzzle;

import java.util.Scanner;

/**
 * The class which contains the Main method to be called by the client to begin the game.
 * The analysis is available at the bottom of this page.
 * @author p13223165
 */
public class Cwk_Play {

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
		System.out.println("You Succesfully completed the game in " + j + " moves!!!");		

		
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
		case "u":case "U":
			board = Game.moveUp(board);
			return 1+turn(board, s);
		case "d":case "D":
			board = Game.moveDown(board);
			return 1+turn(board, s);
		case "l":case "L":
			board = Game.moveLeft(board);
			return 1+turn(board, s);
		case "r":case "R":
			board = Game.moveRight(board);
			return 1+turn(board, s);
		default:
			System.out.println("Incorrect move, please use only u, d, l and r");
			return 0+turn(board, s);
		}
		
		
	}
}


/*
 * In the Game Class - within the Static setTilesToBoard() method, contains the Stream
 * which I feel best represents the strengths of the functional style; 
 * It incorporates the use of Higher-Order Functions (HOF), Infinite Streams and 'Lazy Evaluation'
 * which are all aspects that Object-Oriented and Iterative-style programming cannot boast
 * (at least to the extent of 'ease of use' and effectivity). This code is shown below:
 
   List<Integer> randomSolvableNumbers = 
				Stream.iterate(0, increment)
					.map(numb -> randomZeroToEight())
					.filter(list -> isSolvable(list)==true)
					.limit(1)
					.findFirst()
					.get();

 *********************************************************************************
 * 									ANALYSIS									 *
 ********************************************************************************* 
 * The first line generates the infinite list of Integers, and you are aware of the way
 * this is performed as the UnaryOperator is descriptive. The next lines generate the List
 * of numbers and cleverly use a filter to call a boolean method to calculate the lists' solvability. 
 * It keeps the 'solvable' lists, and limits it to 1 element and retrieves it, which is when evaluation
 * is actually deemed necessary, showing its 'laziness'.
 * It is concise, as it essentially mimics a 'while loop' - including the predicate - 
 * without the use of one-time count variables. Line-by-line, it can
 * be read and understood effectively as it is essentially plain English, and the change of
 * objects and List of objects within the Stream is followable, showing how expressive it is.
 * This therefore makes it more maintainable, meaning someone else could modify my code with ease in the future.
 * The functional style is also practical, as it allows for a level of indentation which essentially
 * shows "what's next", making it far less cognitively straining than a loop with many paths.
 * The use of the lambda operators also improves readability and maintainability as it is used
 * here to specify the type of object contained within the Stream, allowing an initial glance
 * and in-depth look to get a better idea of what each HOF is performing to or with the pipe.	
 */










