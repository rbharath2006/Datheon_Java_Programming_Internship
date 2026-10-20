
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class StudentFileStorage {

    private final Path filePath;

    public StudentFileStorage(String fileName) {
        this.filePath = Path.of(fileName).toAbsolutePath();
    }

    public Path getFilePath() {
        return filePath;
    }

    // Load student records into the manager.
    public void loadInto(GradeManager manager) throws IOException {
        if (!Files.exists(filePath)) {
            System.out.println(
                    "No saved data found. Starting with an empty student list."
            );
            return;
        }

        List<String> lines = Files.readAllLines(
                filePath, StandardCharsets.UTF_8
        );

        int loadedRows = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            if (i == 0 && line.equals(
                    "studentId,name,subject,marks")) {
                continue;
            }

            try {
                List<String> fields = parseCsvLine(line);

                if (fields.size() != 4) {
                    throw new IllegalArgumentException(
                            "Expected exactly 4 CSV columns."
                    );
                }

                String id = fields.get(0).trim();
                String name = fields.get(1).trim();
                String subjectName = fields.get(2).trim();
                String marksText = fields.get(3).trim();

                if (id.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Student ID and name are required."
                    );
                }

                // A student with no marks has blank subject and marks.
                boolean noSubject = subjectName.isEmpty()
                        && marksText.isEmpty();

                boolean hasSubject = !subjectName.isEmpty()
                        && !marksText.isEmpty();

                if (!noSubject && !hasSubject) {
                    throw new IllegalArgumentException(
                            "Subject and marks must both be present or both blank."
                    );
                }

                Student student = manager.findStudentById(id);

                if (student == null) {
                    if (!manager.addStudent(id, name)) {
                        throw new IllegalArgumentException(
                                "Could not register student."
                        );
                    }
                    student = manager.findStudentById(id);
                } else if (!student.getName().equals(name)) {
                    throw new IllegalArgumentException(
                            "Conflicting names for the same student ID."
                    );
                }

                if (hasSubject) {
                    int marks;

                    try {
                        marks = Integer.parseInt(marksText);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Marks must be a whole number."
                        );
                    }

                    student.addOrUpdateSubject(subjectName, marks);
                }

                loadedRows++;

            } catch (IllegalArgumentException e) {
                System.err.println(
                        "Skipping invalid CSV row " + (i + 1)
                                + ": " + e.getMessage()
                );
            }
        }

        System.out.println(
                "CSV loading completed. Valid rows processed: " + loadedRows
        );
    }

    // Save all students, including students with no marks.
    public void save(GradeManager manager) throws IOException {
        Path parent = filePath.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Path tempFile = Files.createTempFile(
                parent, "students-", ".tmp"
        );

        boolean moved = false;

        try {
            try (var writer = Files.newBufferedWriter(
                    tempFile, StandardCharsets.UTF_8)) {

                writer.write("studentId,name,subject,marks");
                writer.newLine();

                for (Student student : manager.getStudents()) {
                    if (student.getSubjects().isEmpty()) {
                        writeRow(writer,
                                student.getId(),
                                student.getName(),
                                "",
                                ""
                        );
                    } else {
                        for (Subject subject :
                                student.getSubjects().values()) {
                            writeRow(writer,
                                    student.getId(),
                                    student.getName(),
                                    subject.getName(),
                                    String.valueOf(subject.getMarks())
                            );
                        }
                    }
                }
            }

            try {
                Files.move(tempFile, filePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempFile, filePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            moved = true;
            System.out.println("Student data saved to: " + filePath);

        } finally {
            if (!moved) {
                Files.deleteIfExists(tempFile);
            }
        }
    }

    private void writeRow(
            java.io.BufferedWriter writer,
            String id,
            String name,
            String subject,
            String marks) throws IOException {

        writer.write(escapeCsv(id));
        writer.write(",");
        writer.write(escapeCsv(name));
        writer.write(",");
        writer.write(escapeCsv(subject));
        writer.write(",");
        writer.write(escapeCsv(marks));
        writer.newLine();
    }

    // Escape commas, quotes, and line breaks according to CSV rules.
    private String escapeCsv(String value) {
        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",")
                || escaped.contains("\"")
                || escaped.contains("\n")
                || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    // Parse one CSV record, including quoted commas and escaped quotes.
    private List<String> parseCsvLine(String line) {
        java.util.ArrayList<String> fields =
                new java.util.ArrayList<>();

        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        boolean closedQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length()
                            && line.charAt(i + 1) == '"') {
                        field.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                        closedQuote = true;
                    }
                } else {
                    field.append(c);
                }
            } else if (c == ',') {
                fields.add(field.toString());
                field.setLength(0);
                closedQuote = false;
            } else if (c == '"' && field.length() == 0
                    && !closedQuote) {
                inQuotes = true;
            } else {
                if (closedQuote) {
                    throw new IllegalArgumentException(
                            "Unexpected text after a closing quote."
                    );
                }
                field.append(c);
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException(
                    "Unclosed quoted CSV field."
            );
        }

        fields.add(field.toString());
        return fields;
    }
}
