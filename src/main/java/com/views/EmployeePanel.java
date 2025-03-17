package com.views;

import com.component.ButtonEditor;
import com.component.ButtonRenderer;
import com.mapper.EmployeeMapper;
import com.pojo.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeePanel extends JPanel {
    private DefaultTableModel tableModel;
    private EmployeeMapper employeeMapper;
    private JTable table;
    private JTextField searchField;
    private String[] columns = {"ID", "Username", "Name", "Phone", "Gender", "Create Time", "Actions"};

    public EmployeePanel() {
        employeeMapper = EmployeeMapper.instance;
        setLayout(new BorderLayout());

        // Create Search Bar Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        //add search panel at top
        add(searchPanel, BorderLayout.NORTH);

        // Create Table Model
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only "Actions" column is editable
            }
        };

        // Create JTable
        table = new JTable(tableModel);
        table.setRowHeight(35); // Set row height to fit buttons properly
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(table, employeeMapper, new EmployeeForm(this)));
        table.getColumn("Actions").setMinWidth(180);

        // Wrap JTable inside JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the button and align center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Employee");
        buttonPanel.add(addButton);

        // Add button panel at the bottom
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshTable(null);

        // Add button action
        addButton.addActionListener(e -> {
            EmployeeForm form = new EmployeeForm(this);
            form.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            refreshTable(keyword);
        });

        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable(null);
        });
    }

    public void refreshTable(String keyword) {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Fetch new data
        List<Employee> employeeList = new ArrayList<>();
        if (keyword != null) {
            employeeList = employeeMapper.findByUsername(keyword);
        } else {
            employeeList = employeeMapper.findAll();
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Add rows to table model
        for (Employee employee : employeeList) {
            tableModel.addRow(new Object[]{
                    employee.getId(),
                    employee.getUserName(),
                    employee.getName(),
                    employee.getPhone(),
                    employee.getStringGender(),
                    employee.getCreateTime().format(formatter),
                    "Actions"
            });
        }

        // Notify the table model that data has changed
        tableModel.fireTableDataChanged();
    }
}

