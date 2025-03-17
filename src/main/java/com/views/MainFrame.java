package com.views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        // Create JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add different content panels
        tabbedPane.addTab("Booking", new BookingPanel());
        tabbedPane.addTab("Room Type", new RoomTypePanel());
        tabbedPane.addTab("Daily Room Status", new DailyRoomStatusPanel());
        tabbedPane.addTab("Employee", new EmployeePanel());

        // Add tabbed pane to frame
        add(tabbedPane, BorderLayout.CENTER);
    }
}
