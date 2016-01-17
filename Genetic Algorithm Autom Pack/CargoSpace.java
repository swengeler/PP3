import java.util.ArrayList;

/**
* A class which represents the cargo space that should be filled with packages. It contains
* multiple arrays storing information about where the individual packages are positioned as
* well as methods to place packages in the cargo space.
*
* @author Simon Wengeler
*/

public class CargoSpace {

    public static Package[] packageTypes;

    private static final boolean DEBUG = false;
    private static final boolean PRINT_CONSOLE = false;

    /** The length of the cargo space (in 0.5m).*/
    private int length;
    /** The width of the cargo space (in 0.5m).*/
    private int width;
    /** The height of the cargo space (in 0.5m).*/
    private int height;
    /** The total value of all packages in the cargo space (calculated when the algorithm is finished).*/
    private double totalValue;

    /**
    * A three-dimensional array of String objects (denoting the space taken up by certain packages)
    * used as an internal representation of the cargo space. It does not contain any information about the
    * placement of individual packages (which are indiscernible in it).
    */
    private String[][][] cargoSpace;

    /**
    * An array containing information about the placement of individual packages in the form of the types
    * of packages filling certain positions in the cargo space denoted by the corresponding coordinates in
    * the packageCoords array.
    */
    public Package[] csPacking;
    /** REDUNDANT
    * An array containing information about the placement of individual packages in form of the coordinates
    * of the lower left back-most corner of each package placed in the cargo space (corresponds to the package
    * types listed in the csPacking array).
    */
    public int[][] packageCoords;

    private int nrPlaced;

    /**
    * The constructor for a CargoSpace object, assigning it its dimensions, initialising the cargoSpace array
    * and filling it with NoPackage type "packages".
    *
    * @param length The length of the cargo space (in 0.5m).
    * @param width  The width of the cargo space (in 0.5m).
    * @param height The height of the cargo space (in 0.5m).
    */
    public CargoSpace(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        totalValue = 0;
        cargoSpace = new String[length][width][height];
        initialiseCS();
    }

    /**
    * A method to retrieve the three-dimensional internal representation of the cargo space, the cargoSpace
    * array.
    *
    * @return cargoSpace The three-dimensional array representing the cargo space.
    */
    public String[][][] getArray() {return cargoSpace;}

