public class Individual {

	int[][] chromosomes;
	double fitness;

	public Individual(int[][] chromosomes) {
		this.chromosomes = chromosomes;
		this.fitness = 0;
	}

	public int[][] getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(int[][] chromosome) {
		this.chromosomes = chromosomes;
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
		int[][] chromClone = new int[chromosomes.length][chromosomes[0].length];
		for(int i = 0; i < chromClone.length; i++) {
			chromClone[i] = chromosomes[i];
		}
		return new Individual(chromClone);
	}

}
