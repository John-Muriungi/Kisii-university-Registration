package view;

import controller.RegistrationController;
import model.Course;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvisorGUI extends JFrame {
    // GUI Components
    private JTextField studentIdField, courseIdField;
    private JTextArea displayArea;
    private JButton checkButton;

    // We need an instance of the controller to handle the logic
    private RegistrationController controller;

    // Hardcoded data simulating a database for now
    private Student dummyStudent;
    private Course dummyCourse;

    public AdvisorGUI() {
        controller = new RegistrationController();


        // Window Setup
        setTitle("Kisii University - Advisor Registration System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window
        setLayout(new BorderLayout(10, 10));

        // 1. Top Panel: Inputs
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Registration Input"));

        inputPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        inputPanel.add(studentIdField);

        inputPanel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField();
        inputPanel.add(courseIdField);

        add(inputPanel, BorderLayout.NORTH);

        // 2. Center Panel: Output Display
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // 3. Bottom Panel: Action Button
        checkButton = new JButton("Approve & Register");
        add(checkButton, BorderLayout.SOUTH);

        // Event Listener for the button
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        String enteredStudent = studentIdField.getText().trim();
        String enteredCourse = courseIdField.getText().trim();

        // Validation: Did they fill the boxes?
        if (enteredStudent.isEmpty() || enteredCourse.isEmpty()) {
            displayArea.setText("Error: Please fill in both fields.");
            return;
        }

        displayArea.setText("Connecting to Kisii University Database...\n");

        // Run the rules via the database-connected controller
        String result = controller.attemptRegistration(enteredStudent, enteredCourse);

        displayArea.append(result);
    }


    // Main method to run the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdvisorGUI().setVisible(true);
        });
    }
}
