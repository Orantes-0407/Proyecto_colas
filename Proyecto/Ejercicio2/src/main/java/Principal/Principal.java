package Principal;

public class Principal {

    public static int[] secondMinMax(int[] numbers) {

        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;

        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;

        for (int num : numbers) {

            // Segundo menor
            if (num < min) {
                secondMin = min;
                min = num;
            } else if (num > min && num < secondMin) {
                secondMin = num;
            }

            // Segundo mayor
            if (num > max) {
                secondMax = max;
                max = num;
            } else if (num < max && num > secondMax) {
                secondMax = num;
            }
        }

        return new int[]{secondMin, secondMax};
    }

    public static void main(String[] args) {
        int[] numbers = {8, 3, 5, 1, 9, 7};

        int[] result = secondMinMax(numbers);

        System.out.println("Segundo menor: " + result[0]);
        System.out.println("Segundo mayor: " + result[1]);
    }
}
