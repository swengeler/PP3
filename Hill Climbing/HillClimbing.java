import java.awt.BorderLayout;

import javax.swing.JFrame;

public class HillClimbing {
	
	private CargoSpace curCargo;
	private Package[] packageTypes;
	
	public void init() {	
		packageTypes = new Package[3];
		packageTypes[0] = new Package("A");
		packageTypes[1] = new Package("B");
		packageTypes[2] = new Package("C");
		curCargo = new CargoSpace(33,5,8);
		curCargo.fillRandom(packageTypes);
	}
	
	/**
	 * 
	 * @return
	 */
	public CargoSpace[] genNeighbourhood() {
		final int NR_NEIGHBOURS = 100;
		CargoSpace[] neighbours = new CargoSpace[NR_NEIGHBOURS];
		for (int i=0; i<100; i++) {
			CargoSpace neighbour = new CargoSpace(33,5,8);
			neighbour.fillCargoSpace(curCargo.getPacking());
			neighbour.remove(neighbour.getPacking()[Random.randomWithRange(0, neighbour.getPacking().length-1)]);
			neighbour.fillRandom(packageTypes);
			neighbours[i] = neighbour;
		}
		return neighbours;
	}
	
	
	public void displaySolution(CargoSpace curCargo) {
		System.out.println("Total value: " + curCargo.getTotalValue());
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
		localSearch.displaySolution(localSearch.curCargo);
		localSearch.genNeighbourhood();
	}

}
