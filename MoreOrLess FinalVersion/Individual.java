package Poly3D;

/**
* A class that is used in a genetic algorithm to comprise all of the important information about
* an individual that is part of the population used in the genetic algorithm.
*
* @author Simon Wengeler
*/

public class Individual {

	/**
	* The CargoSpace object which the packages encoded by a chromosome have to fit into. It is primarily
	* used in the methdo setFitness.
	*/
	private static CargoSpace cs;
	/**
	* Similar to the CargoSpace object, an array of packages that specifies how to interpret the individual's
	* chromosome.
	*/
	private static Package[] sArr;

	/**
	* An array containing index numbers that can be interpreted as a certain type of package (to be placed in
	* a cargo space).
	*/
	private int[] chromosome;
	/**
	* A variable used to store the fitness of an individual set by the method setFitness.
	*/
	private double fitness;

	/**
	* The constructor to create a new individual using a certain chromosome. The fitness of that individual is
	* immediately evaluated and saved in the constructor.
	*
	* @param chr An integer array representing a chromosome.
	*/
	public Individual(int[] chr) {
		this.chromosome = new int[chr.length];
		for (int i = 0; i < chr.length; i++) {
			this.chromosome[i] = chr[i];
		}
		this.setFitness();
	}

	/**
	* A method to access the information stored in the individual's chromosome.
	*
	* @return chromosome The chromosome holding the defining information about the particular individual.
	*/
	public int[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(int[] chr) {
		this.chromosome = chr;
		this.getFitness();
	}

	/**
	* A method to evaluate the fitness of an individual by interpreting its chromosome, filling a cargo space
	* and then setting the individual's fitness value to the total value of the packing of that cargo space.
	*/
	public void setFitness() {
		CargoSpace newCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		for (int i = 0; i < this.chromosome.length; i++) {
				newCS.initialPosition(sArr[this.chromosome[i]]);
				if (!newCS.overlap(sArr[this.chromosome[i]])) {
						newCS.putPackage(sArr[this.chromosome[i]]);
				}
		}
		this.fitness = 1320 - newCS.getTotalGaps();
	}

	/**
	* A method to get information about an individual's fitness.
	*
	* @return fitness The individual fitness value.
	*/
	public double getFitness() {
		return fitness;
	}

	/**
	* A method to clone an individual (i.e. actually create a completely new object instead of copying the
	* reference).
	*
	* @return newInd The clone of the individual.
	*/
	public Individual clone() {
		int[] newChr = new int[chromosome.length];
		for (int i = 0; i < chromosome.length; i++) {
			newChr[i] = chromosome[i];
		}
		Individual newInd = new Individual(newChr);
		newInd.setFitness();
		return newInd;
	}

	/**
	* A method to create the "phenotype" of the individual. It interprets the individual chromosome to
	* construct and fill a CargoSpace object.
	*
	* @return newCS The filled cargo space with a packing achieved by the interpretation of the individual's
	* 							chromosome.
	*/
	public CargoSpace toCargoSpace() {
		CargoSpace newCS = new CargoSpace(cs.getLength(), cs.getWidth(), cs.getHeight());
		for (int i = 0; i < chromosome.length; i++) {
				newCS.initialPosition(sArr[this.chromosome[i]]);
				if (!newCS.overlap(sArr[this.chromosome[i]]))
						newCS.putPackage(sArr[this.chromosome[i]]);
		}
		return newCS;
	}

	/**
	* A method to define the global CargoSpace object used by the class Individual.
	*
	* @param cargoSpace The CargoSpace object defined for all instances of the class Individual.
	*/
	public static void setCargoSpace(CargoSpace cargoSpace) {
		cs = cargoSpace;
	}

	/**
	* A method to define the global array of Package objects used to interpret chromosomes.
	*
	* @param statesArray An array of Package objects in certain states.
	*/
	public static void setStatesArray(Package[] statesArray) {
		sArr = statesArray;
	}

}
