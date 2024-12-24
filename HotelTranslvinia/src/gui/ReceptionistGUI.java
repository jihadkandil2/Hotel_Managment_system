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
        serviceField = new JTextField(); // يمكن تركه فارغًا
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
        displayArea.setText(""); // تفريغ منطقة العرض قبل تنفيذ العملية

        String name = nameField.getText();
        String phone = phoneField.getText();
        String durationText = durationField.getText();
        String service = serviceField.getText();
        String roomType = roomTypeField.getText();

        // التحقق من وجود قيم في الحقول الإلزامية
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
            // إزالة آخر فاصلة
            missingFields.setLength(missingFields.length() - 2);
            JOptionPane.showMessageDialog(frame, "Please fill in the following required fields: " + missingFields.toString());
            return;
        }

        // إذا كانت حقل الخدمة فارغًا، تعيينه إلى قيمة افتراضية
        if (service.isEmpty()) {
            service = "Bed & Breakfast"; // الخدمة الافتراضية
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
            Room room = RoomFactory.CreateRoomType(roomType);

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

                // تفريغ الحقول بعد تسجيل الدخول
               // clearInputFields();
            } else {
                // في حالة عدم وجود غرفة
                displayArea.setText("No available room for the requested type: " + roomType + ".\n");
            }
            clearInputFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number for duration.");
        }
    }

    private void handleEditResident() {
        displayArea.setText(""); // تفريغ منطقة العرض قبل تنفيذ العملية

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

            // تفريغ الحقول بعد التعديل
            clearInputFields();
            // تفريغ منطقة العرض بعد التعديل
            displayArea.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Resident not found.");
            clearInputFields();  // تفريغ الحقول في حالة عدم العثور على المقيم
            displayArea.setText("");  // تفريغ منطقة العرض
        }
    }

    private void handleDeleteResident() {
        displayArea.setText(""); // تفريغ منطقة العرض قبل تنفيذ العملية

        String name = nameField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the resident's name to delete.");
            return;
        }

        // التحقق مما إذا كان المقيم موجودًا في قاعدة البيانات
        ProxyResidentDataFetcher proxyFetcher = new ProxyResidentDataFetcher();
        Resident residentToDelete = null;
        for (Resident resident : proxyFetcher.fetchResidents()) {
            if (resident.getResidentName().equals(name)) {
                residentToDelete = resident;
                break;
            }
        }

        if (residentToDelete != null) {
            // حذف المقيم
            receptionist.deleteResident(name);
            JOptionPane.showMessageDialog(frame, "Resident deleted.");
            clearInputFields();  // تفريغ الحقول بعد الحذف
            displayArea.setText("");  // تفريغ منطقة العرض بعد الحذف
        } else {
            JOptionPane.showMessageDialog(frame, "Resident not found.");
            clearInputFields();  // تفريغ الحقول في حالة عدم العثور على المقيم
            displayArea.setText("");  // تفريغ منطقة العرض في حالة عدم العثور على المقيم
        }
    }

    private void handleViewResidents() {
        displayArea.setText(""); // تفريغ منطقة العرض قبل تنفيذ العملية

        String residentsData = receptionist.viewResidentDetails();
        displayArea.setText(residentsData);
        // تفريغ الحقول بعد عرض البيانات
        clearInputFields();
    }

    // دالة لتفريغ الـ input fields بعد الحذف أو عدم العثور على المقيم
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
