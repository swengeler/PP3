import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {

    private final int POPULATION_SIZE = 100;
    private final int NR_TO_PLACE = 300;
    private final int MUTATION_FREQ = 100;
    private final int CROSSOVER_FREQ = 2;
    private final String SELECTION_MODE = "ROULETTE";
    private final double ELITIST_TOP_PERCENT = 0.2;

    private Individual[] population;
    private Package[] packageTypes;
    private CargoSpace cargoSpace;

    private int gene;
    private boolean test;

    public GeneticAlgorithm() {
        this.test = false;
    }

    public GeneticAlgorithm(int gene) {
        this.gene = gene;
        this.test = true;
    }

    public void initialPopulation(Package[] types) {
        cargoSpace = new CargoSpace(33, 5, 8);
        int chrLength = 0;
        //packageTypes = types;
        packageTypes = new Package[3];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");

        for (int i = 0; i < packageTypes.length; i++) {
            chrLength += packageTypes[i].getNrStates(cargoSpace.getLength(), cargoSpace.getWidth(), cargoSpace.getHeight())[0];
        }

        // initialising the population
        population = new Individual[POPULATION_SIZE];
        for (int i = 0; i < population.length; i++) {
            int[] chromosome = new int[chrLength];
            int[] ones = Random.randomListWithRange(0, chrLength - 1, NR_TO_PLACE);
            for (int j = 0; j < ones.length; j++) {
                chromosome[ones[j]] = 1;
            }
            population[i] = new Individual(chromosome);
            population[i].setFitness(Converter.chromosomeToCargoSpace(chromosome, packageTypes, new CargoSpace(33, 5, 8)).getTotalValue());
        }

        HeapSort.sortDownInd(population);


        int counter = 1000;
        int decreaseSince = 0;

        CargoSpace bestCS = Converter.chromosomeToCargoSpace(population[0].getChromosome(), packageTypes, new CargoSpace(33, 5, 8));
        //CargoSpace lastBest = Converter.chromosomeToCargoSpace(population[0].getChromosome(), packageTypes, new CargoSpace(33, 5, 8));

        while (counter > 0) {
            population = reproduce(population);
            population = fitnessAndSort(population);
            if (counter % 10 == 0) {
                System.out.println("counter = " + counter);
                System.out.println("Maximum total value = " + Converter.chromosomeToCargoSpace(population[0].getChromosome(), packageTypes, new CargoSpace(33, 5, 8)).getTotalValue());
                System.out.println();
            }
            counter--;
            if (population[0].getFitness() > bestCS.getFitness()) {
                bestCS = Converter.chromosomeToCargoSpace(population[0].getChromosome(), packageTypes, new CargoSpace(33, 5, 8));
                decreaseSince = 0;
            }
            if (population[0].getFitness() < bestCS.getFitness() && decreaseSince >= 2) {
                population = repopulate(population);
                decreaseSince = 0;
            }
            else if (population[0].getFitness() < bestCS.getFitness())
                decreaseSince++;
        }

        cargoSpace = Converter.chromosomeToCargoSpace(population[0].getChromosome(), packageTypes, new CargoSpace(33, 5, 8));
        System.out.println("Maximum total value: " + cargoSpace.getTotalValue());

    }

    private Individual[] repopulate(Individual[] population) {
        Individual[] newPopulation = new Individual[population.length];
        for (int i = 0; i < newPopulation.length; i++) {
            if (Math.random() < 0.75)
                newPopulation[i] = population[i];
            else {
                int[] chr = new int[population[0].getChromosome().length];
                int[] ones = Random.randomListWithRange(0, chr.length - 1, NR_TO_PLACE);
                for (int j = 0; j < ones.length; j++) {
                    chr[ones[j]] = 1;
                }
                newPopulation[i] = new Individual(chr);
            }
        }
        return newPopulation;
    }

    private Individual[] fitnessAndSort(Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness(Converter.chromosomeToCargoSpace(population[i].getChromosome(), packageTypes, new CargoSpace(33, 5, 8)).getTotalValue());
        }
        HeapSort.sortDownInd(population);
        return population;
    }

    private Individual[] reproduce(Individual[] population) {
        Individual[] newPopulation = new Individual[POPULATION_SIZE];
        for (int i = 0; i < newPopulation.length; i++) {
            if (SELECTION_MODE.equalsIgnoreCase("ELITIST")) {
                Individual parent1 = elitistSelection(population);
                Individual parent2 = elitistSelection(population);
                newPopulation[i] = crossOver(parent1, parent2);
            } else if (SELECTION_MODE.equalsIgnoreCase("TOURNAMENT")) {
            } else if (SELECTION_MODE.equalsIgnoreCase("ROULETTE")) {
                Individual parent1 = rouletteSelection(population);
                Individual parent2 = rouletteSelection(population);
                newPopulation[i] = crossOver(parent1, parent2);
            }
        }
        return newPopulation;
    }

    public Individual crossOver(Individual parent1, Individual parent2) {
        int[] childChr = new int[parent1.getChromosome().length];
        int[] curParentChr;
        int[] crossOverPoints = Random.randomListWithRange(0, parent1.getChromosome().length - 1, CROSSOVER_FREQ);
        HeapSort.sortUpInt(crossOverPoints);
        int lastCrPoint = 0;
        for (int i = 0; i < crossOverPoints.length; i++) {
            if (i % 2 == 0)
                curParentChr = parent1.getChromosome();
            else
                curParentChr = parent2.getChromosome();
            for (int j = lastCrPoint; j < crossOverPoints[i]; j++) {
                childChr[j] = curParentChr[j];
            }
            lastCrPoint = crossOverPoints[i];
        }
        Individual child = new Individual(childChr);
        return mutate(child);
    }

    private Individual mutate(Individual ind) {
        int[] chromosome = ind.getChromosome();
        int[] randomGenes = Random.randomListWithRange(0, chromosome.length - 1, MUTATION_FREQ);
        for (int i = 0; i < randomGenes.length; i++) {
            if (chromosome[randomGenes[i]] == 0)
                chromosome[randomGenes[i]] = 1;
            else
                chromosome[randomGenes[i]] = 0;
        }
        ind.setChromosome(chromosome);
        return ind;
    }

    // ***************** //
    // SELECTION METHODS //
    // ***************** //

    private Individual elitistSelection(Individual[] population) {
        int randomSelect = (int) (Math.random() * (population.length * ELITIST_TOP_PERCENT));
        return population[randomSelect];
    }

    private Individual rouletteSelection(Individual[] population) {
        double randomSelect = Math.random();
        int randomFromSelected;
        if (randomSelect < 0.4)
            randomFromSelected = (int)(Math.random() * (population.length * 0.1));
        else if (randomSelect < 0.7)
            randomFromSelected = (int)(population.length * 0.1) + (int)(Math.random() * (population.length * 0.2));
        else if (randomSelect < 0.9)
            randomFromSelected = (int)(population.length * 0.3) + (int)(Math.random() * (population.length * 0.3));
        else
            randomFromSelected = (int)(population.length * 0.6) + (int)(Math.random() * (population.length * 0.4));
        return population[randomFromSelected];
    }

    // ********************* //
    // END SELECTION METHODS //
    // ********************* //

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
        // System.out.print("nr = ");
        // int gene = in.nextInt();
        GeneticAlgorithm gA = new GeneticAlgorithm();
        gA.initialPopulation(null);
        gA.displaySolution();
    }

}
