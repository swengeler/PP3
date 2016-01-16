import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {

    private final boolean LOG1 = false;
    private final boolean LOG2 = true;

    private int POPULATION_SIZE = 100;
    private int SWAP_RANGE = 100;

    private double MUTATION_PROB = 0.00;
    private double SWAP_PROB = 0.05;
    private int CROSSOVER_FREQ = 1;
    private String SELECTION_MODE = "TOURNAMENT";
    private double ELITIST_TOP_PERCENT = 0.2;
    private int TOURNAMENT_SIZE = 30;
    private boolean GENITOR = false;

    private Individual[] population;
    private Package[] packageTypes;
    private Package[] statesArray;
    private CargoSpace cargoSpace;
    private int amountSum;

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
        amountOfType[0] = 83;
        amountOfType[1] = 55;
        amountOfType[2] = 50;

        amountSum = 0;
        for (int i = 0; i < amountOfType.length; i++) {
            amountSum += amountOfType[i];
        }

        int[] amountForReduction = new int[amountOfType.length];
        System.arraycopy(amountOfType, 0, amountForReduction, 0, amountOfType.length);

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

        statesArray = new Package[stateSum];
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
            chromosome = new int[amountSum];
            System.arraycopy(amountOfType, 0, amountForReduction, 0, amountOfType.length);
            for (int j = 0; j < chromosome.length; j++) {
                int packageChosen = -1;
                while (packageChosen < 0) {
                    packageChosen++;
                    int random1 = Random.randomWithRange(0, packageTypes.length - 1);
                    for (int k = 0; k < random1; k++) {
                        packageChosen += packageTypes[k].getNrRotations();
                    }
                    int random2 = Random.randomWithRange(0, packageTypes[random1].getNrRotations() - 1);
                    packageChosen += random2;
                    if (amountForReduction[this.getPositionInArray(statesArray[packageChosen])] <= 0)
                        packageChosen = -1;
                }
                amountForReduction[this.getPositionInArray(statesArray[packageChosen])]--;
                chromosome[j] = packageChosen;
            }
            population[i] = new Individual(chromosome);
        }

        if (POPULATION_SIZE == 1 || true) {
            for (int i = 0; i < population[0].getChromosome().length; i++) {
                System.out.println(i + ": " + population[0].getChromosome()[i]);
            }
        }

        HeapSort.sortDownInd(population);

        int generation = 0;
        int noChange = 0;
        boolean change = true;
        Individual bestInd = new Individual(population[0].getChromosome());

        while (generation < 1500 && change) {
            if (generation % 50 == 0 && population.length != 1) {
                if (LOG2) System.out.println("Generation " + generation);
                if (LOG2) System.out.println("Maximum fitness value = " + population[0].getFitness());
                for (int i = 0; i < POPULATION_SIZE; i += 50) {
                    System.out.println((i + 1) + ". in the population: " +  population[i].getFitness());
                }
                System.out.println();
            }
            population = reproduce(population);
            population = fitnessAndSort(population);
            if (population[0].getFitness() > bestInd.getFitness()) {
                bestInd = population[0].clone();
                if (LOG2) System.out.println("Best individual changed in generation " + generation + " (" + bestInd.getFitness() + ")");
            }
            if (population[0].getFitness() <= bestInd.getFitness())
                noChange++;
            else
                noChange = 0;
            if (noChange > 500) {
                noChange = 0;
                //change = false;
            }
            generation++;
        }

        for (int i = 0; i < bestInd.toCargoSpace().getPacking().length; i++) {
            System.out.println(bestInd.toCargoSpace().getPacking()[i].getType() + "-package with value " + bestInd.toCargoSpace().getPacking()[i].getValue());
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
        if (GENITOR) {
            Individual[] newPopulation = new Individual[POPULATION_SIZE + 1];
            System.arraycopy(population, 0, newPopulation, 0, population.length);
            if (SELECTION_MODE.equalsIgnoreCase("ELITIST")) {
                Individual parent1 = elitistSelection(population);
                Individual parent2 = elitistSelection(population);
                newPopulation[population.length] = crossOver(parent1, parent2);
            } else if (SELECTION_MODE.equalsIgnoreCase("TOURNAMENT")) {
                Individual parent1 = tournamentSelection(population);
                Individual parent2 = tournamentSelection(population);
                newPopulation[population.length] = crossOver(parent1, parent2);
            } else if (SELECTION_MODE.equalsIgnoreCase("ROULETTE")) {
                Individual parent1 = rouletteSelection(population);
                Individual parent2 = rouletteSelection(population);
                newPopulation[population.length] = crossOver(parent1, parent2);
            } else if (SELECTION_MODE.equalsIgnoreCase("RANDOM")) {
                Individual parent1 = randomSelection(population);
                Individual parent2 = randomSelection(population);
                newPopulation[population.length] = crossOver(parent1, parent2);
            }
            HeapSort.sortDownInd(newPopulation);
            System.arraycopy(newPopulation, 0, population, 0, population.length);
            return population;
        } else {
            Individual[] newPopulation = new Individual[POPULATION_SIZE];
            for (int i = 0; i < newPopulation.length; i++) {
                if (SELECTION_MODE.equalsIgnoreCase("ELITIST")) {
                    Individual parent1 = elitistSelection(population);
                    Individual parent2 = elitistSelection(population);
                    newPopulation[i] = crossOver(parent1, parent2);
                } else if (SELECTION_MODE.equalsIgnoreCase("TOURNAMENT")) {
                    Individual parent1 = tournamentSelection(population);
                    Individual parent2 = tournamentSelection(population);
                    newPopulation[i] = crossOver(parent1, parent2);
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
    }

    public Individual crossOver(Individual parent1, Individual parent2) {
        int[] childChr = new int[parent1.getChromosome().length];
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
        Individual child = new Individual(childChr);
        return mutate(mutateSwap(child));
    }

    private Individual mutate(Individual ind) {
        int[] chromosome = ind.getChromosome();
        Package p;
        for (int i = 0; i < chromosome.length; i++) {
            if (Math.random() < MUTATION_PROB && statesArray[chromosome[i]].getNrRotations() > 1) {
                p = statesArray[chromosome[i]].clone();
                int position = this.getPositionInArray(p);
                int nrRot = p.getNrRotations();
                int newGene = chromosome[i];
                while (newGene == chromosome[i]) {
                    newGene = Random.randomWithRange(position, (position + nrRot - 1));
                }
                //System.out.print("Gene changed from " + statesArray[chromosome[i]].getType());
                chromosome[i] = newGene;
                //System.out.println(" to " + statesArray[chromosome[i]].getType());
            }
        }
        ind.setChromosome(chromosome);
        return ind;
    }

    private Individual mutateSwap(Individual ind) {
        int[] chromosome = ind.getChromosome();
        for (int i = 0; i < chromosome.length; i++) {
            if (Math.random() < SWAP_PROB) {
                int temp = chromosome[i];
                int randomGene = Random.randomWithRange(0, chromosome.length - 1);
                chromosome[i] = chromosome[randomGene];
                chromosome[randomGene] = temp;
            }
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

    private Individual tournamentSelection(Individual[] population) {
        Individual[] tournament = new Individual[TOURNAMENT_SIZE];
        int[] randomList = Random.randomListWithRange(0, population.length - 1, TOURNAMENT_SIZE);
        for (int i = 0; i < randomList.length; i++) {
            tournament[i] = population[randomList[i]];
        }
        HeapSort.sortDownInd(tournament);
        return tournament[0];
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
        f.setTitle("GA - Automated Packing");
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
