import java.awt.BorderLayout;

import javax.swing.JFrame;

public class HillClimbing {
	
	private CargoSpace curCargo;
	private Package[] packageTypes;
	
	/** Initialize the curCargo with an arbitrary solution **/
	public void init() {	
		packageTypes = new Package[3];
		packageTypes[0] = new Package("A");
		packageTypes[1] = new Package("B");
		packageTypes[2] = new Package("C");
		curCargo = new CargoSpace(33,5,8);
		curCargo.fillRandom(packageTypes,true);
	}
	
	/**
	 * Return the curCargo
	 * @return CargoSpace curCargo
	 */
	public CargoSpace getCurCargo() {
		return this.curCargo;
	}
	
	/**
	 * Set the curCargo
	 * @param newCargo a CargoSpace object representing the new cargo
	 */
	public void setCurCargo(CargoSpace newCargo) {
		this.curCargo = newCargo;
	}
	
	/**
	 * Generate the neighborhood removing n packages (where n is equals to mutationRate) from the curCargo 
	 * and trying to refill the remaining spaces with random parcels.
	 * @return CargoSpace[] array containing the generated neighbors. 
	 */
	public CargoSpace[] genNeighbourhood(int nr_neighbours, int mutationRate) {
		CargoSpace neighbour;
		CargoSpace[] neighbours = new CargoSpace[nr_neighbours];
		for (int i=0; i<nr_neighbours; i++) {
			neighbour = new CargoSpace(33,5,8);
			neighbour.fillCargoSpace(curCargo.getPacking());
			for (int j=0;j<mutationRate; j++) {
				int remIndex = Random.randomWithRange(0, neighbour.getPacking().length-1);
				neighbour.remove(neighbour.getPacking(),remIndex);
			}
			neighbour.fillRandom(packageTypes, true);
			neighbours[i] = neighbour;
		}
		return neighbours;
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
		
		HillClimbing localSearch = new HillClimbing();
		localSearch.init();
		int keepGoing = 100;
		double mutationRate = .15;
		int nrNeighbours = 100;
		System.out.println("Starting total value: " + localSearch.getCurCargo().getTotalValue());
		System.out.println("Total number of package placed: " + localSearch.getCurCargo().getPacking().length);
		CargoSpace[] successors;
		boolean end = false;
		long startTime = System.currentTimeMillis();
		while (!end) {
			successors = localSearch.genNeighbourhood(nrNeighbours,(int)(localSearch.getCurCargo().length*mutationRate));	
			HeapSort.sort(successors);
			if (successors[0].getTotalValue() <= localSearch.getCurCargo().getTotalValue()) {
				if (keepGoing == 0)
					end = true;
				keepGoing--;
			}
			else {
				localSearch.setCurCargo(successors[0]);
				keepGoing = 100;
				System.out.println("Current total value: " + localSearch.getCurCargo().getTotalValue());
				System.out.println("Total number of package placed: " + localSearch.getCurCargo().getPacking().length);

			}
		}
		long endTime = System.currentTimeMillis();
		long totTime = endTime - startTime; 
		System.out.println("Local maximum: " + localSearch.getCurCargo().getTotalValue() + " found in " + totTime + " ms");
		localSearch.displaySolution(localSearch.getCurCargo());
		
	}

}
