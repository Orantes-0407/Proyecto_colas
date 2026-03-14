package ejercicio;

public class Main {
    
    public static void main(String[] args) {
        int[] example = {1, 2,3, 4, 5};
        int result = score(example);
        System.out.println("Score for example array: " + result);
    }

  
    public static int score(int[] nums) {
        if (nums == null) return 0;
        int total = 0;
        for (int n : nums) {
            if (n == 5) {
                total += 5;
            } else if (n % 2 == 0) {
              
                total += 1;
            } else {
           
                total += 3;
            }
        }
        return total;
    }
}
//Justificacion de respuesta:Solo se usan un número fijo de variables primitivas (total y la variable de iteración). No se crean estructuras auxiliares cuyo tamaño dependa de n (el arreglo de entrada no se cuenta como espacio adicional).