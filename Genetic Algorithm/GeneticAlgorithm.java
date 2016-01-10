import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 100;
    private final int MUTATION_FREQ = 1000;
    private final int CROSSOVER_FREQ = 10;
    private final String SELECTION_MODE = "ELITIST";

    private Individual[] population;
    private Package[] packageTypes;

    public void initialPopulation(Package[] types) {
        population = new Individual[POPULATION_SIZE];
        CargoSpace cargoSpace = new CargoSpace(33, 5, 8);
        Package p = new Package("C");
        int chrLength = 0;
        packageTypes = types;
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");

        // THERE HAS TO BE AN EASIER WAY
        for (int i = 0; i < packageTypes.length; i++) {
            p = packageTypes[i];
            if (p.getLength() == p.getWidth() && p.getLength() == p.getHeight()) {
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                //System.out.println("CS height = " + cargoSpace.getHeight());
                //System.out.println("PK height = " + p.getHeight());
                //System.out.println((cargoSpace.getLength() - p.getLength() + 1) + " " + (cargoSpace.getWidth() - (p.getWidth() - 1)) + " " + (cargoSpace.getHeight() - (p.getHeight() - 1)));
            } else if (p.getLength() == p.getWidth()) {
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateY();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateZ();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
            } else if (p.getLength() == p.getHeight()) {
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateX();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateY();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
            } else if (p.getWidth() == p.getHeight()) {
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateY();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateX();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
            } else {
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateX();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateY();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateZ();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateX();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
                p.rotateY();
                chrLength += (cargoSpace.getLength() - (p.getLength() - 1)) * (cargoSpace.getWidth() - (p.getWidth() - 1)) * (cargoSpace.getHeight() - (p.getHeight() - 1));
            }
        }

        int[] chromosome = new int[chrLength];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                if (Math.random() < 0.5)
                    chromosome[j] = 0;
                else
                    chromosome[j] = 1;
            }
            population[i] = new Individual(chromosome);
        }
        System.out.println(chrLength);

        /*
        * Order in which the chromosome is "interpreted":
        * - order of package types in packageTypes[]
        *   - unrotated
        *   - rotated around x
        *   - rotated around x and y
        *   - rotated around x, y and z
        *   - rotated around x, y, z and x
        *   - rotated around x, y, z, x and y
        *     - go through entire length
        *       - go through entire width
        *         - go through entire height
        */

    }

    public void displaySolution() {
        JFrame f = new JFrame();
        f.setSize(750, 770);
        Display display = new Display(null);
        f.add(display, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    /**
    * The main class for the genetic algorithm for the knapsack problem.
    *
    * @param args Not used.
    */
    public static void main(String[] args) {
        GeneticAlgorithm gA = new GeneticAlgorithm();
        gA.initialPopulation();
    }

}
