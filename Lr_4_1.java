import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Lr_4_1 {

    public static void main(String[] args) {
        // Асинхронно генеруємо 3x3 матрицю
        CompletableFuture<int[][]> generateMatrix = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            int[][] matrix = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    matrix[i][j] = ThreadLocalRandom.current().nextInt(1, 101);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Матриця згенерована за " + (end - start) + " мс.");
            return matrix;
        });

        // Асинхронно виводимо згенеровану матрицю
        CompletableFuture<int[][]> printMatrix = generateMatrix.thenApplyAsync(matrix -> {
            long start = System.currentTimeMillis();
            System.out.println("Згенерована матриця:");
            for (int[] row : matrix) {
                System.out.println(Arrays.toString(row));
            }
            long end = System.currentTimeMillis();
            System.out.println("Матриця виведена за " + (end - start) + " мс.");
            return matrix;
        });

        // Асинхронно виводимо кожен стовпчик матриці
        CompletableFuture<Void> printColumns = printMatrix.thenAcceptAsync(matrix -> {
            long start = System.currentTimeMillis();
            for (int col = 0; col < 3; col++) {
                StringBuilder columnOutput = new StringBuilder("Стовпчик " + (col + 1) + ": ");
                for (int row = 0; row < 3; row++) {
                    columnOutput.append(matrix[row][col]).append(", ");
                }
                // Видаляємо зайву кому та пробіл
                System.out.println(columnOutput.substring(0, columnOutput.length() - 2));
            }
            long end = System.currentTimeMillis();
            System.out.println("Стовпчики виведені за " + (end - start) + " мс.");
        });

        // Демонстрація роботи thenRunAsync()
        CompletableFuture<Void> completionTask = printColumns.thenRunAsync(() -> {
            long start = System.currentTimeMillis();
            System.out.println("Усі задачі завершені! Виконуємо фінальне завдання...");
            System.out.println("Фінальне завдання: очищення ресурсів та завершення роботи.");
            long end = System.currentTimeMillis();
            System.out.println("Фінальне завдання виконано за " + (end - start) + " мс.");
        });

        // Чекаємо завершення усіх асинхронних задач
        try {
            completionTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
