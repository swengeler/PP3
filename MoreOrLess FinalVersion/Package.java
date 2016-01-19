/**
* A class representing packages that can be placed in a cargo space. It contains all
* the needed information to know what part of the space a package occupies and the methods
* to manipulate a package (e.g. by rotating it).
*
* @author Henri Viigimäe
* @author Simon Wengeler
*/

public class Package {

    private static String[] newTypes = new String[0];
    private static int[][] newDimensions = new int[0][3];
    private static double[] newValues = new double[0];

    private String type;
    private double value;

    private int height;
    private int width;
    private int length;

    // the original dimensions of the package (to enable the packingToChromosome method to know which state of rotation the package is in)
    private int orHeight;
    private int orWidth;
    private int orLength;

    private int[] baseCoords = new int[3]; // where [X][Y][Z]
    private int[] rotations = new int[3]; // where [X][Y][Z]
    private int[][] coords;

    private int[][][] coordsTable = new int[][][] {
        {{0,0,0}, {0,1,0}, {1,0,0}, {2,0,0}, {3,0,0}}, // L package
        {{0,0,0}, {0,1,0}, {1,0,0}, {1,1,0}, {2,0,0}}, // P package
        {{0,0,0}, {0,1,0}, {0,2,0}, {1,1,0}, {2,1,0}} // T package
    };

    /**
    * A constructor that constructs a package with certain values according to the definition of
    * the six available pre-defined package types (A, B, C, L, P, T) or the data stored temporarily
    * in the static arrays that store information about new package types..
    *
    * @param type The type/name of package that is supposed to be constructed.
    */
    public Package(String type) {
        if (type.equals("A")) {
            length = 4;
            width = 2;
            height = 2;
            orLength = 4;
            orWidth = 2;
            orHeight = 2;
            value = 3;
            setPackage("A");
        }
        else if (type.equals("B")) {
            length = 4;
            width = 3;
            height = 2;
            orLength = 4;
            orWidth = 3;
            orHeight = 2;
            value = 4;
            setPackage("B");
        }
        else if (type.equals("C")) {
            length = 3;
            width = 3;
            height = 3;
            orLength = 3;
            orWidth = 3;
            orHeight = 3;
            value = 5;
            setPackage("C");
        }
        else if (type.equals("L")) {
            length = 4;
            height = 1;
            width = 2;
            orLength = 4;
            orWidth = 1;
            orHeight = 2;
            value = 3;
            setPackage("L");
        } else if (type.equals("P")) {
            length = 3;
            height = 1;
            width = 2;
            orLength = 3;
            orWidth = 1;
            orHeight = 2;
            value = 4;
            setPackage("P");
        } else if (type.equals("T")) {
            length = 3;
            height = 1;
            width = 3;
            orLength = 3;
            orWidth = 1;
            orHeight = 3;
            value = 5;
            setPackage("T");
        }
        else if (type.equals("Truck")) {
            length = 33;
            height = 7;
            width = 10;
            orLength = 7;
            orWidth = 35;
            orHeight = 9;
            value = 0;
            setPackage("Truck");
        } else {
            boolean found = false;
            for (int i = 0; i < newTypes.length && !found; i++) {
                if (type.equalsIgnoreCase(newTypes[i])) {
                    found = true;
                    length = newDimensions[i][0];
                    width = newDimensions[i][1];
                    height = newDimensions[i][2];
                    orLength = newDimensions[i][0];
                    orWidth = newDimensions[i][1];
                    orHeight = newDimensions[i][2];
                    value = newValues[i];
                    setPackage(newTypes[i]);
                }
            }
        }
    }

