package app;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lib.List;
import lib.Picture;

public class Test {
	public static void main(String[] args) {
		
		
		Game g = new Game();
		List<Tile> board = g.startNewGame();
		System.out.println(g.display(board) + "\n\n--------------------\n");
		board = g.up(board);
		System.out.println(g.display(board) + "\n\n--------------------\n");
		
	}
}
