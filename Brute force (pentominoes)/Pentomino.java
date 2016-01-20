
/**
 * Class representing a single pentomino, including all variations
 *
 * @author v1.0	Project group 13 (2015)
 * @author v2.0 Raffaele Piccini
 */
public class Pentomino {
	
	/**
	 * Array of all the variation of the piece
	 */
	private int[][][] pentomino;

	/**
	 * @param pentomino array of all the variations. A variation is a 2d array of ints
	 */
	public Pentomino(int[][][] pentomino){
		this.pentomino = pentomino;
	}

	/**
	 * @return a single variation of the pentomino
	 */
	public int[][] getVariation(int variation){
		return pentomino[variation];
	}

	/**
	 * @return the amount of variations for this pentomino
	 */
	public int getVariations(){
		return pentomino.length;
	}
}