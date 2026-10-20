# Student Grade Management System

A Java console-based application for managing student records, recording subject marks, calculating academic performance, and generating student report cards. The project demonstrates Object-Oriented Programming (OOP), Java Collections, input validation, CSV file persistence, and automated unit testing with JUnit 5.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [System Architecture](#system-architecture)
- [Academic Calculations](#academic-calculations)
- [Data Persistence](#data-persistence)
- [Getting Started](#getting-started)
- [How to Run](#how-to-run)
- [Running Tests](#running-tests)
- [Sample Output](#sample-output)
- [Limitations](#limitations)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

## Overview

The Student Grade Management System helps teachers or administrators maintain student academic records through a simple menu-driven interface.

Users can register students, record and update subject marks, search for students, calculate totals and averages, determine letter grades and GPA, generate report cards, and view class statistics.

The application saves student records to a CSV file so that data can be restored when the application is restarted.

## Features

- **Student Registration:** Register students with unique IDs and names.
- **Subject Management:** Record and update marks for multiple subjects.
- **Input Validation:** Reject invalid marks outside the range of 0–100 and blank required fields.
- **Duplicate Prevention:** Prevent duplicate student IDs and update existing subject records instead of duplicating them.
- **Student Search:** Find student records using their unique IDs.
- **Academic Calculations:** Calculate total marks, average marks, letter grades, and GPA.
- **Report Cards:** Generate formatted individual student reports.
- **Class Statistics:** Calculate class average, median, highest average, and lowest average.
- **Student Ranking:** Sort students by average marks.
- **Student Removal:** Remove student records.
- **CSV Persistence:** Save and restore student records between application sessions.
- **Automated Testing:** Verify core functionality using JUnit 5.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 or newer | Application development |
| IntelliJ IDEA | Development environment |
| ArrayList | Store student records |
| HashMap | Store subject-wise marks |
| Java I/O and NIO | Read and write CSV records |
| CSV | Persistent student data storage |
| JUnit 5 | Automated unit testing |

## Project Structure

```text
StudentGradeManagement/
├── src/
│   ├── Main.java
│   ├── Student.java
│   ├── Subject.java
│   ├── GradeManager.java
│   └── StudentFileStorage.java
├── test/
│   └── GradeManagerTest.java
├── students.csv
├── .gitignore
└── README.md
```

`students.csv` is generated when student data is saved. It may not exist before the first save.

## System Architecture

The application is divided into separate classes to improve readability and maintainability.

### Main.java
Provides the console menu, collects user input, and invokes the appropriate management operations.

### Student.java
Stores student details and subject marks. Calculates total marks, average marks, letter grades, and GPA.

### Subject.java
Represents a subject and validates its name and marks.

### GradeManager.java
Manages student registration, searching, removal, listing, sorting, report cards, and class statistics.

### StudentFileStorage.java
Loads student records from CSV and saves updated records to persistent storage.

### GradeManagerTest.java
Contains JUnit 5 tests for registration, duplicate IDs, validation, calculations, subject updates, searching, and empty-class behavior.

## Academic Calculations

### Total Marks

The total is the sum of all recorded subject marks.

### Average Marks

Average marks are calculated by dividing the total marks by the number of recorded subjects.

### Letter Grade

The application uses the following example grading scale:

| Average Marks | Grade |
|---|---|
| 90–100 | A+ |
| 80–89.99 | A |
| 70–79.99 | B |
| 60–69.99 | C |
| 50–59.99 | D |
| Below 50 | F |

### GPA

The application assigns example grade points to each subject:

| Subject Marks | Grade Points |
|---|---:|
| 90–100 | 10 |
| 80–89 | 9 |
| 70–79 | 8 |
| 60–69 | 7 |
| 50–59 | 6 |
| Below 50 | 0 |

The GPA is the unweighted average of the grade points for the student's recorded subjects. This is a simplified project calculation and does not represent an official university GPA policy.

### Class Statistics

The application calculates the class average, median student average, highest average, and lowest average.

## Data Persistence

Student records are stored in `students.csv`.

The CSV contains four columns:

```csv
studentId,name,subject,marks
101,Arun,Java,85
101,Arun,DBMS,78
102,Priya,Java,92
```

Each row represents one subject for a student. Students without recorded marks can be stored with empty subject and marks fields.

The application loads saved records at startup and saves changes after successful registration, mark updates, and student removal.

CSV fields containing commas or quotation marks are escaped. Invalid rows are reported and skipped during loading.

**Note:** The current lightweight CSV parser processes one physical line at a time. Although multiline values can be escaped when saving, multiline CSV fields are not supported when loading.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or newer.
- IntelliJ IDEA or another Java-compatible IDE.
- JUnit 5 configured in the project to run automated tests.

### Setup

1. Clone or download the project.
2. Open the project folder in IntelliJ IDEA.
3. Configure a JDK version of 17 or newer.
4. Confirm that all Java source files are in the same package, or use matching package declarations.
5. Ensure JUnit 5 is configured for the test source directory.

No database server or external database is required.

## How to Run

1. Open `Main.java` in IntelliJ IDEA.
2. Click the green **Run** button beside the `main()` method.
3. Use the console menu to select an operation.

### Main Menu

```text
========================================
    STUDENT GRADE MANAGEMENT SYSTEM
========================================
1. Register Student
2. Record / Update Marks
3. Search Student
4. Display All Students
5. Generate Student Report Card
6. Calculate Class Statistics
7. Sort Students by Average
8. Remove Student
9. Exit
========================================
```

The application saves student data to a CSV file when records are modified. When the application is restarted, previously saved records are loaded automatically.

## Running Tests

The project uses JUnit 5 to test the core functionality.

### Using IntelliJ IDEA

1. Open `GradeManagerTest.java`.
2. Right-click inside the editor.
3. Select **Run 'GradeManagerTest'**.
4. Review the results in the Test Results panel.

### Test Coverage

The test suite covers:

- Student registration.
- Duplicate student ID rejection.
- Invalid marks and valid boundary marks.
- Duplicate subject updates.
- Total marks and average calculations.
- Letter grade and GPA calculations.
- Student search behavior.
- Empty-class statistics.
- Students without recorded marks.

**Observed result:** 12 tests passed and 0 failed in the current test run.

The CSV save-and-reload workflow has also been manually verified across application restarts. The 12 unit tests do not independently cover every persistence scenario.

## Sample Output

Example student report card:

```text
========================================
            STUDENT REPORT CARD
========================================
Student ID   : 101
Student Name : Arun
----------------------------------------
Subject                         Marks
----------------------------------------
Java                               85
DBMS                               78
Mathematics                        92
----------------------------------------
Subjects     : 3
Total Marks  : 255
Average      : 85.00
Letter Grade : A
GPA          : 9.00 / 10.00
========================================
```

The values shown are illustrative. Actual output depends on the subjects and marks entered.

## Limitations

- The application uses a console interface.
- CSV storage does not provide database transactions or concurrent-write protection.
- The grading scale and GPA calculation are illustrative.
- The application does not include authentication or role-based access control.
- Multiline CSV fields are not supported by the current loading parser.
- The application is designed as an educational project rather than a production student information system.

## Future Enhancements

Potential improvements include:

- A graphical user interface.
- Database integration using MySQL or PostgreSQL.
- Authentication and role-based access control.
- Credit-weighted GPA calculations.
- More comprehensive automated persistence tests.
- Improved CSV handling and backup management.
- Exporting formatted reports.

## Author

**Bharath**  
Engineering Student, Alliance University

Project Purpose

This project was developed as part of the Datheon Java Programming Internship — 2026 Cohort to demonstrate practical Java programming skills and the application of core software development concepts.

The Student Grade Management System showcases object-oriented programming, Java Collections, input validation, exception handling, academic performance calculations, CSV-based file persistence, and automated unit testing using JUnit 5.

The project demonstrates the ability to translate requirements into a functional Java application, organize code into modular classes, validate user input, preserve application data, and verify functionality through automated testing.

Internship Program: Java Programming Internship
Organization: Datheon
Cohort: 2026
Project: Student Grade Management System