import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nicolagheza on 07/01/16.
 */
public class HillClimbing {

    private CargoSpace currentSolution;
    private CargoSpace bestCurrentSolution;
    private PackageType[] packages;
    private int[][] coordinates;
    private int[][] rotations;

    final int PACKAGE_CHANGE = 100;
    final int RANGE = 10;
    final int NEIGHBOURHOOD_SIZE = 500;

    /**
     *  The initialSolution method creates a partial solution for the Hill Climbing Algorithm
     */
    public void initialSolution() {
        packages = new PackageType[188];
        coordinates = new int[188][3];
        rotations = new int[188][3];

        for (int i=0; i<83; i++) {
            packages[i] = PackageType.APackage;
            packages[i+55] = PackageType.BPackage;
            packages[i+105] = PackageType.CPackage;
        }

        for (int i=0; i<coordinates.length; i++) {
            coordinates[i][0] = Random.randomWithRange(0,32);
            rotations[i][0] = Random.randomWithRange(0,3);

            coordinates[i][1] = Random.randomWithRange(0,4);
            rotations[i][1] = Random.randomWithRange(0,3);

            coordinates[i][2] = Random.randomWithRange(0,7);
            rotations[i][2] = Random.randomWithRange(0,3);
        }
        
        bestCurrentSolution = new CargoSpace(33,5,8);
        currentSolution = new CargoSpace(33,5,8);
        fillCargo(this.currentSolution,coordinates,rotations);
        
    }

    public void fillCargo(CargoSpace cargoSpace, int[][] coordinates, int[][] rotations) {
        Package currentPackage;
        for (int i=0; i<packages.length; i++) {
            currentPackage = new Package(packages[i]);
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
                int[][] newCoordinates = new int[188][3];
                int[][] newRotations = new int[188][3];
                for (int i = 0; i < coordinates.length; i++) {
                    System.arraycopy(coordinates[i], 0, newCoordinates[i], 0, 3);
                    System.arraycopy(rotations[i], 0, newRotations[i], 0, 3);
                }
                int[][] rotations = this.rotations;

                for (int i = 0; i < PACKAGE_CHANGE; i++) {
                    int n = Random.randomWithRange(0, 187);
                    int m = Random.randomWithRange(0,2);
                    int random = Random.randomWithRange(-RANGE, +RANGE);
                    
                    if (m == 0) {
                        if (newCoordinates[n][0] + random < 0)
                            newCoordinates[n][0] = 0;
                        else if (newCoordinates[n][0] + random >= currentSolution.length)
                            newCoordinates[n][0] = currentSolution.length;
                        else
                            newCoordinates[n][0] += random;
                    } else if (m == 1) {
                        random = Random.randomWithRange(-RANGE, +RANGE);
                        if (newCoordinates[n][1] + random < 0)
                            newCoordinates[n][1] = 0;
                        else if (newCoordinates[n][1] + random >= currentSolution.width)
                            newCoordinates[n][1] = currentSolution.width;
                        else
                            newCoordinates[n][1] += random;
                    } else if (m == 2) {
                        random = Random.randomWithRange(-RANGE, +RANGE);
                        if (newCoordinates[n][2] + random < 0)
                            newCoordinates[n][2] = 0;
                        else if (newCoordinates[n][0] + random >= currentSolution.height)
                            newCoordinates[n][2] = currentSolution.height;
                        else
                            newCoordinates[n][2] += random;
                    }

                    random = Random.randomWithRange(0, 1);
                    if (newRotations[n][0] + random >= 4)
                        newRotations[n][0] = 0;
                    else
                        newRotations[n][0] += random;

                    random = Random.randomWithRange(0, 1);
                    if (newRotations[n][1] + random >= 4)
                        newRotations[n][1] = 0;
                    else
                        newRotations[n][1] += random;

                    random = Random.randomWithRange(0, 1);
                    if (newRotations[n][2] + random >= 4)
                        newRotations[n][2] = 0;
                    else
                        newRotations[n][2] += random;

                }

                newSolution = new CargoSpace(33, 5, 7);
                this.fillCargo(newSolution, newCoordinates, newRotations);
                this.evaluate(newSolution);
            }

            System.out.println("Current best total value: " + currentSolution.getTotalValue());
        }
    }

    public void evaluate(CargoSpace other) {
        if (other.getTotalValue() > this.currentSolution.getTotalValue())
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
    
    public void initWithGreedy() {
        GreedyAlgorithm cs = new GreedyAlgorithm();
        cs.run();
        currentSolution = cs.bestCS;
        packages = currentSolution.cargoSpaceFilled;
        coordinates = currentSolution.packageCoords;
    }

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
