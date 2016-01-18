public class SolutionTest {
    public static void main(String[] args) {
        int bestValue = 0;
        for (int i = 0; i < 66; i++) {
            for (int j = 0; j < 147; j++) {
                for (int k = 0; k < 28; k++) {
                    //for (int l = 0; l < 221; l++) {
                        if (i * 20 + j * 9 + k * 48 <= 1320) {
                            if (i   + j  + k   > bestValue) {
                                bestValue = i  + j  + k ;
                            }
                        }
                    //}
                }
            }
        }
        System.out.println("Best value: " + bestValue);
    }
}
