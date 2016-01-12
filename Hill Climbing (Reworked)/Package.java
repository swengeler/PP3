/** 
* A new enumerated type that contains a default value and three types of packages.
*
* @author Henri Viigimäe
* @author Simon Wengeler
*/

public class Package {

    private String type;
    private int height;
    private int width;
    private int length;
    private double value;

    private int[] baseCoords = new int[3]; // where [X][Y][Z]
    private int[] rotations = new int[3]; // where [X][Y][Z]
    private int[][] coords;
    
    /**
    * A constructor that constructs a package with certain values according to the definition of
    * the three available pre-defined package types (APackage, BPackage, CPackage).
    * 
    * @param type The type of package that is supposed to be constructed.
    */
    public Package(String type) {
        if (type.equals("A")) {
            length = 4;
            width = 2;
            height = 2;
            value = 3;
            setPackage("A");
        }
        else if (type.equals("B")) {
            length = 4;
            width = 3;
            height = 2;
            value = 4;
            setPackage("B");
        }
        else if (type.equals("C")) {
            length = 3;
            width = 3;
            height = 3;
            value = 5;
            setPackage("C");
        }
    }
    
    /**
    * A constructor that creates a package with a given height, length and width(to create the "Other" package).
    */
    public Package(String type, int height, int width, int length, double value) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.value = value;
        setPackage(type);
    }
    
    // {X,Y,Z}, FROM LEFT TO RIGHT.
    /**
    * This method sets the shape of a package (the coordinates it occupies in the cargo space). The number and
    * values of the stored coordinates depend on the dimensions of the package. (In a previous versions the
    * measurements for the pre-demined packages A, B and C were stored in a three-dimensional array that would
    * be accessed in order to get the cooridates of the outer corners of the package.)
    *
    * @param type The desired type of package.
    */
    public void setPackage(String type) {
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
        this.type = type;
    }
    
    public void setBaseCoords(int x, int y, int z) {
        baseCoords[0] = x;
        baseCoords[1] = y;
        baseCoords[2] = z;
    }
    
    public void setBaseX(int x) {
        baseCoords[0] = x;
    }
    
    public void setBaseY(int y) {
        baseCoords[1] = y;
    }
    
    public void setBaseZ(int z) {
        baseCoords[2] = z;
    }
    
    public int[] getBaseCoords() {
        return baseCoords;
    }
    
    public void setRotations(int x, int y, int z) {
        for (int i = 0; i < x; i++)
            this.rotateX();
        for (int i = 0; i < y; i++)
            this.rotateY();
        for (int i = 0; i < z; i++)
            this.rotateZ();
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
    * @return length The length of the package (int 0.5m).
    */
    public int getLength() {
        return length;
    }
    
    /**
    * A method giving information about the dimensions of the package (width).
    *
    * @return width The width of the package (int 0.5m).
    */
    public int getWidth() {
        return width;
    }
    
    /**
    * A method giving information about the dimensions of the package (height).
    *
    * @return height The height of the package (int 0.5m).
    */
    public int getHeight() {
        return height;
    }
    
    public double getValue() {
        return value;
    }
    
    /**
    * A method giving information about the type of package that it is called on.
    *
    * @return type The type of package (APackage, BPackage, CPackage or Other).
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
    *Returns the minimum value of the x-coordinate of package
    *@return minimum value of the x-coordinate of a package
    */
    public int minX() {
        int min = coords[0][0];
        for (int i = 0; i < 8; i++)
            min = Math.min(min, coords[i][0]);
        return min;
    }
    /**
    *Returns the minimum value of the y-coordinate of a package
    *@return minimum value of the y-coordinate of a package
    */
    public int minY() {
        int min = coords[0][1];
        for (int i = 0; i < 8; i++)
            min = Math.min(min, coords[i][1]);
        return min;
    }
    /**
    *Returns the minimum value of the z-coordinate of a package
    *@return minimum value of the z-coordinate of a package
    */
    public int minZ() {
        int min = coords[0][2];
        for (int i = 0; i < 8; i++)
            min = Math.min(min, coords[i][2]);
        return min;
    }

    /**
     * Returns a random Package
     * @return random Package p
     */
    public Package random() {
        int random = Random.randomWithRange(1,3);
        if (random == 1) {
            return new Package("A");
        } else if (random == 2) {
            return new Package("B");
        } else {
            return new Package("C");
        }
    }
    
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
}