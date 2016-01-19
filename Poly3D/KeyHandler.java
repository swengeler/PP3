package Poly3D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

	public class KeyHandler implements KeyListener{

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP){
				Display3D.rotateX(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				Display3D.rotateX(10);
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				Display3D.rotateY(10);
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				Display3D.rotateY(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_X){
				Display3D.rotateZ(-10);
			}
			if(e.getKeyCode() == KeyEvent.VK_C){
				Display3D.rotateZ(10);
			}
			if(e.getKeyCode() == KeyEvent.VK_R){
				Display3D.setUp();
			}
			Display3D.f.repaint();
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
