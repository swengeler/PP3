import java.util.Scanner;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
* A class that uses a greedy algorithm to determine a possible (most likely not optimal)
* packing solution for a certain number of type A, B and C packages that are supposed
* to fit into a cargo area of 16.5m x 2.5m x 4m.
*
* @author Simon Wengeler
*/
public class GreedyAlgorithm {

    /**
    * The number of times the algorithm is used to calculate a solution in order to find the best one
    * out of those.
    */
    private static final int NR_RUNS = 10;

    /**
    * A list (in array form) of the packages which are supposed to be placed in the cargo space.
    */
    private static Package[] packages;

    /**
    * The main method of the class, initialising the array used for the internal representation
    * of the cargo space, asking for the number of certain package types to be used and
    * determining a packing for them.
    *
    * @param args Not used.
    */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        CargoSpace[] allCS = new CargoSpace[NR_RUNS];

        System.out.print("Please enter the number of A packages: ");
        int nrA = in.nextInt();
        System.out.print("Please enter the number of B packages: ");
        int nrB = in.nextInt();
        System.out.print("Please enter the number of C packages: ");
        int nrC = in.nextInt();

        System.out.print("Please enter the number of L packages: ");
        int nrL = in.nextInt();
        System.out.print("Please enter the number of P packages: ");
        int nrP = in.nextInt();
        System.out.print("Please enter the number of T packages: ");
        int nrT = in.nextInt();

        int nrPackages = nrA + nrB + nrC + nrL + nrP + nrT;
        packages = new Package[nrPackages];
        for (int i = 0; i < nrA; i++)
            packages[i] = new Package("A");
        for (int i = nrA; i < nrA + nrB; i++)
            packages[i] = new Package("B");
        for (int i = nrA + nrB; i < nrA + nrB + nrC; i++)
            packages[i] = new Package("C");
        for (int i = nrC + nrB + nrA; i < nrC + nrB + nrA + nrL; i++)
            packages[i] = new Package("L");
        for (int i = nrC + nrB + nrA + nrL; i < nrC + nrB + nrA + nrL + nrP; i++)
            packages[i] = new Package("P");
        for (int i = nrC + nrB + nrA + nrL + nrP; i < nrPackages; i++)
            packages[i] = new Package("T");

        ArrayList<Integer> randomNumbers;
        ArrayList<Package> packagesLeft;

        int forAverage = 0;
        for (int i  = 0; i < NR_RUNS; i++) {
            CargoSpace cs = new CargoSpace(33, 5, 8);
            boolean done = false;
            int counter = 0;
            randomNumbers = new ArrayList<Integer>();
            packagesLeft = new ArrayList<Package>();
            for (int j = 0; j < nrPackages; j++)
                randomNumbers.add(new Integer(j));
            //Collections.shuffle(randomNumbers);
            while (!done) {
                Package p = packages[randomNumbers.get(counter).intValue()];
                //p.rotateRandom();
                cs.initialPosition(p);
                if (!cs.overlap(p))
                    cs.putPackage(p);
                else
                    packagesLeft.add(p);

                counter++;

                if (counter >= nrPackages){done = true;}

            }
            //System.out.println("There are " + packagesLeft.size() + " packages left");
            long startTime = System.currentTimeMillis();
            cs.fillGaps(packagesLeft);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
        		//System.out.println("Runtime: " + totalTime + "ms");
            allCS[i] = cs;
            forAverage += allCS[i].getTotalValue();
        }

        double best = 0;
        double worst = Double.MAX_VALUE;
        CargoSpace bestCS = new CargoSpace(0, 0, 0);
        CargoSpace worstCS = new CargoSpace(0, 0, 0);
        for (int i = 0; i < allCS.length; i++) {
            //System.out.println(allCS[i].getTotalValue());
            if (allCS[i].getTotalValue() > best) {
                bestCS = allCS[i];
                best = bestCS.getTotalValue();
            }
            if (allCS[i].getTotalValue() < worst) {
                worstCS = allCS[i];
                worst = worstCS.getTotalValue();
            }
        }
        System.out.println("BEST VALUE: " + bestCS.getTotalValue());
        System.out.println("WORST VALUE: " + worstCS.getTotalValue());
        System.out.println("AVERAGE VALUE: " + ((double)forAverage/(double)NR_RUNS));

        JFrame f = new JFrame();
        f.setSize(1000, 1020);

        Display display = new Display(bestCS.getArray());
        f.add(display, BorderLayout.CENTER);
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

}
