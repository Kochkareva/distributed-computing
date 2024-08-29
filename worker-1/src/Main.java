import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        File dataDir = new File("/var/data");
        File resultDir = new File("/var/result");
        // Получаем все файлы в каталоге
        File[] files = dataDir.listFiles();
        if (files != null && files.length > 0) {
            // Ищем файл с самым коротким названием
            File shortestFileName = files[0];
            for (File file : files) {
                if (file.getName().length() < shortestFileName.getName().length()) {
                    shortestFileName = file;
                }
            }
            // Перекладываем файл
            File destination = new File(resultDir, "data.txt");
            try {
                Files.move(shortestFileName.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Приложение 1: Файл успешно перемещен.");
            } catch (IOException e) {
                System.out.println("Приложение 1: Ошибка при перемещении файла: " + e.getMessage());
            }
        } else {
            System.out.println("Приложение 1: В каталоге /var/data нет файлов.");
        }
    }
}