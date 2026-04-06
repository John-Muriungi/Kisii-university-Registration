package view;

import model.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrarGUI extends JFrame {

    // Components for Tab 1 (Add Course)
    private JTextField courseIdField, descField, scheduleField;
    private JTextArea courseLogArea;

    // Components for Tab 2 (Check Enrollments)
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    public RegistrarGUI() {
        // Window Setup
        setTitle("Kisii University - Registrar Office");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add the two tabs we need
        tabbedPane.addTab("Add New Course", createAddCoursePanel());
        tabbedPane.addTab("Check Enrollments", createEnrollmentPanel());

        add(tabbedPane);
    }

    // --- TAB 1: ADD COURSE UI ---
    private JPanel createAddCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Form Inputs
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("Course ID (e.g., COMP103):"));
        courseIdField = new JTextField();
        formPanel.add(courseIdField);

        formPanel.add(new JLabel("Course Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        formPanel.add(new JLabel("Schedule Slot (e.g., TTH 10:00-11:30):"));
        scheduleField = new JTextField();
        formPanel.add(scheduleField);

        panel.add(formPanel, BorderLayout.NORTH);

        // Center Log
        courseLogArea = new JTextArea();
        courseLogArea.setEditable(false);
        panel.add(new JScrollPane(courseLogArea), BorderLayout.CENTER);

        // Button
        JButton addCourseBtn = new JButton("Save Course to System");
        panel.add(addCourseBtn, BorderLayout.SOUTH);

        // Button Event
        addCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewCourse();
            }
        });

        return panel;
    }

    // --- TAB 2: CHECK ENROLLMENTS UI ---
    private JPanel createEnrollmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top Refresh Button
        JButton refreshBtn = new JButton("🔄 Refresh Enrollment List");
        panel.add(refreshBtn, BorderLayout.NORTH);

        // Table to display data
        String[] columnNames = {"Student ID", "Student Name", "Course ID", "Course Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        enrollmentTable = new JTable(tableModel);

        panel.add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);

        // Button Event
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEnrollmentData();
            }
        });

        // Load data immediately when the panel is created
        loadEnrollmentData();

        return panel;
    }

    // --- DATABASE LOGIC FOR TAB 1 ---
    private void addNewCourse() {
        String id = courseIdField.getText().trim();
        String desc = descField.getText().trim();
        String schedule = scheduleField.getText().trim();

        if (id.isEmpty() || desc.isEmpty() || schedule.isEmpty()) {
            courseLogArea.append("Error: All fields must be filled!\n");
            return;
        }

        String query = "INSERT INTO courses (course_id, description, schedule_slot) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            pstmt.setString(2, desc);
            pstmt.setString(3, schedule);

            pstmt.executeUpdate();
            courseLogArea.append("Success: Course " + id + " has been added to the catalog.\n");

            // Clear inputs
            courseIdField.setText("");
            descField.setText("");
            scheduleField.setText("");

        } catch (SQLException ex) {
            courseLogArea.append("Database Error: " + ex.getMessage() + "\n");
        }
    }

    // --- DATABASE LOGIC FOR TAB 2 ---
    private void loadEnrollmentData() {
        // Clear previous rows from the table
        tableModel.setRowCount(0);

        // SQL query that joins Students, Enrollments, and Courses tables
        String query = "SELECT s.student_number, s.name, c.course_id, c.description " +
                "FROM enrollments e " +
                "JOIN students s ON e.student_number = s.student_number " +
                "JOIN courses c ON e.course_id = c.course_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String sId = rs.getString("student_number");
                String sName = rs.getString("name");
                String cId = rs.getString("course_id");
                String cDesc = rs.getString("description");

                // Add the data to our GUI table
                tableModel.addRow(new Object[]{sId, sName, cId, cDesc});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    // Main method to run this screen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrarGUI().setVisible(true);
        });
    }
}