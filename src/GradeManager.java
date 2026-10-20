
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class GradeManager {
    private final ArrayList<Student> students = new ArrayList<>();

    public boolean addStudent(String id, String name) {
        if (findStudentById(id) != null) {
            return false;
        }

        students.add(new Student(id, name));
        return true;
    }

    public Student findStudentById(String id) {
        if (id == null) {
            return null;
        }

        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id.trim())) {
                return student;
            }
        }

        return null;
    }

    public boolean removeStudent(String id) {
        Student student = findStudentById(id);
        return student != null && students.remove(student);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.println();
        System.out.printf("%-12s %-22s %10s %10s %-8s%n",
                "Student ID", "Name", "Total", "Average", "Grade");
        System.out.println("----------------------------------------------------------------");

        for (Student s : students) {
            System.out.printf("%-12s %-22s %10d %10.2f %-8s%n",
                    s.getId(),
                    s.getName(),
                    s.getTotalMarks(),
                    s.getAverage(),
                    s.getGrade());
        }
    }

    public void generateReportCard(String id) {
        Student s = findStudentById(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("            STUDENT REPORT CARD");
        System.out.println("========================================");
        System.out.println("Student ID   : " + s.getId());
        System.out.println("Student Name : " + s.getName());
        System.out.println("----------------------------------------");
        System.out.printf("%-25s %10s%n", "Subject", "Marks");
        System.out.println("----------------------------------------");

        for (Subject subject : s.getSubjects().values()) {
            System.out.printf("%-25s %10d%n",
                    subject.getName(), subject.getMarks());
        }

        System.out.println("----------------------------------------");
        System.out.println("Subjects     : " + s.getSubjects().size());
        System.out.println("Total Marks  : " + s.getTotalMarks());
        System.out.printf("Average      : %.2f%n", s.getAverage());
        System.out.println("Letter Grade : " + s.getGrade());
        System.out.printf("GPA          : %.2f / 10.00%n", s.getGPA());
        System.out.println("========================================");
    }

    public void displayClassStatistics() {
        if (students.isEmpty()) {
            System.out.println("No students available for statistics.");
            return;
        }

        double totalAverage = 0.0;
        double highest = Double.NEGATIVE_INFINITY;
        double lowest = Double.POSITIVE_INFINITY;

        ArrayList<Double> averages = new ArrayList<>();

        for (Student s : students) {
            double average = s.getAverage();
            averages.add(average);
            totalAverage += average;
            highest = Math.max(highest, average);
            lowest = Math.min(lowest, average);
        }

        Collections.sort(averages);

        int n = averages.size();
        double median;

        if (n % 2 == 1) {
            median = averages.get(n / 2);
        } else {
            median = (averages.get(n / 2 - 1)
                    + averages.get(n / 2)) / 2.0;
        }

        System.out.println("\n========== CLASS STATISTICS ==========");
        System.out.printf("Number of Students : %d%n", n);
        System.out.printf("Class Average      : %.2f%n",
                totalAverage / n);
        System.out.printf("Median Average     : %.2f%n", median);
        System.out.printf("Highest Average    : %.2f%n", highest);
        System.out.printf("Lowest Average     : %.2f%n", lowest);
        System.out.println("======================================");
    }

    public void sortStudentsByAverage() {
        if (students.isEmpty()) {
            System.out.println("No students to sort.");
            return;
        }

        ArrayList<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingDouble(Student::getAverage)
                .reversed());

        System.out.println("\n======= STUDENTS RANKED BY AVERAGE =======");
        System.out.printf("%-6s %-12s %-22s %10s%n",
                "Rank", "ID", "Name", "Average");

        int rank = 1;
        for (Student s : sorted) {
            System.out.printf("%-6d %-12s %-22s %10.2f%n",
                    rank++, s.getId(), s.getName(), s.getAverage());
        }
    }
}
