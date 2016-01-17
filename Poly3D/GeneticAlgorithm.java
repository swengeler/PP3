package Poly3D;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 1;
    private final int MUTATION_FREQ = 1000;
    private final int CROSSOVER_FREQ = 10;
    private final String SELECTION_MODE = "ELITIST";

    private Individual[] population;
    private Package[] packageTypes;
    private CargoSpace cargoSpace;

    private int gene;

    /*public GeneticAlgorithm(int gene) {
        this.gene = gene;
    }*/
    public void initialPopulation(Package[] types) {
        population = new Individual[POPULATION_SIZE];
        cargoSpace = new CargoSpace(33, 5, 8);
        int chrLength = 0;
        //packageTypes = types;
        Package[] packageTypes = new Package[2];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");
        //packageTypes[0] = new Package("A");
        //packageTypes[1] = new Package("B");
        //packageTypes[2] = new Package("C");

        for (int i = 0; i < packageTypes.length; i++) {
            chrLength += packageTypes[i].getNrStates(cargoSpace.getLength(), cargoSpace.getWidth(), cargoSpace.getHeight())[0];
        }
        System.out.println(chrLength);

        int[] chromosome = new int[chrLength];
        /*for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                if (Math.random() < 0.5)
                    chromosome[j] = 0;
                else
                    chromosome[j] = 1;
            }
            population[i] = new Individual(chromosome);
        }*/
        for (int j = 0; j < chromosome.length; j++) {
            if (Math.random() < 0.5) {
                chromosome[j] = 0;
                System.out.println("chromosome[" + j + "] = " + chromosome[j]);
            } else {
                chromosome[j] = 1;
                System.out.println("chromosome[" + j + "] = " + chromosome[j]);
            }
        }
        //chromosome[gene] = 1;
        cargoSpace = Converter.chromosomeToCargoSpace(chromosome, packageTypes, cargoSpace);

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
        Display display = new Display(cargoSpace.getArray());
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
        Scanner in = new Scanner(System.in);
        //System.out.print("nr = ");
        //int gene = in.nextInt();
        GeneticAlgorithm gA = new GeneticAlgorithm();
        gA.initialPopulation(null);
        gA.displaySolution();
    }

}
