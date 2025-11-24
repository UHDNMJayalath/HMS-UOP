package com.hostelManagementSystem.HostelManagementSystem.dto;

public class HostelReq {

    private Integer id;
    private String name;
    private Integer totalRooms;
    private Integer availableRooms;
    private Integer totalCapacity;
    private Integer availabilityPercentage;

    public HostelReq(Integer id, String name, Integer availableRooms, Integer totalCapacity, Integer availabilityPercentage) {
        this.id = id;
        this.name = name;
        this.availableRooms = availableRooms;
        this.totalCapacity = totalCapacity;
        this.availabilityPercentage = availabilityPercentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getAvailabilityPercentage() {
        return availabilityPercentage;
    }

    public void setAvailabilityPercentage(Integer availabilityPercentage) {
        this.availabilityPercentage = availabilityPercentage;
    }
}
