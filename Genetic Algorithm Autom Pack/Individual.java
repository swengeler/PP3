public class Individual {

	private static CargoSpace cs;
	private static Package[] sArr;

	private int[] chromosome;
	private double fitness;

	public Individual(int[] chromosome) {
		this.chromosome = chromosome;
		this.setFitness();
	}

	public int[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(int[] chromosome) {
		this.chromosome = chromosome;
		this.getFitness();
	}

	public void setFitness() {
		CargoSpace newCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		for (int i = 0; i < chromosome.length; i++) {
				newCs.putPackage(sArr[chromosome[i]]);
		}
		return newCs.getTotalValue();
	}

	public double getFitness() {
		return fitness;
	}

	public Individual clone() {
		int[] newChr = new int[chromosome.length];
		for (int i = 0; i < chromosome.length; i++) {
			newChr[i] = chromosome[i].clone();
		}
		Individual newInd = new Individual(newChr);
		newInd.setFitness();
		return newInd;
	}

	public static void setCargoSpace(CargoSpace cargoSpace) {
		cs = cargoSpace;
	}

	public static void setStatesArray(CargoSpace statesArray) {
		sArr = statesArray;
	}

}
