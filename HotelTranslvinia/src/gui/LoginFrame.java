package gui;
import main.Database;
import models.Manager;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(600, 600); // Increased size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel appNameLabel = new JLabel("Welcome to Hotel Translvinia");
        // Use a GridBagLayout for precise positioning and centering
        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        placeComponents(panel);

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Email Label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        // Email Text Field
        JTextField emailText = new JTextField(20);
        emailText.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailText, gbc);

        // Username Label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        // Username Text Field
        JTextField userText = new JTextField(20);
        userText.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(userText, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        // Password Text Field
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setFont(new Font("Arial", Font.PLAIN, 16)); // Make the font larger
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordText, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16)); // Make the font larger and bold
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailText.getText();  // Get email entered by user
            String username = userText.getText(); // Get username entered by user
            String password = new String(passwordText.getPassword()); // Get password entered by user

            // Validate user credentials from the database
            try (Connection connection = Database.getConnection()) {
                // SQL query to check both email and username
                String query = "SELECT * FROM users WHERE email = ? OR userName = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, email);  // Set email parameter
                    stmt.setString(2, username); // Set username parameter

                    ResultSet rs = stmt.executeQuery(); // Execute the query

                    if (rs.next()) { // If user is found in the database
                        String storedPassword = rs.getString("password");
                        String role = rs.getString("role"); // Retrieve the role (admin/receptionist)

                        // If passwords match
                        if (storedPassword.equals(password)) {
                            if ("admin".equals(role)) {
                                JOptionPane.showMessageDialog(this, "Admin logged in successfully!");
                                Manager manager = Manager.getInstance(); // Create Manager instance
                                new ManagerFrame(manager); // Pass the Manager instance to ManagerFrame
                                dispose();
                            } else if ("receptionist".equals(role)) {
                                JOptionPane.showMessageDialog(this, "Receptionist logged in successfully!");
                                // Navigate to receptionist dashboard (implement this page)

                                new ReceptionistFrame(); // Example class for receptionist dashboard
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "Unknown role!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid password!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "User not found!");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });
    }

}
