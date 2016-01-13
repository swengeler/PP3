package Poly3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class cargoSpace3D extends JPanel{
	static boolean keyPressed = false;
	private static Cube[] cubes;
	static cargoSpace3D panel; 
	public static void represent(Package[] packages) {
		cubes = new Cube[packages.length] ;
		for(int i=0;i<cubes.length;i++){
			cubes[i]= new Cube(packages[i],packages[i].getBaseCoords()[0]+500,packages[i].getBaseCoords()[1]+500,packages[i].getBaseCoords()[2]+500);
		}
		
		JFrame f = new JFrame();
		f.setSize(1900, 1000);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new cargoSpace3D();
		
		panel.setPreferredSize(new Dimension(200,200));
		f.add(panel);
		f.setVisible(true);
		f.addKeyListener(new KeyHandler());
		panel.repaint();
	}
	
	public static void rotateX(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateX(angle);
		}
	}

	public static void rotateY(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateY(angle);
		}
	}
	
	public static void rotateZ(int angle){
		for(int i = 0; i<cubes.length; i++){
			cubes[i].RotateZ(angle);
		}
	}
	
	public void paintComponent(Graphics g){
		
		cubes = cubes[0].sort(cubes);
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//g2.setStroke(new BasicStroke(5));
		for(int i = 0; i<cubes.length; i++){
			for(int l = 0; l<3; l++){
				
				System.out.println(cubes[i].getMaxZ());
				if(cubes[i].getPackage() == "A") g.setColor(Color.blue);
				if(cubes[i].getPackage() == "B") g.setColor(Color.red);
				if(cubes[i].getPackage() == "C") g.setColor(Color.green);
				
				g2.fillPolygon(cubes[i].createPolygons()[l]);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(cubes[i].createPolygons()[l]);
				
			}
		}
	}
}
