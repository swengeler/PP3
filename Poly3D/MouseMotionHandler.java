package Poly3D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener{
	private int mx = 0, my = 0;
		
	public void mouseDragged(MouseEvent e) {
	      int new_mx = e.getX();
	      int new_my = e.getY();
	      System.out.println(new_mx -mx);

	      cargoSpace3D.rotateX(-(new_mx - mx));
	      cargoSpace3D.rotateY(-(new_my - my));
	      cargoSpace3D.rotateZ(-(int)Math.sqrt((new_mx - mx)*(new_mx - mx)+(new_my - my)*(new_my - my)));

	      mx = new_mx;
	      my = new_my;
	  	  cargoSpace3D.panel.repaint();
		
	      e.consume();		
	}

	public void mouseMoved(MouseEvent arg0) {}
	
	class MouseHandler implements MouseListener{

		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent e) {
			mx = e.getX();
			my = e.getY();					
			e.consume();
		}
		public void mouseReleased(MouseEvent arg0) {}
		
	}
}
