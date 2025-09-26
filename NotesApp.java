import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * NotesApp - CLI notes manager with File I/O persistence.
 * 
 * Features:
 * - Add Note (title, content, timestamp)
 * - View All Notes
 * - View Notes by Title
 * - Modify Note by Title
 * - Delete Note by Title
 * - Delete All Notes (with safety checks)
 * - Exit
 */
public class NotesApp {

    private static final String FILE_NAME = "notes.txt";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ensureFileExists();

        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> addNote();
                case 2 -> viewAllNotes();
                case 3 -> viewNotesByTitle();
                case 4 -> modifyNoteByTitle();
                case 5 -> deleteNoteByTitle();
                case 6 -> deleteAllNotes();
                case 7 -> {
                    System.out.println("Exiting application... Goodbye!");
                    delay();
                    exit = true;
                }
                default -> {
                    System.out.println("Invalid choice. Please select between 1 and 7.");
                    delay();
                }
            }
        }
        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println("\n===== Notes Manager =====");
        System.out.println("1. Add a Note");
        System.out.println("2. View All Notes");
        System.out.println("3. View Notes by Title");
        System.out.println("4. Modify a Note by Title");
        System.out.println("5. Delete a Note by Title");
        System.out.println("6. Delete All Notes");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(SCANNER.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void ensureFileExists() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Oops, Failed to initialize storage file: " + e.getMessage());
            System.exit(1);
        }
    }

    // --------------------------- CORE FEATURES -----------------------------

    private static void addNote() {
        System.out.print("Enter note title: ");
        String title = SCANNER.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            delay();
            return;
        }

        System.out.print("Enter note content: ");
        String content = SCANNER.nextLine().trim();

        if (content.isEmpty()) {
            System.out.println("Content cannot be empty.");
            delay();
            return;
        }

        String timestamp = LocalDateTime.now().format(FORMATTER);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("TITLE: " + title);
            writer.newLine();
            writer.write("CONTENT: " + content);
            writer.newLine();
            writer.write("CREATED_AT: " + timestamp);
            writer.newLine();
            writer.write("-----");
            writer.newLine();
            System.out.println("Note added successfully!");
        } catch (IOException e) {
            System.err.println("Oops, Error writing note: " + e.getMessage());
            delay();
        }
    }

    private static void viewAllNotes() {
        List<String> lines = readFile();
        if (lines.isEmpty()) {
            System.out.println("Oh-no, No notes available. Please Add One.");
            delay();
            return;
        }
        System.out.println("\n----- All Notes -----");
        printNotes(lines);
    }

    private static void viewNotesByTitle() {
        List<String> lines = readFile();
        if (lines.isEmpty()) {
            System.out.println("Oh-no, No notes available.");
            delay();
            return;
        }

        System.out.print("Enter title to search: ");
        String searchTitle = SCANNER.nextLine().trim();

        List<String> matches = extractNotesByTitle(lines, searchTitle);
        if (matches.isEmpty()) {
            System.out.println("No notes found with title: " + searchTitle);
        } else {
            System.out.println("\n----- Matching Notes -----");
            printNotes(matches);
        }
        delay();
    }

    private static void modifyNoteByTitle() {
        System.out.print("Enter title of note to modify: ");
        String title = SCANNER.nextLine().trim();

        List<String> lines = readFile();
        List<String> matches = extractNotesByTitle(lines, title);

        if (matches.isEmpty()) {
            System.out.println("No notes found with that title.");
            delay();
            return;
        }

        System.out.print("Enter new content: ");
        String newContent = SCANNER.nextLine().trim();
        String newTimestamp = LocalDateTime.now().format(FORMATTER);

        // Replace first matching note’s content
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("TITLE: " + title)) {
                lines.set(i + 1, "CONTENT: " + newContent);
                lines.set(i + 2, "CREATED_AT: " + newTimestamp);
                break;
            }
        }

        writeFile(lines);
        System.out.println("Note updated successfully!");
        delay();
    }

    private static void deleteNoteByTitle() {
        System.out.print("Enter title of note to delete: ");
        String title = SCANNER.nextLine().trim();

        List<String> lines = readFile();
        if (lines.isEmpty()) {
            System.out.println("Oh-no, No notes available.");
            delay();
            return;
        }

        List<String> newLines = new ArrayList<>();
        boolean deleted = false;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("TITLE: " + title)) {
                // Skip 4 lines (TITLE, CONTENT, CREATED_AT, -----)
                i += 3;
                deleted = true;
            } else {
                newLines.add(lines.get(i));
            }
        }

        if (deleted) {
            writeFile(newLines);
            System.out.println("Note(s) deleted with title: " + title);
        } else {
            System.out.println("No notes found with that title.");
        }
        delay();
    }

    private static void deleteAllNotes() {
        List<String> lines = readFile();
        if (lines.isEmpty()) {
            System.out.println("Oh-no, No notes available to delete.");
            delay();
            return;
        }

        System.out.print("Are you sure you want to delete ALL notes? (yes/no): ");
        String confirm = SCANNER.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            writeFile(new ArrayList<>());
            System.out.println("All notes deleted successfully!");
        } else {
            System.out.println("Please Enter Your Choice in yes or no.");
        }
        delay();
    }

    // --------------------------- UTIL METHODS -----------------------------

    private static List<String> readFile() {
        try {
            return Files.readAllLines(Paths.get(FILE_NAME));
        } catch (IOException e) {
            System.err.println("Oops, Error reading notes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void writeFile(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Oops, Error saving notes: " + e.getMessage());
        }
    }

    private static void printNotes(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static List<String> extractNotesByTitle(List<String> lines, String title) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("TITLE: " + title)) {
                // Collect TITLE, CONTENT, CREATED_AT, -----
                for (int j = 0; j < 4 && i + j < lines.size(); j++) {
                    result.add(lines.get(i + j));
                }
                result.add(""); // extra space between notes
            }
        }
        return result;
    }

    // Utility delay for smoother UX
    private static void delay() {
        try {
            Thread.sleep(450); // tweak delay as needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
