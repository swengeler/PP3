package Poly3D;

/**
* A class containing methods used to generate random integers.
*
* @author Nicola Gheza (08/01/16)
* @author Simon Wengeler
*/
public class Random {
    /**
    * A method that returns a random number (int) within the range (min, max).
    *
    * @param min The minimum value that can be generated.
    * @param max The maxiumum value that can be generated.
    * @return random Random number the designated range.
    */
    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
    * A method that generates a list of a certain amount of random numbers between
    * min and max. It also makes sure that none of the number repeat.
    *
    * @param min The minimum value that can be generated.
    * @param max The maximum value that can be generated.
    * @param number The amount of numbers that should be generated.
    * @return list An array containing all the random numbers.
    */
    public static int[] randomListWithRange(int min, int max, int number) {
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
