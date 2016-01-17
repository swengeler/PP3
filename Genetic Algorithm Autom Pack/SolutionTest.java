public class SolutionTest {

    public static void main(String[] args) {
        int highestValue = 0;
        for (int i = 1; i <= 83; i++) {
            for (int j = 1; j <= 55; j++) {
                for (int k = 1; k <= 50; k++) {
                    for (int l = 1; l <= 221; l++) {
                        if ((i * (4 * 2 * 2)) + (j * (2 * 3 * 4)) + (k * (3 * 3 * 3)) + (l * (1 * 2 * 3)) <= (33 * 5 * 8)) {
                            //System.out.println("A: " + i + ", B: " + j + ", C: " + k + ", total value: " + ((i * 3) + (j * 4) + (k * 5)));
                            if ((i * 3) + (j * 4) + (k * 5) + (l * 1) > highestValue)
                                highestValue = (i * 3) + (j * 4) + (k * 5) * (l * 1);
                        }
                      }
                }
            }
        }
        System.out.println("BEST VALUE: " + highestValue);
    }

}
