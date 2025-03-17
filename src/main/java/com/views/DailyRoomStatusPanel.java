package com.views;

import com.component.ButtonEditor;
import com.component.ButtonRenderer;
import com.dto.DailyRoomStatusDTO;
import com.mapper.DailyRoomStatusMapper;
import com.mapper.RoomTypeMapper;
import com.pojo.DailyRoomStatus;
import com.pojo.RoomType;
import com.utils.DateLabelFormatter;
import org.apache.commons.beanutils.BeanUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DailyRoomStatusPanel extends JPanel {
    private DefaultTableModel tableModel;
    private RoomTypeMapper roomTypeMapper;
    private DailyRoomStatusMapper dailyRoomStatusMapper;
    private JTable table;
    private JDatePickerImpl datePicker;
    private String[] columns = {"Id", "Date", "Room Id", "Room Name", "Price", "Suggested Price", "Available Count", "Booked Count", "Actions"};

    public DailyRoomStatusPanel() {
        roomTypeMapper = RoomTypeMapper.instance;
        dailyRoomStatusMapper = DailyRoomStatusMapper.instance;

        setLayout(new BorderLayout());

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Date picker setup
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchPanel.add(new JLabel("Select Date:"));
        searchPanel.add(datePicker);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        add(searchPanel, BorderLayout.NORTH);  // Add search bar at the top

        // Create Table Model
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only "Actions" column is editable
            }
        };

        // Create JTable
        table = new JTable(tableModel);
        table.setRowHeight(35); // Set row height to fit buttons properly
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(table, dailyRoomStatusMapper, new DailyRoomStatusForm(this)));
        table.getColumn("Actions").setMinWidth(180);

        // Wrap JTable inside JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        refreshTable();

        // Add search button action
        searchButton.addActionListener(e -> {
            if (datePicker.getModel().getValue() != null) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getModel().getValue());
                refreshTable(date);
            } else {
                refreshTable();
            }
        });

        clearButton.addActionListener(e -> {
            datePicker.getModel().setValue(null);
            refreshTable();
        });
    }

    public void refreshTable() {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Fetch new data
        List<RoomType> roomTypeList = roomTypeMapper.findAll();
        List<DailyRoomStatus> dailyRoomStatusList = dailyRoomStatusMapper.findAll();

        List<DailyRoomStatusDTO> dailyRoomStatusDTOList = new ArrayList<>();
        dailyRoomStatusList.forEach(dailyRoomStatus -> {
            DailyRoomStatusDTO dailyRoomStatusDTO = new DailyRoomStatusDTO();

            try {
                BeanUtils.copyProperties(dailyRoomStatusDTO, dailyRoomStatus);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            RoomType roomType = roomTypeList.stream().filter(rt -> rt.getId().equals(dailyRoomStatus.getRoomTypeId())).findFirst().get();

            dailyRoomStatusDTO.setRoomName(roomType.getRoomName());
            dailyRoomStatusDTO.setSuggestedPrice(roomType.getSuggestedPrice());

            dailyRoomStatusDTOList.add(dailyRoomStatusDTO);
        });

        // Add rows to table model
        for (DailyRoomStatusDTO dailyRoomStatusDTO : dailyRoomStatusDTOList) {
            tableModel.addRow(new Object[]{
                    dailyRoomStatusDTO.getId(),
                    dailyRoomStatusDTO.getDate(),
                    dailyRoomStatusDTO.getRoomTypeId(),
                    dailyRoomStatusDTO.getRoomName(),
                    dailyRoomStatusDTO.getPrice(),
                    dailyRoomStatusDTO.getSuggestedPrice(),
                    dailyRoomStatusDTO.getAvailableCount(),
                    dailyRoomStatusDTO.getBookedCount(),
                    "Actions"
            });
        }

        // Notify the table model that data has changed
        tableModel.fireTableDataChanged();
    }

    public void refreshTable(String date) {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Fetch new data
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        List<RoomType> roomTypeList = roomTypeMapper.findAll();
        List<DailyRoomStatus> dailyRoomStatusList = dailyRoomStatusMapper.findByDate(localDate);
        List<DailyRoomStatusDTO> dailyRoomStatusDTOList = new ArrayList<>();

        //Display all room type
        //If not respective daily room status, display price as not set, availableCount as count, id as null
        roomTypeList.forEach(rt -> {
            DailyRoomStatusDTO dailyRoomStatusDTO = new DailyRoomStatusDTO();

            //check if dailyRoomStatus already exists
            DailyRoomStatus dailyRoomStatus = dailyRoomStatusList.stream()
                    .filter(drs -> drs.getRoomTypeId().equals(rt.getId())).findFirst().orElse(null);

            if(dailyRoomStatus != null){
                try {
                    BeanUtils.copyProperties(dailyRoomStatusDTO, dailyRoomStatus);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }else {
                dailyRoomStatusDTO.setDate(localDate);
                dailyRoomStatusDTO.setAvailableCount(rt.getCount());
                dailyRoomStatusDTO.setBookedCount(0);
                dailyRoomStatusDTO.setRoomTypeId(rt.getId());
            }

            dailyRoomStatusDTO.setRoomName(rt.getRoomName());
            dailyRoomStatusDTO.setSuggestedPrice(rt.getSuggestedPrice());

            dailyRoomStatusDTOList.add(dailyRoomStatusDTO);
        });

        // Add rows to table model
        for (DailyRoomStatusDTO dailyRoomStatusDTO : dailyRoomStatusDTOList) {
            tableModel.addRow(new Object[]{
                    dailyRoomStatusDTO.getId() != null ? dailyRoomStatusDTO.getId():"not set",
                    dailyRoomStatusDTO.getDate(),
                    dailyRoomStatusDTO.getRoomTypeId(),
                    dailyRoomStatusDTO.getRoomName(),
                    dailyRoomStatusDTO.getPrice(),
                    dailyRoomStatusDTO.getSuggestedPrice(),
                    dailyRoomStatusDTO.getAvailableCount(),
                    dailyRoomStatusDTO.getBookedCount(),
                    "Actions"
            });
        }

        // Notify the table model that data has changed
        tableModel.fireTableDataChanged();
    }
}