    /**
    * A constructor that creates a package with a given type/name, length, width, height and value.
    * That information is stored in several static arrays so that a package of the same type can
    * later be created without completely defining it new.
    */
    public Package(String type, int length, int width, int height, double value) {
        this.height = height;
        this.orHeight = height;
        this.width = width;
        this.orWidth = width;
        this.length = length;
        this.orLength = length;
        this.value = value;


        String[] nnTypes = new String[newTypes.length + 1];
        System.arraycopy(newTypes, 0, nnTypes, 0, newTypes.length);
        nnTypes[nnTypes.length - 1] = type;
        newTypes = nnTypes;

        int[][] nnDimensions = new int[newDimensions.length + 1][3];
        for (int i = 0; i < newDimensions.length; i++) {
          System.arraycopy(newDimensions[i], 0, nnDimensions[i], 0, 3);
        }
        nnDimensions[nnDimensions.length - 1][0] = length;
        nnDimensions[nnDimensions.length - 1][1] = width;
        nnDimensions[nnDimensions.length - 1][2] = height;
        newDimensions = nnDimensions;

        double[] nnValues = new double[newValues.length + 1];
        System.arraycopy(newValues, 0, nnValues, 0, newValues.length);
        nnValues[nnValues.length - 1] = value;
        newValues = nnValues;

        setPackage(type);
    }

