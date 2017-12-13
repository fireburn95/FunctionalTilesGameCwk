package app;

import static lib.List.streamToList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lib.List;
import lib.PicHashMap;
import lib.Picture;

public class Game {
	
	/* *******************************************************************************
	 * 									Helpers
	 *********************************************************************************/
	
	private enum NUMBERS{
		empty, one, two, three, four, five,
		six, seven, eight, nine
	}
	
	/* *******************************************************************************
	 * 									Public Methods
	 *********************************************************************************/
	
	public static List<Tile> startNewGame() {
		//Setup the ASCII art seeded from text file
		final PicHashMap picmap = setPicturesToMap();
		
		//Create a 'board' with random placement
		final List<Tile> board = setTilesToBoard(picmap);
		
		//Return the board
		return board;
	}
	
	public static Picture display(List<Tile> board) {
		//Convert the List of Tiles into a list of pictures (As the ordering mechanism is no longer needed)
		//Group each three Pictures into a List of a List of Pictures
		//Then 'join' side-by-side each List of Picture into a single Picture
		List<Picture> rows = Tile.tileToPicList(board).group(3)
		.map(list -> Picture.spread(list, Picture.CTR));
		
		//Take each 'row' in the List of Pictures, and stack them on top of each other
		Picture grid = Picture.stack(rows.group(rows.length()).at(0), Picture.CTR);
		
		//Return the final picture
		return grid;
	}
	
	public static List<Tile> moveUp(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition >= ((3 * 3) - 3)) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = swapElements(zeroPosition, zeroPosition+3, board);
		
		//Return board
		return boardSwapped;
	}
	
	public static List<Tile> moveDown(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition < 3) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = swapElements(zeroPosition, zeroPosition-3, board);
		
		//Return board
		return boardSwapped;
	}
	
	public static List<Tile> moveLeft(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition % 3 == 2) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = swapElements(zeroPosition, zeroPosition+1, board);
		
		//Return board
		return boardSwapped;
	}
	
	public static List<Tile> moveRight(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		if(zeroPosition % 3 == 0) {
			return board;
		}
		
		//Swap the two elements
		final List<Tile> boardSwapped = swapElements(zeroPosition, zeroPosition-1, board);
		
		//Return board
		return boardSwapped;
	}
	
	public static boolean isComplete(List<Tile> board) {
		//Generate a Stream of 0..7
		final IntStream a = IntStream.iterate(0, n -> n + 1).limit(8);
		
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
	
	private static PicHashMap setPicturesToMap() {
		//Create a HashMap Wrapper
		final PicHashMap picmap = new PicHashMap();
		
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
	
	private static List<Tile> setTilesToBoard(PicHashMap picmap) {
		//A lazy 'infinite' stream essentially mimicking a while loop
		//Make a stream of Lists of unique random Integers 0..8
		//Then perform the method to check if it is solvable and keep the list if true
		//Then gets the first element
		List<Integer> randomSolvableNumbers = 
				Stream.iterate(0, n -> n + 1)
				.map(n -> randomZeroToEight())
				.filter(n -> isSolvable(n)==true)
				.limit(1)
				.findFirst()
				.get();
		
		
		//Take the list of random numbers 0..8
		//Create a Stream of Tile's using the index to get the name of the picture from the hashmap
		List<Tile> associated = 
				randomSolvableNumbers
				.map(numb -> 
					new Tile(picmap.get(NUMBERS.values()[numb].toString()).toString(), 
					numb));
		

		//Return the stream above as a list
		return associated;
			
	}

	private static List<Integer> randomZeroToEight() {
		//Produce a stream of distinct Integers 0..8
		Stream<Integer> random0to8 = new Random().ints(0,9).distinct().limit(9).mapToObj(n -> n);
		
		//Return as a list
		return List.streamToList(random0to8);
	}
	
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
		Stream.iterate(0, n -> n + 1).limit(7)
		.forEach(index -> {
			
			numbsWithoutZero.toArrayList().subList(index+1, 8).stream()
			.forEach(value ->{
				if(numbsWithoutZero.toArrayList().get(index) > value) {countInversions[index]++;}
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
