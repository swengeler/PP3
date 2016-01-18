package Poly3D;

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
	static Display3D panel3D;
	static Display2D panel2D;
	public static JFrame f;
	
	public static void represent(CargoSpace bestCS) {
		f = new JFrame();
		f.setSize(MainFrame.width, MainFrame.height);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
		f.addKeyListener(new KeyHandler());
		
		cubes = new Cube[bestCS.getPacking().length+1] ;
		for(int i=0;i<cubes.length-1;i++){
			cubes[i]= new Cube(bestCS.getPacking()[i]);
		}
		cubes[cubes.length-1]= new Cube(new Package("Truck"));
		
		panel2D = new Display2D(bestCS.getArray());
		panel3D = new Display3D();
		
		panel3D.setPreferredSize(new Dimension(200,200));
		panel2D.setPreferredSize(new Dimension(200,200));
		
		panel2D.setVisible(false);
		panel3D.setVisible(true);
		f.add(panel2D);
		f.add(panel3D);
	}
	
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
		for(int i = 0; i<cubes.length; i++){
			for(int l = 0; l<3; l++){
				if(cubes[i].getPackage() == "A"){
					g2.setStroke(new BasicStroke(1));
					if(l == 0)g.setColor(new Color(250,0,0));
					if(l == 1)g.setColor(new Color(200,0,0));
					if(l == 2)g.setColor(new Color(150,0,0));
				}
				if(cubes[i].getPackage() == "B"){
					g2.setStroke(new BasicStroke(1));
					if(l == 0)g.setColor(new Color(0,250,0));
					if(l == 1)g.setColor(new Color(0,200,0));
					if(l == 2)g.setColor(new Color(0,150,0));
				}
				if(cubes[i].getPackage() == "C"){
					g2.setStroke(new BasicStroke(1));
					if(l == 0)g.setColor(new Color(0,0,250));
					if(l == 1)g.setColor(new Color(0,0,200));
					if(l == 2)g.setColor(new Color(0,0,150));
				}
				if(cubes[i].getPackage() == "Truck"){
					g2.setStroke(new BasicStroke(5));
				}
				if(cubes[i].getPackage() != "Truck"){
					g2.fillPolygon(cubes[i].createPolygons()[l]);
				}
				g2.setColor(Color.BLACK);
				g2.drawPolygon(cubes[i].createPolygons()[l]);
				
			}
		}
	}
}
