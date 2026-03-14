package ejercicio;

public class Main {
    
    public static void main(String[] args) {
        int[] example = {1, 2,3, 4, 5};
        int result = score(example);
        System.out.println("Score for example array: " + result);
    }

    // Returns +1 for each even number (0 counted as even), +3 for each odd number except 5, and +5 for each 5
    public static int score(int[] nums) {
        if (nums == null) return 0;
        int total = 0;
        for (int n : nums) {
            if (n == 5) {
                total += 5;
            } else if (n % 2 == 0) {
                // even (0 included)
                total += 1;
            } else {
                // odd and not 5
                total += 3;
            }
        }
        return total;
    }
}