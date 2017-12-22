package puzzle;

import java.util.Scanner;
import java.util.stream.Stream;

/**
 * The class which contains the Main method to be called by the client to begin the game.
 * The analysis is available at the bottom of this page.
 * @author p13223165
 */
public class Cwk_StartPlay {

	/**
	 * Main method to begin the game. Creates the Scanner for input, then uses a Stream for iteration, 
	 * calling startNewGame() method of the Game class which returns the List of Tiles/Board into the arguments
	 * of turn(), which contains the structure to play. Once the game is played via the turn() method recursively
	 * calculating the number of turns, it prints out the results, and the Stream (which is lazy) stops once
	 * the turns() method returns false. This therefore allows multiple playthroughs until the user is finished.
	 * @param args Command-line arguments. Not relevant in this context.
	 */
	public static void main(String[] args) {
		
		//Scanner for reading in input
		Scanner s = new Scanner(System.in);
		
		//'Infinite Stream' which for each element, plays the game.
		//Plays the game by calling the turn() method and storing the boolean result in the stream
		//Plays until 'filter' picks up a false element, implying the user requested an end to the game
		//It's lazy so will not generate the infinite stream, but only keep going until it returns false
		//Limit's to one elements (which will only be false) and prints out a message
		Stream.iterate(0, n -> n + 1)
		.map(n -> turn(0, Game.startNewGame(), s))
		.filter(n -> n==false)
		.limit(1)
		.forEach(n -> System.out.println("Thank you for playing!"));
		
	}
	
	/**
	 * The method which represents each turn in a game. It uses recursive techniques to
	 * be able to also calculate how many turns each game took. It starts off by prompting the user
	 * whether or not they want to play (returning false if they don't) at the beginning of each round.
	 * Then for each turn, displays the board, then checks if the game is complete.
	 * If not complete, it reads the prompted input, and calls the relevant action on
	 * the passed list, however, recursively so it can calculate the turns without storing the
	 * value in a count variable, or using loop iterations.
	 * This method uses recursion, therefore it could potentially be draining on the performance of a system
	 * if too many turns are played, but for the small scope of the programme, it should have no trouble
	 * getting through a few games.
	 * @param board The List of Tiles created by Game.startNewGame() representing the game board
	 * @param s The scanner to read input via console
	 * @return An integer recursively incremented for each 'turn' when the method is called.
	 */
	public static boolean turn(int turns, List<Tile> board, Scanner s) {
		//Check if user wants to play
		if(turns==0) {
			System.out.println("Type 'a' to start a new game, or 'enter' to exit.");
			switch(s.nextLine()) {
				case "a": case "A":
					break;
				default:
					return false;
						
			}
		}
		
		//Display the board
		System.out.println(Game.display(board) + "\n\n");

		
		//Check if Complete
		if(Game.isComplete(board)) {
			System.out.println("You Succesfully completed the game in " + turns + " moves!!!");		
			return true;
		}
		
		//Prompt the next moves
		System.out.println("u - Up\t\td - Down\tl - Left\tr - Right\n");
		System.out.print("Enter next move: ");
		
		//Read move and update board
		//Then display the board and recursively perform the next action
		switch (s.nextLine()) {
		case "u":case "U":
			board = Game.moveUp(board);
			return turn(turns + 1, board, s);
		case "d":case "D":
			board = Game.moveDown(board);
			return turn(turns + 1, board, s);
		case "l":case "L":
			board = Game.moveLeft(board);
			return turn(turns + 1, board, s);
		case "r":case "R":
			board = Game.moveRight(board);
			return turn(turns + 1, board, s);
		default:
			System.out.println("Incorrect move, please use only u, d, l and r");
			return turn(turns, board, s);
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

 ********************************************************************************************************************
 * 									                    ANALYSIS			                                        *
 ********************************************************************************************************************
 * I feel that the example provided above is a perfect example of the benefits that adopting a Functional Style could 
 * provide. The syntax is expressive because of how descriptive it is; a novice should have a better understanding of 
 * it when trying to follow it through, compared to trying to comprehend the complex structuring and multiple paths of 
 * loops and if's with different paths. On a surface-level, it reads like: 'Generate an infinite stream, and map those 
 * elements to a list of random numbers, then if the list is defined as solvable, keep it, and retrieve the first 
 * (and only) list that remains'. Being able to simply translate it acts as a great learning tool for new 
 * programmers, who often learn traditional styles anyway in pseudocode, which maintains many of the flaws of the style
 * it is based of. The use of methods and operators add to the level of expression, meaning that a developer could
 * further enhance the readability of their operations.
 * It is concise as it essentially mimics a 'while' loop - including the predicate - without the use of temporary 
 * count variables, and the amount of clutter is minimal (arguably not even present in the selected example).
 * Easy-to-read and 'avoidance of clutter and to the point' code is beneficial as it helps improve maintainability,
 * particularly if someone else was to work on my code in the future; identifying where an addition of a filter
 * or a modification of an algorithm i.e. randomZeroToEight is far easier to visualise due to the straightforward,
 * pipe-like nature. The functional style is also practical, as it allows for a level of indentation which essentially
 * shows "what's next", making it far less cognitively straining than a loop with many paths. The use of the lambda 
 * operators also improves readability and maintainability as it is used here to specify the type of object contained 
 * within the Stream, allowing an initial glance and in-depth look to get a better idea of what each HOF is performing
 * to or with the pipe.	
 */






