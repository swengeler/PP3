import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
* A class used to give a proper visual representation of the cargo space. It includes
* methods to rotate the view of the cargo space as well as scroll through the layers
* of the cargo space (with a thickness of 0.5 m) since it is the only way to give
* information about the third dimension right now.
*
* @author Nicola Gheza
* @author Daniel Kaestner
* @author Simon Wengeler
*/

public class Display2D extends JPanel {

	public String[][][] x;
	public int[][] y;
	public int layer = 0;

	private JLabel layerLabel;

    public static final int SQUARE_SIZE = 20;

    /**
    * A constructor to create a Display object (funtioning as an extension of a JPanel panel)
    * that also acts as a listener for the key- and mousewheel events which can change the view
    * of the cargo space.
    *
    * @param cargoSpace The three-dimensional array representation of the cargo space that is
    *                   supposed to be displayed graphically.
    */
	public Display2D(String[][][] cargoSpace) {
        setMinimumSize(new Dimension(750, 750));
        setPreferredSize(new Dimension(750, 750));
        setMaximumSize(new Dimension(750, 750));

        setLayout(new BorderLayout());
		addControlPanel();

		//[length][width][height]
		x = new String[cargoSpace.length][cargoSpace[0].length][cargoSpace[0][0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                for (int k = 0; k < x[0][0].length; k++) {
                    x[i][j][k] = cargoSpace[i][j][k];
                }
            }
        }

        setLayout(new BorderLayout());
		addControlPanel();
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

    /**
    * A method to add a label in the GUI that gives information about which layer is being viewed.
    */
	private void addControlPanel() {
		layerLabel = new JLabel("Current layer: " + layer);
		add(layerLabel, BorderLayout.SOUTH);
	}

    /**
    * A class that is used for processing user input (via the keyboard).
    */
    class KeyHandler implements KeyListener {
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

    /**
    * The overridden paintComponent method that is used to draw the visual representation of the
    * cargo space on the panel.
    *
    * @param g The Graphics object used to do the drawing.
    */
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

    /**
    * A method that draws a square of a certain color (to distinguish between different) types of
    * packages in each location of the cargo space that is filled with a package.
    *
    * @param g2 The Graphics2D object passed down from the paintComponent method.
    * @param x The x-coordinate (in pixels) of the upper left corner of the square that is drawn.
    * @param y The y-coordinate (in pixels) of the upper left corner of the square that is drawn.
    * @param type The type of package that is represented by the square (to decide the color).
    */
    private void drawSquare(Graphics2D g2, int x, int y, String type) {

        Color[] colors = {new Color(250, 0, 0), new Color(0, 250, 0), new Color(0, 0, 250), new Color(255, 227, 40), new Color(170, 40, 255), new Color(50, 50, 50)};

        if (type == null || type.equals("Empty"))
        	g2.setColor(Color.LIGHT_GRAY);
        else if (type.equals("A"))
            g2.setColor(colors[0]);
        else if (type.equals("B"))
            g2.setColor(colors[1]);
        else if (type.equals("C"))
            g2.setColor(colors[2]);
        else if (type.equals("L"))
            g2.setColor(colors[3]);
        else if (type.equals("P"))
            g2.setColor(colors[4]);
        else if (type.equals("T"))
            g2.setColor(colors[5]);
        else
            g2.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));

        g2.fill(new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE));
        g2.setColor(Color.black);
        g2.draw(new Rectangle(x, y, SQUARE_SIZE, SQUARE_SIZE));


	}

    //------------------------------------------------------------------------------------------------------------------------------------------------------

  	/**
    * A Method to "rotate" the cargo space array clockwise around the y-axis (front side: bottom).
    */
  	public void rotateYClockwise() {
  		String[][][] temp = new String[x[0][0].length][x[0].length][x.length];

  		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[k][j][temp.length-1-i];
				}
			}
		}
		System.out.println("Rotate Y (clockwise)");
        x = temp;
  	}
  	//------------------------------------------------------------------------------------------------------------------------------------------------------

  	/**
    * A Method to "rotate" the cargo space array counter-clockwise around the y-axis (front side: top).
    */
  	public void rotateYCounterClockwise() {
  		String[][][] temp = new String[x[0][0].length][x[0].length][x.length];

  		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][temp[i][j].length-1-k] = x[k][j][i];
				}
			}
		}
		System.out.println("Rotate Y (counter-clockwise)");
        x = temp;
  	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
    * A Method to "rotate" the cargo space array counter-clockwise around the z-axis (front side: left).
    */
	public void rotateZCounterClockwise() {
		String[][][] temp = new String[x[0].length][x.length][x[0][0].length];
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

	/**
    * A Method to "rotate" the cargo space array clockwise around the z-axis (front side: right).
    */
	public void rotateZClockwise() {
		String[][][] temp = new String[x[0].length][x.length][x[0][0].length];
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

	/**
    * A Method to "rotate" the cargo space array counter-clockwise around the x-axis (upper side: right).
    */
	public void rotateXCounterClockwise() {
		String[][][] temp = new String[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][k] = x[i][k][temp[i].length-j-1];
				}
			}
		}
		System.out.println("Rotate X (counter-clockwise)");
        x = temp;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
    * A Method to "rotate" the cargo space array clockwise around the x-axis (upper side: left).
    */
	public void rotateXClockwise() {
		String[][][] temp = new String[x.length][x[0][0].length][x[0].length];
		for(int i = 0; i<temp.length;i++){
			for(int j = 0; j<temp[i].length;j++){
				for(int k = 0; k<temp[i][j].length;k++){
					temp[i][j][temp[i][j].length-k-1] = x[i][k][j];
				}
			}
		}
		System.out.println("Rotate X (clockwise)");
        x = temp;
	}

	//------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
    * A method that gives a simple printout of the cargo space array in the command line (using letters
    * instead of graphical representations of the single units/packages).
    */
	public void print() {
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
