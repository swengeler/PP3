public class Converter {

    public static CargoSpace chromosomeToCargoSpace(int[] chr, Package[] types, CargoSpace csToFill) {
        int[] nrStates;
        int nrDone = 0;
        Package p;
        for (int i = 0; i < types.length; i++) {
            nrStates = types[i].getNrStates(csToFill.getLength(), csToFill.getWidth(), csToFill.getHeight());
            for (int j = 1; j < nrStates.length; j++) {
                for (int k = nrDone; k < nrStates[j] + nrDone; k++) {
                    if (chr[k] == 1) {
                        p = new Package(types[i].getType());
                        if (nrStates.length == 2) {
                            newX = (int) (k / csToFill.getLength());
                            rX = k % csToFill.getLength();
                            newY = (int) (rX / csToFill.getWidth())
                            newZ = rX % csToFill.getLength();
                        }
                    }
                }
            }
            nrDone += nrStates[0];
        }
    }

}
