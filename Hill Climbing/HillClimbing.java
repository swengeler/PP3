/**
 * Created by nicolagheza on 18/01/16.
 */
public class HillClimbing {

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

    public static void main(String args[]) {

        Package[] packageTypes = new Package[3];
        packageTypes[0] = new Package("A");
        packageTypes[1] = new Package("B");
        packageTypes[2] = new Package("C");
/*
        boolean done = false;
        boolean allowRotations = false;
        int mutationRate = 1;
        int nrNeighbours = 100;

        LocalSearch localSearch = new LocalSearch();
        CargoSpace current = new CargoSpace(33,5,8);
        CargoSpace[] neighbours;

        current = localSearch.genArbitrarySolution(current, packageTypes, allowRotations);
        System.out.println("Starting tot. value: " + current.getTotalValue());
        long startTime = System.currentTimeMillis();
        while (!done) {
            neighbours = localSearch.genNeighbourhood(current, packageTypes, allowRotations, nrNeighbours, mutationRate);
            if (neighbours != null) {
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
        System.out.println("Nunber of A: " + nrA + "\nNumber of B: " + nrB + "\nNumber of C: " + nrC);
        System.out.println("Runtime: " + totTime + "ms");
        System.out.println("========================================="); */

        double results[][] = new double[50][6];

        for (int t=0; t<50; t++) {

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
            results[t][0] = current.getTotalValue();
            results[t][1] = current.getTotalGaps();
            results[t][2] = nrA;
            results[t][3] = nrB;
            results[t][4] = nrC;
            results[t][5] = totTime;
        }
        double average[] = new double[6];
        for (int i=0; i<results.length; i++) {
            average[0] += results[i][0];
            average[1] += results[i][1];
            average[2] += results[i][2];
            average[3] += results[i][3];
            average[4] += results[i][4];
            average[5] += results[i][5];
        }
        System.out.println("Average Total Value: " + (average[0]/50));
        System.out.println("Average Number Gaps: " + (average[1]/50));
        System.out.println("Average A Parcels: " + (average[2]/50));
        System.out.println("Average B Parcels: " + (average[3]/50));
        System.out.println("Average C Parcels: " + (average[4]/50));
        System.out.println("Average Runtime: " + (average[5]/50));
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
        if (successors[0].getTotalValue(successors[0].getPacking()) <= curCargo.getTotalValue(curCargo.getPacking()))
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
