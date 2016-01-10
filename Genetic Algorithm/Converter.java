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
                        int newX = (int) ((k - nrDone) / csToFill.getLength());
                        int restX = (k - nrDone) % csToFill.getLength();
                        int newY = (int) (restX / csToFill.getWidth());
                        int restY = restX % csToFill.getWidth();
                        int newZ = restY % csToFill.getHeight();
                        System.out.println("x = " + newX + ", y = " + newY + ", z = " + newZ);
                        p.setBaseCoords(newX, newY, newZ);
                        if (nrStates.length == 4 && p.getLength() == p.getWidth()) {
                            if (j >= 2)
                                p.rotateY();
                            if (j == 3)
                                p.rotateZ();
                        } else if (nrStates.length == 4 && p.getLength() == p.getHeight()) {
                            if (j >= 2)
                                p.rotateX();
                            if (j == 3)
                                p.rotateY();
                        } else if (nrStates.length == 4 && p.getWidth() == p.getHeight()) {
                            if (j >= 2)
                                p.rotateY();
                            if (j == 3)
                                p.rotateX();
                        } else if (nrStates.length == 7) {
                            if (j >= 2)
                                p.rotateX();
                            if (j >= 3)
                                p.rotateY();
                            if (j >= 4)
                                p.rotateZ();
                            if (j >= 5)
                                p.rotateX();
                            if (j == 6)
                                p.rotateY();
                        }
                        if (!csToFill.overlap(p))
                            csToFill.place(p);
                    }
                }
                nrDone += nrStates[j];
            }
            nrDone += nrStates[0];
        }
        return csToFill;
    }

}
