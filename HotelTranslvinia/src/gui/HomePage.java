package gui;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        // Set up the frame
        setTitle("Hotel Transylvania - Home");
        setSize(600, 600); // Frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Use BorderLayout to arrange components
        add(panel);

        // Add the welcome text to the center
        JLabel welcomeLabel = new JLabel("Welcome to Hotel Transylvania", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font style and size
        panel.add(welcomeLabel, BorderLayout.CENTER);

        // Create a panel to hold the buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Layout for buttons

        // Create Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font style and size
        loginButton.addActionListener(e -> {
            // Handle login button click (for now just print to console)
            // Create an instance of LoginFrame when the button is clicked
            new LoginFrame();
            // Close the current HomePage window
            dispose();

            System.out.println("Login button clicked!");
            // You can redirect to Login page later
        });



        // Add buttons to the button panel
        buttonPanel.add(loginButton);

        // Add button panel to the frame
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true); // Make the frame visible
    }

    public static void main(String[] args) {
        new HomePage(); // Run the HomePage when the program starts
    }
}