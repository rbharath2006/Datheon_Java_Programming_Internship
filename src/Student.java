
import java.util.HashMap;
import java.util.Map;

public class Student {
    private final String id;
    private String name;
    private final HashMap<String, Subject> subjects;

    public Student(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student ID cannot be empty."
            );
        }

        this.id = id.trim();
        this.subjects = new HashMap<>();
        setName(name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Subject> getSubjects() {
        return java.util.Collections.unmodifiableMap(subjects);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student name cannot be empty."
            );
        }
        this.name = name.trim();
    }


    public void addOrUpdateSubject(String subjectName, int marks) {
        if (subjectName == null || subjectName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Subject name cannot be empty."
            );
        }

        // Subject validates that marks are between 0 and 100.
        Subject subject = new Subject(subjectName.trim(), marks);

        // Normalize the key so Java, JAVA, and java are treated
        // as the same subject.
        String key = subject.getName().toLowerCase(
                java.util.Locale.ROOT
        );

        // Existing subjects are updated; new subjects are inserted.
        subjects.put(key, subject);
    }


    public int getTotalMarks() {
        int total = 0;
        for (Subject subject : subjects.values()) {
            total += subject.getMarks();
        }
        return total;
    }

    public double getAverage() {
        if (subjects.isEmpty()) {
            return 0.0;
        }
        return (double) getTotalMarks() / subjects.size();
    }

    public String getGrade() {
        double average = getAverage();

        if (average >= 90) return "A+";
        if (average >= 80) return "A";
        if (average >= 70) return "B";
        if (average >= 60) return "C";
        if (average >= 50) return "D";
        return "F";
    }

    private int gradePoints(int marks) {
        if (marks >= 90) return 10;
        if (marks >= 80) return 9;
        if (marks >= 70) return 8;
        if (marks >= 60) return 7;
        if (marks >= 50) return 6;
        return 0;
    }

    public double getGPA() {
        if (subjects.isEmpty()) {
            return 0.0;
        }

        int totalPoints = 0;
        for (Subject subject : subjects.values()) {
            totalPoints += gradePoints(subject.getMarks());
        }

        return (double) totalPoints / subjects.size();
    }
}
