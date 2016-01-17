import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneticAlgorithm {

    private final boolean LOG1 = false;
    private final boolean LOG2 = false;
    private final boolean TEST_LOG1 = false;
    private final boolean TEST_LOG2 = true;

    private int POPULATION_SIZE = 200;

    private double MUTATION_PROB = 0.0; // default  = 0.0
    private double SWAP_PROB = 0.05; // default = 0.5
    private int CROSSOVER_FREQ = 2; // default = 2
    private String SELECTION_MODE = "TOURNAMENT"; // default = "TOURNAMENT"
    private double ELITIST_TOP_PERCENT = 0.1; // default = 0.1
    private int TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE); // default = (int) (0.1 * POPULATION_SIZE)

    private Individual[] population;
    private Package[] packageTypes;
    private Package[] statesArray;
    private CargoSpace cargoSpace;
    private int amountSum;

    private int[] amountOfType;
    private int[] amountForReduction;

    private int gene;
    private boolean test;

    public GeneticAlgorithm() {
        this.test = false;
    }

    public GeneticAlgorithm(int gene) {
        this.gene = gene;
        this.test = true;
    }

    public void run(Package[] types, int[] amountOfType) {
        int NR_RUNS = 25;
        double overallBest = 0;
        double overallWorst = Double.MAX_VALUE;
        double totalValue = 0;

        long bestTime = Long.MAX_VALUE;
        long worstTime = 0;
        long totalTimeForAverage = 0;

        int bestGaps = Integer.MAX_VALUE;
        int worstGaps = 0;
        int totalGapsForAverage = 0;

        this.amountOfType = new int[4];
        this.amountOfType[0] = 10;
        this.amountOfType[1] = 10;
        this.amountOfType[2] = 10;
        this.amountOfType[3] = 10;
        //this.amountOfType[4] = 55;
        //this.amountOfType[5] = 44;

        this.amountForReduction = new int[this.amountOfType.length];
        System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);

        packageTypes = new Package[4];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");
        packageTypes[3] = new Package("D", 4, 4, 1, 1.0);
        //packageTypes[4] = new Package("E", 6, 2, 2, 1.0);
        //packageTypes[5] = new Package("F", 5, 2, 3, 1.0);

        CargoSpace.packageTypes = packageTypes;
        int[] placed = new int[packageTypes.length];
        int[] most = new int[packageTypes.length];
        int[] fewest = new int[packageTypes.length];
        for (int i = 0; i < fewest.length; i++) {
            fewest[i] = Integer.MAX_VALUE;
        }

        for (int runs = 0; runs < NR_RUNS; runs++) {
            if (runs % 10 == 0 && TEST_LOG2)
                System.out.println("Run No. " + runs);


            amountSum = 0;
            int maxValueSum = 0;
            for (int i = 0; i < this.amountOfType.length; i++) {
                amountSum += this.amountOfType[i];
                maxValueSum += this.amountOfType[i] * packageTypes[i].getValue();
            }


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

            cargoSpace = new CargoSpace(33, 5, 8);
            Individual.setCargoSpace(cargoSpace);
            Individual.setStatesArray(statesArray);

            for (int i = 0; i < statesArray.length; i++) {
                p = statesArray[i];
                if (LOG1) System.out.println(p.getType() + "-package: length = " + p.getLength() + " width = " + p.getWidth() + " height = " + p.getHeight());
            }

            // initialising the population
            int[] chromosome = new int[amountSum];
            population = new Individual[POPULATION_SIZE];
            for (int i = 0; i < population.length; i++) {
                chromosome = new int[amountSum];
                System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);
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
                        if (this.amountForReduction[this.getPositionInArray(statesArray[packageChosen])] <= 0)
                            packageChosen = -1;
                    }
                    //System.out.println("P chosen: " + packageChosen);
                    this.amountForReduction[this.getPositionInArray(statesArray[packageChosen])]--;
                    chromosome[j] = packageChosen;
                }
                population[i] = new Individual(chromosome);
            }

            HeapSort.sortDownInd(population);

            int generation = 0;
            int noChange = 0;
            boolean change = true;
            Individual bestInd = new Individual(population[0].getChromosome());

            long startTime = System.currentTimeMillis();

            while (generation < 1500 && change) {
                if (generation % 50 == 0 && population.length != 1) {
                    if (LOG2 || LOG1) System.out.println("Generation " + generation);
                    if (LOG2 || LOG1) System.out.println("Maximum fitness value = " + population[0].getFitness());
                    for (int i = 0; i < POPULATION_SIZE; i += 50) {
                        if (LOG2) System.out.println((i + 1) + ". in the population: " +  population[i].getFitness());
                    }
                    if (LOG2) System.out.println();
                }
                population = reproduce(population);
                population = fitnessAndSort(population);
                if (population[0].getFitness() > bestInd.getFitness()) {
                    bestInd = population[0].clone();
                    if (LOG1) System.out.println("Best individual changed in generation " + generation + " (" + bestInd.getFitness() + ")");
                }
                if (population[0].getFitness() <= bestInd.getFitness())
                    noChange++;
                else
                    noChange = 0;
                if ((bestInd.getFitness() > 230.0 || bestInd.getFitness() > (0.8 * maxValueSum)) && noChange > 200) {
                    noChange = 0;
                    change = false;
                }
                generation++;
            }

            long endTime = System.currentTimeMillis();
            long totTime = endTime - startTime;

            for (int i = 0; i < bestInd.toCargoSpace().getPacking().length; i++) {
                if (LOG1) System.out.println(bestInd.toCargoSpace().getPacking()[i].getType() + "-package with value " + bestInd.toCargoSpace().getPacking()[i].getValue());
            }

            cargoSpace = new CargoSpace(33, 5, 8);
            cargoSpace = bestInd.toCargoSpace();

            for (int i = 0; i < placed.length; i++) {
                placed[i] += cargoSpace.getNrIndivPackages()[i];
                //System.out.println(placed[i]);
                if (cargoSpace.getNrIndivPackages()[i] > most[i])
                    most[i] = cargoSpace.getNrIndivPackages()[i];
                if (cargoSpace.getNrIndivPackages()[i] < fewest[i])
                    fewest[i] = cargoSpace.getNrIndivPackages()[i];
            }

            if (TEST_LOG1) {
                System.out.println("Iteration " + (runs + 1));
                System.out.println("Maximum fitness value: " + bestInd.getFitness());
                System.out.println("Maximum total value: " + cargoSpace.getTotalValue());
                System.out.println("Runtime: " + totTime + "ms");
                System.out.println();
            }

            if (bestInd.getFitness() > overallBest)
                overallBest = bestInd.getFitness();
            if (bestInd.getFitness() < overallWorst)
                overallWorst = bestInd.getFitness();
            if (totTime < bestTime)
                bestTime = totTime;
            if (totTime > worstTime)
                worstTime = totTime;
            if (cargoSpace.getTotalGaps() < bestGaps)
                bestGaps = cargoSpace.getTotalGaps();
            if (cargoSpace.getTotalGaps() > worstGaps)
                worstGaps = cargoSpace.getTotalGaps();
            totalValue += bestInd.getFitness();
            totalTimeForAverage += totTime;
            totalGapsForAverage += cargoSpace.getTotalGaps();
        }

        System.out.println("OVERALL RESULTS OVER " + NR_RUNS + " ITERATIONS");
        System.out.println("VALUE - Best: " + overallBest);
        System.out.println("VALUE - Average: " + (totalValue / (double)NR_RUNS));
        System.out.println("VALUE - Worst: " + overallWorst);
        System.out.println("RUNTIME - Best: " + bestTime);
        System.out.println("RUNTIME - Average: " + (totalTimeForAverage / (double)NR_RUNS));
        System.out.println("RUNTIME - Worst: " + worstTime);
        System.out.println("GAPS - Best: " + bestGaps);
        System.out.println("GAPS - Average: " + (totalGapsForAverage / (double)NR_RUNS));
        System.out.println("GAPS - Worst: " + worstGaps);

        System.out.println("\nPACKAGES PLACED");
        System.out.print("Most:     ");
        for (int i = 0; i < packageTypes.length; i++) {
            System.out.print(packageTypes[i].getType() + ": " + most[i] + " ");
        }
        System.out.print("\nAverage:  ");
        for (int i = 0; i < packageTypes.length; i++) {
            System.out.print(packageTypes[i].getType() + ": " + ((double)placed[i] / (double)NR_RUNS) + " ");
        }
        System.out.print("\nFewest:   ");
        for (int i = 0; i < packageTypes.length; i++) {
            System.out.print(packageTypes[i].getType() + ": " + fewest[i] + " ");
        }
        System.out.println();
        System.out.println("\nPARAMETERS USED");
        System.out.println("Population size: " + POPULATION_SIZE);
        System.out.println("Selection mode: " + SELECTION_MODE.toLowerCase());
        if (SELECTION_MODE.equalsIgnoreCase("TOURNAMENT"))
            System.out.println("Tournament size: " + TOURNAMENT_SIZE);
        else if (SELECTION_MODE.equalsIgnoreCase("ELITIST"))
            System.out.println("Elitist top percent: " + ELITIST_TOP_PERCENT);
        System.out.println("Mutation probability: " + MUTATION_PROB);
        System.out.println("Swap probability: " + SWAP_PROB);
        System.out.println("Crossover frequency: " + CROSSOVER_FREQ);

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

    public Individual crossOver(Individual parent1, Individual parent2) {
        System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);
        for (int i = 0; i < this.amountForReduction.length; i++) {
            //System.out.println(this.packageTypes[i].getType() + ": " + this.amountForReduction[i]);
        }
        int[] childChr = new int[parent1.getChromosome().length];
        int[] curParentChr;
        if (CROSSOVER_FREQ > 0) {
            int[] crossOverPoints = Random.randomListWithRange(0, childChr.length - 1, CROSSOVER_FREQ);
            HeapSort.sortUpInt(crossOverPoints);
            int lastCrPoint = 0;
            for (int i = 0; i < crossOverPoints.length; i++) {
                if (i % 2 == 0)
                    curParentChr = parent1.getChromosome();
                else
                    curParentChr = parent2.getChromosome();
                for (int j = lastCrPoint; j < crossOverPoints[i]; j++) {
                    if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[j]])] > 0) {
                        childChr[j] = curParentChr[j];
                        this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[j]])]--;
                    }
                    else {
                        boolean found = false;
                        for (int k = j; k < curParentChr.length && !found; k++) {
                            if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])] > 0) {
                                childChr[j] = curParentChr[k];
                                this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])]--;
                                found = true;
                            }
                        }
                        if (!found) {
                            boolean found2 = false;
                            for (int k = 0; k < j && !found2; k++) {
                                if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])] > 0) {
                                    childChr[j] = curParentChr[k];
                                    this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])]--;
                                    found2 = true;
                                }
                            }
                        }
                    }
                }
                lastCrPoint = crossOverPoints[i];
            }

            if (crossOverPoints.length % 2 == 0)
                curParentChr = parent2.getChromosome();
            else
                curParentChr = parent1.getChromosome();

            for (int i = crossOverPoints[crossOverPoints.length - 1]; i < childChr.length; i++) {
                if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[i]])] > 0) {
                    childChr[i] = curParentChr[i];
                    this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[i]])]--;
                }
                else {
                    boolean found = false;
                    for (int k = i; k < curParentChr.length && !found; k++) {
                        if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])] > 0) {
                            childChr[i] = curParentChr[k];
                            this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])]--;
                            found = true;
                        }
                    }
                    if (!found) {
                        boolean found2 = false;
                        for (int k = 0; k < i && !found2; k++) {
                            if (this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])] > 0) {
                                childChr[i] = curParentChr[k];
                                this.amountForReduction[this.getPositionInArray(statesArray[curParentChr[k]])]--;
                                found2 = true;
                            }
                        }
                    }
                }
            }
        } else {
            if (Math.random() < 0.5)
                System.arraycopy(parent1.getChromosome(), 0, childChr, 0, childChr.length);
            else
                System.arraycopy(parent2.getChromosome(), 0, childChr, 0, childChr.length);
        }
        Individual child = new Individual(childChr);
        return mutate(mutateSwap(child));
    }

    public Individual crossOver1(Individual parent1, Individual parent2) {
        System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);
        for (int i = 0; i < this.amountForReduction.length; i++) {
            //System.out.println(this.packageTypes[i].getType() + ": " + this.amountForReduction[i]);
        }
        int[] childChr = new int[parent1.getChromosome().length];
        if (CROSSOVER_FREQ > 0) {
            int[] crossOverPoints = Random.randomListWithRange(0, childChr.length - 1, 2);
            HeapSort.sortUpInt(crossOverPoints);
            System.out.println("Crossover points: " + crossOverPoints[0] + " and " + crossOverPoints[1]);
            for (int i = crossOverPoints[0]; i < crossOverPoints[1]; i++) {
                childChr[i] = parent2.getChromosome()[i];
                this.amountForReduction[this.getPositionInArray(statesArray[parent2.getChromosome()[i]])]--;
                System.out.println("Middle " + i + ": " + childChr[i]);
                System.out.println("Amount array (" + this.getPositionInArray(statesArray[parent2.getChromosome()[i]]) + ", " + statesArray[parent2.getChromosome()[i]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent2.getChromosome()[i]])]);
            }
            for (int i = crossOverPoints[1]; i < childChr.length; i++) {
                if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])] > 0) {
                    childChr[i] = parent1.getChromosome()[i];
                    this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])]--;
                    System.out.println("1Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[i]])+ ", " + statesArray[parent1.getChromosome()[i]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])]);
                } else {
                    boolean found = false;
                    for (int k = i; k < parent1.getChromosome().length && !found; k++) {
                        if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])] > 0) {
                            childChr[i] = parent1.getChromosome()[k];
                            this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]--;
                            System.out.println("2Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[k]])+ ", " + statesArray[parent1.getChromosome()[k]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]);
                            found = true;
                        }
                    }
                    if (!found) {
                        boolean found2 = false;
                        for (int k = 0; k < i && !found2; k++) {
                            if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])] > 0) {
                                childChr[i] = parent1.getChromosome()[k];
                                this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]--;
                                System.out.println("3Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[k]])+ ", " + statesArray[parent1.getChromosome()[k]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]);
                                found2 = true;
                            }
                        }
                    }
                }
                System.out.println("End " + i + ": " + childChr[i]);
            }
            for (int i = 0; i < crossOverPoints[0]; i++) {
                if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])] > 0) {
                    childChr[i] = parent1.getChromosome()[i];
                    this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])]--;
                    System.out.println("4Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[i]])+ ", " + statesArray[parent1.getChromosome()[i]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[i]])]);
                } else {
                    boolean found = false;
                    for (int k = i; k < parent1.getChromosome().length && !found; k++) {
                        if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])] > 0) {
                            childChr[i] = parent1.getChromosome()[k];
                            this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]--;
                            System.out.println("5Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[k]])+ ", " + statesArray[parent1.getChromosome()[k]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]);
                            found = true;
                        }
                    }
                    if (!found) {
                        boolean found2 = false;
                        for (int k = 0; k < i && !found2; k++) {
                            if (this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])] > 0) {
                                childChr[i] = parent1.getChromosome()[k];
                                this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]--;
                                System.out.println("6Amount array (" + this.getPositionInArray(statesArray[parent1.getChromosome()[k]])+ ", " + statesArray[parent1.getChromosome()[k]].getType() + ") to " + this.amountForReduction[this.getPositionInArray(statesArray[parent1.getChromosome()[k]])]);
                                found2 = true;
                            }
                        }
                    }
                }
                System.out.println("Front " + i + ": " + childChr[i]);
            }
            System.out.println();
        } else {
            if (Math.random() < 0.5)
                System.arraycopy(parent1.getChromosome(), 0, childChr, 0, childChr.length);
            else
                System.arraycopy(parent2.getChromosome(), 0, childChr, 0, childChr.length);
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
        if (TOURNAMENT_SIZE > 0) {
            Individual[] tournament = new Individual[TOURNAMENT_SIZE];
            int[] randomList = Random.randomListWithRange(0, population.length - 1, TOURNAMENT_SIZE);
            for (int i = 0; i < randomList.length; i++) {
                tournament[i] = population[randomList[i]];
            }
            HeapSort.sortDownInd(tournament);
            return tournament[0];
        } else
            return population[Random.randomWithRange(0, population.length - 1)];
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

    public void runTest() {
        System.out.println("------ 1.1 ------");
        POPULATION_SIZE = 10;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.2 ------");
        POPULATION_SIZE = 20;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.3 ------");
        POPULATION_SIZE = 35;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.4 ------");
        POPULATION_SIZE = 50;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.5 ------");
        POPULATION_SIZE = 70;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.6 ------");
        POPULATION_SIZE = 85;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        System.out.println("\n------ 1.7 ------");
        POPULATION_SIZE = 100;
        //TOURNAMENT_SIZE = (int) (0.1 * POPULATION_SIZE);
        this.run(null, null);
        POPULATION_SIZE = 85;

        System.out.println("\n\n------ 2.1 ------");
        ELITIST_TOP_PERCENT = 0.05;
        this.run(null, null);
        System.out.println("\n------ 2.2 ------");
        ELITIST_TOP_PERCENT = 0.1;
        this.run(null, null);
        System.out.println("\n------ 2.3 ------");
        ELITIST_TOP_PERCENT = 0.15;
        this.run(null, null);
        System.out.println("\n------ 2.4 ------");
        ELITIST_TOP_PERCENT = 0.2;
        this.run(null, null);
        System.out.println("\n------ 2.5 ------");
        ELITIST_TOP_PERCENT = 0.3;
        this.run(null, null);
        System.out.println("\n------ 2.6 ------");
        ELITIST_TOP_PERCENT = 0.4;
        this.run(null, null);
        ELITIST_TOP_PERCENT = 0.1;

        System.out.println("\n\n------ 3.1 ------");
        CROSSOVER_FREQ = 0;
        this.run(null, null);
        System.out.println("\n------ 3.2 ------");
        CROSSOVER_FREQ = 1;
        this.run(null, null);
        System.out.println("\n------ 3.3 ------");
        CROSSOVER_FREQ = 2;
        this.run(null, null);
        System.out.println("\n------ 3.4 ------");
        CROSSOVER_FREQ = 3;
        this.run(null, null);
        System.out.println("\n------ 3.5 ------");
        CROSSOVER_FREQ = 4;
        this.run(null, null);
        System.out.println("\n------ 3.6 ------");
        CROSSOVER_FREQ = 5;
        this.run(null, null);
        CROSSOVER_FREQ = 2;

        System.out.println("\n\n------ 4.1 ------");
        SWAP_PROB = 0.01;
        this.run(null, null);
        System.out.println("\n------ 4.2 ------");
        SWAP_PROB = 0.03;
        this.run(null, null);
        System.out.println("\n------ 4.3 ------");
        SWAP_PROB = 0.05;
        this.run(null, null);
        System.out.println("\n------ 4.4 ------");
        SWAP_PROB = 0.07;
        this.run(null, null);
        System.out.println("\n------ 4.5 ------");
        SWAP_PROB = 0.1;
        this.run(null, null);
        System.out.println("\n------ 4.6 ------");
        SWAP_PROB = 0.15;
        this.run(null, null);
        SWAP_PROB = 0.05;

        System.out.println("\n\n------ 5.1 ------");
        MUTATION_PROB = 0;
        this.run(null, null);
        System.out.println("\n------ 5.2 ------");
        MUTATION_PROB = 0.01;
        this.run(null, null);
        System.out.println("\n------ 5.3 ------");
        MUTATION_PROB = 0.02;
        this.run(null, null);
        System.out.println("\n------ 5.4 ------");
        MUTATION_PROB = 0.03;
        this.run(null, null);
        System.out.println("\n------ 5.5 ------");
        MUTATION_PROB = 0.04;
        this.run(null, null);
        MUTATION_PROB = 0;
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
        gA.run(null, null);
        //gA.runTest();
    }

}
