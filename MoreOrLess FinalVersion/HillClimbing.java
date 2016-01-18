package Poly3D;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class HillClimbing {
	
	private static long totTime;
	private static int nrA, nrB, nrC;
	private static CargoSpace current;
	
	/** genArbitrarySolution **/
	public CargoSpace genArbitrarySolution(CargoSpace current, Package[] packageTypes, boolean allowRotations) {
		CargoSpace arbitraryCargo = new CargoSpace(current.getLength(),current.getWidth(),current.getHeight());
		arbitraryCargo.fillRandom(packageTypes,allowRotations);
		return arbitraryCargo;
	}

	/**
	 * Generate the neighborhood removing n packages (where n is equals to mutationRate) from the curCargo
	 * and trying to refill the remaining spaces with random parcels.
	 * @return CargoSpace[] array containing the generated neighbors.
	 */
	public CargoSpace[] genNeighbourhood(CargoSpace current, Package[] packageTypes, boolean allowRotations, int nrNeighbours, int mutationRate) {
		CargoSpace neighbour;
		CargoSpace[] nextNeighbourhood = new CargoSpace[nrNeighbours];
		for (int i=0; i<nrNeighbours; i++) {
			neighbour = new CargoSpace(current.getLength(),current.getWidth(),current.getHeight());
			neighbour.fillCargoSpace(current.getPacking());
			for (int j=0;j<mutationRate; j++) {
				int remIndex = Random.randomWithRange(0, neighbour.getPacking().length-1);
				neighbour.remove(neighbour.getPacking(),remIndex);
			}
			neighbour.fillRandom(packageTypes, allowRotations);
			nextNeighbourhood[i] = neighbour;
		}

		nextNeighbourhood = sort_and_prume(nextNeighbourhood, current);

		return nextNeighbourhood;
	}

	public static void main(String[] args) {
		Package[] packageTypes = new Package[3];
		packageTypes[0] = new Package("A");
		packageTypes[1] = new Package("B");
		packageTypes[2] = new Package("C");

		boolean done = false;
		boolean allowRotations = true;
		int mutationRate = 1;
		int nrNeighbours = 100;

		HillClimbing localSearch = new HillClimbing();
		current = new CargoSpace(33,5,8);
		CargoSpace[] neighbours;
		current = localSearch.genArbitrarySolution(current, packageTypes, allowRotations);
		long startTime = System.currentTimeMillis();
		while (!done) {
			neighbours = localSearch.genNeighbourhood(current, packageTypes, allowRotations, nrNeighbours, mutationRate);
			if (neighbours != null) {
				//int random = Random.randomWithRange(0, neighbours.length-1);
				current = neighbours[0];
			} else {
					done = true;
			}
			System.out.println("Local max: " + current.getTotalValue());
			System.out.println("Gaps left: " + current.getTotalGaps());
			System.out.println("N of packages: " + current.getPacking().length);
			System.out.println("Number of A: " + nrA + "\n Number of B: " + nrB + "\n Number of C: " + nrC);
			System.out.println("Runtime: " + totTime + "ms");
			
		}
		long endTime = System.currentTimeMillis();
		totTime = endTime - startTime;

		nrA = 0;
		nrB = 0;
		nrC = 0;

		for (int i=0; i<current.getPacking().length; i++) {
				if (current.getPacking()[i].getType() == "A")
					nrA++;
				else if (current.getPacking()[i].getType() == "B")
					nrB++;
				else if (current.getPacking()[i].getType() == "C")
					nrC++;
		}

		System.out.println("Local max: " + current.getTotalValue());
		System.out.println("Gaps left: " + current.getTotalGaps());
		System.out.println("N of packages: " + current.getPacking().length);
		System.out.println("Number of A: " + nrA + "\n Number of B: " + nrB + "\n Number of C: " + nrC);
		System.out.println("Runtime: " + totTime + "ms");
		Display3D cs = new Display3D();
		cs.represent(current);

	}

	/**
	 * Static method used to sort and prume bad solutions from the successors array
	 * @param successors
	 * @param curCargo
	 * @return
	 */
	private static CargoSpace[] sort_and_prume(CargoSpace[] successors, CargoSpace curCargo) {
		HeapSort.sort(successors);
		for (int i=0; i<successors.length; i++) {
			if (successors[i].getTotalValue() <= curCargo.getTotalValue()) {
				successors = prume(successors, i);
				i=0;
			}
		}
		if (successors[0].getTotalValue() < curCargo.getTotalValue())
			return null;
		return successors;
	}

	/**
	 * Static method used to prume a solution from the successors (neighbors) array
	 * @param successors
	 * @param remIndex
	 * @return a new CargoSpace[] array withouth the solution at remIndex
	 */
	private static CargoSpace[] prume(CargoSpace[] successors, int remIndex) {
		CargoSpace[] newSuccessors = new CargoSpace[successors.length-1];
		for (int i=0; i<newSuccessors.length; i++) {
			if (i<remIndex)
				newSuccessors[i] = successors[i];
			else
				newSuccessors[i] = successors[i+1];
		}
		return newSuccessors;
	}
	
	public long getRuntime(){
		return totTime;
	}
	
	public int[] getNrPack(){
		return new int[]{nrA,nrB,nrC};
	}
	
	public int getGaps(){
		return current.getTotalGaps();
	}

}
