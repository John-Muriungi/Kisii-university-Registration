package view;

import model.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdmissionsGUI extends JFrame {
    // GUI Components
    private JTextField idField, nameField;
    private JComboBox<String> standingDropdown;
    private JButton saveButton;
    private JTextArea logArea;

    public AdmissionsGUI() {
        // Window Setup
        setTitle("Kisii University - Admissions Office");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes just this window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Top Panel: The Form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Student Number / ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Academic Standing:"));
        String[] standings = {"Good", "Probation", "Suspended"};
        standingDropdown = new JComboBox<>(standings);
        formPanel.add(standingDropdown);

        add(formPanel, BorderLayout.NORTH);

        // 2. Center Panel: Log / Output
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createTitledBorder("System Log"));
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        // 3. Bottom Panel: Action Button
        saveButton = new JButton("Add New Student");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(saveButton, BorderLayout.SOUTH);

        // Event Listener for the button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewStudent();
            }
        });
    }

    private void addNewStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String standing = (String) standingDropdown.getSelectedItem();

        // Simple validation
        if (id.isEmpty() || name.isEmpty()) {
            logArea.append("Error: All fields are required!\n");
            return;
        }

        // Database operation
        String query = "INSERT INTO students (student_number, name, academic_standing) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, standing);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                logArea.append("Success: Student " + id + " (" + name + ") added successfully!\n");
                // Clear fields for the next entry
                idField.setText("");
                nameField.setText("");
                standingDropdown.setSelectedIndex(0);
            }

        } catch (SQLException ex) {
            logArea.append("Database Error: " + ex.getMessage() + "\n");
        }
    }

    // Main method to test it independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdmissionsGUI().setVisible(true);
        });
    }
}
