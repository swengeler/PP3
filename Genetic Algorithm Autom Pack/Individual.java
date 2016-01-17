public class Individual {

	private static CargoSpace cs;
	private static Package[] sArr;

	private int[] chromosome;
	private double fitness;

	public Individual(int[] chr) {
		this.chromosome = new int[chr.length];
		for (int i = 0; i < chr.length; i++) {
			this.chromosome[i] = chr[i];
		}
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
		for (int i = 0; i < this.chromosome.length; i++) {
				newCS.initialPosition(sArr[this.chromosome[i]]);
				if (!newCS.overlap(sArr[this.chromosome[i]])) {
						newCS.putPackage(sArr[this.chromosome[i]]);
						//System.out.println("Current fitness = " + this.fitness + ", now added = " + sArr[this.chromosome[i]].getValue());
				}
		}
		//System.out.println("Final fitness = " + this.fitness);
		//System.out.println();
		this.fitness = newCS.getTotalValue();
	}

	public void setFitness1() {
		CargoSpace newCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		for (int i = 0; i < this.chromosome.length; i++) {
				newCS.initialPosition(sArr[this.chromosome[i]]);
				if (!newCS.overlap(sArr[this.chromosome[i]])) {
						newCS.putPackage(sArr[this.chromosome[i]]);
				}
		}
		this.fitness = (double)((cs.getLength() * cs.getWidth() * cs.getHeight()) - newCS.getTotalGaps()) / (double)(cs.getLength() * cs.getWidth() * cs.getHeight());
	}

	public double getFitness() {
		return fitness;
	}

	public Individual clone() {
		int[] newChr = new int[chromosome.length];
		for (int i = 0; i < chromosome.length; i++) {
			newChr[i] = chromosome[i];
		}
		Individual newInd = new Individual(newChr);
		newInd.setFitness();
		return newInd;
	}

	public CargoSpace toCargoSpace() {
		CargoSpace newCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		for (int i = 0; i < chromosome.length; i++) {
				newCS.initialPosition(sArr[this.chromosome[i]]);
				if (!newCS.overlap(sArr[this.chromosome[i]]))
						newCS.putPackage(sArr[this.chromosome[i]]);
		}
		return newCS;
	}

	public static void setCargoSpace(CargoSpace cargoSpace) {
		cs = cargoSpace;
	}

	public static void setStatesArray(Package[] statesArray) {
		sArr = statesArray;
	}

}
