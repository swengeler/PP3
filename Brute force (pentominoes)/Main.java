/**
 * 
 * @author Raffaele Piccini
 *
 */
public class Main {
private static final int L_V=3;
private static final int P_V=4;
private static final int T_V=5;
private static int L_N;
private static int P_N;
private static int T_N;
private static int V_8x5;
private static Board board8x5;
private static int V_8x33;
private static int V_33x5;
private static Board board33x5;
private static Board board8x33;
private static int bestValue;

	public static void main(String[] args) {
		//try with 8x5 side of the container
				V_8x5=51;
				int counter =0;
				int[][] solutions= {{0,0,0}};
				solutions[0] = getComb(V_8x5,11,solutions);
				L_N= solutions[counter][0];
				P_N= solutions[counter][1];
				T_N= solutions[counter][2];
				Board board=null;
				while(board==null)
				{	
					counter++;
					int[][] newSolutions = new int[solutions.length+1][solutions[0].length];
					for (int i = 0; i < solutions.length; i++) {
					    System.arraycopy(solutions[i], 0, newSolutions[i], 0, solutions[0].length);
					}
					solutions = newSolutions;
					solutions[counter]= getComb(V_8x5,11,solutions);
					if(solutions[counter][0]==-1){
						V_8x5--;
					}else{
						L_N= solutions[counter][0];
						P_N= solutions[counter][1];
						T_N= solutions[counter][2];
						System.out.println(solutions[counter][0]+" "+solutions[counter][1]+" "+solutions[counter][2]);
						board = PentominoSolver.start(11, 5, L_N, P_N, T_N);
						System.out.println(board);
					}
				}
	}
	public static void maina(String[] args) {
		//try with 8x5 side of the container
		V_8x5=40;
		int counter =0;
		int[][] solutions= {{0,0,0}};
		solutions[0] = getComb(V_8x5,8,solutions);
		L_N= solutions[counter][0];
		P_N= solutions[counter][1];
		T_N= solutions[counter][2];
		Board board=null;
		while(board==null)
		{	
			counter++;
			int[][] newSolutions = new int[solutions.length+1][solutions[0].length];
			for (int i = 0; i < solutions.length; i++) {
			    System.arraycopy(solutions[i], 0, newSolutions[i], 0, solutions[0].length);
			}
			solutions = newSolutions;
			solutions[counter]= getComb(V_8x5,8,solutions);
			if(solutions[counter][0]==-1){
				V_8x5--;
			}else{
				L_N= solutions[counter][0];
				P_N= solutions[counter][1];
				T_N= solutions[counter][2];
				System.out.println(solutions[counter][0]+" "+solutions[counter][1]+" "+solutions[counter][2]);
				board = PentominoSolver.start(8, 5, L_N, P_N, T_N);
				System.out.println(board);
			}
		}
		board8x5=board;
		V_8x5 = V_8x5*33;
		board=null;
		//try with the 33x5 side of the container.
		V_33x5=165;
		counter=0;
		int[][]theNewSolution={{0,0,0}};
		solutions = theNewSolution;
		solutions[0]= getComb(V_33x5,33,solutions);
		L_N= solutions[counter][0];
		P_N= solutions[counter][1];
		T_N= solutions[counter][2];
		
		
		while(board==null)
		{	
			counter++;
			int[][] newSolutions = new int[solutions.length+1][solutions[0].length];
			for (int i = 0; i < solutions.length; i++) {
			    System.arraycopy(solutions[i], 0, newSolutions[i], 0, solutions[0].length);
			}
			solutions = newSolutions;
			solutions[counter]= getComb(V_33x5,33,solutions);
			if(solutions[counter][0]==-1){
				V_33x5--;
			}else{
				L_N= solutions[counter][0];
				P_N= solutions[counter][1];
				T_N= solutions[counter][2];
				System.out.println(solutions[counter][0]+" "+solutions[counter][1]+" "+solutions[counter][2]);
				board = PentominoSolver.start(33, 5, L_N, P_N, T_N);
				System.out.println(board);
			}
		}
		
		board33x5=board;
		V_33x5 = V_33x5*8;
		board=null;
		//try with the 33x8 side.
		V_8x33=260;
		counter=0;
		int[][]aNewSolution={{0,0,0}};
		solutions = aNewSolution;
		solutions[0]= getComb(V_8x33,52,solutions);
		L_N= solutions[counter][0];
		P_N= solutions[counter][1];
		T_N= solutions[counter][2];
		
			
		while(board==null)
		{	
			counter++;
			int[][] newSolutions = new int[solutions.length+1][solutions[0].length];
			for (int i = 0; i < solutions.length; i++) {
			    System.arraycopy(solutions[i], 0, newSolutions[i], 0, solutions[0].length);
			}
			solutions = newSolutions;
			solutions[counter]= getComb(V_8x33,52,solutions);
			if(solutions[counter][0]==-1){
				V_8x33--;
			}else{
				L_N= solutions[counter][0];
				P_N= solutions[counter][1];
				T_N= solutions[counter][2];
				System.out.println(solutions[counter][0]+" "+solutions[counter][1]+" "+solutions[counter][2]);
				board = PentominoSolver.start(8, 33, L_N, P_N, T_N);
				System.out.println(board);
			}
		}
		board8x33=board;
		V_8x33 = V_8x33*5;
		board=null;
		
		
		//get the best of the 3 solutions
		bestValue= Math.max(V_33x5, Math.max(V_8x33, V_8x5));
		System.out.println("Te best value is: ");
		if(bestValue == V_33x5){
			System.out.print(bestValue*8+"\n"+board33x5);
		}else if(bestValue == V_8x5){
			System.out.println(bestValue*33+"\n"+board8x5);
		}if(bestValue == V_8x33){
			System.out.println(bestValue*5+"\n"+board8x33);
		}
	}
	/**
	 * 
	 * @param V the value that you want to make with N pentominoes
	 * @param N the number of pentominoes you have
	 * @param solutions the solutions you already discovered for the same V and N
	 * @return an array with [0]: number of L pent, [1]: number of P pent, [2]: number of T pent.
	 */
	public static int[] getComb(int V,int N ,int[][] solutions){
		int[] sol = {0,0,N};
		int numSol= sol[0]*L_V + sol [1]*P_V + sol[2]*T_V;
		int phase=0;
		while((numSol!=V || isIn(sol,solutions))&& (sol[0]<8)){
			
			if(phase==0){
				if(sol[2]>=1){
					sol[2]--;
					sol[1]++;
				}
				phase=1;
			}else if(phase==1){
				sol[1]--;
				sol[0]++;
				if(sol[1]==0)
					phase=2;
			}else if(phase==2){
				sol[1] += sol[0]+1;
				sol[0]=0;
				sol[2]--;
				phase=0;
			}
			
			
			numSol= sol[0]*3 + sol [1]*4 + sol[2]*5;
			System.out.println("Target: "+V+" Value: "+numSol+"  L: "+sol[0]+" P: "+sol[1]+"  T: "+sol[2]);
		}if(sol[0]==8 && numSol != V)
			sol[0]=-1;
		return sol;
	}
	/**
	 * 
	 * @param sol the solution to check
	 * @param solutions array of arrays where to check
	 * @return sol is an array of solutions
	 */
	private static boolean isIn(int[] sol, int[][] solutions) {
		boolean found = false;
	
		for(int i=0; i< solutions.length && !found; i++){
			for(int j=0; j<sol.length; j++){
				if(sol[j]!=solutions[i][j]){
					j=sol.length;
				}else if(sol[j]== solutions[i][j] && j==sol.length-1){
				found=true;
				}
			}
		}
		return found;
	}
}
