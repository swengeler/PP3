import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JFrame;

public class DisplayFrame extends JFrame {
	
	public int[][][] x;
	public int[][] y;
	public int layer = 0;
	
	public DisplayFrame() {
		setSize(1000, 1000);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new KeyHandler());
    
		//[length][width][height]
		x = new int[33][5][8];
		
		for(int i = x.length-1; i>=0; i--){
			for(int j = x[i].length-1; j>=0; j--){
				for(int k = x[i][j].length-1; k>=0; k--){
					x[i][j][k] = i;
				}	
			}		
		}

	}
    
    class KeyHandler implements KeyListener {
			private boolean top, bottom, back, front, left, right;
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					x = rotateUp();
                    repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					x = rotateDown();
                    repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					x = rotateLeft();
                    repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					x = rotateRight();
                    repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_PLUS){
					if(layer<x.length-1)
					layer++;
                    repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_MINUS){
					if(layer>0)
					layer--;
                    repaint();
				}
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					print();
				}
			}

			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0) {}
	    }
	
    //------------------------------------------------------------------------------------------------------------------------------------------------------

    public void painComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        int abstand = 20;
        int squareSize = 20;
        
        for (int i = 0; i < x[0].length; i++) {
            for (int j = 0; j < x[0][0].length; j++) {
                if (x[layer][i][j] != 0)
                    drawSquare(g2, abstand + squareSize * i, abstand + (x[0][0].length * squareSize - (j + 1) * squareSize), x[layer][i][j] - 1);
            }
        }
    }

    private void drawSquare(Graphics2D g2, int x, int y, int index) {
        
        Color[] colors = {new Color(169, 24, 24), new Color(0, 0, 102), new Color(0, 102, 0), new Color(36, 191, 175), new Color(255, 227, 40), new Color(170, 40, 255)};
		
        g2.setColor(colors[index]);
        g2.fill(new Rectangle(x, y, 20, 20)); // 20 = squareSize
 		
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
		System.out.println("Rotate Up");
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
		System.out.println("Rotate Down");
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
		System.out.println("Rotate Left");
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
		System.out.println("Rotate Right");
		return temp;
	}
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void print(){
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