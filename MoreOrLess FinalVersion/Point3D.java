/**
 * A Class, creating new Points, holding x, y and z positions
 *
 * @author Daniel Kaestner
 * @author Raffaele Piccini
 */
public class Point3D {

	private double x = 0, y = 0, z = 0;

	/**
	 * A Constructor to create new Points in the 3rd dimension
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param z z-coordinate
	 */
	public Point3D(double x, double y, double z){
		this.x = x; this.y = y; this.z = z;
	}

	/**
	 * Gives the x-coordinate of a Point
	 * @return x-coordinate
	 */
	public int getX(){
		return (int) x;
	}

	/**
	 * Sets the x-coordinate of a Point
	 * @return x-coordinate
	 */
	public void setX(double x){
		this.x = x;
	}

	/**
	 * Gives the y-coordinate of a Point
	 * @return y-coordinate
	 */
	public int getY(){
		return (int) y;
	}

	/**
	 * Sets the y-coordinate of a Point
	 * @return y-coordinate
	 */
	public void setY(double y){
		this.y = y;
	}

	/**
	 * Gives the z-coordinate of a Point
	 * @return z-coordinate
	 */
	public int getZ(){
		return (int) z;
	}

	/**
	 * Sets the z-coordinate of a Point
	 * @return z-coordinate
	 */
	public void setZ(double z){
		this.z = z;
	}

	public String toString(){
		return ("X: " + x + " Y: " + y + " Z: " + z);
	}
}
