package Poly3D;

public class SolutionTest {
    public static void main(String[] args) {
        int bestValue = 0;
        for (int i = 0; i <= 15; i++) {
            for (int j = 0; j <= 15; j++) {
                for (int k = 0; k <= 15; k++) {
                    for (int l = 0; l <= 15; l++) {
                        //for (int m = 0; m <= 15; m++) {
                            if ((i * 16 + j * 24 + k * 27 + l * 16) <= 1320) {
                                if ((i * 3  + j * 4 + k * 5  + l) > bestValue) {
                                    bestValue = i * 3 + j * 4 + k * 5 + l;
                                }
                            }
                        //}
                    }
                }
            }
        }
        System.out.println("Best value: " + bestValue);
    }
}
