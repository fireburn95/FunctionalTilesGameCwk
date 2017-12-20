package puzzle;

import static puzzle.List.streamToList;
import static puzzle.Picture.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class contains all the static methods to play the 3x3 Tile game (8-tile game).
 * It doesn't store the state of the game board, but rather creates and modifies it for
 * other classes to use. The general game board is represented by a List of Tiles,
 * and the display() method will represent it visually as a 3x3 board. The publicly
 * available methods are the creation, display and 'modification' of the game board,
 * and they use a majority of the other private methods which perform the actions,
 * often by using streams to assist with the actions on a List. The game can begin by
 * calling the startNewGame() method.
 * @author p13223165
 */
public class Game {
	
	/* *******************************************************************************
	 * 									Helpers
	 *********************************************************************************/
	
	/** Enum to store the values which will represent the ascii pictures from the text file*/
	private enum NUMBERS{
		empty, one, two, three, four, five,
		six, seven, eight, nine
	}
	
	/** Increment e.g. 0..1..2..3.. */
	private static UnaryOperator<Integer> increment = n -> n + 1;
	
	/* *******************************************************************************
	 * 									Public Methods
	 *********************************************************************************/
	/**
	 * Starts the game by returning the list of tiles necessary for the game to start, 
	 * It sets up the list by initiating the processes of mapping the picture to a name, 
	 * and then creating the tiles consisting of that picture, which contains further set-ups, 
	 * such as randomising the list. This happens behind the scenes, removing the workload for the
	 * 'client'/Play class.
	 * @return The starting List of Tiles generated in a random sequence
	 */
	public static List<Tile> startNewGame() {
		//Setup the ASCII art seeded from text file
		final HashMap<String, Picture> picmap = setPicturesToMap();
		
		//Create a 'board' with random placement
		final List<Tile> board = setTilesToBoard(picmap);
		
		//Return the board
		return board;
	}
	
	/**
	 * This method takes the state of the board and converts it into a single Picture display. This will 
	 * be called whenever you want a visual representation of the board (ideally after each turn). 
	 * It applies the design-elements of frames at relevant points to get a coherent-looking structure
	 * exclusively using higher-order functions. The list is grouped into three's, and then piled on top of
	 * each other via the methods in the Picture library, and that Picture is returned.
	 * @param board The List of Tiles to be printed
	 * @return The Picture grid-representation of the board
	 */
	public static Picture display(List<Tile> board) {
		//Fix the width of each tile (height is already same)
		//Group each three Pictures into a List of a List of Pictures
		//Then 'join' side-by-side each List of Picture into a single Picture
		//Frame at certain points to make a decent-looking display
		List<Picture> rows = board
			.map(tile -> tile.fixWidth(14, CTR, ' '))
			.map(tile -> tile.rightFrame())
			.group(3)
			.map(group -> group.map(tile -> tile.topFrame()))
			.map(list -> Picture.spread(list, CTR));
		
		//Take each 'row' in the List of Pictures, and stack them on top of each other
		//And add final frames
		Picture grid = Picture
				.stack(rows.group(rows.length()).at(0), Picture.CTR)
				.bottomFrame()
				.leftFrame();
		
		//Return the final picture
		return grid;
	}
	
	/**
	 * This method will be called whenever the client requests for an 'Up' move.
	 * It takes the List of Tiles, and calls the necessary helpers to return the
	 * new List of Tiles which will represent the new 'swapped' state of the board. 
	 * It initially uses a helper to find the position of 0/empty, then checks the move is
	 * valid (ie if 'empty' is on the bottom row). If invalid it just returns the original list,
	 * otherwise it will call the swapElements helper with the calculated parameters, and return
	 * the new list.
	 * @param board The List of Tiles representing the board
	 * @return The 'swapped' list of tiles, otherwise the original List of Tiles if invalidated.
	 */
	public static List<Tile> moveUp(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition >= ((3 * 3) - 3)) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = 
				swapElements(zeroPosition, zeroPosition+3, board);
		
