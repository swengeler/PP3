public class Individual {

	int[] chromosome;
	double fitness;

	public Individual(int[] chromosome) {
		this.chromosome = chromosome;
		this.fitness = 0;
	}


	public int[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(int[] chromosome) {
		this.chromosome = chromosome;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public CargoSpace genoToPhenotype() {
    // convert the chromosome into a filled cargo space
    return null;
	}

	public Individual clone() {
		int[] chromClone = new int[chromosome.length];
		for(int i = 0; i < chromClone.length; i++) {
			chromClone[i] = chromosome[i];
		}
		return new Individual(chromClone);
	}

}
