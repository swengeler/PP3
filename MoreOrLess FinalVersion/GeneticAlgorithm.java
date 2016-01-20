public class GeneticAlgorithm {

    private int POPULATION_SIZE = 85; // default = 85

    private double MUTATION_PROB = 0.0; // default  = 0.0
    private double SWAP_PROB = 0.05; // default = 0.5
    private int CROSSOVER_FREQ = 2; // default = 2
    private String SELECTION_MODE = "TOURNAMENT"; // default = "TOURNAMENT"
    private double ELITIST_TOP_PERCENT = 0.1; // default = 0.1
    private double TOURNAMENT_SIZE = 0.1; // default = (int) (0.1 * POPULATION_SIZE)
    private long totTime;

    private Individual[] population;
    private Package[] packageTypes;
    private Package[] statesArray;
    private CargoSpace cargoSpace;
    private static CargoSpace endCargoSpace;
    private int amountSum;

    private int[] amountOfType;
    private int[] amountForReduction;

    /**
    * The primary method of the class GeneticAlgorithm. It creates an initial population of individuals
    * with randomly generated chromosomes (that contain all of the packages specified). Then it uses a
    * loop to evolve that population over several generations using the selection, crossover and mutation
    * methods provided in the class.
    *
    * @param types An array containing all the types of packages that can be used to generate a solution.
    * @param amountOfType An array corresponding to the types array, designating the maximum number of
    *                     packages that can be placed of the corresponding package type.
    */
    public void run(Package[] types, int[] amountOfType) {
      	this.amountOfType = new int[amountOfType.length];
      	for(int i = 0; i<amountOfType.length; i++){
      		this.amountOfType[i] = amountOfType[i];
      	}

        this.amountForReduction = new int[this.amountOfType.length];
        System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);

        packageTypes = new Package[types.length];
      	for(int i = 0; i<amountOfType.length; i++){
      		this.packageTypes[i] = types[i];
      	}

        CargoSpace.packageTypes = this.packageTypes;

        amountSum = 0;
        int maxValueSum = 0;
        for (int i = 0; i < this.amountOfType.length; i++) {
            amountSum += this.amountOfType[i];
            maxValueSum += this.amountOfType[i] * this.packageTypes[i].getValue();
        }


        int stateSum = 0;
        for (int i = 0; i < this.packageTypes.length; i++) {
            stateSum += this.packageTypes[i].getNrRotations();
        }

        statesArray = new Package[stateSum];
        Package p;
        int counter = 0;
        for (int j = 0; j < this.packageTypes.length; j++) {
            for (int k = 0; k < this.packageTypes[j].getNrRotations(); k++) {
                p = new Package(this.packageTypes[j].getType());
                if (this.packageTypes[j].getNrRotations() > 1) {
                    p.setRotations(k);
                }
                statesArray[counter] = p.clone();
                counter++;
            }
        }

        cargoSpace = new CargoSpace(33, 5, 8);
        Individual.setCargoSpace(cargoSpace);
        Individual.setStatesArray(statesArray);
       
        
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
                    int random1 = Random.randomWithRange(0, this.packageTypes.length - 1);
                    for (int k = 0; k < random1; k++) {
                        packageChosen += this.packageTypes[k].getNrRotations();
                    }
                    int random2 = Random.randomWithRange(0, this.packageTypes[random1].getNrRotations() - 1);
                    packageChosen += random2;
                    if (this.amountForReduction[this.getPositionInArray(statesArray[packageChosen])] <= 0)
                        packageChosen = -1;
                }
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
            population = reproduce(population);
            population = fitnessAndSort(population);
            if (population[0].getFitness() > bestInd.getFitness()) {
                bestInd = population[0].clone();
            }
            if (population[0].getFitness() <= bestInd.getFitness())
                noChange++;
            else
                noChange = 0;
            if ((bestInd.getFitness() > 190 || bestInd.getFitness() > (0.9 * maxValueSum)) && noChange > 200) {
                noChange = 0;
                change = false;
            }
            generation++;
        }
   
        long endTime = System.currentTimeMillis();
        totTime = endTime - startTime;
        
        cargoSpace = new CargoSpace(33, 5, 8);
        cargoSpace = bestInd.toCargoSpace();

        endCargoSpace = cargoSpace;
        Display3D.represent(endCargoSpace);
    
    }

    /**
    * A method used to determine the position of a package used in the packing of the cargo cargo space
    * in the packageType array. It is mostly used to also get information about the number of packages
    * of that type which should be placed/that are left.
    *
    * @param p The package for which the position in the packageType/amountOfType array should be determined.
    * @return counter The index of the position of said package in the array.
    */
    private int getPositionInArray(Package p) {
        int counter = 0;
        while (!p.getType().equalsIgnoreCase(this.packageTypes[counter].getType()))
            counter++;
        return counter;
    }

    /**
    * A method to set the fitness of every individual in the population to be sorted and then sorting the
    * individuals by their fitness values.
    *
    * @param population The population to be sorted.
    * @return population The same population, now with updated fitness values and sorted according to the
    *                    individuals' fitness.
    */
    private Individual[] fitnessAndSort(Individual[] population) {
        for (int i = 0; i < population.length; i++) {
            population[i].setFitness();
        }
        HeapSort.sortDownInd(population);
        return population;
    }

    /**
    * A method creating a new population from the former generation's population by crossover. Depending on
    * one of the parameters of the genetic algorithms different selection methods for the parents of each
    * child individual in the new population are chosen.
    *
    * @param population The old population (parents).
    * @return newPopulation The new population (children).
    */
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
            }
        }
        return newPopulation;
    }

    /**
    * A method that applies a "modified crossover" which maintains package ordering and the correct number
    * of each package type in the chromosome.
    *
    * @param parent1 The first parent.
    * @param parent2 The second parent.
    * @return child The child created by crossover between the parents' chromosomes.
    */
    public Individual crossOver(Individual parent1, Individual parent2) {
        System.arraycopy(this.amountOfType, 0, this.amountForReduction, 0, this.amountOfType.length);
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

    /**
    * A (mostly unused) method that randomly rotates the packages in an individuals chromosome,
    * thereby mutating it.
    *
    * @param ind The individual whose chromosome should be mutated.
    * @return ind The individual after its chromosome has been mutated.
    */
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
                chromosome[i] = newGene;
            }
        }
        ind.setChromosome(chromosome);
        return ind;
    }

    /**
    * A second mutation method. It swaps a certain number of genes in a chromosome with other genes.
    *
    * @param ind The individual whose chromosome should be mutated.
    * @return ind The individual after its chromosome has been mutated.
    */
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

    /**
    * A selection method applying elitist selection. It randomly chooses one individual from a certain
    * percentage of the fittest individuals in the population.
    *
    * @param population The population from which an individual should be chosen for reproduction.
    * @return Individual The randomly selected individual.
    */
    private Individual elitistSelection(Individual[] population) {
        int randomSelect = (int) (Math.random() * (population.length * ELITIST_TOP_PERCENT));
        return population[randomSelect];
    }

    /**
    * A selection method applying roulette selection. Each individual has a certain probability to be
    * selected for reproduction. That probability is much higher for fitter individuals than for
    * "weakest" ones.
    *
    * @param population The population from which an individual should be chosen for reproduction.
    * @return Individual The selected individual.
    */
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

    /**
    * A selection method applying tournament selecction. A certain number of individuals from the
    * population is chosen for a "tournament". The fittest of these individual is selected for
    * reproduction.
    *
    * @param population The population from which an individual should be chosen for reproduction.
    * return Individual The selected indivual (fittest in the current tournament).
    */
    private Individual tournamentSelection(Individual[] population) {
        if (TOURNAMENT_SIZE > 0) {
            Individual[] tournament = new Individual[(int) (TOURNAMENT_SIZE*POPULATION_SIZE)];
            int[] randomList = Random.randomListWithRange(0, population.length - 1, (int) (TOURNAMENT_SIZE*POPULATION_SIZE));
            for (int i = 0; i < randomList.length; i++) {
                tournament[i] = population[randomList[i]];
            }
            HeapSort.sortDownInd(tournament);
            return tournament[0];
        } else
            return population[Random.randomWithRange(0, population.length - 1)];
    }


    /**
     * A Method to get the runtime of the genetic algorithm
     *
     * @return totTime Runtime of the algorithm
     */
	public long getRuntime(){
		return totTime;
	}

	/**
     * A Method to get the used packages
     *
     * @return int[] containing the amount of A, B, C Packages used
     */
	public int[] getNrPack(){
		return endCargoSpace.getNrIndivPackages();
	}

	/**
     * A Method to get the gaps wich are left
     *
     * @return int number of gaps left
     */
	public int getGaps(){
		return endCargoSpace.getTotalGaps();
	} 
	
	public void setTournamentSize(double tournamentSize){
		TOURNAMENT_SIZE = (0.01*tournamentSize);
	}
	
	public double getTournamentSize(){
		return 100*TOURNAMENT_SIZE;
	}

	public void setElitestTop(double elitistTopPercentage){
		ELITIST_TOP_PERCENT = (int) 0.01*elitistTopPercentage;
	}
	
	public double getElitestTop(){
		return 100*ELITIST_TOP_PERCENT;
	}
	
	public void setPopulationSize(int populationSize){
		POPULATION_SIZE = populationSize;
	}
	
	public int getPopulationSize(){
		return POPULATION_SIZE;
	}
	
	public void setCrossover(int crossoverFreq){
		CROSSOVER_FREQ = crossoverFreq;
	}
	
	public int getCrossover(){
		return CROSSOVER_FREQ;
	}
	 
	public void setSwapProbability(double swapProb){
		SWAP_PROB = 0.01*swapProb;
	}
	
	public double getSwapProbability(){
		return SWAP_PROB*100;
	}

	public void setMutationProbability(double mutation){
		MUTATION_PROB = mutation;
	}
	
	public double getMutationProbability(){
		return MUTATION_PROB;
	}	
}
