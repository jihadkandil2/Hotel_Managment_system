package gui;

import models.Factory.RoomFactory;
import models.ProxyFiles.ProxyResidentDataFetcher;
import models.Receptionist;
import models.Resident;
import models.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceptionistGUI {

    private JFrame frame;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField durationField;
    private JTextField serviceField;
    private JTextField roomTypeField;
    private JTextArea displayArea;
    private Receptionist receptionist;

    public ReceptionistGUI() {
        receptionist = new Receptionist();
        frame = new JFrame("Hotel Management System");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Resident Information"));

        inputPanel.add(new JLabel("Resident Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Duration of Stay (in days):"));
        durationField = new JTextField();
        inputPanel.add(durationField);

        inputPanel.add(new JLabel("Service Type (Full/Half/Bed & Breakfast):"));
        serviceField = new JTextField();
        inputPanel.add(serviceField);


        inputPanel.add(new JLabel("Room Type (Single/Double/Triple):"));
        roomTypeField = new JTextField();
        inputPanel.add(roomTypeField);

        frame.add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton checkInButton = new JButton("Check In");
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckIn();
            }
        });
        buttonPanel.add(checkInButton);

        JButton editButton = new JButton("Edit Resident");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEditResident();
            }
        });
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Resident");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteResident();
            }
        });
        buttonPanel.add(deleteButton);

        JButton viewButton = new JButton("View Residents");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewResidents();
            }
        });
        buttonPanel.add(viewButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void handleCheckIn() {
        displayArea.setText("");

        String name = nameField.getText();
        String phone = phoneField.getText();
        String durationText = durationField.getText();
        String service = serviceField.getText();
        String roomType = roomTypeField.getText();


        StringBuilder missingFields = new StringBuilder();

        if (name.isEmpty()) {
            missingFields.append("Resident Name, ");
        }
        if (phone.isEmpty()) {
            missingFields.append("Phone Number, ");
        }
        if (durationText.isEmpty()) {
            missingFields.append("Duration of Stay, ");
        }
        if (roomType.isEmpty()) {
            missingFields.append("Room Type, ");
        }

        if (missingFields.length() > 0) {

            missingFields.setLength(missingFields.length() - 2);
            JOptionPane.showMessageDialog(frame, "Please fill in the following required fields: " + missingFields.toString());
            return;
        }


        if (service.isEmpty()) {
            service = "Bed & Breakfast";
        }


        if (!roomType.equalsIgnoreCase("Single") && !roomType.equalsIgnoreCase("Double") && !roomType.equalsIgnoreCase("Triple")) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid room type (Single/Double/Triple).");
            return;
        }

        if (!service.equalsIgnoreCase("Full") && !service.equalsIgnoreCase("Half") && !service.equalsIgnoreCase("Bed & Breakfast")) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid service type (Full/Half/Bed & Breakfast).");
            return;
        }

        try {
            int duration = Integer.parseInt(durationText);
            Room room = RoomFactory.CreateRoomType(roomType);

            Resident resident = new Resident(name, phone, duration);
            room.setRoomType(roomType);
            resident.setServiceType(service);
            resident.setAssignedRoom(room);

            receptionist.residentCheckIn(resident);


            if (resident.getTotalCost() > 0) {
                displayArea.setText("Resident Checked In Successfully!\n");
                displayArea.append("Name: " + name + "\n");
                displayArea.append("Phone: " + phone + "\n");
                displayArea.append("Duration: " + duration + " days\n");
                displayArea.append("Service Type: " + service + "\n");
                displayArea.append("Room Type: " + roomType + "\n");


            } else {

                displayArea.setText("No available room for the requested type: " + roomType + ".\n");
            }
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration.");
        }
    }

    private void handleEditResident() {
        displayArea.setText("");

        String oldName = nameField.getText();
        String newName = JOptionPane.showInputDialog(frame, "Enter new name:");
        String newPhone = JOptionPane.showInputDialog(frame, "Enter new phone number:");
        if (oldName.isEmpty() || newName.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the resident's name, new name, and new phone to edit.");
            return;
        }

        ProxyResidentDataFetcher proxyFetcher = new ProxyResidentDataFetcher();
        Resident residentToEdit = null;
        for (Resident resident : proxyFetcher.fetchResidents()) {
            if (resident.getResidentName().equals(oldName)) {
                residentToEdit = resident;
                break;
            }
        }

        if (residentToEdit != null) {
            receptionist.editResident(residentToEdit, newName, newPhone);
            JOptionPane.showMessageDialog(frame, "Resident info updated successfully.");

            clearInputFields();
            displayArea.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Resident not found.");
            clearInputFields();
            displayArea.setText("");
        }
    }

    private void handleDeleteResident() {
        displayArea.setText("");

        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the resident's name to delete.");
            return;
        }

        ProxyResidentDataFetcher proxyFetcher = new ProxyResidentDataFetcher();
        Resident residentToDelete = null;
        for (Resident resident : proxyFetcher.fetchResidents()) {
            if (resident.getResidentName().equals(name)) {
                residentToDelete = resident;
                break;
            }
        }

        if (residentToDelete != null) {
            receptionist.deleteResident(name);
            JOptionPane.showMessageDialog(frame, "Resident deleted.");
            clearInputFields();
            displayArea.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Resident not found.");
            clearInputFields();
            displayArea.setText("");
        }
    }

    private void handleViewResidents() {
        displayArea.setText("");

        String residentsData = receptionist.viewResidentDetails();
        displayArea.setText(residentsData);

        clearInputFields();
    }


    private void clearInputFields() {
        nameField.setText("");
        phoneField.setText("");
        durationField.setText("");
        serviceField.setText("");
        roomTypeField.setText("");
    }

    public static void main(String[] args) {
        new ReceptionistGUI();
    }
}
