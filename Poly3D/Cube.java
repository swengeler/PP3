package Poly3D;

import java.awt.Point;
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
	private int  Scale = 10, x, y, z;
	private Point3D[] allPoints;
	double[] fx = new double[8], fy = new double[8], fz = new double[8];
	
	/**
	 * A constructor to create a new cube 
	 * 
	 * @param Package type of the package
	 */
	public Cube(Package type, int x, int y, int z){
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		allPoints = createPoints(type.getBaseCoords());
		for(int i = 0; i<fx.length; i++){
			fx[i] = allPoints[i].getX();
			fy[i] = allPoints[i].getY();
			fz[i] = allPoints[i].getZ();
		}
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
	public Polygon[] createPolygons(int xpos, int ypos, int zpos){
		Polygon[] polygon = new Polygon[6], surface = new Polygon[3];
		int[][] places=new int[6][4];
		int[] surfacePolygons = new int[3];
		int temp = 0, maxZ=maxZ();;	 
        
		//Front
		polygon[0] = new Polygon(new int[]{allPoints[0].getX() + xpos, allPoints[1].getX() + xpos, allPoints[2].getX() + xpos, allPoints[3].getX() + xpos},
					 		     new int[]{allPoints[0].getY() + ypos, allPoints[1].getY() + ypos, allPoints[2].getY() + ypos, allPoints[3].getY() + ypos},
					 		     4);                            
		//Back                                                                            
		polygon[1] = new Polygon(new int[]{allPoints[4].getX() + xpos, allPoints[5].getX() + xpos, allPoints[6].getX() + xpos, allPoints[7].getX() + xpos},
	 		     	 			 new int[]{allPoints[4].getY() + ypos, allPoints[5].getY() + ypos, allPoints[6].getY() + ypos, allPoints[7].getY() + ypos},
	 		     	 			 4);                                                       
		//Left                                                                             
		polygon[2] = new Polygon(new int[]{allPoints[0].getX() + xpos, allPoints[1].getX() + xpos, allPoints[5].getX() + xpos, allPoints[4].getX() + xpos},
								 new int[]{allPoints[0].getY() + ypos, allPoints[1].getY() + ypos, allPoints[5].getY() + ypos, allPoints[4].getY() + ypos},
								 4);
		//Right
		polygon[3] = new Polygon(new int[]{allPoints[3].getX() + xpos, allPoints[2].getX() + xpos, allPoints[6].getX() + xpos, allPoints[7].getX() + xpos},
				 				 new int[]{allPoints[3].getY() + ypos, allPoints[2].getY() + ypos, allPoints[6].getY() + ypos, allPoints[7].getY() + ypos},
				 				 4);
		//Top
		polygon[4] = new Polygon(new int[]{allPoints[1].getX() + xpos, allPoints[5].getX() + xpos, allPoints[6].getX() + xpos, allPoints[2].getX() + xpos},
				 				 new int[]{allPoints[1].getY() + ypos, allPoints[5].getY() + ypos, allPoints[6].getY() + ypos, allPoints[2].getY() + ypos},
				 				 4);
		//Bottom
		polygon[5] = new Polygon(new int[]{allPoints[0].getX() + xpos, allPoints[4].getX() + xpos, allPoints[7].getX() + xpos, allPoints[3].getX() + xpos},
				 				 new int[]{allPoints[0].getY() + ypos, allPoints[4].getY() + ypos, allPoints[7].getY() + ypos, allPoints[3].getY() + ypos},
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
	               max = allPoints[i].getZ();
	               maxz = i;
	        }
	    }
	    return maxz;
	}

   /**
    * A Method to rotate a Cube around the x-axis
    * @param a Degree of rotation
    */
   public void RotateX(int a) {
       for (int i = 0; i < allPoints.length; i++) {
           double newfy = Math.cos(Math.toRadians(a)) * fy[i] + -1*Math.sin(Math.toRadians(a)) *fz[i];
           double newfz = Math.sin(Math.toRadians(a)) * fy[i] + Math.cos(Math.toRadians(a)) * fz[i];
           fy[i]=newfy;
           fz[i]=newfz;

           allPoints[i].setY((int)fy[i]);
           allPoints[i].setZ((int)fz[i]);
       }
   }
   
   /**
    * A Method to rotate a Cube around the y-axis
    * @param a Degree of rotation
    */
   public void RotateY(int a) {
       for (int i = 0; i < allPoints.length; i++) {
           double newfx =Math.cos(Math.toRadians(a)) * fx[i] + Math.sin(Math.toRadians(a)) *fz[i];
           double newfz =-1 * Math.sin(Math.toRadians(a)) * fx[i] + Math.cos(Math.toRadians(a)) *fz[i];
           fx[i]=newfx;
           fz[i]=newfz;

           allPoints[i].setX((int)fx[i]);
           allPoints[i].setZ((int)fz[i]);
       }
   }

   /**
    * A Method to rotate a Cube around the z-axis
    * @param a Degree of rotation
    */
   public void RotateZ(int a) {
	   for (int i = 0; i < allPoints.length; i++) {
		   double newfx =Math.cos(Math.toRadians(a)) * fx[i] + -1*Math.sin(Math.toRadians(a)) *fy[i];    
	       double newfy =Math.sin(Math.toRadians(a)) * fx[i] + Math.cos(Math.toRadians(a)) *fy[i];
	       
	       fx[i]=newfx;
	       fy[i]=newfy;
	       allPoints[i].setX((int)fx[i]);
	       allPoints[i].setY((int)fy[i]);      
	   }
   }

   /**
    * A method to set the points of the corner of the cube
    * 
    * @param baseCoords coordinates of the upper right corner from the cube according to the package type 
    * @return an array holding 8 Points with x, y and z coordinate
    */
   private Point3D[] createPoints(int[] baseCoords){
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
	    *	 /
	    *   /
	    *  /
	    * Z
	    * 
	   */
		Point3D[] allPoints = new Point3D[8];
		//						   X 									      ,Y											 ,Z
		allPoints[0] = new Point3D(-(baseCoords[0]) * Scale				      , -(baseCoords[1]) * Scale 				     ,   baseCoords[2] * Scale);
		allPoints[1] = new Point3D(-(baseCoords[0])	* Scale	 		          ,  (baseCoords[1]  + type.getHeight()) * Scale ,   baseCoords[2] * Scale);
		allPoints[2] = new Point3D( (baseCoords[0]  + type.getWidth())* Scale ,  (baseCoords[1]  + type.getHeight()) * Scale ,   baseCoords[2] * Scale);
		allPoints[3] = new Point3D( (baseCoords[0]  + type.getWidth())* Scale , -(baseCoords[1]) * Scale			         ,   baseCoords[2] * Scale);
		allPoints[4] = new Point3D(-(baseCoords[0]) * Scale				      , -(baseCoords[1]) * Scale 			         , -(baseCoords[2]  + type.getLength())* Scale);
		allPoints[5] = new Point3D(-(baseCoords[0])	* Scale			   	 	  ,  (baseCoords[1]  + type.getHeight()) * Scale , -(baseCoords[2]  + type.getLength())* Scale);
		allPoints[6] = new Point3D( (baseCoords[0]  + type.getWidth()) * Scale,  (baseCoords[1]  + type.getHeight()) * Scale , -(baseCoords[2]  + type.getLength())* Scale);
		allPoints[7] = new Point3D( (baseCoords[0]  + type.getWidth()) * Scale, -(baseCoords[1]) * Scale				     , -(baseCoords[2]  + type.getLength())* Scale);
		
		for(int i = 0; i<allPoints.length; i++){
			allPoints[i].setX(allPoints[i].getX() + 20*x);
			allPoints[i].setY(allPoints[i].getY() + 20*y);
			allPoints[i].setZ(allPoints[i].getZ() + 20*z);
		}
		
	/*	System.out.println(type.getType());
		for(int i = 0; i<allPoints.length; i++){
			System.out.println(allPoints[i].toString());
		}*/
		return allPoints;
	}
}
