import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {

    private final boolean LOG1 = false;
    private final boolean LOG2 = true;

    private int POPULATION_SIZE = 200;
    private int MUTATION_FREQ = 10;
    private int MUTATION_RANGE = 5;
    private int SWAP_FREQ = 5;
    private int SWAP_RANGE = 2;
    private int CROSSOVER_FREQ = 1;
    private String SELECTION_MODE = "ELITIST";
    private double ELITIST_TOP_PERCENT = 0.2;

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

    public void initialPopulation(Package[] types, int[] amountOfType) {
        cargoSpace = new CargoSpace(33, 5, 8);

        amountOfType = new int[3];
        amountOfType[0] = 100;
        amountOfType[1] = 100;
        amountOfType[2] = 100;
        int amountSum = 0;
        for (int i = 0; i < amountOfType.length; i++) {
            amountSum += amountOfType[i];
        }

        packageTypes = new Package[3];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");
        /*packageTypes[3] = new Package("D", 8, 8, 10, 1.0);
        packageTypes[4] = new Package("E", 2, 4, 9, 1.5);
        packageTypes[5] = new Package("F", 3, 6, 7, 4.5);*/


        int stateSum = 0;
        for (int i = 0; i < packageTypes.length; i++) {
            stateSum += packageTypes[i].getNrRotations();
        }

        Package[] statesArray = new Package[stateSum];
        Package p;
        int counter = 0;
        for (int j = 0; j < packageTypes.length; j++) {
            for (int k = 0; k < packageTypes[j].getNrRotations(); k++) {
                p = new Package(packageTypes[j].getType());
                if (packageTypes[j].getNrRotations() > 1) {
                    p.setRotations(k);
                }
                statesArray[counter] = p.clone();
                counter++;
            }
        }

        Individual.setCargoSpace(cargoSpace);
        Individual.setStatesArray(statesArray);

        for (int i = 0; i < statesArray.length; i++) {
            p = statesArray[i];
            System.out.println(p.getType() + "-package: length = " + p.getLength() + " width = " + p.getWidth() + " height = " + p.getHeight());
        }

        // initialising the population
        int[] chromosome = new int[amountSum];
        population = new Individual[POPULATION_SIZE];
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                chromosome = new int[amountSum];
                int packageChosen = -1;
                while (packageChosen < 0 || amountOfType[statesArray[packageChosen].getPositionInArray(statesArray[packageChosen])] <= 0) {
                    packageChosen = Random.randomWithRange(0, statesArray.length - 1);
                }
                amountOfType[statesArray[packageChosen].getPositionInArray(statesArray[packageChosen])]--;
                chromosome[j] = packageChosen;
            }
            population[i] = new Individual(chromosome);
        }

        HeapSort.sortDownInd(population);

        int generation = 0;
        Individual bestInd = new Individual(population[0].getChromosome());

        while (generation < 2500) {
            if (generation % 50 == 0 && population.length != 1) {
                if (LOG2) System.out.println("Generation " + generation);
                if (LOG2) System.out.println("Maximum fitness value = " + population[0].getFitness());
                for (int i = 0; i < POPULATION_SIZE; i += 10) {
                    System.out.println((i + 1) + ". in the population: " +  population[i].getFitness());
                }
                System.out.println();
            }
            if (POPULATION_SIZE == 1 && LOG1) {
                System.out.println("---- Generation " + generation + " ----");
                System.out.println("Fitness value = " + population[0].getFitness());
                for (int i = 0; i < population[0].getChromosome().length; i++) {
                    int[] bC = population[0].getChromosome()[i].getBaseCoords();
                    System.out.println("Package No." + i + " (" + population[0].getChromosome()[i].getType() + "): x = " + bC[0] + ", y = " + bC[1] + ", z = " + bC[2]);
                }
                System.out.println();
            }
            population = reproduce(population);
            population = fitnessAndSort(population);
            if (population[0].getFitness() > bestInd.getFitness()) {
                bestInd = population[0].clone();
                if (LOG2) System.out.println("Best individual changed in generation " + generation + " (" + bestInd.getFitness() + ")");
            }
            generation++;
        }

        if (POPULATION_SIZE == 1 && LOG1) {
            System.out.println("---- Generation " + generation + " ----");
            System.out.println("Fitness value = " + population[0].getFitness());
            for (int i = 0; i < population[0].getChromosome().length; i++) {
                int[] bC = population[0].getChromosome()[i].getBaseCoords();
                System.out.println("Package No." + i + " (" + population[0].getChromosome()[i].getType() + "): x = " + bC[0] + ", y = " + bC[1] + ", z = " + bC[2]);
            }
            System.out.println();
        }

        cargoSpace = new CargoSpace(33, 5, 8);
        cargoSpace = bestInd.toCargoSpace();
        System.out.println("Final maximum fitness value: " + bestInd.getFitness());
        System.out.println("Final maximum total value: " + cargoSpace.getTotalValue());


    }

    private int getPositionInArray(Package p) {
        int counter = 0;
        while (!p.getType().equalsIgnoreCase(packageTypes[counter].getType()))
            counter++;
        return counter;
    }

    private Individual[] fitnessAndSort(Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness();
        }
        HeapSort.sortDownInd(population);
        return population;
    }

    private Individual[] reproduce(Individual[] population) {
        Individual[] newPopulation = new Individual[POPULATION_SIZE];
        int counter = 0;
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
            counter++;
        }
        return newPopulation;
    }

    public Individual crossOver(Individual parent1, Individual parent2) {
        int[] childChr = new Package[parent1.getChromosome().length];
        try {
            int[] curParentChr;
            int[] crossOverPoints = Random.randomListWithRange(0, childChr.length - 1, CROSSOVER_FREQ);
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
            for (int i = crossOverPoints[crossOverPoints.length - 1]; i < childChr.length; i++) {
                if (crossOverPoints.length % 2 == 0)
                    childChr[i] = parent2.getChromosome()[i];
                else
                    childChr[i] = parent1.getChromosome()[i];
            }
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        Individual child = new Individual(childChr);
        return mutateSwap(mutate(child));
    }

    private Individual mutate(Individual ind) {
        try {
            int[] chromosome = ind.getChromosome();
            int[] randomGenes = Random.randomListWithRange(0, chromosome.length - 1, MUTATION_FREQ);
            if (LOG1) System.out.println("Changing " + randomGenes.length + " gene(s)");
            for (int i = 0; i < randomGenes.length; i++) {
                int changeCoords = Random.randomWithRange(1, 3);
                if (changeCoords == 1) {
                    int newX = Random.randomWithRangeNN(chromosome[randomGenes[i]].getBaseCoords()[0] - MUTATION_RANGE, chromosome[randomGenes[i]].getBaseCoords()[0] + MUTATION_RANGE);
                    if (LOG1) System.out.print("Gene " + randomGenes[i] + " from " + chromosome[randomGenes[i]].getBaseCoords()[0] + " to newX = " + newX);
                    if (newX < 0) {
                        newX = 0;
                        if (LOG1) System.out.println(" set to " + newX);
                    } else if (newX > (cargoSpace.getLength() - chromosome[randomGenes[i]].getLength())) {
                        newX = cargoSpace.getLength() - chromosome[randomGenes[i]].getLength();
                        if (LOG1) System.out.println(" set to " + newX);
                    } else {
                        if (LOG1) System.out.println();
                    }
                    chromosome[randomGenes[i]].setBaseX(newX);
                } else if (changeCoords == 2) {
                    int newY = Random.randomWithRangeNN(chromosome[randomGenes[i]].getBaseCoords()[1] - MUTATION_RANGE, chromosome[randomGenes[i]].getBaseCoords()[1] + MUTATION_RANGE);
                    if (LOG1) System.out.print("Gene " + randomGenes[i] + " from " + chromosome[randomGenes[i]].getBaseCoords()[1] + " to newY = " + newY);
                    if (newY < 0) {
                        newY = 0;
                        if (LOG1) System.out.println(" set to " + newY);
                    } else if (newY > (cargoSpace.getWidth() - chromosome[randomGenes[i]].getWidth())) {
                        newY = cargoSpace.getWidth() - chromosome[randomGenes[i]].getWidth();
                        if (LOG1) System.out.println(" set to " + newY);
                    } else {
                        if (LOG1) System.out.println();
                    }
                    chromosome[randomGenes[i]].setBaseY(newY);
                } else if (changeCoords == 3) {
                    int newZ = Random.randomWithRangeNN(chromosome[randomGenes[i]].getBaseCoords()[2] - MUTATION_RANGE, chromosome[randomGenes[i]].getBaseCoords()[2] + MUTATION_RANGE);
                    if (LOG1) System.out.print("Gene " + randomGenes[i] + " from " + chromosome[randomGenes[i]].getBaseCoords()[2] + " to newZ = " + newZ);
                    if (newZ < 0) {
                        newZ = 0;
                        if (LOG1) System.out.println(" set to " + newZ);
                    } else if (newZ > (cargoSpace.getHeight() - chromosome[randomGenes[i]].getHeight())) {
                        newZ = cargoSpace.getHeight() - chromosome[randomGenes[i]].getHeight();
                        if (LOG1) System.out.println(" set to " + newZ);
                    } else {
                        if (LOG1) System.out.println();
                    }
                    chromosome[randomGenes[i]].setBaseZ(newZ);
                }
            }
            if (LOG1) System.out.println();
            ind.setChromosome(chromosome);
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        return ind;
    }

    private Individual mutateSwap(Individual ind) {
        Package[] chromosome = ind.getChromosome();
        for (int k = 0; k < SWAP_FREQ; k++) {
            int randomGene = Random.randomWithRange(0, chromosome.length - 1);
            int swapIndex = Random.randomWithRange(randomGene - SWAP_RANGE, randomGene + SWAP_RANGE);
            if (swapIndex < 0)
                swapIndex = 0;
            else if (swapIndex >= chromosome.length)
                swapIndex = chromosome.length - 1;
            Package temp = chromosome[randomGene].clone();
            chromosome[randomGene] = chromosome[swapIndex].clone();
            chromosome[swapIndex] = temp.clone();
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
        GeneticAlgorithm gA = new GeneticAlgorithm();
        gA.initialPopulation(null, null);
        gA.displaySolution();
    }

}