    /**
    * This method sets the shape of a package (the coordinates it occupies in the cargo space). The number and
    * values of the stored coordinates depend on the dimensions of the package. Alternatively (for the
    * pentomino-shaped packages) an array holding the coordinates occupied by the package is accessed to create
    * the package.
    *
    * @param type The desired type of package.
    */
    public void setPackage(String type) {
        if (type.equalsIgnoreCase("L")) {
            coords = new int[5][3];
            for (int i = 0; i < coords.length; i++) {
                for (int j = 0; j < coords[0].length; j++) {
                    coords[i][j] = coordsTable[0][i][j];
                }
            }
        } else if (type.equalsIgnoreCase("P")) {
            coords = new int[5][3];
            for (int i = 0; i < coords.length; i++) {
                for (int j = 0; j < coords[0].length; j++) {
                    coords[i][j] = coordsTable[1][i][j];
                }
            }
        } else if (type.equalsIgnoreCase("T")){
            coords = new int[5][3];
            for (int i = 0; i < coords.length; i++) {
                for (int j = 0; j < coords[0].length; j++) {
                    coords[i][j] = coordsTable[2][i][j];
                }
            }
        } else {
            coords = new int[height * width * length][3];
            int counter = 0;
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < width; j++) {
                    for (int k = 0; k < height; k++) {
                        coords[counter][0] = i;
                        coords[counter][1] = j;
                        coords[counter][2] = k;
                        counter++;
                    }
                }
            }
        }
        this.type = type;
    }

    /**
    * A method used set the base coordinates of a package to certain values.
    *
    * @param x The new x-coordinate of the base point.
    * @param y The new y-coordinate of the base point.
    * @param z The new z-coordinate of the base point.
    */
    public void setBaseCoords(int x, int y, int z) {
        baseCoords[0] = x;
        baseCoords[1] = y;
        baseCoords[2] = z;
    }

    /**
    * A method to set only one of the base coordinates to a certain value (x).
    *
    * @param x The new x-coordinate of the base point.
    */
    public void setBaseX(int x) {
        baseCoords[0] = x;
    }

    /**
    * A method to set only one of the base coordinates to a certain value (y).
    *
    * @param y The new y-coordinate of the base point.
    */
    public void setBaseY(int y) {
        baseCoords[1] = y;
    }

    /**
    * A method to set only one of the base coordinates to a certain value (z).
    *
    * @param z The new z-coordinate of the base point.
    */
    public void setBaseZ(int z) {
        baseCoords[2] = z;
    }

    /**
    * A method that gives information about the base coordinates of the package (the coordinates
    * of the lower left back corner of the pacakge).
    *
    * @return baseCoords An array storing the three base coordinates of the package.
    */
    public int[] getBaseCoords() {
        return baseCoords;
    }

    /**
    * A method that rotates the packages a certain number of times around each axis.
    *
    * @param x The number of rotations around the x-axis.
    * @param y The number of rotations around the y-axis.
    * @param z The number of rotations around the z-axis.
    */
    public void setRotations(int x, int y, int z) {
        for (int i = 0; i < x; i++)
            this.rotateX();
        for (int i = 0; i < y; i++)
            this.rotateY();
        for (int i = 0; i < z; i++)
            this.rotateZ();
    }

    /**
    * A different method of rotating a package. It uses the fact that there are a certain number
    * of states in which a package can be rotated. The index of that state determines how the
    * package will be rotated.
    *
    * @param state The index designating a certain rotation state of the package.
    */
    public void setRotations(int state) {
      if (this.getNrRotations() == 3 && this.length == this.width) {
          if (state >= 1)
              this.rotateY();
          if (state == 2)
              this.rotateZ();
      } else if (this.getNrRotations() == 3 && this.length == this.height) {
          if (state >= 1)
              this.rotateX();
          if (state == 2)
              this.rotateY();
      } else if (this.getNrRotations() == 3 && this.width == this.height) {
          if (state >= 1)
              this.rotateY();
          if (state == 2)
              this.rotateX();
      } else if (this.getNrRotations() == 6) {
          if (state >= 1)
              this.rotateX();
          if (state >= 2)
              this.rotateY();
          if (state >= 3)
              this.rotateZ();
          if (state >= 4)
              this.rotateX();
          if (state == 5)
              this.rotateY();
      }
    }

    /**
    * A method that returns the coordinates occupied by the package.
    *
    * @return coords The array holding the coordinates of the 8 outer corners of the package.
    */
    public int[][] getCoords() {
        return coords;
    }

    /**
    * A method that changes the array holding the coordinates which the package occupies (mainly
    * used for rotations).
    *
    * @param newCoords The new coordinates occupied by the package.
    */
    public void setCoords(int[][] newCoords) {
        this.coords = newCoords;
    }

    /**
    * A method giving information about the dimensions of the package (length).
    *
    * @return length The length of the package (in 0.5m).
    */
    public int getLength() {
        return length;
    }

    /**
    * A method giving information about the dimensions of the package (width).
    *
    * @return width The width of the package (in 0.5m).
    */
    public int getWidth() {
        return width;
    }

    /**
    * A method giving information about the dimensions of the package (height).
    *
    * @return height The height of the package (in 0.5m).
    */
    public int getHeight() {
        return height;
    }

    /**
    * A method giving information about the original dimensions of the package (length).
    *
    * @return length The original length of the package (in 0.5m).
    */
    public int getOrLength() {
        return orHeight;
    }

    /**
    * A method giving information about the original dimensions of the package (width).
    *
    * @return width The original width of the package (in 0.5m).
    */
    public int getOrWidth() {
        return orWidth;
    }

    /**
    * A method giving information about the original dimensions of the package (height).
    *
    * @return height The original height of the package (in 0.5m).
    */
    public int getOrHeight() {
        return orHeight;
    }

    /**
    * A method giving information about the value of the package.
    *
    * @return value The value of the package.
    */
    public double getValue() {
        return value;
    }

    /**
    * A method giving information about the type of package that it is called on.
    *
    * @return type The type of package.
    */
    public String getType() {
        return type;
    }

    /**
    * A method that changes the coordinates of the package in such a manner that they now represent
    * the package rotated around the x-axis (length-axis) of the imaginary coordinate system.
    */
    public void rotateX() {
        int[][] newCoords = new int[coords.length][coords[0].length];
        for (int i = 0; i < newCoords.length; i++) {
            newCoords[i][0] = coords[i][0];
            newCoords[i][1] = -coords[i][2];
            newCoords[i][2] = coords[i][1];
        }
        for (int i = 0; i < newCoords.length; i++) {
            for (int j = 0; j < newCoords[0].length; j++) {
                if (newCoords[i][j] < 0)
                    newCoords[i][j] = -newCoords[i][j];
            }
        }
        if (rotations[0] == 3)
            rotations[0] = 0;
        else
            rotations[0]++;
        int temp = width;
        width = height;
        height = temp;
        this.coords = newCoords;
    }

    /**
    * A method that changes the coordinates of the package in such a manner that they now represent
    * the package rotated around the y-axis (width-axis) of the imaginary coordinate system.
    */
    public void rotateY() {
        int[][] newCoords = new int[coords.length][coords[0].length];
        for (int i = 0; i < newCoords.length; i++) {
            newCoords[i][0] = coords[i][2];
            newCoords[i][1] = coords[i][1];
            newCoords[i][2] = -coords[i][0];
        }
        for (int i = 0; i < newCoords.length; i++) {
            for (int j = 0; j < newCoords[0].length; j++) {
                if (newCoords[i][j] < 0)
                    newCoords[i][j] = -newCoords[i][j];
            }
        }
        if (rotations[1] == 3)
            rotations[1] = 0;
        else
            rotations[1]++;
        int temp = length;
        length = height;
        height = temp;
        this.coords = newCoords;
    }

    /**
    * A method that changes the coordinates of the package in such a manner that they now represent
    * the package rotated around the z-axis (height-axis) of the imaginary coordinate system.
    */
    public void rotateZ() {
        int[][] newCoords = new int[coords.length][coords[0].length];
        for (int i = 0; i < newCoords.length; i++) {
            newCoords[i][0] = -coords[i][1];
            newCoords[i][1] = coords[i][0];
            newCoords[i][2] = coords[i][2];
        }
        for (int i = 0; i < newCoords.length; i++) {
            for (int j = 0; j < newCoords[0].length; j++) {
                if (newCoords[i][j] < 0)
                    newCoords[i][j] = -newCoords[i][j];
            }
        }
        if (rotations[2] == 3)
            rotations[2] = 0;
        else
            rotations[2]++;
        int temp = length;
        length = width;
        width = temp;
        this.coords = newCoords;
    }

    /**
    * A method that rotates the package a random number of times around each axis.
    */
    public void rotateRandom() {
        int randomX = (int) (Math.random() * 4);
        int randomY = (int) (Math.random() * 4);
        int randomZ = (int) (Math.random() * 4);

        for (int i = 0; i < randomX; i++)
            this.rotateX();
        for (int i = 0; i < randomY; i++)
            this.rotateY();
        for (int i = 0; i < randomZ; i++)
            this.rotateZ();
    }

    /**
    * A method used to determine whether two packages overlap.
    *
    * @param other The other package which might overlap with the one that the method is called on.
    * @return boolean A boolean giving information about whether or not the packages overlap.
    */
    public boolean overlaps(Package other) {
        int[][] otherCoords = other.getCoords();
        int[] otherBCoords = other.getBaseCoords();
        for (int i = 0; i < otherCoords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                if (otherCoords[i][0] + otherBCoords[0] == coords[j][0] + baseCoords[0] && otherCoords[i][1] + otherBCoords[1] == coords[j][1] + baseCoords[1] && otherCoords[i][2] + otherBCoords[2] == coords[j][2] + baseCoords[2])
                    return true;
            }
        }
        return false;
    }

    /**
    * A method giving information about the number of rotation states that a package can have.
    *
    * @return rotationStates The number of different rotation states that the package has.
    */
    public int getNrRotations() {
      int rotationStates = 0;
      if (this.length == this.width && this.length == this.height) {
          rotationStates = 1;
      } else if (this.length == this.width || this.length == this.height || this.width == this.height) {
          rotationStates = 3;
      } else {
          rotationStates = 6;
      }
      return rotationStates;
    }

    /**
    * A method that clones a package (actually creating a new instance of the class).
    *
    * @return clone The clone of the package.
    */
    public Package clone() {
        Package clone = new Package(this.getType());
        clone.type = this.type;
        clone.value = this.value;

        clone.height = this.height;
        clone.width = this.width;
        clone.length = this.length;

        // the original dimensions of the package (to enable the packingToChromosome method to know which state of rotation the package is in)
        clone.orHeight = this.orHeight;
        clone.orWidth = this.orWidth;
        clone.orLength = this.orLength;

        for (int i = 0; i < baseCoords.length; i++) {
            clone.baseCoords[i] = this.baseCoords[i];
            clone.rotations[i] = this.rotations[i];
        }

        for (int i = 0; i < coords.length; i++) {
            clone.coords[i] = this.coords[i];
        }

        return clone;
    }

}
