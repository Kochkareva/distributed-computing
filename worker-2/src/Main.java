import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String inputFile = "/var/result/data.txt";
        String outputFile = "/var/result/result.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split("\\s+");

                if (numbers.length > 0) {
                    int firstNumber = Integer.parseInt(numbers[0]);
                    int lastNumber = Integer.parseInt(numbers[numbers.length - 1]);
                    int result = firstNumber * lastNumber;
                    System.out.println("Приложение 2: Результат умножения первого и последнего числа: " + result);
                    writer.write(String.valueOf(result));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}