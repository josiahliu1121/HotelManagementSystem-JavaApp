package com.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private String customerName;
    private String customerPhone;
    private double cost;
    private LocalDate bookingDate;
    private int Adult;
    private int Child;
    private String remarks;
    private Long dailyRoomStatusId;
    private Long roomTypeId;
    private Long employeeId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Booking() {
    }

    public Booking(Long id, String customerName, String customerPhone, double cost, LocalDate bookingDate, int adult, int child, String remarks, Long dailyRoomStatusId, Long roomTypeId, Long employeeId, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.cost = cost;
        this.bookingDate = bookingDate;
        Adult = adult;
        Child = child;
        this.remarks = remarks;
        this.dailyRoomStatusId = dailyRoomStatusId;
        this.roomTypeId = roomTypeId;
        this.employeeId = employeeId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getDailyRoomStatusId() {
        return dailyRoomStatusId;
    }

    public void setDailyRoomStatusId(Long dailyRoomStatusId) {
        this.dailyRoomStatusId = dailyRoomStatusId;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getAdult() {
        return Adult;
    }

    public void setAdult(int adult) {
        Adult = adult;
    }

    public int getChild() {
        return Child;
    }

    public void setChild(int child) {
        Child = child;
    }
}
