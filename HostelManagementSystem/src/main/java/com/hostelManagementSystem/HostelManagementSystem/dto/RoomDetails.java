package com.hostelManagementSystem.HostelManagementSystem.dto;


import com.hostelManagementSystem.HostelManagementSystem.entity.Student;

import java.util.List;

public class RoomDetails {

    private String roomNo;
    private Integer hostelId;
    private Integer capacity;
    private Integer currentAllocations;
    private String status;
    private List<Student> students;

    public RoomDetails() {
    }

    public RoomDetails(String roomNo, Integer hostelId, Integer capacity, Integer currentAllocations, String status, List<Student> students) {
        this.roomNo = roomNo;
        this.hostelId = hostelId;
        this.capacity = capacity;
        this.currentAllocations = currentAllocations;
        this.status = status;
        this.students = students;
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }
}
