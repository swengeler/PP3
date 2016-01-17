package Poly3D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

	public class KeyHandler implements KeyListener{

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP){
				cargoSpace3D.rotateX(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				cargoSpace3D.rotateX(10);
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				cargoSpace3D.rotateY(10);
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				cargoSpace3D.rotateY(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_X){
				cargoSpace3D.rotateZ(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_C){
				cargoSpace3D.rotateZ(10);
			}
			cargoSpace3D.panel.repaint();
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
