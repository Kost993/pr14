import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    static final int MAX_RECORDS = 50;
    static String[] dates = new String[MAX_RECORDS];
    static String[] entries = new String[MAX_RECORDS];
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Створити новий щоденник");
        System.out.println("2. Відновити щоденник з файлу");
        System.out.print("Ваш вибір: ");
        String initChoice = scanner.nextLine();

        if (initChoice.equals("2")) {
            System.out.print("Введіть шлях до файлу: ");
            String filePath = scanner.nextLine();
            loadDiary(filePath);
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=== Мій щоденник ===");
            System.out.println("1. Додати запис");
            System.out.println("2. Видалити запис");
            System.out.println("3. Переглянути усі записи");
            System.out.println("4. Вийти");
            System.out.print("Оберіть опцію: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                addEntry(scanner);
            } else if (choice.equals("2")) {
                deleteEntry(scanner);
            } else if (choice.equals("3")) {
                viewEntries();
            } else if (choice.equals("4")) {
                System.out.print("Бажаєте зберегти щоденник? (так/ні): ");
                String saveChoice = scanner.nextLine();
                if (saveChoice.equalsIgnoreCase("так")) {
                    System.out.print("Введіть шлях до файлу для збереження: ");
                    String filePath = scanner.nextLine();
                    saveDiary(filePath);
                }
                running = false;
            } else {
                System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }

        scanner.close();
    }

    static void addEntry(Scanner scanner) {
        LocalDateTime now = LocalDateTime.now();
        String date = FORMATTER.format(now);

        System.out.println("Введіть запис: ");
        String text = "";
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("")) break;
            text += line + "\n";
        }

        for (int i = 0; i < MAX_RECORDS; i++) {
            if (dates[i] == null) {
                dates[i] = date;
                entries[i] = text;
                System.out.println("Запис додано.");
                return;
            }
        }

        System.out.println("Щоденник переповнений.");
    }

    static void deleteEntry(Scanner scanner) {
        System.out.print("Введіть дату і час запису: ");
        String date = scanner.nextLine();

        for (int i = 0; i < MAX_RECORDS; i++) {
            if (date.equals(dates[i])) {
                dates[i] = null;
                entries[i] = null;
                System.out.println("Запис видалено.");
                return;
            }
        }

        System.out.println("Запис з такою датою не знайдено.");
    }

    static void viewEntries() {
        boolean empty = true;
        for (int i = 0; i < MAX_RECORDS; i++) {
            if (dates[i] != null) {
                System.out.println("Дата і час: " + dates[i]);
                System.out.println("Запис:\n" + entries[i]);
                System.out.println("----------------------");
                empty = false;
            }
        }

        if (empty) {
            System.out.println("Щоденник порожній.");
        }
    }

    static void saveDiary(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (int i = 0; i < MAX_RECORDS; i++) {
                if (dates[i] != null) {
                    writer.println(dates[i]);
                    writer.print(entries[i]);
                    writer.println();
                }
            }
            System.out.println("Щоденник збережено.");
        } catch (IOException e) {
            System.out.println("Помилка збереження файлу: " + e.getMessage());
        }
    }

    static void loadDiary(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < MAX_RECORDS) {
                String date = line;
                String text = "";
                while ((line = reader.readLine()) != null && !line.equals("")) {
                    text += line + "\n";
                }
                dates[index] = date;
                entries[index] = text;
                index++;
            }
            System.out.println("Щоденник відновлено з файлу.");
        } catch (IOException e) {
            System.out.println("Помилка зчитування файлу: " + e.getMessage());
        }
    }
}
