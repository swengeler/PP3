package Poly3D;

import java.util.Scanner;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;

/**
* A class that uses a greedy algorithm to determine a possible (most likely not optimal)
* packing solution for a certain number of type A, B and C packages that are supposed
* to fit into a cargo area of 16.5m x 2.5m x 4m.
*
* @author Simon Wengeler
*/
public class GreedyAlgorithm {
	
    static ArrayList<Package> packagesLeft;


    /**
    * The number of times the algorithm is used to calculate a solution in order to find the best one
    * out of those.
    */
    private static final int NR_RUNS = 1;

    static int[] nrPack = new int[6];
    
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
         
        int nrA = nrPack[0], nrB = nrPack[1], nrC = nrPack[2], nrL = nrPack[3], nrP = nrPack[4], nrT = nrPack[5];
        
        int nrPackages = nrA + nrB + nrC + nrL + nrP + nrT;
        packages = new Package[nrPackages];
        for (int i = 0; i < nrC; i++)
            packages[i] = new Package("C");
        for (int i = nrC; i < nrC + nrB; i++)
            packages[i] = new Package("B");
        for (int i = nrC + nrB; i < nrC + nrB + nrA; i++)
            packages[i] = new Package("A");
        for (int i = nrC + nrB + nrA; i < nrC + nrB + nrA + nrL; i++)
            packages[i] = new Package("L");
        for (int i = nrC + nrB + nrA + nrL; i < nrC + nrB + nrA + nrL + nrP; i++)
            packages[i] = new Package("P");
        for (int i = nrC + nrB + nrA + nrL + nrP; i < nrPackages; i++)
            packages[i] = new Package("T");

        ArrayList<Integer> randomNumbers;
  
        for (int i  = 0; i < NR_RUNS; i++) {
            CargoSpace cs = new CargoSpace(33, 5, 8);
            boolean done = false;
            int counter = 0;
            randomNumbers = new ArrayList<Integer>();
            packagesLeft = new ArrayList<Package>();
            for (int j = 0; j < nrPackages; j++)
                randomNumbers.add(new Integer(j));
            Collections.shuffle(randomNumbers);
            while (!done) {
                Package p = packages[randomNumbers.get(counter).intValue()];
                p.rotateRandom();
                cs.initialPosition(p);
                if (!cs.overlap(p))
                    cs.putPackage(p);
                else
                    packagesLeft.add(p);

                counter++;

                if (counter >= nrPackages){done = true;}

            }
            System.out.println("There are " + packagesLeft.size() + " packages left");
            long startTime = System.currentTimeMillis();
            cs.fillGaps(packagesLeft);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
        		System.out.println("Runtime: " + totalTime + "ms");
            allCS[i] = cs;
        }

        double best = 0;
        CargoSpace bestCS = new CargoSpace(0, 0, 0);
        for (int i = 0; i < allCS.length; i++) {
            if (allCS[i].getTotalValue() > best) {
                bestCS = allCS[i];
                best = bestCS.getTotalValue();
            }
        }
        /*for(int i = 0; i<bestCS.cargoSpaceFilled.length; i++){
        	bestCS.simplePrint();
        }*/
        System.out.println("BEST VALUE: " + bestCS.getTotalValue());  
        cargoSpace3D.represent(bestCS);
    }
}
