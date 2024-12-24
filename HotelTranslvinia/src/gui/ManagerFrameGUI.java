package gui;

import models.Manager;
import models.Receptionist;
import models.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class ManagerFrameGUI extends JFrame {

    private Manager manager;

    public ManagerFrameGUI(Manager manager) {
        this.manager = manager;
        setTitle("Hotel Manager Admin Page");
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

        JTable receptionistTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(receptionistTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.NORTH);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            int salary = Integer.parseInt(salaryField.getText());
            String email = emailField.getText();
            String role = (String) roleDropdown.getSelectedItem();
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

            viewButton.doClick(); // Refresh the table
        });

        viewButton.addActionListener(e -> {
            List<Receptionist> receptionists = manager.viewAllReciptionist();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Phone", "Salary", "Email", "Role", "Edit", "Delete"}, 0);
            receptionistTable.setModel(model);

            for (Receptionist receptionist : receptionists) {
                Object[] row = new Object[]{
                        receptionist.getUserName(),
                        receptionist.getPhone(),
                        receptionist.getSalary(),
                        receptionist.getEmail(),
                        receptionist.getRole(),
                        "Edit", // Button for editing
                        "Delete" // Button for deleting
                };

                model.addRow(row);
            }

            // Set the buttons for Edit and Delete columns
            receptionistTable.getColumn("Edit").setCellRenderer(new ButtonRenderer("Edit"));
            receptionistTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), receptionistTable, "edit", viewButton));
            receptionistTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
            receptionistTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), receptionistTable, "delete", viewButton));
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
            DefaultTableModel model = new DefaultTableModel(new String[]{"Room Number", "Type", "Price", "Occupied" , "Resident Name"}, 0);

            for (Room room : rooms) {
                model.addRow(new Object[]{room.getRoomNum(), room.getRoomType(), room.getRoomPrice(), room.getIsOccupied(), room.getAssignedResident().getResidentName()});
            }
            roomTable.setModel(model);
        });

        return panel;
    }

    private JPanel createIncomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(3, 2));

        formPanel.add(new JLabel("Select Range:"));
        JComboBox<String> rangeBox = new JComboBox<>(new String[]{"Weekly", "Monthly", "Annual"});
        formPanel.add(rangeBox);

        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField();
        formPanel.add(startDateField);

        JButton calculateButton = new JButton("Calculate Income");
        formPanel.add(calculateButton);

        JLabel incomeLabel = new JLabel("Total Income: $0.0", SwingConstants.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(incomeLabel, BorderLayout.CENTER);

        calculateButton.addActionListener(e -> {
            String range = (String) rangeBox.getSelectedItem();
            Date startDate = Date.valueOf(startDateField.getText());
            double income = manager.trackHotelIncome(range, startDate);

            incomeLabel.setText("Total Income: $" + income);
        });

        return panel;
    }

    // This will be used for rendering the buttons in the table cell
    public class ButtonRenderer extends JButton implements TableCellRenderer {
        private String label;

        public ButtonRenderer(String label) {
            this.label = label;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(label);
            return this;
        }
    }

    // This will handle the button actions (Edit and Delete) inside the table
    public class ButtonEditor extends DefaultCellEditor {
        private JTable table;
        private JButton button;
        private String label;
        private String action;
        private JButton viewButton;

        public ButtonEditor(JCheckBox checkBox, JTable table, String action, JButton viewButton) {
            super(checkBox);
            this.table = table;
            this.action = action;
            this.viewButton = viewButton;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                label = table.getValueAt(row, 0).toString(); // Get the value in the first column (name)

                if (action.equals("edit")) {
                    // Edit button action
                    String newPhone = JOptionPane.showInputDialog("Enter new phone:", table.getValueAt(row, 1).toString());
                    int newSalary = Integer.parseInt(JOptionPane.showInputDialog("Enter new salary:", table.getValueAt(row, 2).toString()));
                    String newPassword = JOptionPane.showInputDialog("Enter new password:", table.getValueAt(row, 4).toString());

                    Receptionist receptionist = new Receptionist();
                    receptionist.setUserName(label);
                    receptionist.setPhone(newPhone);
                    receptionist.setSalary(newSalary);
                    receptionist.setPassword(newPassword);

                    manager.editReceptionist(receptionist);

                    // Refresh the table
                    viewButton.doClick();
                } else if (action.equals("delete")) {
                    // Delete button action
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        manager.deleteReceptionist(label);

                        // Refresh the table by reloading the data
                        viewButton.doClick();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

    public static void main(String[] args) {
        Manager manager = Manager.getInstance();
        new ManagerFrameGUI(manager);
    }
}
