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

    /** The length of the cargo space (in 0.5m).*/
    private int length;
    /** The width of the cargo space (in 0.5m).*/
    private int width;
    /** The height of the cargo space (in 0.5m).*/
    private int height;

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
            csPacking[0] = p.clone();
        } else {
            Package[] newCSF = new Package[csPacking.length + 1];
            System.arraycopy(csPacking, 0, newCSF, 0, csPacking.length);
            newCSF[newCSF.length - 1] = p.clone();
            csPacking = newCSF;
        }
    }

    /**
    * A method that removes a package from the array holding all packages included in the solution.
    *
    * @param remIndex The index of the package to be removed in the array.
    */
    public void removeFromDoc(int remIndex) {
    	Package[] newCargoSpaceFilled = new Package[csPacking.length-1];
    	int index = 0;
    	while (index < newCargoSpaceFilled.length)
    	{
    		if (index<remIndex) {
    			newCargoSpaceFilled[index] = csPacking[index];
    			index++;
    		} else {
    			newCargoSpaceFilled[index] = csPacking[index+1];
    			index++;
    		}
    	}
    	this.csPacking = newCargoSpaceFilled;
    }

    /**
    * A method to check whether a package can still be moved. It is mostly used to ensure that a package
    * actually does not have any more space to move, so that there are as few gaps between packages
    * as possible.
    *
    * return boolean Returns true if the package can be moved in some direction, otherwise returns false.
    **/
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
        for (int x = 0; x < p.getLength(); x++) {
            for (int y = 0; y < p.getWidth(); y++) {
                for (int z = 0; z < p.getHeight(); z++) {
                    cargoSpace[x + p.getBaseCoords()[0]][y + p.getBaseCoords()[1]][z + p.getBaseCoords()[2]] = p.getType();
                }
            }
        }
        addToDoc(p);
    }

    /**
    * An array that removes a package from the internal array representation of the cargo space.
    *
    * @param p The Package to be removed.
    */
    public void remove(Package[] packing, int remIndex) {
    	Package p = packing[remIndex];
    	for (int i=0; i < p.getLength(); i++) {
    		for (int j=0; j<p.getWidth(); j++) {
    			for (int k=0; k<p.getHeight(); k++) {
    				cargoSpace[i + p.getBaseCoords()[0]][j + p.getBaseCoords()[1]][k + p.getBaseCoords()[2]] = "Empty";
    			}
    		}
    	}
    	removeFromDoc(remIndex);
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

    /**
    * A method that counts the number of "gaps" in the cargo space, that is, the number of
    * 0.5m x 0.5m x 0.5m cubes that are n ot occupied by any package.
    *
    * @return counter The number of gaps in the cargo space.
    */
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
    * A method that locates gaps in the cargo space and tries to fill it with packages that were not
    * placed by the main algorithm.
    *
    * @param packagesLeft An array list containing the packages left over by the greedy algorithm.
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
            if (p.getBaseCoords()[0] + coords[i][0] < 0 || p.getBaseCoords()[1] + coords[i][1] < 0 || p.getBaseCoords()[2] + coords[i][2]< 0)
                noOverlap = false;
            if (noOverlap && (p.getBaseCoords()[0] + coords[i][0] >= cargoSpace.length || p.getBaseCoords()[1] + coords[i][1] >= cargoSpace[0].length || p.getBaseCoords()[2] + coords[i][2] >= cargoSpace[0][0].length || !cargoSpace[p.getBaseCoords()[0] + coords[i][0]][p.getBaseCoords()[1] + coords[i][1]][p.getBaseCoords()[2] + coords[i][2]].equals("Empty"))) {
                noOverlap = false;
            }
        }
        return !noOverlap;
    }

    /**
    * A method returning the total value of all the packages that were placed in the cargo space.
    *
    * @return tV The total value of all included packages.
    */
    public double getTotalValue() {
        double tV = 0;
        for (int i = 0; i < csPacking.length; i++) {
            tV = tV + csPacking[i].getValue();
        }
        return tV;
    }

    /**
    * A method returning the total value of all the packages that are included in an array of
    * packages that is used as a parameter..
    *
    * @return totalValue The total value of all included packages.
    */
    public double getTotalValue(Package[] packing) {
        double totValue = 0;
        for (int i=0; i<packing.length; i++) {
            totValue += packing[i].getValue();
        }
        return totValue;
    }

    /**
    * A method that fills the cargo space with packages by putting them in the position given by their
    * base coordinates.
    *
    * @param cargo An array of the packages that should be placed.
    */
    public void fillCargoSpace(Package[] cargo) {
        for (int i = 0; i < cargo.length; i++) {
            if (!overlap(cargo[i])) {
                place(cargo[i]);
            }
        }
    }

    /**
    * A method that returns the length of the cargo space in 0.5m (i.e. the length of the three-dimensional
    * array representing it).
    *
    * @param int The length of the cargo space in 0.5m/the cargoSpace array.
    */
    public int getLength() {
        return cargoSpace.length;
    }

    /**
    * A method that returns the width of the cargo space in 0.5m (i.e. the length of the three-dimensional
    * array representing it).
    *
    * @param int The width of the cargo space in 0.5m/the cargoSpace array.
    */
    public int getWidth() {
        return cargoSpace[0].length;
    }

    /**
    * A method that returns the height of the cargo space in 0.5m (i.e. the length of the three-dimensional
    * array representing it).
    *
    * @param int The height of the cargo space in 0.5m/the cargoSpace array.
    */
    public int getHeight() {
        return cargoSpace[0][0].length;
    }

    /**
    * A method that fills a cargo space using an array of packages and placing them in random order.
    *
    * @param packing The packages that are placed in random order.
    */
    public void packRandom(Package[] packing) {
        int countPacked = 0;
        for (int i = 0; i < packing.length; i++) {
            int index = (int) (Math.random() * packing.length);
            if (!overlap(packing[index])) {
              place(packing[index]);
              countPacked++;
            }
        }
    }

    /**
    * A method that gives access to the list of all packages that were successfully placed in the
    * cargo space.
    *
    * @return csPacking The array of all packages included in the cargo space.
    */
    public Package[] getPacking() {
        return csPacking;
    }

    /**
    * Looks for an empty space where the aPackage can be placed and then sets its base coordinates.
    *
    * @param aPackage The package that should be placed.
    * @return int[] An array containing the new base coordinates of the package.
    */
  	public int[] getNextEmptySpaceCoords(Package aPackage) {
    		for (int i = 0; i < cargoSpace.length; i++) {
    	          for (int j = 0; j < cargoSpace[i].length; j++) {
    	              for (int k = 0; k < cargoSpace[i][j].length; k++) {
    	                  if (cargoSpace[i][j][k].equals("Empty")) {
    	                	  int coords[] = new int[3];
    	                	  coords[0] = i;
    	                	  coords[1] = j;
    	                	  coords[2] = k;
    	                	  aPackage.setBaseCoords(i, j, k);
    	                	  if (!overlap(aPackage))
    	                		  return coords;
    	                  }
    	              }
    	          }
    		}
    		return null;
  	}

  	/**
  	* A method to fill the cargo space according to a sequence of packages defined by the parameter packageTypes.
  	* Until there is space to place at least one of the smallest packages it keeps looking for empty space and
  	* trying to fill it with a specific package.
    *
  	* @param packageTypes An array containing the package types to place in the order they should be place.
  	*/
  	public void fillCargo(Package[] packageTypes) {
    		while (getNextEmptySpaceCoords(new Package("A")) != null) {
      			for (int i=0; i<packageTypes.length; i++) {
      				int[] coords;
      				Package p = new Package(packageTypes[i].getType());
      				while (getNextEmptySpaceCoords(p) != null) {
      					p = new Package(packageTypes[i].getType());
      					coords = getNextEmptySpaceCoords(p);
      					place(p);
      				}
      			}
    		}
  	}

    /**
    * A method that randomly fills the cargo space by selecting random packages and placing them until there
    * is no more space left.
    *
    * @param packageTypes An array containing a list of possible package types.
    * @param allowRotations A boolean value deciding whether the packages can be rotated or not.
    */
  	public void fillRandom(Package[] packageTypes, boolean allowRotations) {
    		Package p;
    		int[] coords;
    		while (getNextEmptySpaceCoords(new Package("A")) != null) {
      			p = new Package(packageTypes[Random.randomWithRange(0, packageTypes.length-1)].getType());
      			if (allowRotations) {
      				p.rotateRandom();
      			}
      			coords = getNextEmptySpaceCoords(p);
      			if (coords != null) {
      				place(p);
      			}
    		}
  	}

    /**
    * A method that provides information about the number of packages of each type in packageTypes
    * that are included in the cargo space.
    *
    * @return placed An array with the numbers of packages included in the cargo space corresponding
    *                to all the types in the global packageTypes array.
    */
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
