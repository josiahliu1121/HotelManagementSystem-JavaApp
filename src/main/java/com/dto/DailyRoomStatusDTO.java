package com.dto;

import java.time.LocalDate;

public class DailyRoomStatusDTO {
    private Long id;
    private LocalDate date;
    private Long roomTypeId;
    private String roomName;
    private double price;
    private double suggestedPrice;
    private int availableCount;
    private int bookedCount;

    public DailyRoomStatusDTO(Long id, LocalDate date, Long roomTypeId, String roomName, double price, double suggestedPrice, int availableCount, int bookedCount) {
        this.id = id;
        this.date = date;
        this.roomTypeId = roomTypeId;
        this.roomName = roomName;
        this.price = price;
        this.suggestedPrice = suggestedPrice;
        this.availableCount = availableCount;
        this.bookedCount = bookedCount;
    }

    public DailyRoomStatusDTO() {
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


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public int getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
}
