package Solution;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class RotateArray {
	
	public static int[][][] x;
	public static int[][] y;
	public static int layer = 0;
	
	public static void main(String[] args) {
	 
		class KeyHandler implements KeyListener{
			private boolean top, bottom, back, front, left, right;
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP){
					x = rotateUp();
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					x = rotateDown();
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					x = rotateLeft();
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					x = rotateRight();
				}
				if(e.getKeyCode() == KeyEvent.VK_PLUS){
					if(layer<x.length-1)
						layer++;
				}
				if(e.getKeyCode() == KeyEvent.VK_MINUS){
					if(layer>0)
						layer--;
				}
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					print();
				}
			}

			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent arg0) {}
	    }
	
		JFrame f = new JFrame();
		f.setSize(100, 100);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addKeyListener(new KeyHandler());
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
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/*
	 * A Method to rotate the "Truck-Array" up
	 * @return int[][][] The new rotated Array
	 */
	public static int[][][] rotateUp(){
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
	public static int[][][] rotateDown(){
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
	public static int[][][] rotateLeft(){		
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
	public static int[][][] rotateRight(){
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
	
	public static void print(){
		if(layer>=x.length) layer = x.length-1;
		
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