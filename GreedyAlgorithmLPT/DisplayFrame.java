import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.*;
import javax.swing.BorderFactory;

public class DisplayFrame extends JPanel {
	
	public int[][][] x;
	public int[][] y;
	public int layer = 0;
    
    public static final int SQUARE_SIZE = 20;
	
	public DisplayFrame() {
        setMinimumSize(new Dimension(1000, 1000));
		setPreferredSize(new Dimension(1000, 1000));
        setMaximumSize(new Dimension(1000, 1000));
    
		//[length][width][height]
		x = new int[33][5][8];
		
		for (int i = 0; i < 6; i++) {
            for (int j = 0; j < x[0].length; j++) {
                for (int k = 0; k < x[0][0].length; k++) {
                    x[i][j][k] = i;
                }
            }
        }

		addKeyListener(new KeyHandler());
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if ((e.getWheelRotation() > 0 && layer < x.length - 1) || (e.getWheelRotation() < 0 && layer > 0)) {
                    layer += e.getWheelRotation();
                    repaint();
                }
            }
        });
        setFocusable(true);
        setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.RED));
	}
    
    class KeyHandler implements KeyListener {
			private boolean top, bottom, back, front, left, right;
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP){
					x = rotateUp();
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					x = rotateDown();
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					x = rotateLeft();
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					x = rotateRight();
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_PLUS){
					if (layer < x.length - 1)
					layer++;
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_MINUS){
					if (layer > 0)
					layer--;
                    repaint();
				}
				
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					print();
				}
			}

			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0) {}
	    }
	
    //------------------------------------------------------------------------------------------------------------------------------------------------------

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int abstand = 20;
        
        for (int i = 0; i < x[0].length; i++) {
            for (int j = 0; j < x[0][0].length; j++) {
                drawSquare(g2, abstand + SQUARE_SIZE * i, abstand + /*(x[0][0].length * SQUARE_SIZE - (j + 1)*/j * SQUARE_SIZE, x[layer][i][j]);
            }
        }
    }


    private void drawSquare(Graphics2D g2, int x, int y, int index) {
        
        Color[] colors = {new Color(169, 24, 24), new Color(0, 0, 102), new Color(0, 102, 0), new Color(36, 191, 175), new Color(255, 227, 40), new Color(170, 40, 255)};
		
        g2.setColor(colors[index]);
        g2.fill(new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE)); // 20 = SQUARE_SIZE
 		
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * A Method to rotate the "Truck-Array" up
	 * @return int[][][] The new rotated Array
	 */
	public int[][][] rotateUp(){
		int[][][] temp = new int[x[0].length][x.length][x[0][0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][temp[i].length-1-j][k] = x[j][i][k];
				}	
			}	
		}
		System.out.println("Rotate Z (counter-clockwise from above)");
		return temp;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * A Method to rotate the "Truck-Array" down
	 * @return int[][][] The new rotated Array
	 */
	public int[][][] rotateDown(){
		int[][][] temp = new int[x[0].length][x.length][x[0][0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[j][temp.length-1-i][k];
				}	
			}	
		}
		System.out.println("Rotate Z (clockwise from above)");
		return temp;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * A Method to rotate the "Truck-Array" to the left
	 * @return int[][][] The new rotated Array
	 */
	public int[][][] rotateLeft(){		
		int[][][] temp = new int[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[i][k][temp[i].length-j-1];
				}	
			}	
		}
		System.out.println("Rotate X (left)");
		return temp;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * A Method to rotate the "Truck-Array" to the right
	 * @return int[][][] The new rotated Array
	 */
	public int[][][] rotateRight(){
		int[][][] temp = new int[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][temp[i][j].length-k-1] = x[i][k][j];
				}	
			}	
		}
		System.out.println("Rotate X (right)");
		return temp;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void print(){
		if (layer >= x.length) {layer = x.length - 1;}
		
		System.out.println(layer);
			for(int j = x[layer].length-1; j>=0; j--){					
				for(int k = x[layer][j].length-1; k>=0; k--){
						System.out.print(x[layer][j][k]);
				}
				System.out.println();
			}
			System.out.println();
		
	}
}