public class SolutionTest {

    public static void main(String[] args) {
        int highestValue = 0;
        int thenA = 0;
        int thenB = 0;
        int thenC = 0;
        int thenD = 0;
        for (int i = 0; i <= 66; i++) {
            for (int j = 0; j <= 147; j++) {
                for (int k = 0; k <= 28; k++) {
                    //for (int l = 0; l <= 83; l++) {
                        //for (int m = 1; m <= 55; m++) {
                            //for (int n = 1; n <= 44; n++) {
                                if ((i * 20) + (j * 9) + (k * 48) /*+ (l * 16) + (m * 24) + (n * 30)*/ <= (33 * 5 * 8)) {
                                    //System.out.println("A: " + i + ", B: " + j + ", C: " + k + ", total value: " + ((i * 3) + (j * 4) + (k * 5)));
                                    if ((i * 1) + (j * 1) + (k * 1)/* + l + m + n*/ > highestValue) {
                                        highestValue = (i * 1) + (j * 1) + (k * 1)/* + l + m + n*/;
                                        thenA = i;
                                        thenB = j;
                                        thenC = k;
                                        //thenD = l;
                                    }
                                }
                            //}
                        //}
                    //}
                }
            }
        }
        System.out.println("BEST VALUE: " + highestValue);
    }

}
