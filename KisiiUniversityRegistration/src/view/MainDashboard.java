package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        // Window Setup
        setTitle("Kisii University - Student Registration System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Closes the whole app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // 1. Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185)); // A nice university blue
        JLabel titleLabel = new JLabel("KISII UNIVERSITY PORTAL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Center Panel: The Navigation Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton admissionsBtn = new JButton("Admissions Office (Add Students)");
        JButton advisorBtn = new JButton("Course Advisor (Register Student)");
        JButton registrarBtn = new JButton("Registrar Office (Manage Courses)");
        JButton transcriptBtn = new JButton("Enrollment Dept (View Transcript)");

        // Making the buttons look a bit more modern
        Font btnFont = new Font("Arial", Font.PLAIN, 16);
        admissionsBtn.setFont(btnFont);
        advisorBtn.setFont(btnFont);
        registrarBtn.setFont(btnFont);
        transcriptBtn.setFont(btnFont);

        buttonPanel.add(admissionsBtn);
        buttonPanel.add(advisorBtn);
        buttonPanel.add(registrarBtn);
        buttonPanel.add(transcriptBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // 3. Footer / Copyright
        JLabel footerLabel = new JLabel("© 2026 Kisii University Registration System", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        add(footerLabel, BorderLayout.SOUTH);

        // --- BUTTON EVENTS ---

        // Open Admissions
        admissionsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdmissionsGUI().setVisible(true);
            }
        });

        // Open Advisor
        advisorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdvisorGUI().setVisible(true);
            }
        });

        // Open Registrar
        registrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrarGUI().setVisible(true);
            }
        });
        // Open Transcript
        transcriptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TranscriptGUI().setVisible(true);
            }
        });
    }

    // This is now the MASTER main method for your entire project!
    public static void main(String[] args) {
        // Apply a clean Look and Feel if supported by the OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}