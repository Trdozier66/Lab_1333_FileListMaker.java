import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().toUpperCase();

            try {
                switch (choice) {
                    case "A" -> addItem(scanner);
                    case "D" -> deleteItem(scanner);
                    case "I" -> insertItem(scanner);
                    case "M" -> moveItem(scanner);
                    case "O" -> openFile(scanner);
                    case "S" -> saveFile(scanner);
                    case "C" -> clearList();
                    case "V" -> viewList();
                    case "Q" -> running = quitProgram(scanner);
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (IOException e) {
                System.out.println("File operation failed: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("""
                A - Add an item to the list
                D - Delete an item from the list
                I - Insert an item into the list
                M - Move an item
                O - Open a list file
                S - Save the current list
                C - Clear the list
                V - View the list
                Q - Quit
                """);
    }

    private static void addItem(Scanner scanner) {
        System.out.println("Enter the item to add:");
        String item = scanner.nextLine();
        list.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner scanner) {
        viewList();
        System.out.println("Enter the index of the item to delete:");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index < list.size()) {
                list.remove(index);
                needsToBeSaved = true;
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void insertItem(Scanner scanner) {
        viewList();
        System.out.println("Enter the index to insert the item at:");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index >= 0 && index <= list.size()) {
                System.out.println("Enter the item to insert:");
                String item = scanner.nextLine();
                list.add(index, item);
                needsToBeSaved = true;
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void moveItem(Scanner scanner) {
        viewList();
        System.out.println("Enter the index of the item to move:");
        try {
            int fromIndex = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the new index for the item:");
            int toIndex = Integer.parseInt(scanner.nextLine());
            if (fromIndex >= 0 && fromIndex < list.size() && toIndex >= 0 && toIndex <= list.size()) {
                String item = list.remove(fromIndex);
                list.add(toIndex, item);
                needsToBeSaved = true;
            } else {
                System.out.println("Invalid index.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private static void openFile(Scanner scanner) throws IOException {
        if (needsToBeSaved) {
            System.out.println("You have unsaved changes. Save before loading a new file? (Y/N)");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                saveFile(scanner);
            }
        }
        System.out.println("Enter the file name to load (without extension):");
        String filename = scanner.nextLine() + ".txt";
        Path filePath = Paths.get(filename);

        if (Files.exists(filePath)) {
            list = Files.readAllLines(filePath);
            currentFileName = filename;
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
        } else {
            System.out.println("File not found.");
        }
    }

    private static void saveFile(Scanner scanner) throws IOException {
        if (currentFileName == null) {
            System.out.println("Enter a file name to save (without extension):");
            currentFileName = scanner.nextLine() + ".txt";
        }
        Files.write(Paths.get(currentFileName), list);
        needsToBeSaved = false;
        System.out.println("File saved successfully.");
    }

    private static void clearList() {
        list.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }

    private static void viewList() {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("Current List:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ": " + list.get(i));
            }
        }
    }

    private static boolean quitProgram(Scanner scanner) throws IOException {
        if (needsToBeSaved) {
            System.out.println("You have unsaved changes. Save before quitting? (Y/N)");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                saveFile(scanner);
            }
        }
        System.out.println("Exiting program. Goodbye!");
        return false;
    }
}
