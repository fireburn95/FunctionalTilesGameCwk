package puzzle;

/**
 * This class extends Picture and is used to represent a Tile in the grid board of the
 * game. Each tile has an associated ID, which is immutable after initialisation. 
 * @author p13223165
 */
public class Tile extends Picture{
	
	/** The number/ID of the Tile, can only be assigned once */
	private final int number;

	/**
	 * Constructor for the Tile. It takes the string to construct the Picture, and a number
	 * representation. It calls the Picture classes constructor to construct the Picture
	 * state.
	 * @param string The content of the Tile
	 * @param number The ID of the Tile
	 */
	public Tile(String string, int number) {
		//Call Parent constructor
		super(string);
		
		//Assign ID
		this.number = number;
	}
	
	/**
	 * Standard getter to return the ID/number of the Tile.
	 * @return The state of the Number field.
	 */
	public int getNumber() {
		return number;
	}

	

}
