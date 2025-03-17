package com.views;

import com.form.FormInterface;
import com.mapper.DailyRoomStatusMapper;
import com.mapper.RoomTypeMapper;
import com.pojo.DailyRoomStatus;
import com.pojo.RoomType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyRoomStatusForm extends FormInterface {
    private Long id;
    private LocalDate date;
    private int availableCount;
    private Long roomTypeId;
    private JSpinner priceSpinner;
    private DailyRoomStatusPanel dailyRoomStatusPanel;

    public DailyRoomStatusForm(DailyRoomStatusPanel dailyRoomStatusPanel) {
        this.dailyRoomStatusPanel = dailyRoomStatusPanel;

        setTitle("Daily Room Status Form");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        //Price
        JLabel priceLabel = new JLabel("Price:");
        priceSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 10000.0, 10.0)); // Min: 0, Max: 10000, Step: 10
        JSpinner.NumberEditor priceEditor = new JSpinner.NumberEditor(priceSpinner, "0.00");
        priceSpinner.setEditor(priceEditor);


        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Layout Setup
        gbc.gridx = 0; gbc.gridy = 0; panel.add(priceLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(priceSpinner, gbc);

        // Bottom panel for buttons
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Add action listeners
        saveButton.addActionListener(e -> {

            double price = (double) priceSpinner.getValue();


            if(id == null){
                //insert new room type
                DailyRoomStatus dailyRoomStatus = new DailyRoomStatus(null, date, price, availableCount, 0, roomTypeId);
                DailyRoomStatusMapper.instance.insert(dailyRoomStatus);

                JOptionPane.showMessageDialog(this, "Daily Room Status Added:\n" +
                        "Date: " + date + "\n" +
                        "Price: RM" + price + "\n" +
                        "Available Count: " + availableCount + "\n" +
                        "Room Type Id: " + roomTypeId);
            } else {
                //update room type
                DailyRoomStatus dailyRoomStatus = new DailyRoomStatus(id, null, price, 0, 0, 0L);
                DailyRoomStatusMapper.instance.updatePrice(dailyRoomStatus);

                JOptionPane.showMessageDialog(this, "Daily Room Status Updated:\n" +
                        "Id: " + id + "\n" +
                        "Price: RM" + price);
            }

            // Refresh the table
            if (dailyRoomStatusPanel != null) {
                if(date != null){
                    dailyRoomStatusPanel.refreshTable(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    dailyRoomStatusPanel.refreshTable();
                }
            }

            dispose(); // Close the form after saving
        });

        cancelButton.addActionListener(e -> dispose());

        // Add to frame
        add(panel);
    }

    @Override
    public void setValues(JTable table, int row) {
        this.id = null;
        this.date = null;
        priceSpinner.setValue(0.0);

        if(table.getValueAt(row, 0) != "not set"){
            //edit
            this.id = (Long) table.getValueAt(row, 0);
            priceSpinner.setValue(table.getValueAt(row, 4));
        }else{
            //add
            this.date = LocalDate.parse(table.getValueAt(row, 1).toString(), DateTimeFormatter.ISO_LOCAL_DATE);
            this.availableCount =(int) table.getValueAt(row, 6);
            this.roomTypeId = (Long) table.getValueAt(row, 2);
            priceSpinner.setValue(table.getValueAt(row, 5));
        }
    }
}