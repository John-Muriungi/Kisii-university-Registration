package controller;

import model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationController {

    /**
     * Checks database records to see if a student can register for a course.
     */
    public String attemptRegistration(String studentId, String courseId) {

        // 1. Establish the connection to XAMPP
        try (Connection conn = DatabaseConnection.getConnection()) {

            // --- CHECK 1: DOES THE STUDENT EXIST & ARE THEY IN GOOD STANDING? ---
            String studentQuery = "SELECT academic_standing FROM students WHERE student_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(studentQuery)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    return "Error: Student " + studentId + " not found in database.";
                }

                String standing = rs.getString("academic_standing");
                if (!standing.equalsIgnoreCase("Good")) {
                    return "Registration Denied: Student standing is '" + standing + "'. Must be 'Good'.";
                }
            }

            // --- CHECK 2: DOES THE COURSE EXIST? ---
            String courseQuery = "SELECT description, schedule_slot FROM courses WHERE course_id = ?";
            String scheduleSlot = "";
            String description = "";

            try (PreparedStatement pstmt = conn.prepareStatement(courseQuery)) {
                pstmt.setString(1, courseId);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    return "Error: Course " + courseId + " does not exist.";
                }

                description = rs.getString("description");
                scheduleSlot = rs.getString("schedule_slot");
            }

            // --- CHECK 3: DOES THE STUDENT HAVE A SCHEDULE CONFLICT? ---
            String conflictQuery = "SELECT c.course_id FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.student_number = ? AND c.schedule_slot = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(conflictQuery)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, scheduleSlot);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String conflictingCourse = rs.getString("course_id");
                    return "Registration Denied: Schedule conflict with " + conflictingCourse + " at " + scheduleSlot;
                }
            }

            // --- ALL CHECKS PASSED: REGISTER THE STUDENT ---
            String enrollQuery = "INSERT INTO enrollments (student_number, course_id) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(enrollQuery)) {
                pstmt.setString(1, studentId);
                pstmt.setString(2, courseId);
                pstmt.executeUpdate();
            }

            // Prompt requirement: Automatically print the bill
            printBursarBill(studentId, courseId);

            return "Registration Successful! Course: " + description;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    private void printBursarBill(String studentId, String courseId) {
        System.out.println("\n--- BURSAR BILL GENERATED ---");
        System.out.println("Student: " + studentId);
        System.out.println("Course: " + courseId);
        System.out.println("Status: unpaid. Please collect at the Bursar's Office.");
        System.out.println("-----------------------------\n");
    }
}