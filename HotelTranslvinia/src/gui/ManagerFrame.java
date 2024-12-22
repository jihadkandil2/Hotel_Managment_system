package gui;

import models.Manager;
import models.Receptionist;
import models.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.List;

public class ManagerFrame extends JFrame {

    private  Manager manager;

    public ManagerFrame(Manager manager) {
        this.manager = manager;
        setTitle("Hotel Manager Admon Page");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Receptionist Management", createReceptionistPanel());
        tabbedPane.addTab("Room Management", createRoomPanel());
        tabbedPane.addTab("Income Tracking", createIncomePanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createReceptionistPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top Section: Input Fields
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField();
        inputPanel.add(salaryField);

        inputPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Role:"));
        JComboBox<String> roleDropdown = new JComboBox<>(new String[]{"", "receptionist", "admin"});
        inputPanel.add(roleDropdown);

        inputPanel.add(new JLabel("Password:"));
        JTextField passwordField = new JTextField();
        inputPanel.add(passwordField);

        JButton addButton = new JButton("Add Receptionist");
        inputPanel.add(addButton);

        JButton viewButton = new JButton("View All Receptionists");
        inputPanel.add(viewButton);

        // Center Section: Table
        JTable receptionistTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(receptionistTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.NORTH);

        // Button Actions
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            int salary = Integer.parseInt(salaryField.getText());
            String email = emailField.getText();
            String role = (String) roleDropdown.getSelectedItem(); // Get selected role
            String password = passwordField.getText();

            if (role == null || role.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a valid role.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Receptionist receptionist = new Receptionist();
            receptionist.setUserName(name);
            receptionist.setPhone(phone);
            receptionist.setSalary(salary);
            receptionist.setEmail(email);
            receptionist.setRole(role);
            receptionist.setPassword(password);

            manager.addReceptionist(receptionist);
            JOptionPane.showMessageDialog(this, "Receptionist added successfully!");
        });

        viewButton.addActionListener(e -> {
            List<Receptionist> receptionists = manager.viewAllReciptionist();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Phone", "Salary", "Email", "Role"}, 0);

            for (Receptionist receptionist : receptionists) {
                model.addRow(new Object[]{
                        receptionist.getUserName(),
                        receptionist.getPhone(),
                        receptionist.getSalary(),
                        receptionist.getEmail(),
                        receptionist.getRole()
                });
            }
            receptionistTable.setModel(model);
        });

        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton fetchRoomsButton = new JButton("Fetch Hotel Rooms");
        panel.add(fetchRoomsButton, BorderLayout.NORTH);

        JTable roomTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(roomTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        fetchRoomsButton.addActionListener(e -> {
            List<Room> rooms = manager.fetchHotelsRooms();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Room Number", "Type", "Price", "Occupied"}, 0);

            for (Room room : rooms) {
                model.addRow(new Object[]{room.getRoomNum(), room.getRoomType(), room.getRoomPrice(), room.getIsOccupied()});
            }
            roomTable.setModel(model);
        });

        return panel;
    }

    private JPanel createIncomePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Select Range:"));
        JComboBox<String> rangeBox = new JComboBox<>(new String[]{"Weekly", "Monthly", "Annual"});
        panel.add(rangeBox);

        panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField();
        panel.add(startDateField);

        JButton calculateButton = new JButton("Calculate Income");
        panel.add(calculateButton);

        JLabel incomeLabel = new JLabel("Total Income: $0.0");
        panel.add(incomeLabel);

        calculateButton.addActionListener(e -> {
            String range = (String) rangeBox.getSelectedItem();
            Date startDate = Date.valueOf(startDateField.getText());
            double income = manager.trackHotelIncome(range, startDate);

            incomeLabel.setText("Total Income: $" + income);
        });

        return panel;
    }

    public static void main(String[] args) {
        // Get the singleton instance of Manager
        Manager manager = Manager.getInstance();
        new ManagerFrame(manager);
    }
}
