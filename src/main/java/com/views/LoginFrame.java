package com.views;

import com.mapper.EmployeeMapper;
import com.pojo.Employee;
import com.user.LoginUserInfo;
import com.utils.MD5Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;
    private List<Employee> employees;

    public LoginFrame() {
        //get employee list from database
        employees = EmployeeMapper.instance.findAll();

        setTitle("Login");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        //prefill username and password for fast testing
        usernameField.setText("admin");
        passwordField.setText("123");

        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(usernameLabel, gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
                    dispose(); // Close the login frame after success
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> System.exit(0));
    }

    private boolean authenticate(String username, String password) {
        Employee employee = employees.stream().filter(e -> e.getUserName().equals(username)).findFirst().orElse(null);

        if (employee != null) {
            if (employee.getPassword().equals(MD5Util.md5(password))){
                LoginUserInfo.employee = employee;
                return true;
            }
        }

        return false;
    }
}
