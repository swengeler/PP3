import java.util.Scanner;

/**
* A class that uses a greedy algorithm to determine a possible (most likely not optimal) 
* packing solution for a certain number of type A, B and C packages that are supposed
* to fit into a cargo area of 16.5m x 2.5m x 4m.
*
* @author Simon Wengeler 
*/
public class GreedyAlgorithm {
    
    /**
    * A three-dimensional array of PackageType objects (denoting the space taken up by certain
    * packages) used as an internal representation of the cargo space.
    */
    private static PackageType[][][] cargoSpace;
    
    private static int initialX = 0;
    private static int initialY = 0;
    private static int initialZ = 0;
    
    private static int curX = 0;
    private static int curY = 0;
    private static int curZ = 0;
    
    /**
    * The main method of the class, initialising the array used for the internal representation
    * of the cargo space, asking for the number of certain package types to be used and 
    * determining a packing for them.
    * 
    * @param args Not used.
    */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        // The cargo space in an array representation [length][width][height]
        // The origin of the coordinate system is in the lower left back corner
        cargoSpace = new PackageType[33][5][8];
        initialiseCS();
        
        System.out.print("Please enter the number of A packages:");
        int nrA = in.nextInt();
        System.out.print("Please enter the number of B packages:");
        int nrB = in.nextInt();
        System.out.print("Please enter the number of C packages:");
        int nrC = in.nextInt();
        
        double worthA = A.getWorth();
        double worthB = B.getWorth();
        double worthC = C.getWorth();
        
        int nrPackages = nrA + nrB + nrC;
        
        while (nrPackages > 0 && csNotFull) {
            
        }
        
    }
    
    /**
    * Places the package p in the cargo space at the "best position" (assumed to be as "close" to the 
    * origin of the cargo space coordinate system as possible);
    */
    public static void putPackage(Package p) {
        while (overlap(p) && stillInCA()) {
            curY++;
        }
    }
    
    /**
    * A method that "fills" the cargo space with "no packages", i.e. empties it, either to reset it or
    * in order to have a proper graphical representation in the GUI.
    */
    public static void initialiseCS() {
        for (int i = 0; i < cargoSpace.length; i++) {
            for (int j = 0; j < cargoSpace[i].length; j++) {
                for (int k = 0; k < cargoSpace[i][j].length) {
                    cargoSpace[i][j][k] = PackageType.NoPackage;
                }
            }
        }
    }
    
    /**
    * A method that checks whether there is any overlap between packages already put in the cargo space
    * and the package to be put there at its current position.
    *
    * @param p The package for which the overlap is to be checked.
    * @return boolean Returns true if there is no overlap anywhere and false if there is some overlap.
    */
    public static boolean overlap(Package p) {
        int[][] coords = p.getCoords;
        for (int i = 0; i < p.getCoords().length; i++) {
            if (cargoSpace[curX + coords[i][0]][cargoSpace[curY + coords[i][1]]][cargoSpace[curZ + coords[i][2]]] != PackageType.NoPackage)
                return false;                                                      
        }
        return true;
    }
    
}