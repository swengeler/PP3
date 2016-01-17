public class SolutionTest {

    public static void main(String[] args) {
        for (int i = 1; i <= 83; i++) {
            for (int j = 1; j <= 55; j++) {
                for (int k = 1; k <= 50; k++) {
                    if ((i * (4 * 2 * 2)) + (j * (2 * 3 * 4)) + (k * (3 * 3 * 3)) <= (33 * 5 * 8))
                        System.out.println("A: " + i + ", B: " + j + ", C: " + k + ", total volume: " + ((i * (4 * 2 * 2)) + (j * (2 * 3 * 4)) + (k * (3 * 3 * 3))));
                }
            }
        }
    }

}
