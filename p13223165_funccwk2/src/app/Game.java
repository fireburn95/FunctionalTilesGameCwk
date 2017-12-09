package app;

import static lib.List.streamToList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.text.Position;

import lib.List;
import lib.PicHashMap;
import lib.Picture;

public class Game {
	
	
	private enum NUMBERS {
		empty, one, two, three, four, five,
		six, seven, eight, nine
	}
	
	public List<Tile> startNewGame() {
		//Setup the ASCII art seeded from text file
		final PicHashMap picmap = setPicturesToMap();
		
		//Create a 'board' with random placement
		List<Tile> board = setTilesToBoard(picmap);
		
		//Return the board
		return board;
	}
	
	private PicHashMap setPicturesToMap() {
		final PicHashMap picmap = new PicHashMap();
		
		try (Stream<String> lines = Files.lines(Paths.get("dat/pictures.txt"))) {
			streamToList(lines)
			.group(8)
			.listToStream()
			.forEach(group -> picmap.put(group.head(), new Picture(group.tail().map(Picture::stringToListOfCharacters))));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return picmap;
	}
	
	private List<Tile> setTilesToBoard(PicHashMap picmap) {
		
			//Generate a random stream of non-repeating numbers, and use them to map it to a stream of 'Tiles'
			Stream<Tile> s = List.arrayListToList(randomZeroToEight()).listToStream()
			.map(numb -> new Tile(picmap.get(NUMBERS.values()[numb].toString()).toString(), numb));
			
			//Return the stream above as a list
			return List.streamToList(s);
			
	}
	
	private ArrayList<Integer> randomZeroToEight() {
		//Create Local temp Array Lists
		ArrayList<Integer> a = new ArrayList<>(),
						   b = new ArrayList<>();
		
		//Create Random number generator
		Random r = new Random();
		
		//Generate numbers 0 to 8
		Stream.iterate(0, numb -> numb + 1).limit(9).forEach(numb -> a.add(numb));
		
		//Select a random number, remove from a, add to b, and repeat using stream elements as the bound
		Stream.iterate(8, numb -> numb -1).limit(9).forEach(numb -> b.add(a.remove(r.nextInt(numb+1))));

		//Return List of random numbers
		return b;
	}
	
	public List<Tile> up(List<Tile> board) {
		//Find the index of the Tile '0'
		final int zeroPosition = findZero(board);
		
		//Check if move is valid
		
		//Swap the two elements
		List<Tile> boardSwapped = swapElements(zeroPosition, zeroPosition+3, board);
		
		//Return board
		return boardSwapped;
		
	}
	
	private List<Tile> swapElements(int index1, int index2, List<Tile> board) {
		//Find the smaller and bigger indexes
		int ismall = index1 < index2 ? index1 : index2;
		int ilarge = index1 > index2 ? index1 : index2;
		
		//Temporary ArrayList
		//DO BETTER FUNCTIONAL HEREXXX
		ArrayList<Tile> temp = board.toArrayList();
		Collections.swap(temp, index1, index2);
		
		//Returns the List
		return List.arrayListToList(temp);
		
		
	}
	
	/*public boolean checkIfComplete(List<Tile> board) {
		return null;
	}*/
	
	private int findZero(List<Tile> board) {
		//Predicate to check if element equals 0
		IntPredicate isTileZero = index -> board.at(index).getNumber() == 0;
		
		//Find the index of the Tile '0'
		return IntStream.range(0, board.length())
					.filter(isTileZero)
					.findFirst()
					.getAsInt();
		
		
	}
	
	public Picture display(List<Tile> board) {
		Picture top = board.at(0).beside(board.at(1), Picture.CTR).beside(board.at(2), Picture.CTR);
		Picture middle = board.at(3).beside(board.at(4), Picture.CTR).beside(board.at(5), Picture.CTR);
		Picture bottom = board.at(6).beside(board.at(7), Picture.CTR).beside(board.at(8), Picture.CTR);
		Picture result = top.above(middle, Picture.CTR).above(bottom, Picture.CTR);
		return result;
	}

}
