/**
 * Created by nicolagheza on 08/01/16.
 */
public class Random {
    /**
     * Return a random int number with range (min, max)
     * @param min
     * @param max
     * @return random int number in range (min, max)
     */
    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public static int randomWithRangeNN(int min, int max) {
        int range = (max - min) + 1;
        int random = (int)(Math.random() * range) + min;
        if (random < 0) {
            random = (int)(Math.random() * range) + min;
        }
        return random;
    }

    public static int[] randomListWithRange(int min, int max, int number) throws BadInputException {
        if (number > (max - min + 1))
            throw new BadInputException("The amount of unrepeated random numbers cannot exceed the range given for them.");
        int[] list = new int[number];
        list[0] = Random.randomWithRange(min, max);
        for (int i = 1; i < list.length; i++) {
            boolean allowed = false;
            while (!allowed) {
                list[i] = Random.randomWithRange(min, max);
                boolean noRep = true;
                for (int j = 0; j < i && noRep; j++) {
                    if (list[i] == list [j])
                        noRep = false;
                }
                if (noRep == true)
                    allowed = true;
            }
        }
        return list;
    }
}
