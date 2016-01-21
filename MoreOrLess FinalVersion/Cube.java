//package Poly3D;

import java.awt.Polygon;

/*
 * 	The Middle of the cube is at (0,0,0)
 *
 *     Y	   5X---------X6
 *     |	   /|        /|
 *     |	  / |       / |
 *     |	1X--|------X2 |
 *     |	 |  |	   |  |
 *     |	 | 4X------|--X7
 *     |	 | /       | /
 *     |	 |/		   |/
 *     |	0X---------X3
 *     |
 *     |-----------------------------X
 *	  /
 *   /
 *  /
 * Z
 *
*/

/**
 * A class to create 3D looking Boxes consisting of Polygons
 *
 * @author Daniel Kaestner
 * @author Raffaele Piccini
 */
public class Cube {

	private Package type;
	private boolean mainFrame;
	private double  xpos = 200, ypos = 200, zpos = 200, width, height, length;
	static int tempBaseX = 0, tempBaseY = 0, tempBaseZ = 0;
	private Point3D[] allPoints;
	private double[] fx = new double[8], fy = new double[8], fz = new double[8];
	private static int Scale = 10, Xpos = 33*Scale*2+500, Ypos = 5*Scale*2+700, Zpos = 8*Scale*2+500;

	/**
	 * A constructor to create a new cube
	 *
	 * @param Package type of the package
	 */
	public Cube(Package type){
		mainFrame = false;
		this.type = type;
		allPoints = createPoints();
		for(int i = 0; i<fx.length; i++){
			fx[i] = allPoints[i].getX();
			fy[i] = allPoints[i].getY();
			fz[i] = allPoints[i].getZ();
		}
			xpos = (150+(type.getBaseCoords()[0])*Scale*2+width/2);
			ypos = (250+(type.getBaseCoords()[1])*Scale*2+height/2);
			zpos = (250+(type.getBaseCoords()[2])*Scale*2+length/2);
	}
		
	/**
	 * A Constructor to create cubes at positions defined by the parameters 
	 * 
	 * @param type type of the package
	 * @param xPos x-position of the cube
	 * @param yPos y-position of the cube
	 * @param zPos z-position of the cube
	 */
	public Cube(Package type, int xPos, int yPos, int zPos){
		mainFrame = true;
		this.type = type;
		allPoints = createPoints();
		for(int i = 0; i<fx.length; i++){
			fx[i] = allPoints[i].getX();
			fy[i] = allPoints[i].getY();
			fz[i] = allPoints[i].getZ();
		}
		xpos = xPos;ypos = yPos;zpos = zPos;
	}

	/**
	 * A method which creates the cube, according to the package type consisting of 6 polygons
	 *
	 * @param xpos x-position of the cube
	 * @param ypos y-position of the cube
	 * @param zpos z-position of the cube
	 *
	 * @return returns the 3 Polygons visible on the surface
	 */
	public Polygon[] createPolygons(){
		Polygon[] polygon = new Polygon[6], surface = new Polygon[3];
		int[][] places=new int[6][4];
		int[] surfacePolygons = new int[3];
		int temp = 0, maxZ=maxZ();

		//Front
		polygon[0] = new Polygon(new int[]{(int)(allPoints[0].getX() + xpos), (int)(allPoints[1].getX() + xpos), (int)(allPoints[2].getX() + xpos), (int)(allPoints[3].getX() + xpos)},
					 		     new int[]{(int)(allPoints[0].getY() + ypos), (int)(allPoints[1].getY() + ypos), (int)(allPoints[2].getY() + ypos), (int)(allPoints[3].getY() + ypos)},
					 		     4);
		//Back
		polygon[1] = new Polygon(new int[]{(int)(allPoints[4].getX() + xpos), (int)(allPoints[5].getX() + xpos), (int)(allPoints[6].getX() + xpos), (int)(allPoints[7].getX() + xpos)},
	 		     	 			 new int[]{(int)(allPoints[4].getY() + ypos), (int)(allPoints[5].getY() + ypos), (int)(allPoints[6].getY() + ypos), (int)(allPoints[7].getY() + ypos)},
	 		     	 			 4);
		//Left
		polygon[2] = new Polygon(new int[]{(int)(allPoints[0].getX() + xpos), (int)(allPoints[1].getX() + xpos), (int)(allPoints[5].getX() + xpos), (int)(allPoints[4].getX() + xpos)},
								 new int[]{(int)(allPoints[0].getY() + ypos), (int)(allPoints[1].getY() + ypos), (int)(allPoints[5].getY() + ypos), (int)(allPoints[4].getY() + ypos)},
								 4);
		//Right
		polygon[3] = new Polygon(new int[]{(int)(allPoints[3].getX() + xpos), (int)(allPoints[2].getX() + xpos), (int)(allPoints[6].getX() + xpos), (int)(allPoints[7].getX() + xpos)},
				 				 new int[]{(int)(allPoints[3].getY() + ypos), (int)(allPoints[2].getY() + ypos), (int)(allPoints[6].getY() + ypos), (int)(allPoints[7].getY() + ypos)},
				 				 4);
		//Top
		polygon[4] = new Polygon(new int[]{(int)(allPoints[1].getX() + xpos), (int)(allPoints[5].getX() + xpos), (int)(allPoints[6].getX() + xpos), (int)(allPoints[2].getX() + xpos)},
				 				 new int[]{(int)(allPoints[1].getY() + ypos), (int)(allPoints[5].getY() + ypos), (int)(allPoints[6].getY() + ypos), (int)(allPoints[2].getY() + ypos)},
				 				 4);
		//Bottom
		polygon[5] = new Polygon(new int[]{(int)(allPoints[0].getX() + xpos), (int)(allPoints[4].getX() + xpos), (int)(allPoints[7].getX() + xpos), (int)(allPoints[3].getX() + xpos)},
				 				 new int[]{(int)(allPoints[0].getY() + ypos), (int)(allPoints[4].getY() + ypos), (int)(allPoints[7].getY() + ypos), (int)(allPoints[3].getY() + ypos)},
				 				 4);

	    places[0][0]=0;places[0][1]=1;places[0][2]=2;places[0][3]=3;
	    places[1][0]=4;places[1][1]=5;places[1][2]=6;places[1][3]=7;
	    places[2][0]=0;places[2][1]=1;places[2][2]=5;places[2][3]=4;
	    places[3][0]=3;places[3][1]=2;places[3][2]=6;places[3][3]=7;
	    places[4][0]=1;places[4][1]=5;places[4][2]=6;places[4][3]=2;
	    places[5][0]=0;places[5][1]=4;places[5][2]=7;places[5][3]=3;

		for(int i=0;i<places.length;i++){
	       for (int j = 0; j < places[i].length; j++) {
	    	   int t=places[i][j];
	           if (t == maxZ) {
	        	   surfacePolygons[temp] = i;
	               temp++;
	           }
	       }
	    }
		for(int i = 0; i<surface.length; i++){
			surface[i] = polygon[surfacePolygons[i]];
		}
		return surface;
	}

