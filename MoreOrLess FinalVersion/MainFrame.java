import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * A class to represent the user gui
 * 
 * @author Daniel Kaestner
 */
public class MainFrame extends JPanel{
   private static boolean added = true;
   private static String packages="";
   private static int[] allPacks = new int[0];
   private static Package[] allPackages = new Package[0];
   private static MainFrame m;
   private static JFrame xyz;
   private JMenuBar selectionMethod;
   private static JLabel[] text;
   private static JSpinner[] spinner;
   private static JTextArea[] newPackage;
   private static JButton startButton, hillClimbing, genetic, addPackage;
   private static Cube[] cubes;
   private static JCheckBox disp2D, editGenes;
   private static Timer t;

   public MainFrame(){
       setBounds(0,0,1000,1000);
   }

   public static void main(String[]args) throws InterruptedException, AWTException {
	   GeneticAlgorithm g = new GeneticAlgorithm();
       xyz=new JFrame();
       xyz.setLayout(null);
       xyz.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       xyz.setBounds(1000+50,0,750,420);

       newPackage = new JTextArea[6];
       text=new JLabel[14];
       spinner = new JSpinner[3];

       for(int i=0;i<3;i++) {
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
       
       for(int i = 0; i<text.length-3; i++){
           text[3+i] = new JLabel();
           text[3+i].setFont(new Font(text[3+i].getFont().getName(),text[3+i].getFont().getStyle(),15));
           xyz.add(text[3+i]);
       }
       
       for(int i = 0; i<newPackage.length; i++){
           newPackage[i] = new JTextArea();
           xyz.add(newPackage[i]);
       }

       text[3].setBounds(50, 150, 250, 50);
       text[3].setText("Packages used: ");
       
       text[4].setBounds(50, 175, 250, 50);
       text[4].setText("Time taken: ");

       text[5].setBounds(50, 200, 250, 50);
       text[5].setText("Gaps Left: ");
       
       text[6].setBounds(350, 60, 80, 20);
       text[6].setText("Name   : ");
       
       text[7].setBounds(350, 80, 80, 20);
       text[7].setText("Amount: ");
       
       text[8].setBounds(350, 100, 80, 20);
       text[8].setText("Value   : ");
       
       text[9].setBounds(350, 120, 80, 20);
       text[9].setText("Width   : ");
       
       text[10].setBounds(350, 140, 80, 20);
       text[10].setText("Height : ");
       
       text[11].setBounds(350, 160, 80, 20);
       text[11].setText("Length: ");    
       
       text[12].setBounds(495, 80, 100, 20);
       text[12].setText(packages);   
       	
       text[13].setBounds(495, 60, 100, 20);
       text[13].setText("Packages: ");   
       

       newPackage[0].setBounds(430, 65 , 40, 15);
       newPackage[1].setBounds(430, 85 , 40, 15);
       newPackage[2].setBounds(430, 105, 40, 15);
       newPackage[3].setBounds(430, 125, 40, 15);
       newPackage[4].setBounds(430, 145, 40, 15);
       newPackage[5].setBounds(430, 165, 40, 15);
       
       
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

       editGenes = new JCheckBox();
       editGenes.setBounds(620, 315, 100, 25);
       editGenes.setText("Edit genes");
       editGenes.setVisible(true);
       editGenes.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   if(editGenes.isSelected()){
    			   text[ 6].setBounds(350,  60, 170, 20);
    			   text[ 7].setBounds(350,  80, 170, 20);
    			   text[ 8].setBounds(350, 100, 170, 20);
    			   text[ 9].setBounds(350, 120, 170, 20);
    			   text[10].setBounds(350, 140, 170, 20);
    			   text[11].setBounds(350, 160, 170, 20);
    			   
    			   text[ 6].setText("Tounament size           :");
    		       text[ 7].setText("Elitest top percentage: ");
    		       text[ 8].setText("Population size            :");
    		       text[ 9].setText("Crossover frequency 	 :");
    		       text[10].setText("Swap probability          :");
    		       text[11].setText("Mutation probability    :"); 
    		       
    		       newPackage[0].setBounds(520, 65 , 40, 15);
    		       newPackage[1].setBounds(520, 85 , 40, 15);
    		       newPackage[2].setBounds(520, 105, 40, 15);
    		       newPackage[3].setBounds(520, 125, 40, 15);
    		       newPackage[4].setBounds(520, 145, 40, 15);
    		       newPackage[5].setBounds(520, 165, 40, 15);
    		       System.out.println(g.getTournamentSize());
    		       newPackage[0].setText("" + g.getTournamentSize());
    		       newPackage[1].setText("" + g.getElitestTop());
    		       newPackage[2].setText("" + g.getPopulationSize());
    		       newPackage[3].setText("" + g.getCrossover());
    		       newPackage[4].setText("" + g.getSwapProbability());
    		       newPackage[5].setText("" + g.getMutationProbability());
    		       

    		       addPackage.setBounds(350, 185, 210, 20);
    		       addPackage.setText("Confirm");
    		       
    		       text[12].setVisible(false);
    		       text[13].setVisible(false);
    		   }
    		   else{
    			   text[ 6].setBounds(350,  60, 80, 20);
    			   text[ 7].setBounds(350,  80, 80, 20);
    			   text[ 8].setBounds(350, 100, 80, 20);
    			   text[ 9].setBounds(350, 120, 80, 20);
    			   text[10].setBounds(350, 140, 80, 20);
    			   text[11].setBounds(350, 160, 80, 20);
    			   
    		       text[6].setText("Name   : ");
    		       text[7].setText("Amount: ");
    		       text[8].setText("Value   : ");
    		       text[9].setText("Width   : ");
    		       text[10].setText("Height : ");
    		       text[11].setText("Length: ");   
    		       
    		       newPackage[0].setBounds(430, 65 , 40, 15);
    		       newPackage[1].setBounds(430, 85 , 40, 15);
    		       newPackage[2].setBounds(430, 105, 40, 15);
    		       newPackage[3].setBounds(430, 125, 40, 15);
    		       newPackage[4].setBounds(430, 145, 40, 15);
    		       newPackage[5].setBounds(430, 165, 40, 15);
    		       
    		       addPackage.setBounds(350, 185, 120, 20);
    		       addPackage.setText("Add Package");
    		       
    		       for(int i = 0; i<newPackage.length;i++)newPackage[i].setText("");

    		       text[12].setVisible(true);
    		       text[13].setVisible(true);
    		   }
    		    xyz.repaint();
    		}
       });
       xyz.add(editGenes);
       
       addPackage = new JButton();
       addPackage.setBounds(350, 185, 120, 20);
       addPackage.setText("Add Package");
       addPackage.setVisible(true);
       addPackage.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {    		   
    		   if(!editGenes.isSelected()){
    			   if(packages == ""){
	    		   
	    			   packages += (""+newPackage[0].getText());
	    			   text[12].setText(packages);
	    		   }
	    		   else{
	    			   packages += (", "+newPackage[0].getText());
	    			   text[12].setText(packages);
	    		   }
	    		   
	    		   int[] newAllPacks = new int[allPacks.length+1];
	    		   System.arraycopy(allPacks, 0, newAllPacks, 0, allPacks.length);
	    		   newAllPacks[allPacks.length] = (int)Double.parseDouble(newPackage[1].getText());
	    		   allPacks = newAllPacks;    		   
	    		   
	    		   Package[] newAllPackages = new Package[allPackages.length+1];
	    		   System.arraycopy(allPackages, 0, newAllPackages, 0, allPackages.length);
	    		   newAllPackages[allPackages.length] =  new Package(newPackage[0].getText(),  (int)(2*Double.parseDouble(newPackage[3].getText())),
	    				   								 									   (int)(2*Double.parseDouble(newPackage[4].getText())),
	    				   								 									   (int)(2*Double.parseDouble(newPackage[5].getText())),
	    				   								 									   		   Double.parseDouble(newPackage[2].getText()));
	    		   allPackages = newAllPackages;
	    		   
	    		   for(int i = 0; i<newPackage.length; i++)newPackage[i].setText("");
    		   }
    		   else{
    			   
    			   text[ 9].setText("Crossover frequency 	 :");
    		       text[10].setText("Swap probability          :");
    		       text[11].setText("Mutation probability    :"); 
    		       
    			   g.setTournamentSize(Double.parseDouble(newPackage[0].getText()));
    			   g.setElitestTop(Double.parseDouble(newPackage[1].getText()));
    			   g.setPopulationSize(Integer.parseInt(newPackage[2].getText()));
    			   g.setCrossover(Integer.parseInt(newPackage[3].getText()));
    			   g.setSwapProbability(Double.parseDouble(newPackage[4].getText()));
    			   g.setMutationProbability(Double.parseDouble(newPackage[5].getText()));
    		   }
    		   xyz.repaint();
    		}
       });
       xyz.add(addPackage);
       
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
    			   text[3].setText("Packages left: " + g.getLeftPackages());
    			   text[4].setText("Time taken: " + g.getRuntime() + "ms");
    			   text[5].setText("Gaps left: " + g.getGaps());
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
    		   addStandard();
    		   h.run(allPackages, allPacks);
    		   text[3].setText("Packages used: " + h.getNrPack());
			   text[4].setText("Time taken: " + (h.getRuntime()/1000) + "s");
			   text[5].setText("Gaps left: " + h.getGaps());
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
    		   String packagesUsed = "Packages used: ";
    		   addStandard();
    		   g.run(allPackages, allPacks);
    		   for(int i = 0; i<g.getNrPack().length;i++)packagesUsed += (" " + allPackages[i].getType() + ":" + g.getNrPack()[i] + " ");
    		   text[3].setText(packagesUsed);
			   text[4].setText("Time taken: " + (g.getRuntime()/1000) + "s");
			   text[5].setText("Gaps left: " + g.getGaps());
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
				g2.setColor(cubes[i].getType().getColor());
				g2.fillPolygon(cubes[i].createPolygons()[l]);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(cubes[i].createPolygons()[l]);

			}
		}
   }
   
   public static void addStandard(){
	   if(added){
		   added = false;
		   for(int i = 0; i<spinner.length; i++){
			   if((int)spinner[i].getValue() > 0){
				   int[] newAllPacks = new int[allPacks.length+1];
				   System.arraycopy(allPacks, 0, newAllPacks, 0, allPacks.length);
				   newAllPacks[allPacks.length] = (int)spinner[i].getValue();
				   allPacks = newAllPacks;    		   
				   
				   Package[] newAllPackages = new Package[allPackages.length+1];
				   System.arraycopy(allPackages, 0, newAllPackages, 0, allPackages.length);
				   newAllPackages[allPackages.length] =  new Package(text[i].getText());
				   allPackages = newAllPackages;
			   }
		   }
	   }
   }
}
