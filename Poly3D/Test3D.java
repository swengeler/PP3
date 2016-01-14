package Poly3D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Test3D extends JPanel{
	
	private static Cube[] cubes;
	static boolean keyPressed = false;
	
	public Test3D(){}
	
	public static void setCargo(Package[] cS){
		cubes = new Cube[cS.length];
		for(int i = 0; i<cS.length; i++){
					System.out.println(cS[i]);
					cubes[i] = new Cube(cS[i]);
		}
		main(null);
	}
	
	public static void main(String[] args){
		JFrame f = new JFrame();
		f.setSize(1900, 1000);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Test3D panel = new Test3D();
		
		panel.setPreferredSize(new Dimension(200,200));
		f.add(panel);
		f.setVisible(true);
		f.addKeyListener(new KeyHandler());
		panel.repaint();
		while(true){
			redraw();
			panel.repaint();
		}
	}
	
	public static void redraw(){
		long timeNow = System.currentTimeMillis();
		if(keyPressed == true){
			keyPressed = false;
			for(int i = 0; i<cubes.length; i++){
						cubes[i].RotateZ(10);
						cubes[i].RotateY(10);
						cubes[i].RotateX(1);
			
			}
		}
	}
		
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//g2.setStroke(new BasicStroke(5));
		for(int i = 0; i<cubes.length; i++){
			for(int l = 0; l<3; l++){
						if(l == 0) g.setColor(Color.blue);
						if(l == 1) g.setColor(Color.red);
						if(l == 2) g.setColor(Color.green);
						
					//	g2.fillPolygon(cubes[i][j][k].createPolygons(i*100,j*0,50*k)[l]);
					//	g2.drawPolygon(cubes[i][j][k].createPolygons(i*100,j*100,50*k)[l]);
				
			}
		}
	}
}
