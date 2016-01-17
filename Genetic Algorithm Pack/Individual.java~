public class Individual {

	private static CargoSpace cs;

	private Package[] chromosome;
	private double fitness;

	public Individual(Package[] chromosome) {
		this.chromosome = chromosome;
		this.setFitness();
	}

	public Package[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(Package[] chromosome) {
		this.chromosome = chromosome;
		this.getFitness();
	}

	public void setFitness1() {
		CargoSpace csNew = new CargoSpace(cs.getLength(), cs.getWidth(), cs. getHeight());
		csNew.fillCargoSpace(this.chromosome);
		this.fitness = csNew.getTotalValue() - csNew.getOverlap(chromosome);
	}

	public void setFitness2() {
		CargoSpace csNew = new CargoSpace(cs.getLength(), cs.getWidth(), cs. getHeight());
		csNew.fillCargoSpace(this.chromosome);
		this.fitness = (cs.getLength() * cs.getWidth() * cs.getHeight()) - csNew.getTotalGaps();
	}

	public void setFitness3() {
		CargoSpace csNew = new CargoSpace(cs.getLength(), cs.getWidth(), cs. getHeight());
		csNew.fillCargoSpace(this.chromosome);
		this.fitness = csNew.getTotalValue();
	}

	public double getFitness() {
		return fitness;
	}

	public Individual clone() {
		Package[] newChr = new Package[chromosome.length];
		for (int i = 0; i < chromosome.length; i++) {
			newChr[i] = chromosome[i].clone();
		}
		Individual newInd = new Individual(newChr);
		newInd.setFitness();
		return newInd;
	}

	public CargoSpace toCargoSpace() {
		CargoSpace returnCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		returnCS.fillCargoSpace(this.chromosome);
		return returnCS;
	}

	public static void setCargoSpace(CargoSpace cargoSpace) {
		cs = cargoSpace;
	}

}
