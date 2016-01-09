import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nicolagheza on 07/01/16.
 */
public class HillClimbing {

    private CargoSpace currentSolution;
    private Package[] packages;

    final int PACKAGE_CHANGE = 1;
    final int RANGE = 10;
    final int NEIGHBOURHOOD_SIZE = 1;

    /**
    * The initialSolution method creates a partial solution for the Hill Climbing Algorithm
    */
    public void initialSolution() {
        packages = new Package[188];
        Package aPackage;

        for (int i=0; i < 83; i++) {
            aPackage = new Package("A");
            aPackage.setBaseCoords(Random.randomWithRange(0,32), Random.randomWithRange(0,4), Random.randomWithRange(0,7));
            aPackage.setRotations(Random.randomWithRange(0,3), Random.randomWithRange(0,3), Random.randomWithRange(0,3));
            packages[i] = aPackage;
        }
        for (int i = 0; i < 55; i++) {
            aPackage = new Package("B");
            aPackage.setBaseCoords(Random.randomWithRange(0,32), Random.randomWithRange(0,4), Random.randomWithRange(0,7));
            aPackage.setRotations(Random.randomWithRange(0,3), Random.randomWithRange(0,3), Random.randomWithRange(0,3));
            packages[i + 83] = aPackage;

        }
        for (int i = 0; i < 50; i++) {
            aPackage = new Package("C");
            aPackage.setBaseCoords(Random.randomWithRange(0,32), Random.randomWithRange(0,4), Random.randomWithRange(0,7));
            aPackage.setRotations(Random.randomWithRange(0,3), Random.randomWithRange(0,3), Random.randomWithRange(0,3));
            packages[i + 138] = aPackage;
        }

        currentSolution = new CargoSpace(33,5,8);
        for (int i = 0; i < packages.length; i++) {
            System.out.println("Package " + (i + 1) + ": x = " + packages[i].getBaseCoords()[0] + ", y = " + packages[i].getBaseCoords()[1] + ", z = " + packages[i].getBaseCoords()[2]);
        }
        currentSolution.fillCargoSpace(packages);

    }

    public void fillCargo(CargoSpace cargoSpace, int[][] coordinates, int[][] rotations) {
        Package currentPackage;
        for (int i=0; i<packages.length; i++) {
            currentPackage = packages[i];
            cargoSpace.curX = coordinates[i][0];
            cargoSpace.curY = coordinates[i][1];
            cargoSpace.curZ = coordinates[i][2];
            for (int j=0; j<rotations[i][0]; j++) {
                currentPackage.rotateX();
            }
            for (int j=0; j<rotations[i][1]; j++) {
                currentPackage.rotateY();
            }
            for (int j=0; j<rotations[i][2]; j++) {
                currentPackage.rotateZ();
            }
            if(!cargoSpace.overlap(currentPackage))
                cargoSpace.place(currentPackage);
        }
    }

    public void theHood() {
        for (int a = 0; a < 100; a++) {
            for (int x = 0; x < NEIGHBOURHOOD_SIZE; x++) {
                CargoSpace newSolution;
                Package currentPackage;
                int[] cBC;
                Package[] newPackages = new Package[packages.length];
                System.arraycopy(packages, 0, newPackages, 0, packages.length);

                for (int i = 0; i < PACKAGE_CHANGE; i++) {
                    int n = Random.randomWithRange(0, 187);
                    currentPackage = newPackages[n];
                    cBC = currentPackage.getBaseCoords();

                    int random = Random.randomWithRange(-RANGE, +RANGE);
                    if (cBC[0] + random < 0)
                        currentPackage.setBaseX(0);
                    else if (cBC[0] + random >= currentSolution.length)
                        currentPackage.setBaseX(currentSolution.length);
                    else
                        currentPackage.setBaseX(cBC[0] + random);

                    random = Random.randomWithRange(-RANGE, +RANGE);
                    if (cBC[1] + random < 0)
                        currentPackage.setBaseY(0);
                    else if (cBC[1] + random >= currentSolution.width)
                        currentPackage.setBaseY(currentSolution.width);
                    else
                        currentPackage.setBaseY(cBC[1] + random);

                    random = Random.randomWithRange(-RANGE, +RANGE);
                    if (cBC[2] + random < 0)
                        currentPackage.setBaseZ(0);
                    else if (cBC[2] + random >= currentSolution.height)
                        currentPackage.setBaseZ(currentSolution.height);
                    else
                        currentPackage.setBaseZ(cBC[2] + random);


                    random = Random.randomWithRange(0, 3);
                    for (int j = 0; j < random; j++)
                        currentPackage.rotateX();

                    random = Random.randomWithRange(0, 1);
                    for (int j = 0; j < random; j++)
                        currentPackage.rotateY();

                    random = Random.randomWithRange(0, 1);
                    for (int j = 0; j < random; j++)
                        currentPackage.rotateZ();

                }

                newSolution = new CargoSpace(33, 5, 7);
                newSolution.fillCargoSpace(newPackages);
                this.evaluate(newSolution);
            }

            System.out.println("Current best total value: " + currentSolution.getTotalValue());
        }
    }

    public void evaluate(CargoSpace other) {
        if (other.getFitness() > this.currentSolution.getFitness())
            this.currentSolution = other;
    }

    public void displaySolution() {
        JFrame f = new JFrame();
        f.setSize(750, 770);
        System.out.println("BEST VALUE: " + this.currentSolution.getTotalValue());
        Display display = new Display(this.currentSolution.getArray());
        f.add(display, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    /*public void initWithGreedy() {
        GreedyAlgorithm cs = new GreedyAlgorithm();
        cs.run();
        currentSolution = cs.bestCS;
        packages = currentSolution.cargoSpaceFilled;
        coordinates = currentSolution.packageCoords;
    }*/

    /**
     * The main method..
     * @param args
     */
    public static void main(String[] args) {

        HillClimbing cargo = new HillClimbing();
        cargo.initialSolution();
        //cargo.initWithGreedy();
        cargo.displaySolution();
        cargo.theHood();
        cargo.displaySolution();

    }

}
