package com.views;

import com.component.ButtonEditor;
import com.component.ButtonRenderer;
import com.dto.BookingDTO;
import com.dto.DailyRoomStatusDTO;
import com.mapper.BookingMapper;
import com.mapper.DailyRoomStatusMapper;
import com.mapper.EmployeeMapper;
import com.mapper.RoomTypeMapper;
import com.pojo.Booking;
import com.pojo.DailyRoomStatus;
import com.pojo.Employee;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JDatePickerImpl datePicker;
    private String[] columns = {"Id", "Customer Name", "Phone", "Cost", "Date", "Adult", "Child", "Remarks", "Room Type", "Employee", "Create Time", "UpdateTime", "Actions"};

    public BookingPanel() {

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
                return column == 12; // Only "Actions" column is editable
            }
        };

        // Create JTable
        table = new JTable(tableModel);
        table.setRowHeight(35); // Set row height to fit buttons properly
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(table, BookingMapper.instance, new BookingForm(this)));
        table.getColumn("Actions").setMinWidth(180);

        // Wrap JTable inside JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the button and align center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Make New Booking");
        buttonPanel.add(addButton);

        // Add button panel at the bottom
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshTable(null);

        // Add search button action
        searchButton.addActionListener(e -> {
            if (datePicker.getModel().getValue() != null) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getModel().getValue());
                refreshTable(date);
            } else {
                refreshTable(null);
            }
        });

        clearButton.addActionListener(e -> {
            datePicker.getModel().setValue(null);
            refreshTable(null);
        });

        addButton.addActionListener(e -> {
            BookingForm form = new BookingForm(this);
            form.setVisible(true);
        });
    }

    public void refreshTable(String date) {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Fetch new data
        List<RoomType> roomTypeList = RoomTypeMapper.instance.findAll();
        List<Employee> employeeList = EmployeeMapper.instance.findAll();
        List<Booking> bookingList = new ArrayList<>();

        if (date != null) {
            bookingList = BookingMapper.instance.findByDate(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            bookingList = BookingMapper.instance.findAll();
        }

        List<BookingDTO> bookingDTOList = new ArrayList<>();

        bookingList.forEach(b -> {
            BookingDTO bookingDTO = new BookingDTO();

            try {
                BeanUtils.copyProperties(bookingDTO, b);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            String employee = employeeList.stream()
                    .filter(e -> e.getId().equals(b.getEmployeeId())).findFirst().orElse(new Employee()).getUserName();
            bookingDTO.setEmployeeUsername(employee);

            String roomType = roomTypeList.stream()
                    .filter(rt -> rt.getId().equals(b.getRoomTypeId())).findFirst().get().getRoomName();
            bookingDTO.setRoomType(roomType);

            bookingDTOList.add(bookingDTO);
        });

        // Add rows to table model
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (BookingDTO bookingDTO : bookingDTOList) {
            tableModel.addRow(new Object[]{
                    bookingDTO.getId(),
                    bookingDTO.getCustomerName(),
                    bookingDTO.getCustomerPhone(),
                    bookingDTO.getCost(),
                    bookingDTO.getBookingDate(),
                    bookingDTO.getAdult(),
                    bookingDTO.getChild(),
                    bookingDTO.getRemarks(),
                    bookingDTO.getRoomType(),
                    bookingDTO.getEmployeeUsername(),
                    bookingDTO.getCreateTime().format(formatter),
                    bookingDTO.getUpdateTime().format(formatter),
                    "Actions"
            });
        }

        // Notify the table model that data has changed
        tableModel.fireTableDataChanged();
    }
}

