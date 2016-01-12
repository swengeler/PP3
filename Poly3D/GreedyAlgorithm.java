package Poly3D;

import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Solution.Display;
import Solution.Package;
import Solution.PackageType;

import java.awt.BorderLayout;

/**
* A class that uses a greedy algorithm to determine a possible (most likely not optimal) 
* packing solution for a certain number of type A, B and C packages that are supposed
* to fit into a cargo area of 16.5m x 2.5m x 4m.
*
* @author Simon Wengeler 
*/
public class GreedyAlgorithm {
    
    private static final boolean DEBUG = false;
    private static final boolean PRINT_CONSOLE = false;
    
    /**
    * A three-dimensional array of PackageType objects (denoting the space taken up by certain
    * packages) used as an internal representation of the cargo space.
    */
    private static PackageType[][][] cargoSpace;
    
    private static PackageType[] cargoSpaceFilled;
    private static int[][] packageCoords;
    
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
        
        // The cargo space in an array representation [length][width][height] ([33][5][8])
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
            packages[i] = new Package(PackageType.CPackage);
        for (int i = nrC; i < nrC + nrB; i++)
            packages[i] = new Package(PackageType.BPackage);
        for (int i = nrC + nrB; i < nrPackages; i++)
            packages[i] = new Package(PackageType.APackage);
        
        boolean done = false;
        int counter = 0;
        
        while (!done) {
            Package p = packages[counter];
            initialPosition(p);
            if (!overlap(p))
                putPackage(p);
            counter++;   
            
            if (counter >= nrPackages) 
                done = true;
        }
        
        if (PRINT_CONSOLE) {simplePrint();}
        if (PRINT_CONSOLE) {printDoc();}
        printTotalValue();
        
        JFrame f = new JFrame();
        f.setSize(1000, 1020);
        String[][][] cS = new String[cargoSpace.length][cargoSpace[0].length][cargoSpace[0][0].length];
        for(int i = 0; i<cargoSpace.length; i++){
        	for(int j = 0; j<cargoSpace[i].length; j++){
        		for(int k = 0; k<cargoSpace[i][j].length; k++){
        			if(cargoSpace[i][j][k].toString() == "APackage") cS[i][j][k] = "A";
        			else if(cargoSpace[i][j][k].toString() == "BPackage") cS[i][j][k] = "B";
        			else if(cargoSpace[i][j][k].toString() == "CPackage") cS[i][j][k] = "C";
        			else cS[i][j][k] = "D";                    		
                }	
            }	
        }
        
        Display display = new Display(cargoSpace);
        Test3D.setCargo(cS);
        f.add(display, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
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
        curX = cargoSpace.length - p.getLength();
        if (DEBUG) {System.out.println(curX);}
        curY = cargoSpace[0].length - p.getWidth();
        if (DEBUG) {System.out.println(curY);}
        curZ = cargoSpace[0][0].length - p.getHeight();
        if (DEBUG) {System.out.println(curZ);}
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
        while (movable(p)) {
            while (curY > 0 && !overlap(p)) {curY--;}
            if (overlap(p)) {curY++;}
            while (curX > 0 && !overlap(p)) {curX--;}
            if (overlap(p)) {curX++;}
            while (curZ > 0 && !overlap(p)) {curZ--;}
            if (overlap(p)) {curZ++;}
        }
        if (DEBUG) {System.out.println("After placing: curX = " + curX + " curY = " + curY + " curZ = " + curZ);}
        addToDoc(p);
        place(p);
    }
    