    /**
    * Determines the initial position of the package currently being placed in the most upper, right and
    * front corner of the cargo area.
    *
    * @param p The package whose dimensions determine the values of curX, curY and curZ (i.e. the next
    *          package placed in the cargo area).
    */
    public void initialPosition(Package p) {
        int[][] coords = p.getCoords();
        p.setBaseX(cargoSpace.length - p.getLength());
        p.setBaseY(cargoSpace[0].length - p.getWidth());
        p.setBaseZ(cargoSpace[0][0].length - p.getHeight());
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
            while (p.getBaseCoords()[1] > 0 && !overlap(p)) {p.setBaseY(p.getBaseCoords()[1] - 1);}
            if (overlap(p)) {p.setBaseY(p.getBaseCoords()[1] + 1);}
            while (p.getBaseCoords()[0] > 0 && !overlap(p)) {p.setBaseX(p.getBaseCoords()[0] - 1);}
            if (overlap(p)) {p.setBaseX(p.getBaseCoords()[0] + 1);}
            while (p.getBaseCoords()[2] > 0 && !overlap(p)) {p.setBaseZ(p.getBaseCoords()[2] - 1);}
            if (overlap(p)) {p.setBaseZ(p.getBaseCoords()[2] + 1);}
        }
        if (DEBUG) {System.out.println("After placing: curX = " + p.getBaseCoords()[0] + " curY = " + p.getBaseCoords()[1] + " curZ = " + p.getBaseCoords()[2]);}
        place(p);
    }

    /**
    * A method that adds both the package type and the coordinates of the last package placed to arrays
    * used for documentation that might be useful later on.
    *
    * @param p The latest package that was placed.
    */
    public void addToDoc(Package p) {
        if (csPacking == null) {
            csPacking = new Package[1];
            csPacking[0] = p;
        } else {
            Package[] newCSF = new Package[csPacking.length + 1];
            System.arraycopy(csPacking, 0, newCSF, 0, csPacking.length);
            newCSF[newCSF.length - 1] = p;
            csPacking = newCSF;
        }
        totalValue += p.getValue();
    }

    /**
    * A method to check whether a package can still be moved. It is mostly used to ensure that a package
    * actually does not have any more space to move, so that there are as few gaps between packages
    * as possible.
    *
    * return boolean Returns true if the package can be moved in some direction, otherwise returns false.
    */
    public boolean movable(Package p) {
        p.setBaseY(p.getBaseCoords()[1] - 1);
        if (p.getBaseCoords()[1] >= 0 && !overlap(p)) {
            p.setBaseY(p.getBaseCoords()[1] + 1);
            return true;
        } else {p.setBaseY(p.getBaseCoords()[1] + 1);}
        p.setBaseX(p.getBaseCoords()[0] - 1);
        if (p.getBaseCoords()[0] >= 0 && !overlap(p)) {
            p.setBaseX(p.getBaseCoords()[0] + 1);
            return true;
        } else {p.setBaseX(p.getBaseCoords()[0] + 1);}
        p.setBaseZ(p.getBaseCoords()[2] - 1);
        if (p.getBaseCoords()[2] >= 0 && !overlap(p)) {
            p.setBaseZ(p.getBaseCoords()[2] + 1);
            return true;
        } else {p.setBaseZ(p.getBaseCoords()[2] + 1);}
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
                for (int z = 0; z < p.getHeight(); z++) {
                    cargoSpace[x + p.getBaseCoords()[0]][y + p.getBaseCoords()[1]][z + p.getBaseCoords()[2]] = p.getType();
                }
            }
        }
        nrPlaced++;
        addToDoc(p);
    }

    /**
    * A method that "fills" the cargo space with "no packages", i.e. empties it, either to reset it or
    * in order to have a proper graphical representation in the GUI.
    */
    public void initialiseCS() {
        for (int i = 0; i < cargoSpace.length; i++) {
            for (int j = 0; j < cargoSpace[i].length; j++) {
                for (int k = 0; k < cargoSpace[i][j].length; k++) {
                    cargoSpace[i][j][k] = "Empty";
                }
            }
        }
    }

    public int getTotalGaps() {
        int counter = 0;
        for (int i = 0; i < cargoSpace.length; i++) {
            for (int j = 0; j < cargoSpace[i].length; j++) {
                for (int k = 0; k < cargoSpace[i][j].length; k++) {
                    if (cargoSpace[i][j][k].equals("Empty")) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    /**
    * A method that try to "fills" the cargo gaps with the packages that haven't been placed yet.
    */
    public void fillGaps(ArrayList<Package> packagesLeft) {
      int counter = 0;
      for (int i = 0; i < cargoSpace.length; i++) {
          for (int j = 0; j < cargoSpace[i].length; j++) {
              for (int k = 0; k < cargoSpace[i][j].length; k++) {
                  if (cargoSpace[i][j][k].equals("Empty")) {
                    for (int z = 0; z < packagesLeft.size(); z++) {
                      Package p = packagesLeft.get(z);
                      p.setBaseCoords(i, j, k);
                      p.rotateRandom();
                      if (!this.overlap(p)) {
                        this.putPackage(p);
                        packagesLeft.remove(z);
                        z = packagesLeft.size();
                        counter++;
                      }
                    }
                  }
              }
          }
      }
      if (DEBUG) {System.out.println("Packages added by fillGaps() : " + counter);}
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
            if (p.getBaseCoords()[0] + coords[i][0] >= cargoSpace.length || p.getBaseCoords()[1] + coords[i][1] >= cargoSpace[0].length || p.getBaseCoords()[2] + coords[i][2] >= cargoSpace[0][0].length || !cargoSpace[p.getBaseCoords()[0] + coords[i][0]][p.getBaseCoords()[1] + coords[i][1]][p.getBaseCoords()[2] + coords[i][2]].equals("Empty")) {
                noOverlap = false;
                if (DEBUG) {System.out.println(p.getType() + " overlaps with " + cargoSpace[p.getBaseCoords()[0] + coords[i][0]][p.getBaseCoords()[1] + coords[i][1]][p.getBaseCoords()[2] + coords[i][2]] + " at x = " + (p.getBaseCoords()[0] + coords[i][0]) + " y = " + (p.getBaseCoords()[1] + coords[i][1]) + " z = " + (p.getBaseCoords()[2] + coords[i][2]));}
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
                    if (cargoSpace[i][k][j].equals("Empty"))
                        System.out.print("O ");
                    else if (cargoSpace[i][k][j].equals("A"))
                        System.out.print("A ");
                    else if (cargoSpace[i][k][j].equals("B"))
                        System.out.print("B ");
                    else if (cargoSpace[i][k][j].equals("C"))
                        System.out.print("C ");
                }
                System.out.println();
            }
        }
    }

    /**
    * A method used to print out the "documentation" created during the run of the algorithm. It provides
    * information printed out in the command line about the location of each of the packages placed in the
    * cargo space.
    */
    public void printDoc() {
        for (int i = 0; i < packageCoords.length; i++) {
            System.out.println(csPacking[i].getType() + " package at x = " + packageCoords[i][0] + ", y = " + packageCoords[i][1] + ", z = " + packageCoords[i][2]);
        }
    }

    /**
    * A method returning the total value of all the packages that were placed in the cargo space.
    *
    * @return totalValue The total value of all included packages.
    */
    public double getTotalValue() {
        double tV = 0;
        for (int i = 0; i < csPacking.length; i++) {
            tV = tV + csPacking[i].getValue();
        }
        return tV;
    }

    public double getFitness() {
        int overlapping = 0;
        Package p;
        for (int i = 0; i < csPacking.length; i++) {
            p = csPacking[i];
            for (int j = 0; j < csPacking.length; j++) {
                if (p.overlaps(csPacking[j]))
                    overlapping++;
            }
        }
        //System.out.println("Overlapping: " + overlapping);
        double fitness = totalValue - (nrPlaced - overlapping);
        return fitness;
    }

    public void fillCargoSpace(Package[] cargo) {
        if (DEBUG) {System.out.println("cargo array length: " + cargo.length);}
        for (int i = 0; i < cargo.length; i++) {
            if (!overlap(cargo[i])) {
                if (DEBUG) {System.out.println("Package " + (i + 1) + " placed.");}
                place(cargo[i]);
            }
        }
    }

    public int getLength() {
        return cargoSpace.length;
    }

    public int getWidth() {
        return cargoSpace[0].length;
    }

    public int getHeight() {
        return cargoSpace[0][0].length;
    }

    public void packRandom(Package[] packing) {
        int countPacked = 0;
        for (int i = 0; i < packing.length; i++) {
            int index = (int) (Math.random() * packing.length);
            if (!overlap(packing[index])) {
              place(packing[index]);
              countPacked++;
            }
        }
        System.out.println("Package placed: " + countPacked);
    }

    public Package[] getPacking() {
        return csPacking;
    }

    public int getOverlap(Package[] chromosome) {
        int overlap = 0;
        for (int i = 0; i < csPacking.length; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                if (csPacking[i].overlaps(chromosome[j]) && !csPacking[i].equals(chromosome[j]))
                    overlap++;
            }
        }
        return overlap;
    }

    public int[] getNrIndivPackages() {
        int[] placed = new int[packageTypes.length];
        for (int i = 0; i < csPacking.length; i++) {
            for (int j = 0; j < placed.length; j++) {
                if (csPacking[i].getType().equalsIgnoreCase(packageTypes[j].getType()))
                    placed[j]++;
            }
        }
        return placed;
    }

}
