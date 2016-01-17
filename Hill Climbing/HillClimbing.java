import java.awt.BorderLayout;
import javax.swing.JFrame;

public class HillClimbing {

	/** genArbitrarySolution **/
	public CargoSpace genArbitrarySolution(CargoSpace current, Package[] packageTypes, boolean allowRotations) {
		CargoSpace arbitraryCargo = new CargoSpace(current.length,current.width,current.height);
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
			neighbour = new CargoSpace(current.length,current.width,current.height);
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

	/**
	 * Display the solution
	 * TO BE CHANGED FOR THE 3D REPRESENTATION
	 **/
	public void displaySolution(CargoSpace curCargo) {
        JFrame f = new JFrame();
        f.setSize(750, 770);
        Display display = new Display(curCargo.getArray());
        f.add(display, BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
  }

	public static void main(String[] args) {

		Package[] packageTypes = new Package[2];
		packageTypes[0] = new Package("A");
		packageTypes[1] = new Package("C");
		//packageTypes[2] = new Package("C");

		boolean done = false;
		boolean allowRotations = false;
		int mutationRate = 1;
		int nrNeighbours = 100;

		HillClimbing localSearch = new HillClimbing();
		CargoSpace current = new CargoSpace(33,5,8);
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
		}
		long endTime = System.currentTimeMillis();
		long totTime = endTime - startTime;

		int nrA = 0;
		int nrB = 0;
		int nrC = 0;

		for (int i=0; i<current.getPacking().length; i++) {
				if (current.getPacking()[i].getType() == "A")
					nrA++;
				else if (current.getPacking()[i].getType() == "B")
					nrB++;
				else if (current.getPacking()[i].getType() == "C")
					nrC++;
		}

		System.out.println("Local max: " + current.getTotalValue(current.getPacking()));
		System.out.println("Gaps left: " + current.getTotalGaps());
		System.out.println("N of packages: " + current.getPacking().length);
		System.out.println("Nunber of A: " + nrA + "\n Number of B: " + nrB + "\n Number of C: " + nrC);
		System.out.println("Runtime: " + totTime + "ms");
		localSearch.displaySolution(current);

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
			if (successors[i].getTotalValue(successors[i].getPacking()) <= curCargo.getTotalValue(curCargo.getPacking())) {
				successors = prume(successors, i);
				i=0;
			}
		}
		if (successors[0].getTotalValue(successors[0].getPacking()) < curCargo.getTotalValue(curCargo.getPacking()))
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



}
