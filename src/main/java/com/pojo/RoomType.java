package com.pojo;

public class RoomType {
    private Long id;
    private String roomName;
    private double suggestedPrice;
    private int count;
    private String description;

    public RoomType(Long id, String roomName, double suggestedPrice, int count, String description) {
        this.id = id;
        this.roomName = roomName;
        this.suggestedPrice = suggestedPrice;
        this.count = count;
        this.description = description;
    }

    public RoomType() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
