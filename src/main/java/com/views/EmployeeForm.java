package com.views;

import com.constant.Constant;
import com.form.FormInterface;
import com.mapper.EmployeeMapper;
import com.pojo.Employee;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeForm extends FormInterface {
    private Long id;
    private JTextField usernameField;
    private JTextField nameField;
    private JTextField phoneField;
    JComboBox<String> genderComboBox;
    private EmployeePanel employeePanel;

    public EmployeeForm(EmployeePanel employeePanel) {
        this.employeePanel = employeePanel;

        setTitle("Employee Form");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);

        JLabel genderLabel = new JLabel("Select Gender:");
        String[] genders = {"Male", "Female"};
        genderComboBox = new JComboBox<>(genders);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Layout Setup
        gbc.gridx = 0; gbc.gridy = 0; panel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(genderLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(genderComboBox, gbc);

        // Bottom panel for buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Add action listeners
        saveButton.addActionListener(e -> {
            String username = usernameField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();
            int gender = Employee.getStringInt(genderComboBox.getSelectedItem().toString());

            //validate the form
            if(!validateFields(username, name, phone)) {
                throw new IllegalArgumentException("Invalid input");
            }

            if(id == null){
                //insert employee
                EmployeeMapper.instance.insert(new Employee(null, username, Constant.PASSWORD, name, phone, gender, LocalDateTime.now()));

                JOptionPane.showMessageDialog(this, "Employee Added:\n" +
                        "Username: " + username + "\n" +
                        "Name: " + name + "\n" +
                        "Phone: " + phone + "\n" +
                        "Gender: " + gender);
            } else {
                //update employee
                EmployeeMapper.instance.update(new Employee(id, name, phone, gender));

                JOptionPane.showMessageDialog(this, "Employee Updated:\n" +
                        "Id: " + id + "\n" +
                        "Username: " + username + "\n" +
                        "Name: " + name + "\n" +
                        "Phone: " + phone + "\n" +
                        "Gender: " + gender);
            }

            // Refresh the table
            if (employeePanel != null) {
                employeePanel.refreshTable(null);
            }

            dispose(); // Close the form after saving
        });

        cancelButton.addActionListener(e -> dispose());

        // Add to frame
        add(panel);
    }

    @Override
    public void setValues(JTable table, int row) {
        this.id = (Long) table.getValueAt(row, 0);
        usernameField.setText(table.getValueAt(row, 1).toString());
        nameField.setText(table.getValueAt(row, 2).toString());
        phoneField.setText(table.getValueAt(row, 3).toString());
        genderComboBox.setSelectedItem(table.getValueAt(row, 4).toString());

        //username cannot be change
        usernameField.setEnabled(false);
    }

    private boolean validateFields(String username, String name, String phone) {
        if(username.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(employeePanel, "Please fill all the fields correctly.");
            return false;
        }

        if(username.length() > 50) {
            JOptionPane.showMessageDialog(employeePanel, "Username cannot be longer than 50 characters.");
            return false;
        }

        if(name.length() > 50) {
            JOptionPane.showMessageDialog(employeePanel, "Name cannot be longer than 50 characters.");
            return false;
        }

        Pattern phonePattern = Pattern.compile(Constant.PHONE_REGEX);
        if(!phonePattern.matcher(phone).matches() || phone.length() > 15) {
            JOptionPane.showMessageDialog(employeePanel, "Invalid phone number format! Please enter a valid number.");
            return false;
        }

        //Check if username is duplicate
        List<String> usernameList = EmployeeMapper.instance.findUserName();
        if(usernameList.contains(username)) {
            JOptionPane.showMessageDialog(employeePanel, "Username is already in use!");
            return false;
        }

        return true;
    }
}

