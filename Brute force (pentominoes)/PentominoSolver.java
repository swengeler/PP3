
/**
 * Main class, containing the algorithm to solve the pentomino puzzle.
 *
 * @author v1.0	Project group 13 (2015)
 * @author v2.0 Raffaele Piccini
 */
public class PentominoSolver {
	
	private static final boolean PRINT = false;
	
	private static int X;
	private static int Y;
	private static int P;
	private static int L;
	private static int T;

	private static final Pentomino PENTOMINO_P = new Pentomino(
		new int[][][]{
			{
				{1,1,0,0,0},
				{1,1,0,0,0},
				{1,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,0,0,0},
				{1,1,0,0,0},
				{0,1,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,0,0,0,0},
				{1,1,0,0,0},
				{1,1,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,1,0,0,0},
				{1,1,0,0,0},
				{1,1,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,0,0,0},
				{1,1,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,1,0,0},
				{1,1,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,1,1,0,0},
				{1,1,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,1,0,0},
				{0,1,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			}
		}
	);
	
	
	private static final Pentomino PENTOMINO_T = new Pentomino(
		new int[][][]{
			{
				{1,1,1,0,0},
				{0,1,0,0,0},
				{0,1,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,0,1,0,0},
				{1,1,1,0,0},
				{0,0,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,0,0,0,0},
				{1,1,1,0,0},
				{1,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,1,0,0,0},
				{0,1,0,0,0},
				{1,1,1,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			}
		}
	);
	
	private static final Pentomino PENTOMINO_L = new Pentomino(
		new int[][][]{
			{
				{0,0,0,1,0},
				{1,1,1,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,0,0,0,0},
				{1,1,1,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,1,1,0},
				{1,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,1,1,0},
				{0,0,0,1,0},
				{0,0,0,0,0},
				{0,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,0,0,0},
				{1,0,0,0,0},
				{1,0,0,0,0},
				{1,0,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,0,0,0,0},
				{1,0,0,0,0},
				{1,0,0,0,0},
				{1,1,0,0,0},
				{0,0,0,0,0}
			},
			{
				{1,1,0,0,0},
				{0,1,0,0,0},
				{0,1,0,0,0},
				{0,1,0,0,0},
				{0,0,0,0,0}
			},
			{
				{0,1,0,0,0},
				{0,1,0,0,0},
				{0,1,0,0,0},
				{1,1,0,0,0},
				{0,0,0,0,0}
			},
		}
	);
	


	/**
	 * Array of all the Pentomino objects
	 */
	static Pentomino[] pieces;
	





	private static Board solution;

	/**
	 * 
	 */
	public PentominoSolver(){
		
		
	}

	/**
	 * Prints the final stats: amount of solutions, duplicates and nodes visited.
	 */
	public void printFinalStats(){
		
		System.out.println("\nFinished!");
		System.out.println("Total nodes visited: " + totalNodesVisited);
		
	}

	/**
	 * Processes a solution and ouputs it.
	 *
	 * @param b Board containing the solution
	 */
	public void output(Board b){

		solution = b;

		if(PRINT)System.out.println("Solution found: \n" + solution);
		if(PRINT)printFinalStats();
			//System.exit(0);
		
	}

	/**
	 * Counts the empty squares starting from (x,y) using a recursive flood-fill algorithm
	 *
	 * @param x starting x coordinate
	 * @param y starting y coordinate
	 * @param b Board to use
	 * @param toCheck 2d array to mark pieces which have (not) been checked
	 */
	public int countEmpty(int x, int y, Board b, int[][] toCheck){
		if(b.getBoard()[y][x] != 0)
			return 0;

		int w = b.getBoard()[0].length;
		int h = b.getBoard().length;
		
		int count = 1;
		toCheck[y][x] = 1;

		if(x-1 >= 0 && toCheck[y][x-1] == 0)
			count += countEmpty(x-1, y, b, toCheck);
		if(x+1  < w && toCheck[y][x+1] == 0)
			count += countEmpty(x+1, y, b, toCheck);
		if(y-1 >= 0 && toCheck[y-1][x] == 0)
			count += countEmpty(x, y-1, b, toCheck);
		if(y+1  < h && toCheck[y+1][x] == 0)
			count += countEmpty(x, y+1, b, toCheck);

		return count;
	}

	// Checking for single empty squares

	// public boolean isPossibleSolution(Board board){
	// 	int blockedCounter;
	// 	int h = board.getBoard().length;
	// 	int w = board.getBoard()[0].length;
	// 	for(byte x = 0; x < w; x++){
	// 		for(byte y = 0 ; y <  h; y++){
	// 			if(board.getBoard()[y][x]==0){
	// 				blockedCounter = 0;
	// 				//piece to top
	// 				if ((y-1) < 0 || board.getBoard()[y-1][x] != 0){
	// 					blockedCounter++;
	// 				}
	// 				//piece to left
	// 				if ((x-1) < 0 || board.getBoard()[y][x-1] != 0){
	// 					blockedCounter++;
	// 				}
	// 				//piece down
	// 				if ((y+1) >= (h) || board.getBoard()[y+1][x] != 0){
	// 					blockedCounter++;
	// 				}
	// 				//piece to right
	// 				if ((x+1) >= (w) || board.getBoard()[y][x+1] != 0){
	// 					blockedCounter++;
	// 				}
	// 				//if all orthogonally surrounding pieces are blocked / out of bounds (blockCounter == 4), the solution is impossible and the method returns false
	// 				if (blockedCounter == 4){
	// 					// System.out.println(board);
	// 					return false;
	// 				}
	// 			}
	// 		}
	// 	}		
	// 	return true; 
	// }
	

	/**
	 * Checks if the given partial solution is a possible solution
	 *
	 * @param board partial solution to check
	 */
	public boolean isPossibleSolution(Board board){
		// count all empty spaces and if we find a zone with an area not divisible by 5
		// we return false, to indicate this partial solution is unsolvable
		int[][] toCheck = new int[board.getBoard().length][board.getBoard()[0].length];
		for (int x = 0; x < board.getBoard()[0].length; x++) {
			for (int y = 0; y < board.getBoard().length; y++) {
				if(toCheck[y][x] == 0){
					int area = countEmpty(x, y, board, toCheck);
					if(area%5 != 0){
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the given partial solution is a a symemtry of a previous solution
	 *
	 * @param board partial solution to check
	 */
	public boolean isSymmetry(Board b){
		return false;}
		/*int w = b.getBoard()[0].length;
		int h = b.getBoard().length;
		Board temp = new Board(w, h);

		

			Board sol = solution;

			boolean isHorizontalSymmetry = true;
			boolean isVerticalSymmetry = true;
			boolean isRotationalSymmetry = true;

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					// check all 3 possible variations
					if(b.getBoard()[y][x] != 0 && b.getBoard()[y][x] != sol.getBoard()[y][w-x-1]){
						isHorizontalSymmetry = false;
					}
					if(b.getBoard()[y][x] != 0 && b.getBoard()[y][x] != sol.getBoard()[h-y-1][x]){
						isVerticalSymmetry = false;
					}
					if(b.getBoard()[y][x] != 0 && b.getBoard()[y][x] != sol.getBoard()[h-y-1][w-x-1]){
						isRotationalSymmetry = false;
					}
				}
			}
			if(isHorizontalSymmetry || isVerticalSymmetry || isRotationalSymmetry){
				return true;
			}
		
		return false;
	}*/

	/**
	 * keeps track of the amount of nodes visited
	 */
	public int totalNodesVisited = 0;

	/**
	 * Implements the backtrack algorithm.
	 * Given a Board and a piece to place it'll try to fit it and backtrack if needed
	 *
	 * @param board partial solution to use
	 * @param pieceIndex index of the next piece to place
	 */
	public Board backtrack(Board board, int pieceIndex){
		totalNodesVisited++;

		if(pieceIndex == pieces.length){
			 output(board);
			 return solution;
			
		}

		// iterate all moves
		for(int x = 0; x < board.getBoard()[0].length; x++){
			for(int y = 0; y < board.getBoard().length; y++){
				Pentomino piece = pieces[pieceIndex];
				for(int i = 0; i < piece.getVariations(); i++){
					boolean isValid = board.addPentomino(x, y, pieces[pieceIndex], i, pieceIndex+1);
					// if this solution _might_ be valid, continue
					// if it's definitely not valid, prune this branch
					if(isValid && isPossibleSolution(board)){
						if(pieceIndex != 0 || !isSymmetry(board)){
							if(backtrack(board, pieceIndex+1)!= null)
								return solution;
						}
					}
					// remove this piece so we try the next case
					board.removePentomino(x, y, pieces[pieceIndex], i, pieceIndex+1);
				}
			}
		}
		return null;
	}
	/**
	 * 
	 * @param x: width of the board
	 * @param y: height of the board
	 * @param l: number of L pentominoes
	 * @param p: number of P pentominoes
	 * @param t: number of T pentominoes
	 */
	public static Board start(int x, int y, int l, int p, int t) {
		X=x;
		Y=y;
		L=l;
		P=p;
		T=t;
		pieces = new Pentomino[L+P+T];
		for(int i=0; i<L; i++){
			pieces[i]=PENTOMINO_L;
		}
		for(int i=L; i<L+P; i++){
			pieces[i]=PENTOMINO_P;
		}
		for(int i=L+P; i<pieces.length;i++){
			pieces[i]=PENTOMINO_T;
		}		
		

		PentominoSolver solver = new PentominoSolver();

		Board board = new Board(X, Y);

		// start backtracking with an empty board and the first piece (index=0)
		solver.backtrack(board, 0);

		// kindly tell the user when we're done and then exit
		if(PRINT)solver.printFinalStats();

		//System.exit(0);
		return solution;

	}
}
