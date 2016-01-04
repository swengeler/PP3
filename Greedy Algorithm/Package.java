/** 
* A new enumerated type that contains a default value and three types of packages.
*
* @author Henri Viigimäe
* @author Simon Wengeler
*/

public class Package {

    private PackageType type;
    /**
    * The height that is needed to create a desired package.
    */
    private int height;
    /**
    * The width that is needed to create a desired package.
    */
    private int width;
    /**
    * The length that is needed to create a desired package.
    */
    private int length;
    /**
    * The value of a package.
    */
    private double value;
    /**
    * The volume of a package.
    */
    private double packageVolume;

    /**
    * The package is defined by 8 coordinates.
    */
    private int[][] coords;
    /**
    *The individual coordinates for the different packages are stored in a three-dimensionl array
    */
    
    private int[][] coordsTable/*= new int[][][] {
        {{0,0,0}, {0,0,0}, {0,0,0}, {0,0,0}, {0,0,0},{0,0,0}, {0,0,0}, {0,0,0}}, // No package
        {{0,0,0}, {0,2,0}, {0,2,2}, {0,0,2},{4,0,0},{4,2,0},{4,2,2},{4,0,2}}, // A package
        {{0,0,0}, {0,2,0}, {0,2,3}, {0,0,3}, {4,0,0},{4,2,0},{4,2,2.5},{4,0,3}}, // B package
        {{0,0,0}, {0,3,0}, {0,3,3}, {0,0,3}, {3,0,0},{3,3,0},{3,3,3},{3,0,3}}, // C package
        {{0,0,0}, {0,width * 2,0}, {0,width * 2,height * 2}, {0,0,height * 2}, {length * 2,0,0},{length * 2,0,height * 2},{length * 2,width * 2,height * 2},{length * 2,height * 2,0}}, //  Other package
    }*/;
    
    /**
    *A constructor that creates the "empty package".
    */
    public Package() {
        coords = new int[8][3];
        setPackage(PackageType.NoPackage);
    }
    
    public Package(String type) {
        coords = new int[8][3];
        if (type.equals("A")) {
            length = 4;
            width = 2;
            height = 2;
            value = 3;
            setPackage(PackageType.APackage);
        }
        else if (type.equals("B")) {
            length = 4;
            width = 3;
            height = 2;
            value = 4;
            setPackage(PackageType.BPackage);
        }
        else if (type.equals("C")) {
            length = 3;
            width = 3;
            height = 3;
            value = 5;
            setPackage(PackageType.CPackage);
        }
    }
    
    /**
    * A constructor that creates a package with a given height, length and width(to create the "Other" package).
    */
    public Package(int height, int width, int length, double value) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.value = value;
        setPackage(PackageType.Other);
    }
    
    // {X,Y,Z}, FROM LEFT TO RIGHT.
    /**
    * This method sets the shape of a package.
    * <p>
    * The coordinates of all the packages are stored in a three-dimensional array. 
    * The method looks up where the coordinates of the desired package are located in the array and sets the value of the type variable to the desired package.
    * </p>
    * @param aPackage the desired type of package
    */
    public void setPackage(PackageType type) {
        coordsTable = new int[][] {{0, 0, 0}, {0, width, 0}, {0, width, height}, {0, 0, height}, {length, 0, 0},{length, 0, height},{length, width, height},{length, height, 0}};
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                coords[i][j] = coordsTable[i][j];
            }
        }
        
        this.type = type;
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
    *A method that gives the "worth" of a certain package .
    */
    public double getWorth() {   
        if(type == PackageType.APackage)
            {
            packageVolume = 2;
            value = 1;
            }
        else if(type == PackageType.BPackage)
            {
            packageVolume = 3;
            value = 2;
            }
        else if(type == PackageType.CPackage)
            {
            packageVolume = 3.375;
            value = 2.5;
            }
        else if(type == PackageType.Other)
            {
            packageVolume = height*length*width;
            value = 1.25;
            }
        return value/packageVolume;
    }
    
    /**
    * A method giving information about the type of package that it is called on.
    *
    * @return type The type of package (APackage, BPackage, CPackage or Other).
    */
    public PackageType getType() {
        return type;
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

}
