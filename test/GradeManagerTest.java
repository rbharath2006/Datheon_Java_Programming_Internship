
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class GradeManagerTest {

    @Test
    void shouldRegisterStudent() {
        GradeManager manager = new GradeManager();

        assertTrue(manager.addStudent("101", "Arun"));
        assertEquals(1, manager.getStudents().size());
        assertEquals("Arun",
                manager.findStudentById("101").getName());
    }

    @Test
    void shouldRejectDuplicateStudentId() {
        GradeManager manager = new GradeManager();

        assertTrue(manager.addStudent("101", "Arun"));
        assertFalse(manager.addStudent("101", "Priya"));
        assertEquals(1, manager.getStudents().size());
    }

    @Test
    void shouldRejectInvalidMarks() {
        assertThrows(IllegalArgumentException.class,
                () -> new Subject("Java", -1));

        assertThrows(IllegalArgumentException.class,
                () -> new Subject("Java", 101));

        assertThrows(IllegalArgumentException.class,
                () -> new Subject("   ", 80));
    }

    @Test
    void shouldAcceptBoundaryMarks() {
        assertEquals(0, new Subject("Java", 0).getMarks());
        assertEquals(100, new Subject("Java", 100).getMarks());
    }

    @Test
    void shouldUpdateDuplicateSubjectWithoutIncreasingCount() {
        Student student = new Student("101", "Arun");

        student.addOrUpdateSubject("Java", 85);
        student.addOrUpdateSubject("JAVA", 95);

        assertEquals(1, student.getSubjects().size());
        assertEquals(95,
                student.getSubjects().get("java").getMarks());
    }

    @Test
    void shouldCalculateTotalAndAverage() {
        Student student = new Student("101", "Arun");

        student.addOrUpdateSubject("Java", 85);
        student.addOrUpdateSubject("DBMS", 78);
        student.addOrUpdateSubject("Maths", 92);

        assertEquals(255, student.getTotalMarks());
        assertEquals(85.0, student.getAverage(), 0.001);
    }

    @Test
    void shouldCalculateLetterGrade() {
        Student student = new Student("101", "Arun");

        student.addOrUpdateSubject("Java", 90);
        assertEquals("A+", student.getGrade());

        student.addOrUpdateSubject("DBMS", 70);
        assertEquals("A", student.getGrade()); // Average is 80

        student.addOrUpdateSubject("Maths", 40);
        assertEquals("C", student.getGrade()); // Average is 66.67
    }

    @Test
    void shouldCalculateGpa() {
        Student student = new Student("101", "Arun");

        student.addOrUpdateSubject("Java", 85);  // 9 points
        student.addOrUpdateSubject("DBMS", 78);  // 8 points
        student.addOrUpdateSubject("Maths", 92); // 10 points

        assertEquals(9.0, student.getGPA(), 0.001);
    }

    @Test
    void shouldReturnNullForUnknownStudent() {
        GradeManager manager = new GradeManager();

        assertNull(manager.findStudentById("999"));
    }

    @Test
    void shouldFindStudentWithoutCaseSensitiveIdMatching() {
        GradeManager manager = new GradeManager();
        manager.addStudent("STU101", "Arun");

        assertNotNull(manager.findStudentById("stu101"));
    }

    @Test
    void shouldHandleEmptyClassStatistics() {
        GradeManager manager = new GradeManager();

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true,
                    StandardCharsets.UTF_8));

            assertDoesNotThrow(manager::displayClassStatistics);
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(output.toString(StandardCharsets.UTF_8)
                .contains("No students available"));
    }

    @Test
    void shouldReturnZeroForStudentWithoutMarks() {
        Student student = new Student("101", "Arun");

        assertEquals(0, student.getTotalMarks());
        assertEquals(0.0, student.getAverage(), 0.001);
        assertEquals("F", student.getGrade());
        assertEquals(0.0, student.getGPA(), 0.001);
    }
}