	/**
	 *A method which calculates the maximum z-value to get the surface Polygons
	 *
	 * @return the maximum Z coordinate 
	 */
    private int maxZ(){
    	int max=0;
    	int maxz=0;
	    for (int i = 0; i < 8; i++) {
	    	if (max < allPoints[i].getZ()) {
	               max = (int) allPoints[i].getZ();
	               maxz = i;
	        }
	    }
	    return maxz;
	}

   /**
    * A method to rotate a Cube around the x-axis
    * @param a Degree of rotation
    */
   public void RotateX(int a) {
	   for (int i = 0; i < allPoints.length; i++) {
    	   double newfy = Math.cos(Math.toRadians(a)) * fy[i] + -1*Math.sin(Math.toRadians(a)) *fz[i];
           double newfz = Math.sin(Math.toRadians(a)) * fy[i] + Math.cos(Math.toRadians(a)) * fz[i];
           fy[i]=newfy;
           fz[i]=newfz;
           allPoints[i].setY(fy[i]);
           allPoints[i].setZ(fz[i]);
       }
       if(!mainFrame){
    	   ypos -=(Ypos/2);zpos -= (Zpos/2);
	       double newy = Math.cos(Math.toRadians(a)) * ypos + Math.sin(Math.toRadians(a)) *zpos;
	       double newz = -1*Math.sin(Math.toRadians(a)) * ypos + Math.cos(Math.toRadians(a)) * zpos;
	       if(a<0){
	    	   ypos = (newy+20);
	    	   zpos = (newz+20);
	       }
	       else{
	    	   ypos = (newy-20);
	    	   zpos = (newz-20);
	       }
	       ypos += (Ypos/2);zpos += (Zpos/2);
  		}
   }

   /**
    * A method to rotate a Cube around the y-axis
    * @param a Degree of rotation
    */
   public void RotateY(int a) {
	   for (int i = 0; i < allPoints.length; i++) {
           double newfx =Math.cos(Math.toRadians(a)) * fx[i] + -1 * Math.sin(Math.toRadians(a)) *fz[i];
           double newfz =Math.sin(Math.toRadians(a)) * fx[i] + Math.cos(Math.toRadians(a)) *fz[i];
           fx[i]=newfx;
           fz[i]=newfz;

           allPoints[i].setX(fx[i]);
           allPoints[i].setZ(fz[i]);
       }
       if(!mainFrame){
		   xpos -= (Xpos)/2;zpos -= (Zpos)/2;
		   double newx = Math.cos(Math.toRadians(a)) * xpos + Math.sin(Math.toRadians(a)) *zpos;
	       double newz = -1*Math.sin(Math.toRadians(a)) * xpos + Math.cos(Math.toRadians(a)) * zpos;
	       if(a<0){
	    	   xpos = (newx+20);
	    	   zpos = (newz+20);
	       }
	       else{
	    	   xpos = (newx-20);
	    	   zpos = (newz-20);
	       }
	       xpos +=(Xpos)/2;zpos += (Zpos)/2;
	   }
   }

