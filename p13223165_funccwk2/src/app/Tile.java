package app;

import lib.List;
import lib.Picture;

public class Tile extends Picture{
	
	private final int number;

	public Tile(String string, int number) {
		super(string);
		this.number = number;
	}
	
	public Tile(List<List<Character>> lines, int number) {
		super(lines);
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}

	

}
