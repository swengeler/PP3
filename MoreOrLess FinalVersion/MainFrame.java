package Poly3D;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JPanel{
   static int width=1000;
   static int height=1000;
   static int length=1000;
   private static MainFrame m;
   private static JFrame xyz;
   private static JLabel[] text;
   private static JSpinner[] spinner;
   private static JButton startButton, hillClimbing, genetic;
   private static Cube[] cubes;
   private static JCheckBox disp2D, disp3D;
   private static Timer t;

   public MainFrame(){
       setBounds(0,0,width,height);
   }
   
   public static void main(String[]args) throws InterruptedException, AWTException {
       xyz=new JFrame();
       xyz.setLayout(null);
       xyz.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       xyz.setBounds(width+50,0,750,420);


       text=new JLabel[9];
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
       
       spinner[3].setEnabled(false);
       spinner[4].setEnabled(false);
       spinner[5].setEnabled(false);
       
       text[0].setText("A");
       text[1].setText("B");
       text[2].setText("C");
       text[3].setText("L");
       text[4].setText("P");
       text[5].setText("T");
       
       text[6] = new JLabel();
       text[6].setBounds(50, 150, 250, 50);
       text[6].setFont(new Font(text[6].getFont().getName(),text[6].getFont().getStyle(),15));
       text[6].setText("Packages Left: ");
       xyz.add(text[6]);
       
       text[7] = new JLabel();
       text[7].setBounds(50, 175, 250, 50);
       text[7].setFont(new Font(text[6].getFont().getName(),text[6].getFont().getStyle(),15));
       text[7].setText("Time taken: ");
       xyz.add(text[7]);
        
       text[8] = new JLabel();
       text[8].setBounds(50, 200, 250, 50);
       text[8].setFont(new Font(text[6].getFont().getName(),text[6].getFont().getStyle(),15));
       text[8].setText("Gaps Left: ");
       xyz.add(text[8]);
       
       
       disp2D = new JCheckBox();
       disp2D.setBounds(620, 290, 100, 25);
       disp2D.setText("Toggle rep");
       disp2D.setVisible(true);
       disp2D.setEnabled(false);
       disp2D.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		  Display3D.toggleDisp(disp2D.isSelected());
    		}
       });
       xyz.add(disp2D);
       
       startButton = new JButton();
       startButton.setBounds(625, 75, 100, 50);
       startButton.setText("Greedy");
       startButton.setVisible(true);
       startButton.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   int cntr = 0;
    		   GreedyAlgorithm g = new GreedyAlgorithm();
    		   if((int)spinner[0].getValue() >= 0 || (int)spinner[1].getValue() >= 0 || (int) spinner[2].getValue() >= 0){
	    		   for(int i = 0; i<spinner.length;i++){
	    			   g.nrPack[i] = (int) spinner[i].getValue();      
	    			   cntr += (int) spinner[i].getValue();
	    		   }
    		   }
    		   if(cntr > 0){
    			   GreedyAlgorithm.main(args);
    			   text[6].setText("Packages Left: " + g.getLeftPackages());
    			   text[7].setText("Time taken: " + g.getRuntime() + "ms");
    			   text[8].setText("Gaps left" + g.getGaps());
    			   disp2D.setEnabled(true);
    		   }
    		}
       });
       xyz.add(startButton);
       
       hillClimbing = new JButton();
       hillClimbing.setBounds(625, 150, 100, 50);
       hillClimbing.setText("HillClimbing");
       hillClimbing.setVisible(true);
       hillClimbing.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   HillClimbing h = new HillClimbing();
    		   h.main(args);
    		   text[6].setText("Packages used. A:" + h.getNrPack()[0] + " B:" + h.getNrPack()[1] + " C:" + h.getNrPack()[2]);
			   text[7].setText("Time taken: " + (h.getRuntime()/1000) + "s");
			   text[8].setText("Gaps left: " + h.getGaps());
			   disp2D.setEnabled(true);
			}
       });
       xyz.add(hillClimbing);
       
       genetic = new JButton();
       genetic.setBounds(625, 225, 100, 50);
       genetic.setText("Genetic");
       genetic.setVisible(true);
       genetic.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   GeneticAlgorithm g = new GeneticAlgorithm();
    		   g.main(args);
    		   text[6].setText("Packages Used. A:" + g.getNrPack()[0] + " B:" + g.getNrPack()[1] + " C:" + g.getNrPack()[2]);
			   text[7].setText("Time taken: " + (g.getRuntime()/1000) + "s");
			   text[8].setText("Gaps Left: " + g.getGaps());
			   disp2D.setEnabled(true);
			}
       });
       xyz.add(genetic);
       
       
       cubes = new Cube[3];
       cubes[0] = new Cube(new Package("A"),100,300,100);
       cubes[1] = new Cube(new Package("B"),300,300,100);
       cubes[2] = new Cube(new Package("C"),500,300,100);
    
       t = new Timer(10, new ActionListener(){
    	   public void actionPerformed(ActionEvent e) {
			for(int i = 0; i<cubes.length; i++){
				//cubes[i].RotateX(1);
				cubes[i].RotateY(1);
				//cubes[i].RotateZ(3);
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
