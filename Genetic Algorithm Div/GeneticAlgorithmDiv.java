import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithmDiv {

    private final int POPULATION_SIZE = 100;
    private final int NR_TO_PLACE = 200;
    private final int MUTATION_FREQ = 100;
    private final int CROSSOVER_FREQ = 2;
    private final String SELECTION_MODE = "ROULETTE";
    private final double ELITIST_TOP_PERCENT = 0.2;

    private Individual[] population;
    private Package[] packageTypes;
    private CargoSpace cargoSpace;

    private int gene;
    private boolean test;

    public GeneticAlgorithmDiv() {
        this.test = false;
    }

    public GeneticAlgorithmDiv(int gene) {
        this.gene = gene;
        this.test = true;
    }

    public void initialPopulation(Package[] types, int[] amountOfType) {
        cargoSpace = new CargoSpace(33, 5, 8);
        //packageTypes = types;
        amountOfType = new int[3];
        amountOfType[0] = 50;
        amountOfType[1] = 50;
        amountOfType[2] = 50;

        packageTypes = new Package[3];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");

        int[][] chromosomes = new int[packageTypes.length][0];
        /*
        for (int i = 0; i < packageTypes.length; i++) {
            chromosomes[i] = new int[packageTypes[i].getNrStates(cargoSpace.getLength(), cargoSpace.getWidth(), cargoSpace.getHeight())[0]];
        }
        for (int i = 0; i < chromosomes.length; i++) {
            System.out.println(chromosomes[i].length);
        }
        */
        // initialising the population
        population = new Individual[POPULATION_SIZE];
        try {
            for (int i = 0; i < population.length; i++) {
                chromosomes = new int[packageTypes.length][0];
                for (int j = 0; j < packageTypes.length; j++) {
                    chromosomes[j] = new int[packageTypes[j].getNrStates(cargoSpace.getLength(), cargoSpace.getWidth(), cargoSpace.getHeight())[0]];
                }
                for (int j = 0; j < chromosomes.length; j++) {
                    int[] ones = Random.randomListWithRange(0, chromosomes[j].length - 1, amountOfType[j]);
                    for (int k = 0; k < ones.length; k++) {
                        chromosomes[j][ones[k]] = 1;
                    }
                }
                population[i] = new Individual(chromosomes);
                population[i].setFitness((new CargoSpace(33, 5, 8)).packRandom(Converter.chromosomesToPacking(chromosomes, packageTypes, new CargoSpace(33, 5, 8))).getTotalValue());
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        }

        HeapSort.sortDownInd(population);
        for (int i = 0; i < population.length; i++) {
            //System.out.println(population[i].getFitness());
        }

        cargoSpace = (new CargoSpace(33, 5, 8)).packRandom(Converter.chromosomesToPacking(chromosomes, packageTypes, new CargoSpace(33, 5, 8)));



        int generation = 0;

        while (generation < 100) {
            population = reproduce(population);
            population = fitnessAndSort(population);
            if (generation % 10 == 0) {
                System.out.println("Generation " + (generation + 1));
                System.out.println("Maximum total value = " + (new CargoSpace(33, 5, 8)).packRandom(Converter.chromosomesToPacking(chromosomes, packageTypes, new CargoSpace(33, 5, 8))).getTotalValue());
                System.out.println();
            }
            generation++;
        }

        cargoSpace = (new CargoSpace(33, 5, 8)).packRandom(Converter.chromosomesToPacking(chromosomes, packageTypes, new CargoSpace(33, 5, 8)));
        System.out.println("Final maximum total value: " + cargoSpace.getTotalValue());


    }

    private Individual[] fitnessAndSort(Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness((new CargoSpace(33, 5, 8)).packRandom(Converter.chromosomesToPacking(population[i].getChromosomes(), packageTypes, new CargoSpace(33, 5, 8))).getTotalValue());
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
            } else if (SELECTION_MODE.equalsIgnoreCase("RANDOM")) {
                Individual parent1 = randomSelection(population);
                Individual parent2 = randomSelection(population);
                newPopulation[i] = crossOver(parent1, parent2);
            }
        }
        return newPopulation;
    }

    public Individual crossOver(Individual parent1, Individual parent2) {
        int[][] childChr = new int[parent1.getChromosomes().length][0];
        for (int i = 0; i < childChr.length; i++) {
            childChr[i] = new int[parent1.getChromosomes()[i].length];
        }
        try {
            int[][] curParentChr;
            for (int k = 0; k < childChr.length; k++) {
                int[] crossOverPoints = Random.randomListWithRange(0, parent1.getChromosomes()[k].length - 1, CROSSOVER_FREQ);
                HeapSort.sortUpInt(crossOverPoints);
                int lastCrPoint = 0;
                for (int i = 0; i < crossOverPoints.length; i++) {
                    if (i % 2 == 0)
                        curParentChr = parent1.getChromosomes();
                    else
                        curParentChr = parent2.getChromosomes();
                    for (int j = lastCrPoint; j < crossOverPoints[i]; j++) {
                        childChr[k][j] = curParentChr[k][j];
                    }
                    lastCrPoint = crossOverPoints[i];
                }
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        Individual child = new Individual(childChr);
        return mutate(child);
    }

    private Individual mutate(Individual ind) {
        try {
            int[][] chromosomes = ind.getChromosomes();
            for (int j = 0; j < chromosomes.length; j++) {
                int[] randomGenes = Random.randomListWithRange(0, chromosomes[j].length - 1, MUTATION_FREQ);
                for (int i = 0; i < randomGenes.length; i++) {
                    if (chromosomes[j][randomGenes[i]] == 0)
                        chromosomes[j][randomGenes[i]] = 1;
                    else
                        chromosomes[j][randomGenes[i]] = 0;
                }
            }
            ind.setChromosomes(chromosomes);
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        return ind;
    }

    private Individual mutateSwap(Individual ind) {
        try {
            int[][] chromosomes = ind.getChromosomes();
            for (int j = 0; j < chromosomes.length; j++) {
                int[] randomGenes = Random.randomListWithRange(0, chromosomes[j].length - 1, MUTATION_FREQ);
                for (int i = 0; i < randomGenes.length; i++) {
                    if (chromosomes[j][randomGenes[i]] == 0)
                        chromosomes[j][randomGenes[i]] = 1;
                    else
                        chromosomes[j][randomGenes[i]] = 0;
                }
            }
            ind.setChromosomes(chromosomes);
        } catch (BadInputException e) {
            e.printStackTrace();
        }
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

    private Individual randomSelection(Individual[] population) {
        int randomSelect = Random.randomWithRange(0, population.length - 1);
        return population[randomSelect];
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
        GeneticAlgorithmDiv gA = new GeneticAlgorithmDiv();
        gA.initialPopulation(null, null);
        gA.displaySolution();
    }

}
