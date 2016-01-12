public class HeapTest {

    public static void main(String[] args) {

        int[] i = new int[10];
        for (int j = 0; j < i.length; j++)
            i[j] = 10 - j;
        HeapSort.sort(i);

        for (int j = 0; j < i.length; j++)
            System.out.println("i[" + j + "] = " + i[j]);
    }

}
