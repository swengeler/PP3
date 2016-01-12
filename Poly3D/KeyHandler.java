package Poly3D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

	public class KeyHandler implements KeyListener{

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				Test3D.keyPressed = true;
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	}
