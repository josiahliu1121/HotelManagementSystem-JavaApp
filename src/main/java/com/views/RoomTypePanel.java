package com.views;

import com.component.ButtonEditor;
import com.component.ButtonRenderer;
import com.mapper.RoomTypeMapper;
import com.pojo.RoomType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class RoomTypePanel extends JPanel {
    private DefaultTableModel tableModel;
    private RoomTypeMapper roomTypeMapper;
    private JTable table;
    private String[] columns = {"ID", "Room Name", "Suggested Price", "Count", "Description", "Actions"};

    public RoomTypePanel() {
        roomTypeMapper = RoomTypeMapper.instance;
        setLayout(new BorderLayout());

        // Create Table Model
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only "Actions" column is editable
            }
        };

        // Create JTable
        table = new JTable(tableModel);
        table.setRowHeight(35); // Set row height to fit buttons properly
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(table, roomTypeMapper, new RoomTypeForm(this)));
        table.getColumn("Actions").setMinWidth(180);

        // Wrap JTable inside JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the button and align center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Room Type");
        buttonPanel.add(addButton);

        // Add button panel at the bottom
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshTable();

        // Add button action
        addButton.addActionListener(e -> {
            RoomTypeForm form = new RoomTypeForm(this);
            form.setVisible(true);
        });
    }

    public void refreshTable() {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Fetch new data
        List<RoomType> roomTypeList = roomTypeMapper.findAll();

        // Add rows to table model
        for (RoomType roomType : roomTypeList) {
            tableModel.addRow(new Object[]{
                    roomType.getId(),
                    roomType.getRoomName(),
                    roomType.getSuggestedPrice(),
                    roomType.getCount(),
                    roomType.getDescription(),
                    "Actions"
            });
        }

        // Notify the table model that data has changed
        tableModel.fireTableDataChanged();
    }
}


