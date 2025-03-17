package com.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingDTO {
    private Long id;
    private String customerName;
    private String customerPhone;
    private double cost;
    private LocalDate bookingDate;
    private int Adult;
    private int Child;
    private String remarks;
    private String roomType;
    private String employeeUsername;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public BookingDTO(Long id, String customerName, String customerPhone, double cost, LocalDate bookingDate, int adult, int child, String remarks, String roomType, String employeeUsername, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.cost = cost;
        this.bookingDate = bookingDate;
        Adult = adult;
        Child = child;
        this.remarks = remarks;
        this.roomType = roomType;
        this.employeeUsername = employeeUsername;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public BookingDTO() {
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
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
}
