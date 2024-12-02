package gui;
import models.Manager;
import models.ProxyFiles.ProxyReciptionistDataFetcher;
import models.Receptionist;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManagerFrame extends JFrame {

    private final Manager manager;
    private ProxyReciptionistDataFetcher proxyObjReciptionistFetcher ;
    public ManagerFrame(Manager manager) {
        this.manager = manager;
        proxyObjReciptionistFetcher = new ProxyReciptionistDataFetcher();


        setTitle("Manager Dashboard");
        setSize(800, 600); // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Header label
        JLabel welcomeLabel = new JLabel("Admon Page...", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Scrollable panel for listing receptionists
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); // Vertically stack components

        // Fetch receptionist data
        List<Receptionist> receptionistsList = proxyObjReciptionistFetcher.fetchReceptionists() ;

        for (Receptionist receptionist : receptionistsList) {
            JPanel itemPanel = createReceptionistPanel(receptionist);
            listPanel.add(itemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Log Out button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> {
            dispose();
            new HomePage(); // Re-open the home page
        });
        mainPanel.add(logoutButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the ManagerFrame visible
    }

    private JPanel createReceptionistPanel(Receptionist receptionist) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.setMaximumSize(new Dimension(750, 50)); // Limit the width of each item panel

        JLabel nameLabel = new JLabel(receptionist.getUserName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(nameLabel);

        JButton viewButton = createButton("View Profile", e -> viewProfile(receptionist));
        //JButton editButton = createButton("Edit", e -> editReceptionist(receptionist));
        JButton deleteButton = createButton("Delete", e -> deleteReceptionist(receptionist));

        panel.add(viewButton);
      //  panel.add(editButton);
        panel.add(deleteButton);

        return panel;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(action);
        return button;
    }

    private void viewProfile(Receptionist receptionist) {
        manager.viewReceptionistDetails(receptionist.getUserName());
        JOptionPane.showMessageDialog(this, "Name: " + receptionist.getUserName() +
                "\nJob Title: " + receptionist.getRole() +
                "\nSalary: " + receptionist.getSalary(), "Receptionist Profile", JOptionPane.INFORMATION_MESSAGE);
    }

//    private void editReceptionist(Receptionist receptionist) {
//        String newJobTitle = JOptionPane.showInputDialog(this, "Enter new job title:", receptionist.getRole());
//        String newSalaryStr = JOptionPane.showInputDialog(this, "Enter new salary:", receptionist.getSalary());
//        if (newJobTitle != null && newSalaryStr != null) {
//            try {
//                int newSalary = Integer.parseInt(newSalaryStr);
//                manager.editReceptionist(receptionist.getUserName(), newJobTitle, newSalary);
//                JOptionPane.showMessageDialog(this, "Receptionist updated successfully!");
//            } catch (NumberFormatException e) {
//                JOptionPane.showMessageDialog(this, "Invalid salary value!", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }

    private void deleteReceptionist(Receptionist receptionist) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + receptionist.getUserName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            manager.deleteReceptionist(receptionist.getUserName());
            JOptionPane.showMessageDialog(this, "Receptionist deleted successfully!");
            dispose();
            new ManagerFrame(manager); // Refresh the frame
        }
    }
}
