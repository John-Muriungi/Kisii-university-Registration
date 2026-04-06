package view;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranscriptGUI extends JFrame {
    private JTextField studentIdField;
    private JTextArea transcriptArea;
    private JButton searchBtn;

    public TranscriptGUI() {
        // Window Setup
        setTitle("Kisii University - Student Transcript Viewer");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Top Panel: Search
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(new JLabel("Enter Student ID: "), BorderLayout.WEST);

        studentIdField = new JTextField();
        topPanel.add(studentIdField, BorderLayout.CENTER);

        searchBtn = new JButton("Fetch Transcript");
        topPanel.add(searchBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel: Transcript Display
        transcriptArea = new JTextArea();
        transcriptArea.setEditable(false);
        transcriptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        transcriptArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(transcriptArea), BorderLayout.CENTER);

        // Event Listener
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTranscript();
            }
        });
    }

    private void loadTranscript() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            transcriptArea.setText("Please enter a Student ID.");
            return;
        }

        transcriptArea.setText("Generating Transcript for " + studentId + "...\n");
        transcriptArea.append("=========================================\n");

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Query 1: Get Student Info
            String studentQuery = "SELECT name, academic_standing FROM students WHERE student_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(studentQuery)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    transcriptArea.append("Name: " + rs.getString("name") + "\n");
                    transcriptArea.append("Academic Standing: " + rs.getString("academic_standing") + "\n");
                    transcriptArea.append("=========================================\n\n");
                    transcriptArea.append(String.format("%-12s %-25s\n", "COURSE ID", "DESCRIPTION"));
                    transcriptArea.append("-----------------------------------------\n");
                } else {
                    transcriptArea.setText("Student not found in the system.");
                    return;
                }
            }

            // Query 2: Get Enrolled Courses
            String coursesQuery = "SELECT c.course_id, c.description FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "WHERE e.student_number = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(coursesQuery)) {
                pstmt.setString(1, studentId);
                ResultSet rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                    transcriptArea.append(String.format("%-12s %-25s\n",
                            rs.getString("course_id"),
                            rs.getString("description")));
                }

                transcriptArea.append("\n-----------------------------------------\n");
                transcriptArea.append("Total Courses Registered: " + count + "\n");

                // Prompt condition for graduation fulfillment
                if (count >= 5) {
                    transcriptArea.append("Graduation Status: Requirements Fulfilled!\n");
                } else {
                    transcriptArea.append("Graduation Status: In Progress (Needs " + (5 - count) + " more).\n");
                }
            }

        } catch (SQLException ex) {
            transcriptArea.append("\nDatabase Error: " + ex.getMessage());
        }
    }
}
