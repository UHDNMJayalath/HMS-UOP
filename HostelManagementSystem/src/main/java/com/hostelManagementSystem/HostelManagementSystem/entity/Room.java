package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(RoomID.class)
public class Room {

    @Id
    private Integer hostelId;

    @Id
    private String roomNo;

    private Integer capacity;

    private Integer currentAllocations;

    private String status;

    private Integer floor;

    public Room(Integer hostelId, String roomNo, Integer floor, Integer capacity) {
        this.hostelId = hostelId;
        this.roomNo = roomNo;
        this.floor = floor;
        this.capacity = capacity;
        this.currentAllocations = 0;
    }

    public Room() {
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentAllocations() {
        return currentAllocations;
    }

    public void setCurrentAllocations(Integer currentAllocations) {
        this.currentAllocations = currentAllocations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
