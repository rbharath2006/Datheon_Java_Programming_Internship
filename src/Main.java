
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final GradeManager manager = new GradeManager();

    private static final StudentFileStorage storage =
            new StudentFileStorage("students.csv");

    public static void main(String[] args) {
        boolean running = true;

        System.out.println("Welcome to Student Grade Management System!");

        try {
            storage.loadInto(manager);
        } catch (java.io.IOException e) {
            System.err.println(
                    "Warning: Could not load saved records: " + e.getMessage()
            );
            System.err.println(
                    "Continuing with records loaded before the error."
            );
        }

        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        recordMarks();
                        break;
                    case 3:
                        searchStudent();
                        break;
                    case 4:
                        manager.displayAllStudents();
                        break;
                    case 5:
                        generateReport();
                        break;
                    case 6:
                        manager.displayClassStatistics();
                        break;
                    case 7:
                        manager.sortStudentsByAverage();
                        break;
                    case 8:
                        removeStudent();
                        break;
                    case 9:
                        running = false;
                        System.out.println("Thank you for using the system!");
                        break;
                    default:
                        System.out.println(
                                "Invalid choice. Enter a number from 1 to 9."
                        );
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("========================================");
        System.out.println("    STUDENT GRADE MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Register Student");
        System.out.println("2. Record / Update Marks");
        System.out.println("3. Search Student");
        System.out.println("4. Display All Students");
        System.out.println("5. Generate Student Report Card");
        System.out.println("6. Calculate Class Statistics");
        System.out.println("7. Sort Students by Average");
        System.out.println("8. Remove Student");
        System.out.println("9. Exit");
        System.out.println("========================================");
    }

    private static void addStudent() {
        String id = readText("Enter student ID: ");
        String name = readText("Enter student name: ");

        try {
            boolean added = manager.addStudent(id, name);

            if (added) {
                saveData();
                System.out.println("Student registered successfully.");
            } else {
                System.out.println("Student ID already exists.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register student: "
                    + e.getMessage());
        }
    }


    private static void recordMarks() {
        String id = readText("Enter student ID: ");
        Student student = manager.findStudentById(id);

        if (student == null) {
            System.out.println("Student not found. Register first.");
            return;
        }

        int count = readInt("How many subjects do you want to enter? ");

        if (count <= 0) {
            System.out.println("Enter at least one subject.");
            return;
        }

        for (int i = 1; i <= count; i++) {
            System.out.println("\nSubject " + i);

            while (true) {
                String subjectName = readText("Enter subject name: ");
                int marks = readMarks("Enter marks (0-100): ");

                try {
                    student.addOrUpdateSubject(subjectName, marks);
                    saveData();
                    System.out.println(
                            "Marks saved successfully for " + subjectName + "."
                    );
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Input error: " + e.getMessage());
                    System.out.println("Please enter this subject again.");
                }
            }
        }

        System.out.println("All entered marks have been processed.");
    }


    private static void removeStudent() {
        String id = readText("Enter student ID to remove: ");

        boolean removed = manager.removeStudent(id);

        if (removed) {
            saveData();
            System.out.println("Student removed successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static int readMarks(String prompt) {
        while (true) {
            int marks = readInt(prompt);

            if (marks >= 0 && marks <= 100) {
                return marks;
            }

            System.out.println(
                    "Invalid marks! Enter a value between 0 and 100."
            );
        }
    }


    private static void searchStudent() {
        String id = readText("Enter student ID to search: ");
        Student student = manager.findStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Student found!");
            System.out.println("ID      : " + student.getId());
            System.out.println("Name    : " + student.getName());
            System.out.println("Subjects: " + student.getSubjects().size());
            System.out.printf("Average : %.2f%n", student.getAverage());
        }
    }

    private static void generateReport() {
        String id = readText("Enter student ID for report: ");
        manager.generateReportCard(id);
    }


    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Input cannot be empty. Please try again."
            );
        }
    }

    private static void saveData() {
        try {
            storage.save(manager);
        } catch (java.io.IOException e) {
            System.err.println(
                    "ERROR: Student data could not be saved: "
                            + e.getMessage()
            );
            System.err.println(
                    "Your latest changes may be lost when the program exits."
            );
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }
}