    /**
    * A method that adds both the package type and the coordinates of the last package placed to arrays 
    * used for documentation that might be useful later on.
    *
    * @param p The latest package that was placed.
    */
    public static void addToDoc(Package p) {
        if (cargoSpaceFilled == null) {
            cargoSpaceFilled = new PackageType[1];
            cargoSpaceFilled[0] = p.getType();
        } else {
            PackageType[] newCSF = new PackageType[cargoSpaceFilled.length + 1];
            System.arraycopy(cargoSpaceFilled, 0, newCSF, 0, cargoSpaceFilled.length);
            newCSF[newCSF.length - 1] = p.getType();
            cargoSpaceFilled = newCSF;
        }
        
        if (packageCoords == null) {
            packageCoords = new int[1][3];
            packageCoords[0][0] = curX;
            packageCoords[0][1] = curY;
            packageCoords[0][2] = curZ;
        } else {
            int[][] newPC = new int[packageCoords.length + 1][3];
            for (int i = 0; i < packageCoords.length; i++) {
                System.arraycopy(packageCoords[i], 0, newPC[i], 0, 3);
            }
            newPC[newPC.length - 1][0] = curX;
            newPC[newPC.length - 1][1] = curY;
            newPC[newPC.length - 1][2] = curZ;
            packageCoords = newPC;
        }
    }
    
    /**
    * A method to check whether a package can still be moved. It is mostly used to ensure that a package
    * actually does not have any more space to move, so that there are as few gaps between packages
    * as possible.
    * 
    * return boolean Returns true if the package can be moved in some direction, otherwise returns false.
    */
    public static boolean movable(Package p) {
        curY--;
        if (curY >= 0 && !overlap(p)) {
            curY++;
            return true;
        } else {curY++;}
        curX--;
        if (curX >= 0 && !overlap(p)) {
            curX++;
            return true;
        } else {curX++;}
        curZ--;
        if (curZ >= 0 && !overlap(p)) {
            curZ++;
            return true;
        } else {curZ++;}
        return false;
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
        for (int x = 0; x < p.getLength(); x++) {
            // coords[1][1] should be the y-coordinate of all the corners of the package on the front side
            for (int y = 0; y < p.getWidth(); y++) {
                // coords[3][2] should be the z-coordinate of all the corners of the package on the upper side
                for (int z = 0; z < p.getHeight(); z++)
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
        for (int i = 0; i < coords.length && noOverlap; i++) {
            if (/*curX + coords[i][0] > cargoSpace.length || curY + coords[i][1] > cargoSpace[0].length || curZ + coords[i][2] > cargoSpace[0][0].length || */cargoSpace[curX + coords[i][0]][curY + coords[i][1]][curZ + coords[i][2]] != PackageType.NoPackage) {
                noOverlap = false;
                if (DEBUG) {System.out.println(p.getType() + " overlaps with " + cargoSpace[curX + coords[i][0]][curY + coords[i][1]][curZ + coords[i][2]] + " at x = " + (curX + coords[i][0]) + " y = " + (curY + coords[i][1]) + " z = " + (curZ + coords[i][2]));}
            }
        }
        return !noOverlap;
    }
    
    /**
    * A method that produces a very simple printout of the cargo space to give an idea of how/whether the
    * algorithm is working properly.
    */
    public static void simplePrint() {
        for (int i = 0; i < cargoSpace.length; i++) {
            System.out.println("\nLayer " + (i + 1) + ":");
            for (int j = cargoSpace[0][0].length - 1; j >= 0; j--) {
                for (int k = 0; k < cargoSpace[0].length; k++) {
                    if (cargoSpace[i][k][j] == PackageType.NoPackage)
                        System.out.print("O ");
                    else if (cargoSpace[i][k][j] == PackageType.APackage)
                        System.out.print("A ");
                    else if (cargoSpace[i][k][j] == PackageType.BPackage)
                        System.out.print("B ");
                    else if (cargoSpace[i][k][j] == PackageType.CPackage)
                        System.out.print("C ");
                }
                System.out.println();
            }
        }
    }
    
    public static void printDoc() {
        for (int i = 0; i < packageCoords.length; i++) {
            System.out.println(cargoSpaceFilled[i] + " at x = " + packageCoords[i][0] + ", y = " + packageCoords[i][1] + ", z = " + packageCoords[i][2]);
        }
    }
    
    public static void printTotalValue() {
        double totalValue = 0;
        for (int i = 0; i < cargoSpaceFilled.length; i++) {
            totalValue += (new Package(cargoSpaceFilled[i])).getValue();
        }
        System.out.println("\nTOTAL VALUE: " + totalValue);
    }
    
}