   /**
    * A method to rotate a Cube around the z-axis
    * 
    * @param a Degree of rotation
    */
   public void RotateZ(int a) {
	   for (int i = 0; i < allPoints.length; i++) {
		   double newfx =Math.cos(Math.toRadians(a)) * fx[i] + -1*Math.sin(Math.toRadians(a)) *fy[i];
	       double newfy =Math.sin(Math.toRadians(a)) * fx[i] + Math.cos(Math.toRadians(a)) *fy[i];

	       fx[i]=newfx;
	       fy[i]=newfy;
	       allPoints[i].setX(fx[i]);
	       allPoints[i].setY(fy[i]);
	   }
       if(!mainFrame){
		   ypos-=(Ypos)/2;xpos-=(Xpos)/2;;
		   double newx = Math.cos(Math.toRadians(a)) * xpos + -1*Math.sin(Math.toRadians(a)) *ypos;
	       double newy = Math.sin(Math.toRadians(a)) * xpos + Math.cos(Math.toRadians(a)) * ypos;
	       if(a<0){
	    	   xpos = (newx+20);
	    	   ypos = (newy+20);
	       }
	       else{
	    	   xpos = (newx-20);
	    	   ypos = (newy-20);
	       }
	       ypos+=(Ypos)/2;xpos+=(Xpos)/2;
	   }
   }

   
   /**
    * A Method to sort the cubes according to their z-position
    * 
    * @param cubes the cubes array which is currently used
    * @return cubes returns the sorted cubes array
    */
   public Cube[] sort(Cube[] cubes){
	   int[] zCoords = new int[cubes.length];

	   for(int i = 0; i < zCoords.length; i++){
		   zCoords[i] = (int) cubes[i].zpos;
	   }

	   for (int i = zCoords.length-1; i >= 0; i--) {
           for (int j = 0; j < (zCoords.length - 1); j++) {
               if (zCoords[j] < zCoords[(j+1)]) {
                   int temp;
                   Cube tempCube;

                   temp = zCoords[j];
                   tempCube = cubes[j];

                   zCoords[j] = zCoords[(j+1)];
                   zCoords[(j+1)] = temp;

                   cubes[j] = cubes[(j+1)];
                   cubes[(j+1)] = tempCube;
              }
           }
       }
	   return cubes;
   }

   /**
    * A method to set the points of the corner, the width, the height and the length of the cube
    *
    * @param baseCoords coordinates of the upper right corner from the cube according to the package type
    * @return an array holding 8 Points with x, y and z coordinate
    */
   private Point3D[] createPoints(){
	   allPoints = new Point3D[8];
		//						   X 							,Y							 ,Z
		allPoints[0] = new Point3D(-(type.getLength() * Scale) , -(type.getHeight() * Scale) ,  (type.getWidth() * Scale));
		allPoints[1] = new Point3D(-(type.getLength() * Scale) ,  (type.getHeight() * Scale) ,  (type.getWidth() * Scale));
		allPoints[2] = new Point3D( (type.getLength() * Scale) ,  (type.getHeight() * Scale) ,  (type.getWidth() * Scale));
		allPoints[3] = new Point3D( (type.getLength() * Scale) , -(type.getHeight() * Scale) ,  (type.getWidth() * Scale));
		allPoints[4] = new Point3D(-(type.getLength() * Scale) , -(type.getHeight() * Scale) , -(type.getWidth() * Scale));
		allPoints[5] = new Point3D(-(type.getLength() * Scale) ,  (type.getHeight() * Scale) , -(type.getWidth() * Scale));
		allPoints[6] = new Point3D( (type.getLength() * Scale) ,  (type.getHeight() * Scale) , -(type.getWidth() * Scale));
		allPoints[7] = new Point3D( (type.getLength() * Scale) , -(type.getHeight() * Scale) , -(type.getWidth() * Scale));

		if(allPoints[0].getX()-allPoints[3].getX()<0)
		     width = -(allPoints[0].getX()-allPoints[3].getX());
	    else width =  (allPoints[0].getX()-allPoints[3].getX());

		if(allPoints[0].getY()-allPoints[1].getY()<0)
		     height = -(allPoints[0].getY()-allPoints[1].getY());
  	    else height =  (allPoints[0].getY()-allPoints[1].getY());

		if(allPoints[0].getZ()-allPoints[4].getZ()<0)
		     length = -(allPoints[0].getZ()-allPoints[4].getZ());
	    else length =  (allPoints[0].getZ()-allPoints[4].getZ());

		return allPoints;
   }
   
   /**
    * A Method to return the type of the package
    * 
    * @return String type of the package
    */
   public String getPackage(){
	   return type.getType();
   }
   
   /** A Method to return the type of the package
    * 
    * @return type of the package
    */
   public Package getType(){
	   return type;
   }
}
