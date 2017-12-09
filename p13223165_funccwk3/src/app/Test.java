package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lib.List;
import lib.Picture;

public class Test {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		List<Tile> board = Game.startNewGame();
		
		Stream.iterate(0, i -> i + 1)
		.map(turn -> Game.isComplete(board) ? new String("end"))
		.filter(Game.isComplete(board))
		
		
		
		/*Scanner s = new Scanner(System.in);
		List<Tile> board = Game.startNewGame();
		
		while(true) {
			if(Game.isComplete(board)) {
				System.out.println("U DID IT");
				break;
			}
			else {
				System.out.println("keep going blud");
			}
			System.out.println(Game.display(board));
			switch (s.nextLine()) {
			case "u":
				board = Game.moveUp(board);
				System.out.println(Game.display(board) + "\n\n--------------------\n");
				break;
			case "d":
				board = Game.moveDown(board);
				System.out.println(Game.display(board) + "\n\n--------------------\n");
				break;
			case "l":
				board = Game.moveLeft(board);
				System.out.println(Game.display(board) + "\n\n--------------------\n");
				break;
			case "r":
				board = Game.moveRight(board);
				System.out.println(Game.display(board) + "\n\n--------------------\n");
				break;
			}
		}
		*/
		
	}
}
