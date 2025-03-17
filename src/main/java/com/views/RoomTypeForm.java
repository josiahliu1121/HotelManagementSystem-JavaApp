package com.views;

import com.form.FormInterface;
import com.mapper.RoomTypeMapper;
import com.pojo.RoomType;

import javax.swing.*;
import java.awt.*;

public class RoomTypeForm extends FormInterface {
    private Long id;
    private JTextField nameField;
    private JSpinner priceSpinner;
    private JSpinner countSpinner;
    private JTextArea descriptionArea;
    private RoomTypePanel roomTypePanel;

    public RoomTypeForm(RoomTypePanel roomTypePanel) {
        this.roomTypePanel = roomTypePanel;

        setTitle("Room Type Form");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Room Name
        JLabel nameLabel = new JLabel("Room Name:");
        nameField = new JTextField(20);

        // Suggested Price
        JLabel priceLabel = new JLabel("Suggested Price:");
        priceSpinner = new JSpinner(new SpinnerNumberModel(0, 0.0, 10000.0, 10.0)); // Min: 0, Max: 10000, Step: 10
        JSpinner.NumberEditor priceEditor = new JSpinner.NumberEditor(priceSpinner, "0.00");
        priceSpinner.setEditor(priceEditor);

        // Count
        JLabel countLabel = new JLabel("Count:");
        countSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Min: 1, Max: 100, Step: 1

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Layout Setup
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(priceLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(priceSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(countLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(countSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(descriptionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(descriptionScroll, gbc);

        // Bottom panel for buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Add action listeners
        saveButton.addActionListener(e -> {
            String roomName = nameField.getText();
            double suggestedPrice = (double) priceSpinner.getValue();
            int count = (int) countSpinner.getValue();
            String description = descriptionArea.getText();

            //validate the form
            if(!validateFields(roomName)) {
                throw new IllegalArgumentException("Invalid input");
            }

            if(id == null){
                //insert new room type
                RoomTypeMapper.instance.insert(new RoomType(null, roomName, suggestedPrice, count, description));

                JOptionPane.showMessageDialog(this, "Room Added:\n" +
                        "Name: " + roomName + "\n" +
                        "Price: RM" + suggestedPrice + "\n" +
                        "Count: " + count + "\n" +
                        "Description: " + description);
            } else {
                //update room type
                RoomTypeMapper.instance.update(new RoomType(id, roomName, suggestedPrice, count, description));

                JOptionPane.showMessageDialog(this, "Room Updated:\n" +
                        "Id: " + id + "\n" +
                        "Name: " + roomName + "\n" +
                        "Price: RM" + suggestedPrice + "\n" +
                        "Count: " + count + "\n" +
                        "Description: " + description);
            }

            // Refresh the table
            if (roomTypePanel != null) {
                roomTypePanel.refreshTable();
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
        nameField.setText(table.getValueAt(row, 1).toString());
        priceSpinner.setValue(table.getValueAt(row, 2));
        countSpinner.setValue(table.getValueAt(row, 3));
        descriptionArea.setText(table.getValueAt(row, 4).toString());
    }

    private boolean validateFields(String roomName) {
        if(roomName == null || roomName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a room name.");
            return false;
        }

        if(roomName.length() > 50) {
            JOptionPane.showMessageDialog(this, "Room name cannot be longer than 50 characters.");
            return false;
        }

        return true;
    }
}

