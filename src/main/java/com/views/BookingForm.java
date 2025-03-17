package com.views;

import com.constant.Constant;
import com.dto.DailyRoomStatusDTO;
import com.form.FormInterface;
import com.mapper.BookingMapper;
import com.mapper.DailyRoomStatusMapper;
import com.mapper.RoomTypeMapper;
import com.pojo.Booking;
import com.pojo.DailyRoomStatus;
import com.pojo.RoomType;
import com.user.LoginUserInfo;
import com.utils.DateLabelFormatter;
import org.apache.commons.beanutils.BeanUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class BookingForm extends FormInterface {
    private Long id;
    private Long roomTypeId;
    private Long dailyRoomStatusId;
    private List<DailyRoomStatusDTO> dailyRoomStatusDTOList;
    private BookingPanel bookingPanel;
    private JTextField nameField;
    private JTextField phoneField;
    private UtilDateModel model;
    private JDatePickerImpl datePicker;
    private JSpinner costSpinner;
    private JComboBox<String> roomTypeBox;
    private JSpinner adultSpinner;
    private JSpinner childSpinner;
    private JTextArea remarkArea;

    public BookingForm(BookingPanel bookingPanel) {
        this.bookingPanel = bookingPanel;

        setTitle("Booking Form");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Fields
        JLabel nameLabel = new JLabel("Customer Name:");
        nameField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(20);

        JLabel dateLabel = new JLabel("Date:");
        // Date picker setup
        model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeBox = new JComboBox<>(new String[]{"No available room"});

        JLabel costLabel = new JLabel("Cost:");
        costSpinner = new JSpinner(new SpinnerNumberModel(0, 0.0, 10000.0, 10.0)); // Min: 0, Max: 10000, Step: 10
        JSpinner.NumberEditor costEditor = new JSpinner.NumberEditor(costSpinner, "0.00");
        costSpinner.setEditor(costEditor);

        JLabel adultLabel = new JLabel("Adult:");
        adultSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));

        JLabel childLabel = new JLabel("Child:");
        childSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        JLabel remarkLabel = new JLabel("Remark:");
        remarkArea = new JTextArea(3, 20);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Adding components to panel
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(dateLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(datePicker, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(roomTypeLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(roomTypeBox, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(costLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(costSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(adultLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(adultSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 6; panel.add(childLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; panel.add(childSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 7; panel.add(remarkLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 7; panel.add(remarkScroll, gbc);

        // Bottom panel for buttons
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Add ChangeListener to detect date selection
        model.addChangeListener(e -> {
            //reset selected data
            dailyRoomStatusDTOList = null;
            dailyRoomStatusId = null;
            roomTypeId = null;

            Date selectedDate = model.getValue();
            if (selectedDate != null) {
                //get room daily status and room type data
                List<DailyRoomStatus> dailyRoomStatusList = DailyRoomStatusMapper.instance
                        .findByDate(selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                if(dailyRoomStatusList.isEmpty()) {
                    roomTypeBox.setModel(new DefaultComboBoxModel<>(new String[]{"No available room"}));
                    return;
                }

                List<RoomType> roomTypeList = RoomTypeMapper.instance.findAll();

                dailyRoomStatusDTOList = new ArrayList<>();
                dailyRoomStatusList.forEach(dailyRoomStatus -> {
                    DailyRoomStatusDTO dailyRoomStatusDTO = new DailyRoomStatusDTO();

                    try {
                        BeanUtils.copyProperties(dailyRoomStatusDTO, dailyRoomStatus);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }

                    RoomType roomType = roomTypeList.stream().filter(rt -> rt.getId().equals(dailyRoomStatus.getRoomTypeId())).findFirst().get();

                    dailyRoomStatusDTO.setRoomName(roomType.getRoomName());
                    dailyRoomStatusDTO.setSuggestedPrice(roomType.getSuggestedPrice());

                    dailyRoomStatusDTOList.add(dailyRoomStatusDTO);
                });

                //set room name for select
                String[] roomNames = new String[dailyRoomStatusDTOList.size()];
                for (int i = 0; i < roomNames.length; i++) {
                    roomNames[i] = dailyRoomStatusDTOList.get(i).getRoomName();
                }

                // **Update existing JComboBox model**
                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(roomNames);
                roomTypeBox.setModel(comboBoxModel);
            } else {
                roomTypeBox.setModel(new DefaultComboBoxModel<>(new String[]{"No available room"}));
            }
        });

        roomTypeBox.addActionListener(e -> {
            //get instance of the select room type
            String selectedRoomType = roomTypeBox.getSelectedItem().toString();
            DailyRoomStatusDTO dailyRoomStatusDTO = dailyRoomStatusDTOList.stream()
                    .filter(drs -> drs.getRoomName().equals(selectedRoomType)).findFirst().get();

            //save select item id
            this.dailyRoomStatusId = dailyRoomStatusDTO.getId();
            this.roomTypeId = dailyRoomStatusDTO.getRoomTypeId();

            //set the cost
            costSpinner.setValue(dailyRoomStatusDTO.getSuggestedPrice());
        });

        // Add action listeners
        saveButton.addActionListener(e -> {
            //validate the form
            if(!validateFields()) {
                throw new IllegalArgumentException("Invalid input");
            }

            //get the values
            String customerName = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            double cost = (double) costSpinner.getValue();
            LocalDate bookingDate = model.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int adult = (int) adultSpinner.getValue();
            int child = (int) childSpinner.getValue();
            String remarks = remarkArea.getText().trim();
            LocalDateTime now = LocalDateTime.now();

            if(id == null){
                //insert
                Booking booking = new Booking(null, customerName, phone, cost, bookingDate, adult, child, remarks,
                        dailyRoomStatusId, roomTypeId, LoginUserInfo.employee.getId(), now, now);

                BookingMapper.instance.insert(booking);

                JOptionPane.showMessageDialog(this, "Booking Updated:\n" +
                        "Id: " + id + "\n" +
                        "Customer Name: " + customerName + "\n" +
                        "Phone: " + phone + "\n" +
                        "Cost: " + cost + "\n" +
                        "Date: " + bookingDate + "\n" +
                        "Adult: " + adult + "\n" +
                        "Child: " + child + "\n" +
                        "Remarks: " + remarks + "\n" +
                        "Room Type Id: " + roomTypeId + "\n" +
                        "Daily Room Status Id: " + dailyRoomStatusId + "\n" +
                        "Employee Id: " + booking.getEmployeeId() + "\n" +
                        "Time: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                //update
                Booking booking = new Booking(id, customerName, phone, cost, null, adult, child, remarks,
                        null, null, LoginUserInfo.employee.getId(), null, now);

                BookingMapper.instance.update(booking);

                JOptionPane.showMessageDialog(this, "Booking Updated:\n" +
                        "Id: " + id + "\n" +
                        "Customer Name: " + customerName + "\n" +
                        "Phone: " + phone + "\n" +
                        "Cost: " + cost + "\n" +
                        "Date: " + bookingDate + "\n" +
                        "Adult: " + adult + "\n" +
                        "Child: " + child + "\n" +
                        "Remarks: " + remarks + "\n" +
                        "Employee Id: " + booking.getEmployeeId() + "\n" +
                        "Time: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            // Refresh the table
            bookingPanel.refreshTable(null);

            dispose(); // Close the form after saving
        });

        cancelButton.addActionListener(e -> dispose());

        // Add to frame
        add(panel);
    }

    @Override
    public void setValues(JTable table, int row) {
        this.id = (Long) table.getValueAt(row, 0);
        this.nameField.setText((String) table.getValueAt(row, 1));
        this.phoneField.setText((String) table.getValueAt(row, 2));
        this.costSpinner.setValue(table.getValueAt(row, 3));
        this.adultSpinner.setValue(table.getValueAt(row, 5));
        this.childSpinner.setValue(table.getValueAt(row, 6));
        this.remarkArea.setText(table.getValueAt(row, 7).toString());

        //set date value
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.model.setValue(formatter.parse(table.getValueAt(row,4).toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        model.setSelected(true); // Important: Ensures the date is displayed
        datePicker.getComponent(1).setEnabled(false); // Disable the button
        datePicker.getJFormattedTextField().setEditable(false); // Make the text field read-only

        roomTypeBox.setModel(new DefaultComboBoxModel<>(new String[]{table.getValueAt(row, 8).toString()}));
        roomTypeBox.setEnabled(false);

        //use to bypass form validate, this two properties cannot be update
        this.roomTypeId = 0L;
        this.dailyRoomStatusId = 0L;
    }

    private boolean validateFields() {
        if(dailyRoomStatusId == null || roomTypeId == null) {
            JOptionPane.showMessageDialog(bookingPanel, "Please select a date and room type.");
            return false;
        }

        if(nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(bookingPanel, "Please enter all the name and phone correctly.");
            return false;
        }

        if(nameField.getText().length() > 50) {
            JOptionPane.showMessageDialog(bookingPanel, "Name should be more than 50 characters.");
            return false;
        }

        Pattern phonePattern = Pattern.compile(Constant.PHONE_REGEX);
        if(!phonePattern.matcher(phoneField.getText()).matches() || phoneField.getText().length() > 15) {
            JOptionPane.showMessageDialog(bookingPanel, "Invalid phone number format! Please enter a valid number.");
            return false;
        }

        return true;
    }
}