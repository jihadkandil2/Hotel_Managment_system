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
    private JTextField roomTypeField;  // حقل نوع الغرفة الجديد
    private JTextArea displayArea;
    private Receptionist receptionist;

    public ReceptionistGUI() {
        receptionist = new Receptionist();
        frame = new JFrame("Hotel Management System");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setLayout(new BorderLayout()); // تقسيم الشاشة إلى أجزاء

        // الجزء العلوي لادخال البيانات
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2)); // زيادة عدد الصفوف في الشبكة
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

        // إضافة حقل جديد لنوع الغرفة
        inputPanel.add(new JLabel("Room Type (Single/Double/Triple):"));
        roomTypeField = new JTextField();
        inputPanel.add(roomTypeField);

        frame.add(inputPanel, BorderLayout.NORTH);

        // الجزء الأوسط لعرض البيانات
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // الجزء السفلي للأزرار
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
        String name = nameField.getText();
        String phone = phoneField.getText();
        String durationText = durationField.getText();
        String service = serviceField.getText();
        String roomType = roomTypeField.getText();

        if (name.isEmpty() || phone.isEmpty() || durationText.isEmpty() || service.isEmpty() || roomType.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }

        // التحقق من صحة المدخلات
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
            Room room ;
            room = RoomFactory.CreateRoomType(roomType);

            Resident resident = new Resident(name, phone, duration);
            room.setRoomType(roomType);
            resident.setServiceType(service);
            resident.setAssignedRoom(room);

            receptionist.residentCheckIn(resident);  // إرسال المقيم لتسجيل الدخول

            // التحقق مما إذا تم إضافة المقيم بنجاح
            if (resident.getTotalCost() > 0) {
                displayArea.setText("Resident Checked In Successfully!\n");
                displayArea.append("Name: " + name + "\n");
                displayArea.append("Phone: " + phone + "\n");
                displayArea.append("Duration: " + duration + " days\n");
                displayArea.append("Service Type: " + service + "\n");
                displayArea.append("Room Type: " + roomType + "\n");
            } else {
                // في حالة عدم وجود غرفة
                displayArea.setText("No available room for the requested type: " + roomType + ".\n");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration.");
        }
    }


    private void handleEditResident() {
        String oldName = nameField.getText();  // الاسم القديم الذي سيتم تعديله
        String newName = JOptionPane.showInputDialog(frame, "Enter new name:");  // إدخال الاسم الجديد
        String newPhone = JOptionPane.showInputDialog(frame, "Enter new phone number:");  // إدخال رقم الهاتف الجديد

        if (oldName.isEmpty() || newName.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the resident's name, new name, and new phone to edit.");
            return;
        }

        // العثور على المقيم حسب الاسم القديم عبر ProxyResidentDataFetcher
        ProxyResidentDataFetcher proxyFetcher = new ProxyResidentDataFetcher();  // تأكد من أن هذه الكائن يتم استخدامه
        Resident residentToEdit = null;
        for (Resident resident : proxyFetcher.fetchResidents()) {
            if (resident.getResidentName().equals(oldName)) {
                residentToEdit = resident;
                break;
            }
        }

        if (residentToEdit != null) {
            // تعديل المقيم في قاعدة البيانات
            receptionist.editResident(residentToEdit, newName, newPhone);
            JOptionPane.showMessageDialog(frame, "Resident info updated successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Resident not found.");
        }
    }


    private void handleDeleteResident() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the resident's name to delete.");
            return;
        }
       // Edit here [if name doesnot exist do not display the message box of resident deleted]
        receptionist.deleteResident(name);  // تمرير 'frame' هنا
        JOptionPane.showMessageDialog(frame, "Resident deleted.");
    }


    private void handleViewResidents() {
        String residentsData = receptionist.viewResidentDetails();
        displayArea.setText(residentsData);
    }

    public static void main(String[] args) {
        new ReceptionistGUI();
    }
}
