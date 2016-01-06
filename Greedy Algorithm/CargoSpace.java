public class CargoSpace {
    
    private static final boolean DEBUG = false;
    private static final boolean PRINT_CONSOLE = false;
    
    private int length;
    private int width;
    private int height;
    
    private double totalValue;
    
    /**
    * A three-dimensional array of PackageType objects (denoting the space taken up by certain
    * packages) used as an internal representation of the cargo space.
    */
    private PackageType[][][] cargoSpace;
    private Package[] packages;
    
    private PackageType[] cargoSpaceFilled;
    private int[][] packageCoords;
    
    private int curX = 0;
    private int curY = 0;
    private int curZ = 0;
    
    public CargoSpace(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        cargoSpace = new PackageType[length][width][height];
        initialiseCS();
    }
    
    public PackageType[][][] getArray() {return cargoSpace;}
    
    /**
    * Determines the initial position of the package currently being placed in the most upper, right and
    * front corner of the cargo area.
    *
    * @param p The package whose dimensions determine the values of curX, curY and curZ (i.e. the next 
    *          package placed in the cargo area).
    */
    public void initialPosition(Package p) {
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
    public void putPackage(Package p) {
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
        totalValue += p.getValue();
    }
    
    /**
    * A method that adds both the package type and the coordinates of the last package placed to arrays 
    * used for documentation that might be useful later on.
    *
    * @param p The latest package that was placed.
    */
    public void addToDoc(Package p) {
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
    public boolean movable(Package p) {
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
    public void place(Package p) {
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
    public void initialiseCS() {
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
    public boolean overlap(Package p) {
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
    public void simplePrint() {
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
    
    public void printDoc() {
        for (int i = 0; i < packageCoords.length; i++) {
            System.out.println(cargoSpaceFilled[i] + " at x = " + packageCoords[i][0] + ", y = " + packageCoords[i][1] + ", z = " + packageCoords[i][2]);
        }
    }
    
    public double getTotalValue() {
        /*double totalValue = 0;
        for (int i = 0; i < cargoSpaceFilled.length; i++) {
            totalValue += (new Package(cargoSpaceFilled[i])).getValue();
        }*/
        if (DEBUG) {System.out.println("\nTOTAL VALUE: " + totalValue);}
        return totalValue;
    }
}