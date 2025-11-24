package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.time.LocalDate;

@Entity
@IdClass(AllocationID.class)
public class Allocation {

    @Id
    private String studentId;

    @Id
    private Integer hostelId;

    @Id
    private Integer academicYear;
    private String roomId;
    private LocalDate allocatedDate;
    private LocalDate deallocatedDate;

    public Allocation() {
    }

    public Allocation(String studentId, Integer hostelID, Integer academicYear) {
        this.studentId = studentId;
        this.hostelId = hostelID;
        this.academicYear = academicYear;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getAllocatedDate() {
        return allocatedDate;
    }

    public void setAllocatedDate(LocalDate allocatedDate) {
        this.allocatedDate = allocatedDate;
    }

    public LocalDate getDeallocatedDate() {
        return deallocatedDate;
    }

    public void setDeallocatedDate(LocalDate deallocatedDate) {
        this.deallocatedDate = deallocatedDate;
    }
}
