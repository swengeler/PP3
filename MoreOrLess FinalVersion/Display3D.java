import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display3D extends JPanel{
	static boolean toggleDisp;
	private static Cube[] cubes;
	private static CargoSpace bestCs;
	static Display3D panel3D;
	static Display2D panel2D;
	public static JFrame f;

	/**
	 * Sets up frame and add panels with the CargoSpace in 2D and 3D representation
	 * 
	 * @param bestCS the CargoSpace to represent
	 */
	public static void represent(CargoSpace bestCS) {
		bestCs = bestCS;
		f = new JFrame();
		f.setSize(1000, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
		f.addKeyListener(new KeyHandler());
		
		setUp();
		panel3D = new Display3D();

		panel3D.setPreferredSize(new Dimension(200,200));
		panel2D.setPreferredSize(new Dimension(200,200));

		panel2D.setVisible(false);
		panel3D.setVisible(true);
		f.add(panel2D);
		f.add(panel3D);
	}
	
	/**
	 * sets up the cubes-array from the CargoSpace
	 */
	public static void setUp(){
		cubes = new Cube[bestCs.getPacking().length+1] ;
		for(int i=0;i<cubes.length-1;i++){
			cubes[i]= new Cube(bestCs.getPacking()[i]);
		}
		cubes[cubes.length-1]= new Cube(new Package("Truck"));

		panel2D = new Display2D(bestCs.getArray());
	}

	/**
	 * toggles the representation (2D=true, 3D=false)
	 * 
	 * @param toggle 2D visible or not
	 */
	public static void toggleDisp(boolean toggle){
		toggleDisp = toggle;

		if(toggleDisp){
			panel2D.setVisible(true);
			panel3D.setVisible(false);
			panel2D.requestFocus();
			f.add(panel3D);
			f.add(panel2D);
		}
		else{
			panel2D.setVisible(false);
			panel3D.setVisible(true);
			panel3D.requestFocus();
			f.add(panel2D);
			f.add(panel3D);
		}
		f.repaint();
	}

	/**
	 * Rotates a cube-array around the x-Axis 
	 * 
	 * @param angle degrees to rotate
	 */
	public static void rotateX(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateX(angle);
		}
	}

	/**
	 * Rotates a cube-array around the y-Axis 
	 * 
	 * @param angle degrees to rotate
	 */
	public static void rotateY(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateY(angle);
		}
	}

	/**
	 * Rotates a cube-array around the z-Axis 
	 * 
	 * @param angle degrees to rotate
	 */
	public static void rotateZ(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateZ(angle);
		}
	}

	/**
	 * Draws the CargoSpace
	 */
	public void paintComponent(Graphics g){

		cubes = cubes[0].sort(cubes);

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		for(int i = 0; i<cubes.length; i++){
			for(int l = 0; l<3; l++){
				g2.setColor(cubes[i].getType().getColor());
			//	System.out.println(cubes[i].getType().getColor());
				if(cubes[i].getPackage() != "Truck"){
					g2.fillPolygon(cubes[i].createPolygons()[l]);
					g2.setColor(Color.BLACK);
					g2.drawPolygon(cubes[i].createPolygons()[l]);
				}
			}
		}
		for(int i = 0; i<cubes.length; i++){
			if(cubes[i].getPackage() == "Truck"){	
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.BLACK);
				for(int l = 0; l<3; l++){
				g2.drawPolygon(cubes[i].createPolygons()[l]);
				}
			}
		}
	}
}
