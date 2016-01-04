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
        
        System.out.print("Please enter the number of A packages: ");
        int nrA = in.nextInt();
        System.out.print("Please enter the number of B packages: ");
        int nrB = in.nextInt();
        System.out.print("Please enter the number of C packages: ");
        int nrC = in.nextInt();
        
        int nrPackages = nrA + nrB + nrC;
        Package[] packages = new Package[nrPackages];
        for (int i = 0; i < nrC; i++)
            packages[i] = new Package("C");
        for (int i = nrC; i < nrC + nrB; i++)
            packages[i] = new Package("B");
        for (int i = nrC + nrB; i < nrPackages; i++)
            packages[i] = new Package("A");
        
        boolean done = false;
        int counter = 0;
        
        while (nrPackages > 0 && !done) {
            Package p = packages[counter];
            initialPosition(p);
            if (!overlap(p)) {
                putPackage(p);
                counter++;
            }
            else 
                done = true;
        }
        
    }
    
    /**
    * Determines the initial position of the package currently being placed in the most upper, right and
    * front corner of the cargo area.
    *
    * @param p The package whose dimensions determine the values of curX, curY and curZ (i.e. the next 
    *          package placed in the cargo area).
    */
    public static void initialPosition(Package p) {
        int[][] coords = p.getCoords();
        curX = cargoSpace.length - coords[4][0];
        curY = cargoSpace[0].length - coords[1][1];
        curZ = cargoSpace[0][0].length - coords[3][2];
    }
    
    /**
    * Places the package p in the cargo space at the "best position" (assumed to be as "close" to the 
    * origin of the cargo space coordinate system as possible).
    * 
    * @param p The package for which a position is to be determined and which is to be placed in the 
    *          cargo space.
    */
    public static void putPackage(Package p) {
        // first move as far back as possible, then as far left as possible, then as far down as possible
        // maybe repeat recursively to avoid placing stuff in bad positions?
        while (!overlap(p) && curY >= 0) {curY--;}
        while (!overlap(p) && curX >= 0) {curX--;}
        while (!overlap(p) && curZ >= 0) {curZ--;}
        place(p);
    }
    
    /**
    * Changes the internal representation of the cargo space (the three-dimensional array) in order to 
    * properly represent the package that was placed at a certain position and is now filling up space.
    * 
    * @param p The Package that is placed in the array/cargo space.
    */
    public static void place(Package p) {
        int[][] coords = p.getCoords();
        // coords[4][0] should be the x-coordinate of all the corners of the package on the right side
        for (int x = 0; x < coords[4][0]; x++) {
            // coords[1][1] should be the y-coordinate of all the corners of the package on the front side
            for (int y = 0; y < coords[1][1]; y++) {
                // coords[3][2] should be the z-coordinate of all the corners of the package on the upper side
                for (int z = 0; z < coords[3][2]; z++)
                    cargoSpace[x + curX][y + curY][z + curZ] = p.getType();
            }
        }
    }
    
    /**
    * A method that "fills" the cargo space with "no packages", i.e. empties it, either to reset it or
    * in order to have a proper graphical representation in the GUI.
    */
    public static void initialiseCS() {
        for (int i = 0; i < cargoSpace.length; i++) {
            for (int j = 0; j < cargoSpace[i].length; j++) {
                for (int k = 0; k < cargoSpace[i][j].length; k++) {
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
        int[][] coords = p.getCoords();
        boolean noOverlap = true;
        for (int i = 0; i < p.getCoords().length && noOverlap; i++) {
            if (cargoSpace[curX + coords[i][0]][curY + coords[i][1]][curZ + coords[i][2]] != PackageType.NoPackage)
                noOverlap = false;                                                      
        }
        return !noOverlap;
    }
    
}