public class Converter {

    private static final boolean DEBUG_CTCS = false;

    public static CargoSpace chromosomeToCargoSpace(int[] chr, Package[] types, CargoSpace csToFill) {
        int[] nrStates;
        int nrDone = 0;
        Package p;
        for (int i = 0; i < types.length; i++) {
            nrStates = types[i].getNrStates(csToFill.getLength(), csToFill.getWidth(), csToFill.getHeight());
            if (DEBUG_CTCS) System.out.println(types[i].getType() + " nrStates = " + nrStates[0]);
            for (int j = 1; j < nrStates.length; j++) {
                for (int k = nrDone; k < nrStates[j] + nrDone; k++) {
                    if (chr[k] == 1) {
                        p = new Package(types[i].getType());
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
                        int newX = (int) ((double)(k - nrDone) / (double)((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1)));
                        if (DEBUG_CTCS) System.out.println("Weird expression = " + ((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1)));
                        if (DEBUG_CTCS) System.out.println("k - nrDone = " + (k - nrDone));
                        int restX = (k - nrDone) % ((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1));
                        if (DEBUG_CTCS) System.out.println("restX = " + restX);
                        int newY = (int) (restX / (csToFill.getHeight() - p.getHeight() + 1));
                        int newZ = restX % (csToFill.getHeight() - p.getHeight() + 1);
                        if (DEBUG_CTCS) System.out.println("x = " + newX + ", y = " + newY + ", z = " + newZ);
                        p.setBaseCoords(newX, newY, newZ);
                        if (!csToFill.overlap(p))
                            csToFill.place(p);
                    }
                }
                nrDone += nrStates[j];
            }
        }
        return csToFill;
    }

    public static Package[] chromosomeToPacking(int[] chr, Package[] types, CargoSpace csToFill) {
        int[] nrStates;
        Package[] packing = new Package[0];
        Package[] newPacking = new Package[0];
        int nrDone = 0;
        Package p;
        for (int i = 0; i < types.length; i++) {
            nrStates = types[i].getNrStates(csToFill.getLength(), csToFill.getWidth(), csToFill.getHeight());
            if (DEBUG_CTCS) System.out.println(types[i].getType() + " nrStates = " + nrStates[0]);
            for (int j = 1; j < nrStates.length; j++) {
                for (int k = nrDone; k < nrStates[j] + nrDone; k++) {
                    if (chr[k] == 1) {
                        newPacking = new Package[packing.length + 1];
                        System.arraycopy(packing, 0, newPacking, 0, packing.length);
                        
                        p = new Package(types[i].getType());
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
                        int newX = (int) ((double)(k - nrDone) / (double)((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1)));
                        if (DEBUG_CTCS) System.out.println("Weird expression = " + ((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1)));
                        if (DEBUG_CTCS) System.out.println("k - nrDone = " + (k - nrDone));
                        int restX = (k - nrDone) % ((csToFill.getWidth() - p.getWidth() + 1) * (csToFill.getHeight() - p.getHeight() + 1));
                        if (DEBUG_CTCS) System.out.println("restX = " + restX);
                        int newY = (int) (restX / (csToFill.getHeight() - p.getHeight() + 1));
                        int newZ = restX % (csToFill.getHeight() - p.getHeight() + 1);
                        if (DEBUG_CTCS) System.out.println("x = " + newX + ", y = " + newY + ", z = " + newZ);
                        p.setBaseCoords(newX, newY, newZ);

                        newPacking[newPacking.length - 1] = p;
                        packing = newPacking;
                    }
                }
                nrDone += nrStates[j];
            }
        }
        return packing;
    }

    public static int[] packingToChromosome(Package[] packing) {
        return null;
    }

}
