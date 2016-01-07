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
    private static final int NR_RUNS = 1000;

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

        int nrPackages = nrA + nrB + nrC;
        packages = new Package[nrPackages];
        for (int i = 0; i < nrC; i++)
            packages[i] = new Package(PackageType.CPackage);
        for (int i = nrC; i < nrC + nrB; i++)
            packages[i] = new Package(PackageType.BPackage);
        for (int i = nrC + nrB; i < nrPackages; i++)
            packages[i] = new Package(PackageType.APackage);

        ArrayList<Integer> randomNumbers;
        ArrayList<Package> packagesLeft;

        for (int i = 0; i < NR_RUNS; i++) {
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
                p.rotateRandom();
                cs.initialPosition(p);
                if (!cs.overlap(p))
                    cs.putPackage(p);
                else
                    packagesLeft.add(p);

                counter++;

                if (counter >= nrPackages)
                    done = true;
            }
            System.out.println("There are " + packagesLeft.size() + " packages left");
            cs.fillGaps(packagesLeft);
            allCS[i] = cs;
        }

        double best = 0;
        CargoSpace bestCS = new CargoSpace(0, 0, 0);
        for (int i = 0; i < allCS.length; i++) {
            //System.out.println(allCS[i].getTotalValue());
            if (allCS[i].getTotalValue() > best) {
                bestCS = allCS[i];
                best = bestCS.getTotalValue();
            }
        }
        System.out.println("BEST VALUE: " + bestCS.getTotalValue());

        JFrame f = new JFrame();
        f.setSize(1000, 1020);

        Display display = new Display(bestCS.getArray());
        f.add(display, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

}
