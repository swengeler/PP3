import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.*;
import javax.swing.BorderFactory;

public class Display extends JPanel {

	public PackageType[][][] x;
	public int[][] y;
	public int layer = 0;

	private JLabel layerLabel;

    public static final int SQUARE_SIZE = 20;

	public Display(PackageType[][][] cargoSpace) {
        setMinimumSize(new Dimension(1000, 1000));
		setPreferredSize(new Dimension(1000, 1000));
        setMaximumSize(new Dimension(1000, 1000));

				setLayout(new BorderLayout());
		addControlPanel();

		//[length][width][height]
		x = new PackageType[cargoSpace.length][cargoSpace[0].length][cargoSpace[0][0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                for (int k = 0; k < x[0][0].length; k++) {
                    x[i][j][k] = cargoSpace[i][j][k];
                }
            }
        }

		addKeyListener(new KeyHandler());
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0 && layer < x.length - 1) {
                    layer += e.getWheelRotation();
                    repaint();
                } else if (e.getWheelRotation() < 0 && layer > 0) {
                    layer += e.getWheelRotation();
                    repaint();
                }
            }
        });
        setFocusable(true);
	}

	private void addControlPanel() {

		layerLabel = new JLabel("Current layer: " + layer);
		add(layerLabel, BorderLayout.SOUTH);

	}

    class KeyHandler implements KeyListener {
			private boolean top, bottom, back, front, left, right;
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP){
					rotateZCounterClockwise();
                    layer = 0;
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN){
					rotateZClockwise();
                    layer = 0;
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					rotateXCounterClockwise();
                    layer = 0;
                    repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					rotateXClockwise();
                    layer = 0;
                    repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_X){
					rotateYClockwise();
					layer = 0;
                    repaint();
				}

				if (e.getKeyCode() == KeyEvent.VK_C){
					rotateYCounterClockwise();
					layer = 0;
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

				layerLabel.setText("Current layer: " + layer);

        Graphics2D g2 = (Graphics2D) g;

        int distance = 20;

        for (int i = 0; i < x[0].length; i++) {
            for (int j = 0; j < x[0][0].length; j++) {
                if (layer >= 0 && layer < x.length)
                    drawSquare(g2, distance + SQUARE_SIZE * i, distance + x[0][0].length * SQUARE_SIZE - (j + 1) * SQUARE_SIZE, x[layer][i][j]);
            }
        }
    }


    private void drawSquare(Graphics2D g2, int x, int y, PackageType type) {

        Color[] colors = {Color.LIGHT_GRAY, new Color(0, 0, 102), new Color(0, 102, 0), new Color(36, 191, 175), new Color(255, 227, 40), new Color(170, 40, 255)};

        if (type == null)
            g2.setColor(Color.LIGHT_GRAY);
        else
            g2.setColor(colors[type.ordinal()]);
        g2.fill(new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE));

	}

    //------------------------------------------------------------------------------------------------------------------------------------------------------

  	/*
  	 * A Method to rotate the "Truck-Array" up
  	 * @return int[][][] The new rotated Array
  	 */
  	public void rotateYClockwise(){
  		PackageType[][][] temp = new PackageType[x[0][0].length][x[0].length][x.length];

  		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[k][j][temp.length-1-i];
				}
			}
		}
		System.out.println("Rotate Y (counterclockwise)");
        x = temp;
  	}
  	//------------------------------------------------------------------------------------------------------------------------------------------------------

  	/*
  	 * A Method to rotate the "Truck-Array" down
  	 * @return int[][][] The new rotated Array
  	 */
  	public void rotateYCounterClockwise(){
  		PackageType[][][] temp = new PackageType[x[0][0].length][x[0].length][x.length];

  		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][temp[i][j].length-1-k] = x[k][j][i];
				}
			}
		}
		System.out.println("Rotate Y (clockwise)");
        x = temp;
  	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/*
	 * A Method to rotate the "Truck-Array" up
	 * @return int[][][] The new rotated Array
	 */
	public void rotateZCounterClockwise(){
		PackageType[][][] temp = new PackageType[x[0].length][x.length][x[0][0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][temp[i].length-1-j][k] = x[j][i][k];
				}
			}
		}

		System.out.println("Rotate Z (counter-clockwise)");
        x = temp;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/*
	 * A Method to rotate the "Truck-Array" down
	 * @return int[][][] The new rotated Array
	 */
	public void rotateZClockwise(){
		PackageType[][][] temp = new PackageType[x[0].length][x.length][x[0][0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[j][temp.length-1-i][k];
				}
			}
		}
		System.out.println("Rotate Z (clockwise from above)");
        x = temp;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/*
	 * A Method to rotate the "Truck-Array" to the left
	 * @return int[][][] The new rotated Array
	 */
	public void rotateXCounterClockwise(){
		PackageType[][][] temp = new PackageType[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[i][k][temp[i].length-j-1];
				}
			}
		}
		System.out.println("Rotate X (left)");
        x = temp;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/*
	 * A Method to rotate the "Truck-Array" to the right
	 * @return int[][][] The new rotated Array
	 */
	public void rotateXClockwise(){
		PackageType[][][] temp = new PackageType[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][temp[i][j].length-k-1] = x[i][k][j];
				}
			}
		}
		System.out.println("Rotate X (right)");
        x = temp;
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
