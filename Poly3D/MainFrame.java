package Poly3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JPanel {
   static int width=1000;
   static int height=1000;
   static MainFrame m;
   static JFrame xyz;
   static JLabel[] text;
   static JSpinner[] spinner;
   static JButton startButton;
   static Cube[] cubes;
   static Timer t;

   public MainFrame(){
       setBounds(0,0,width,height);
   }
   
   public static void main(String[]args) throws InterruptedException, AWTException {
       xyz=new JFrame();
       xyz.setLayout(null);
       xyz.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       xyz.setBounds(width+50,0,750,420);


       text=new JLabel[7];
       spinner = new JSpinner[6];

       for(int i=0;i<6;i++) {
    	   spinner[i] = new JSpinner();
           text[i]=new JLabel();
           text[i].setBounds(100*i + 70, 40, 50, 50);
           text[i].setFont(new Font(text[i].getFont().getName(),text[i].getFont().getStyle(),15));
           spinner[i].setBounds(100*i+50, 75, 50, 50);
           spinner[i].setVisible(true);
           xyz.add(text[i]);
           xyz.add(spinner[i]);
       }
       text[0].setText("A");
       text[1].setText("B");
       text[2].setText("C");
       text[3].setText("L");
       text[4].setText("P");
       text[5].setText("T");
       
       text[6] = new JLabel();
       text[6].setBounds(50, 150, 200, 50);
       text[6].setFont(new Font(text[6].getFont().getName(),text[6].getFont().getStyle(),15));
       text[6].setText("Packages Left: ");
       xyz.add(text[6]);
        
       startButton = new JButton();
       startButton.setBounds(625, 75, 100, 50);
       startButton.setText("Start");
       startButton.setVisible(true);
       startButton.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   int cntr = 0;
    		   for(int i = 0; i<spinner.length;i++){
    			   GreedyAlgorithm.nrPack[i] = (int) spinner[i].getValue();      
    			   cntr += (int) spinner[i].getValue();
    		   }
    		   if(cntr > 0){
    			   GreedyAlgorithm.main(args);
    			   text[6].setText("Packages Left: " + GreedyAlgorithm.packagesLeft.size());
    		   }
    		}
       });
       xyz.add(startButton);
              
       cubes = new Cube[3];
       cubes[0] = new Cube(new Package("A"),100,300,100);
       cubes[1] = new Cube(new Package("B"),300,300,100);
       cubes[2] = new Cube(new Package("C"),500,300,100);
    
       t = new Timer(30, new ActionListener(){
    	   public void actionPerformed(ActionEvent e) {
			for(int i = 0; i<cubes.length; i++){
				cubes[i].RotateX(1);
				cubes[i].RotateY(2);
				cubes[i].RotateZ(3);
				xyz.repaint();
			}
    	   }  
       });
       
       t.start();
       
       m = new MainFrame();
       xyz.setVisible(true);
       xyz.add(m);
       xyz.repaint();
          
   }

   @Override
   protected void paintComponent(Graphics g) {
	   super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for(int i = 0; i<cubes.length; i++){
			for(int l = 0; l<3; l++){
				if(cubes[i].getPackage() == "A"){
					if(l == 0)g.setColor(new Color(250,0,0));
					if(l == 1)g.setColor(new Color(200,0,0));
					if(l == 2)g.setColor(new Color(150,0,0));
				}
				if(cubes[i].getPackage() == "B"){
					if(l == 0)g.setColor(new Color(0,250,0));
					if(l == 1)g.setColor(new Color(0,200,0));
					if(l == 2)g.setColor(new Color(0,150,0));
				}
				if(cubes[i].getPackage() == "C"){
					if(l == 0)g.setColor(new Color(0,0,250));
					if(l == 1)g.setColor(new Color(0,0,200));
					if(l == 2)g.setColor(new Color(0,0,150));
				}
				
				g2.fillPolygon(cubes[i].createPolygons()[l]);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(cubes[i].createPolygons()[l]);
				
			}
		}
   }
}
