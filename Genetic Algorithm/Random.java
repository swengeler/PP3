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
}
