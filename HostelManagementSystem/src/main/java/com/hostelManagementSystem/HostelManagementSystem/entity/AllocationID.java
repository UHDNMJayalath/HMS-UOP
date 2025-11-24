package com.hostelManagementSystem.HostelManagementSystem.entity;

import java.io.Serializable;
import java.util.Objects;

public class AllocationID implements Serializable {

    private String studentId;
    private Integer hostelId;
    private Integer academicYear;

    public AllocationID() {
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

    @Override
    public int hashCode() {
        return Objects.hash(this.studentId, this.hostelId, this.academicYear);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AllocationID)) return false;
        AllocationID allocationID = (AllocationID) obj;
        return Objects.equals(studentId, allocationID.studentId)  && Objects.equals(hostelId, allocationID.hostelId) && Objects.equals(academicYear, allocationID.academicYear);
    }
}
