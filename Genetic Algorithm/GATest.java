public class GATest {

    public static void main(String[] args) {
      
        int[][] chromosomes = new int[500][15000];
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = 0; j < chromosomes[i].length; j++) {
                if (Math.random() < 0.5)
                    chromosomes[i][j] = 0;
                else
                    chromosomes[i][j] = 1;
            }
        }

        int[][] nextGeneration = new int[500][15000];
        for (int i = nextGeneration.length - 1; i >= 0; i--) {
            for (int j = nextGeneration[i].length - 1; j >= 0; j--) {
                nextGeneration[nextGeneration.length - 1 - i][nextGeneration[i].length - 1 - j] = chromosomes[i][j];
            }
        }

    }

}
