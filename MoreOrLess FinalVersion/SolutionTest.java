public class SolutionTest {
    public static void main(String[] args) {
        int bestValue = 0;
        //for (int i = 0; i <= 83; i++) {
            //for (int j = 0; j <= 55; j++) {
                //for (int k = 0; k <= 50; k++) {
                    for (int l = 0; l <= 165; l++) {
                        for (int m = 0; m <= 50; m++) {
                            for (int n = 0; n <= 21; n++) {
                                if ((/*i * 16 + j * 24 + k * 27 + */l * 8 + m * 27 + n * 64) <= 1320) {
                                    if ((/*i * 3  + j * 4 + k * 5 +*/  l + m + n) > bestValue) {
                                        bestValue = /*i * 3 + j * 4 + k * 5 +*/ l + m + n;
                                    }
                                }
                            }
                        }
                    }
                //}
            //}
        //}
        System.out.println("Best value: " + bestValue);
    }
}