		//Return board
		return boardSwapped;
	}
	
	/**
	 * This method will be called whenever the client requests for a 'Down' move.
	 * It takes the List of Tiles, and calls the necessary helpers to return the
	 * new List of Tiles which will represent the new 'swapped' state of the board. 
	 * It initially uses a helper to find the position of 0/empty, then checks the move is
	 * valid (ie if 'empty' is on the top row). If invalid it just returns the original list,
	 * otherwise it will call the swapElements helper with the calculated parameters, and return
	 * the new list.
	 * @param board The List of Tiles representing the board
	 * @return The 'swapped' list of tiles, otherwise the original List of Tiles if invalidated.
	 */
	public static List<Tile> moveDown(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition < 3) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = 
				swapElements(zeroPosition, zeroPosition-3, board);
		
		//Return board
		return boardSwapped;
	}
	
	/**
	 * This method will be called whenever the client requests for a 'Left' move.
	 * It takes the List of Tiles, and calls the necessary helpers to return the
	 * new List of Tiles which will represent the new 'swapped' state of the board. 
	 * It initially uses a helper to find the position of 0/empty, then checks the move is
	 * valid (ie if 'empty' is on the right row). If invalid it just returns the original list,
	 * otherwise it will call the swapElements helper with the calculated parameters, and return
	 * the new list.
	 * @param board The List of Tiles representing the board
	 * @return The 'swapped' list of tiles, otherwise the original List of Tiles if invalidated.
	 */
	public static List<Tile> moveLeft(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition % 3 == 2) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = 
				swapElements(zeroPosition, zeroPosition+1, board);
		
		//Return board
		return boardSwapped;
	}
	
	/**
	 * This method will be called whenever the client requests for a 'Right' move.
	 * It takes the List of Tiles, and calls the necessary helpers to return the
	 * new List of Tiles which will represent the new 'swapped' state of the board. 
	 * It initially uses a helper to find the position of 0/empty, then checks the move is
	 * valid (ie if 'empty' is on the left row). If invalid it just returns the original list,
	 * otherwise it will call the swapElements helper with the calculated parameters, and return
	 * the new list.
	 * @param board The List of Tiles representing the board
	 * @return The 'swapped' list of tiles, otherwise the original List of Tiles if invalidated.
	 */
	public static List<Tile> moveRight(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition % 3 == 0) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = 
				swapElements(zeroPosition, zeroPosition-1, board);
		
		//Return board
		return boardSwapped;
	}
	
	/**
	 * Checks if the List of Tiles is complete as to the rules of the 8-game
	 * i.e. ordering will be 1..2..3..4..5..6..7..8..empty..,.
	 * Uses a stream to make a list 0..7, and uses those values as the index to check
	 * that Tile's 1..8 are in order. If a Tile is out of place, it filters out that
	 * Tile, and at the end checks that all 8 elements remain in the stream, implying it is complete.
	 * @param board The List of Tiles representing a grid board to be checked for completeness.
	 * @return True if the board is complete (1..8, 0), else false
	 */
	public static boolean isComplete(List<Tile> board) {
		//Generate a Stream of 0..7
		final Stream<Integer> a = Stream.iterate(0, increment).limit(8);
		
		//Check that the tile at index i equals i+1, otherwise remove from list
		//i.e. index 0 is Tile 1
		final long result = 
				a
				.filter(value -> board.at(value).getNumber() == value+1)
				.count();

		//If all elements remain in the stream, then board is complete
		if(result == 8)
			return true;
		else
			return false;
	}
	
	
	/* *******************************************************************************
	 * 								Private/Helper Methods
	 *********************************************************************************/
	
	/**
	 * A helper method which processes the mapping of a name to an ASCII image. 
	 * The ASCII image is from the pictures.txt file, which contains one line of a
	 * name, and seven lines of ASCII art. This is therefore grouped by every 8 lines, 
	 * and the first String in the List of Strings is assigned as the key of the map, and the
	 * tail/remaining strings as the Value in a Picture representation. This hashmap is then
	 * returned to be used by another method.
	 * @return The Hashmap of Pictures and its numerical name as the key.
	 */
	private static HashMap<String, Picture> setPicturesToMap() {
		//Create a HashMap object
		final HashMap<String, Picture> picmap = new HashMap<>();
		
		//Get all the lines from the pictures text file
		//Group them per every 8 line
		//The 'key' is the first line of each group, which is the name/description
		//The value is the Picture object it creates
		try (Stream<String> lines = Files.lines(Paths.get("dat/pictures.txt"))) {
			
			streamToList(lines)
				.group(8)
				.listToStream()
				.forEach(group -> 
					picmap.put(group.head(), 
					new Picture(group.tail().map(Picture::stringToListOfCharacters)))
				);
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//Return the hashmap
		return picmap;
	}
	
	/**
	 * A helper method which uses the Hashmap of Ascii Art Pictures to generate the
	 * List of Tile's at the beginning of the game. It starts of by creating the
	 * List of Integers for the Tiles to be mapped to. The generation of this List includes
	 * calling the methods to generate random numbers, and calculate its solvability.
	 * This essentially mimics loops, and shows off functional techniques such as
	 * infinite streams, higher-order functions and laziness; a Stream of a 
	 * lists of Integers will be created until a list is 'solvable'. That list is then
	 * used to map the values to the 'numbers' of the Tiles, which is also used to retrieve
	 * the image from the hashmap.
	 * @param picmap The hashmap containing the name, and the ASCII art of an image of numbers
	 * @return The List of Tiles representing the initial state of the grid board
	 */
	private static List<Tile> setTilesToBoard(HashMap<String, Picture> picmap) {
		//A lazy 'infinite' stream essentially mimicking a while loop
		//Make a stream of Lists of unique random Integers 0..8
		//Then perform the method to check if it is solvable and keep the list if true
		//Then gets the first element
		List<Integer> randomSolvableNumbers = 
				Stream.iterate(0, increment)
					.map(numb -> randomZeroToEight())
					.filter(list -> isSolvable(list)==true)
					.limit(1)
					.findFirst()
					.get();
		
		
		//Take the list of random numbers 0..8
		//Create a Stream of Tile's using the index to get the name of the picture from the hashmap
		List<Tile> associated = 
				randomSolvableNumbers
				.map(numb -> 
					new Tile(
							picmap.get(NUMBERS.values()[numb].toString()).toString(), 
							numb)
					);
		

		//Return the stream above as a list
		return associated;
			
	}

	/**
	 * A helper method which generates a stream of numbers 0 to 8 in a random order.
	 * It uses the Random() class to generate a list of integers between 0 and 8 inclusive,
	 * then calls distinct() to make sure there are no duplicates. This stream of Integer
	 * objects are then converted to a List.
	 * @return A List of random, non-repeat numbers between 0 and 8.
	 */
	private static List<Integer> randomZeroToEight() {
		//Produce a stream of distinct Integers 0..8 inclusive
		Stream<Integer> random0to8 = 
				new Random().ints(0,9).distinct()
				.limit(9)
				.mapToObj(n -> n);
		
		//Return as a list
		return List.streamToList(random0to8);
	}
	
	/**
	 * A helper method which returns true or false regarding whether the List of Integers
	 * supplied is actually solvable. An explanation by Mark Ryan (2004) of the University
	 * of Birmingham explains this concept well. Not all combinations of numbers will be
	 * solvable in a 3x3 Tile Slider; the way to work this out is by calculating 'inversions'.
	 * Ignoring 0's/empty, you count the number of 'elements' succeeding the 'current element'
	 * which are smaller than the 'current element', for each 'element'. You then add all those
	 * values, and if the sum is even, then it is solvable, otherwise any odd value for a 3x3 board
	 * is unsolvable.
	 * @param numbersAsIs A list of random and distinct Integers (including the 0) between 0..8
	 * @return true if the sum of inversions is even, false if odd.
	 */
	private static boolean isSolvable(List<Integer> numbersAsIs) {
		//Create an array to store the count of the inversions for each index
		//Can use int variable too but useful for debugging
		final int[] countInversions = {0,0,0,0,0,0,0,0};
		
		//Remove 0 as it will always be 0, hence not included
		List<Integer> numbsWithoutZero = numbersAsIs
										.filter(numb -> numb!=0);
		
		//Create an index of 0..7
		//For each 'loop', create a sublist of the elements after it
		//For each element in the sublist...
		//..If it is smaller than the previous element, increment that index in the array
		Stream.iterate(0, increment).limit(7)
		.forEach(index -> {
			
			numbsWithoutZero.toArrayList().subList(index+1, 8).stream()
			.forEach(value ->{
				if(numbsWithoutZero.toArrayList().get(index) > value) countInversions[index]++;
			});
			
		});
		
		//Add all inversion counts together
		int resultOfInversions = 
				Arrays.stream(countInversions).reduce((a,b) -> a + b).getAsInt();
		
		//If Even then it is solvable, return true, else false
		if(resultOfInversions%2==0) 
			return true;
		else 						
			return false;
		
	}

	/**
	 * A helper method to swap two elements within a List of Tiles using Streams. The
	 * two index's are given, in addition to the List 'passed-by-value'. A Stream
	 * of Integers is generated to act as the index for the list, and if's are
	 * used to map it to the Tile at that position, or the swapped tile at the 'other'
	 * index. The stream is converted to a List and returned.
	 * @param index1 Position of the first Tile to be swapped
	 * @param index2 Position of the second Tile to be swapped
	 * @param board The List of Tile's which is to be changed.
	 * @return The List of Tile's with the index's changed.
	 */
	private static List<Tile> swapElements(int index1, int index2, List<Tile> board) {
		//Make a Stream of 0..8
		//For each index, map to its associated tile excluding the 'swap' tiles...
		//..which maps to the 'to be swapped with' tile
		Stream<Tile> s = IntStream.range(0, 9)
		.mapToObj(index -> {
			if(index==index1) return board.at(index2);
			else if(index==index2) return board.at(index1);
			else return board.at(index);
		});
		
		//Returns the List
		return List.streamToList(s);
	}
	
	/**
	 * A helper method to find the position of the '0'/empty tile in a List of Tiles
	 * for the game. Using the IntStream as an index, it checks if the tile at that 
	 * position in the list equals 0 by filtering it, keeping only the index that
	 * return true, and retrieving it as an int.
	 * @param board The List of Tiles to be searched for a 0 representation.
	 * @return The position of the Tile with the number 0.
	 */
	private static int findZero(List<Tile> board) {
		//Predicate to check if element equals 0
		IntPredicate isTileZero = index -> board.at(index).getNumber() == 0;
		
		//Create a stream of values 0..8
		//If 'Tile' at that index in the 'board' equals zero, then keep the index value
		//There should only be one result as values are unique, so retrieve
		return IntStream.range(0, board.length())
					.filter(isTileZero)
					.findFirst()
					.getAsInt();
	}

}

/*
 * Ryan, M. (2004). Solvability of the Tiles Game. [online] Software Workshop Java. 
 * Available at: http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html 
 * [Accessed 19 Dec. 2017].
*/