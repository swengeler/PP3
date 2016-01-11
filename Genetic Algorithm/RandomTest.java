public class RandomTest {

    public static void main(String[] args) {
        int[] randomList = Random.randomListWithRange(0, 10, 11);

        for (int i = 0; i < randomList.length; i++) {
            System.out.println(randomList[i]);
        }
    }

}
