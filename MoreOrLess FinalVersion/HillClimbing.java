/**
 * Created by nicolagheza on 18/01/16.
 */
public class HillClimbing {
	
	private long totTime = 0;
	private CargoSpace current;
	
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
	public int getNrPack(){
		return current.getPacking().length;
	}

	/**
     * A Method to get the gaps wich are left
     *
     * @return int number of gaps left
     */
	public int getGaps(){
		return current.getTotalGaps();
	} 

    /**
     * Infinite supply
     * Return an arbitrary solution for the Algorithm
     * @param current CargoSpace
     * @param packageTypes Package[]
     * @param allowRotations boolean
     * @return CargoSpace arbitrary solution used from the algorithm
     */
    public CargoSpace genArbitrarySolution(CargoSpace current, Package[] packageTypes, boolean allowRotations) {
        CargoSpace arbitraryCargo = new CargoSpace(current.getLength(),current.getWidth(),current.getHeight());
        arbitraryCargo.fillRandom(packageTypes,allowRotations);
        return arbitraryCargo;
    }

    /**
     * Finite supply
     * Return an arbitrary solution for the Algorithm
     * @param current CargoSpace
     * @param packageTypes Package[]
     * @param allowRotations boolean
     * @return CargoSpace arbitrary solution used from the algorithm
     */
    public CargoSpace genArbitrarySolution(CargoSpace current, Package[] packageTypes,int[] packageTNumber, boolean allowRotations) {
        CargoSpace arbitraryCargo = new CargoSpace(current.getLength(),current.getWidth(),current.getHeight());
        arbitraryCargo.fillRandom(packageTypes,packageTNumber,allowRotations);
        return arbitraryCargo;
    }

    /**
     * Infinite supply
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

    /**
     * Finite supply
     * Generate the neighborhood removing n packages (where n is equals to mutationRate) from the curCargo
     * and trying to refill the remaining spaces with random parcels.
     * @return CargoSpace[] array containing the generated neighbors.
     */
    public CargoSpace[] genNeighbourhood(CargoSpace current, Package[] packageTypes, int[] packageTNumber,boolean allowRotations, int nrNeighbours, int mutationRate) {
        CargoSpace neighbour;
        CargoSpace[] nextNeighbourhood = new CargoSpace[nrNeighbours];
        for (int i=0; i<nrNeighbours; i++) {
            neighbour = new CargoSpace(current.getLength(),current.getWidth(),current.getHeight());
            neighbour.fillCargoSpace(current.getPacking());
            for (int j=0;j<mutationRate; j++) {
                int remIndex = Random.randomWithRange(0, neighbour.getPacking().length-1);

                neighbour.remove(neighbour.getPacking(),remIndex);
            }
            neighbour.fillRandom(packageTypes, packageTNumber,allowRotations);
            nextNeighbourhood[i] = neighbour;
        }

        nextNeighbourhood = sort_and_prume(nextNeighbourhood, current);

        return nextNeighbourhood;
    }

    public void run(Package[] packageTypes, int[] packageTNumber) {

        boolean done = false;
        boolean allowRotations = false;
        int mutationRate = 1;
        int nrNeighbours = 250;

        HillClimbing localSearch = new HillClimbing();
        CargoSpace current = new CargoSpace(33,5,8);
        CargoSpace[] neighbours;

        current = localSearch.genArbitrarySolution(current, packageTypes, packageTNumber,allowRotations);
        long startTime = System.currentTimeMillis();
        while (!done) {
            neighbours = localSearch.genNeighbourhood(current, packageTypes, packageTNumber,allowRotations, nrNeighbours, mutationRate);
            if (neighbours != null) {
                current = neighbours[0];
            } else {
                done = true;
            }
        }
        long endTime = System.currentTimeMillis();
        totTime = endTime - startTime;

        this.current = current;
        Display3D.represent(current);

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
        if (successors.length == 1 && successors[0].getTotalValue(successors[0].getPacking()) <= curCargo.getTotalValue(curCargo.getPacking()))
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
