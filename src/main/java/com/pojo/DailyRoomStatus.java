package com.pojo;

import java.time.LocalDate;

public class DailyRoomStatus {
    private Long id;
    private LocalDate date;
    private double price;
    private int availableCount;
    private int bookedCount;
    private Long roomTypeId;

    public DailyRoomStatus() {
    }

    public DailyRoomStatus(Long id, LocalDate date, double price, int availableCount, int bookedCount, Long roomTypeId) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.availableCount = availableCount;
        this.bookedCount = bookedCount;
        this.roomTypeId = roomTypeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    @Override
    public String toString() {
        return "DailyRoomStatus{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", availableCount=" + availableCount +
                ", bookedCount=" + bookedCount +
                ", roomTypeId=" + roomTypeId +
                '}';
    }
}
