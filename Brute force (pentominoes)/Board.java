

/**
* Class representing a board state and functions to manipulate the board
*
* @author v1.0	Project group 13 (2015)
 * @author v2.0 Raffaele Piccini
*/
public class Board {
	
	
	/**
	 * Stores the state of the board
	 */
	private int[][] board;

	/**
	 * @param w width of the board
	 * @param h height of the board
	 */
	public Board(int w, int h){
		board = new int[h][w];
	}

	/**
	 * Sets a block if it's inside the bounds and non-overlapping
	 *
	 * @param x x coordinate of the block
	 * @param y y coordinate of the block
	 * @param value the value the block has to be set to
	 * @return whether or not it succeeded in placing the block
	 */
	public boolean setBlock(int x, int y, int value){
		// check bounds
		if(x >= 0 && x < board[0].length && y >= 0 && y < board.length){
			// check overlap
			if(board[y][x] == 0 && value != 0){
				board[y][x] = value;
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a pentomino to the board if it's fully inside the bounds and non-overlapping
	 *
	 * @param x x coordinate of the pentomino
	 * @param y y coordinate of the pentomino
	 * @param p Pentomino to place
	 * @param variation variation of the pentomino to use
	 * @param value number to place on the board (index of the piece)
	 * @return whether or not it succeeded in placing the pentomino
	 */
	public boolean addPentomino(int x, int y, Pentomino p, int variation, int value){
		int[][] pentomino = p.getVariation(variation);
		// variable to track if the pentomino is compeltely inside the board
		boolean inside = true;
		for(int i = 0; i < pentomino.length; i++){
			for(int j = 0; j < pentomino[0].length; j++){
				// if we have to add a block at this coordinate,
				// add it and test if it's inside or outide
				// and act accordingly
				if(pentomino[i][j] != 0){
					// boolean a = setBlock(i + x, j + y, value);
					// inside = inside && a;
					if(!setBlock(i + x, j + y, value)){
						return false;
					}
				}
			}
		}
		return inside;
	}
	/**
	 * Removes a pentomino from the board
	 *
	 * @param x x coordinate of the pentomino
	 * @param y y coordinate of the pentomino
	 * @param p Pentomino to place
	 * @param variation variation of the pentomino to use
	 * @param value number of the pentomino on the board (index of the piece)
	 */
	public void removePentomino(int x, int y, Pentomino p, int variation, int value){
		for(int i = y; i < this.board.length; i++){
			for(int j = x; j < this.board[0].length; j++){
				if(this.board[i][j] == value){
					this.board[i][j] = 0;
				}
			}
		}
	}

	/**
	 * Compares 2 boards for equality
	 */
	public boolean equals(Board b){
		for(int i = 0; i < this.board.length; i++){
			for(int j = 0; j < this.board[0].length; j++){
				if(this.board[i][j] != b.board[i][j]){
					return false;
				}
			}
		}
		return true;
	}

	// public boolean isSubset(Board b){
	// 	for(int i = 0; i < this.board.length; i++){
	// 		for(int j = 0; j < this.board[0].length; j++){
	// 			// whenever a non-empty space isn't equal to the equavalent space in the other board we know it isn't a subset
	// 			if(this.board[i][j] != 0 && this.board[i][j] != b.board[i][j]){
	// 				return false;
	// 			}
	// 		}
	// 	}
	// 	return true;
	// }

	/**
	 * @return The current state of the board
	 */
	public int[][] getBoard(){
		return this.board;
	}

	/**
	 * Creates a copy of the board
	 * @return a copy of the board
	 */
	public Board copy(){
		Board copy = new Board(this.board[0].length, this.board.length);
		for(int i = 0; i < this.board.length; i++){
			for(int j = 0; j < this.board[0].length; j++){
				copy.board[i][j] = this.board[i][j];
			}
		}
		return copy;
	}

	/**
	 * @return textual representation of the board
	 */
	public String toString(){
		String s = new String();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				s += Integer.toHexString(board[i][j]) + " ";
			}
			s += "\n";
		}
		return s;
	}
}