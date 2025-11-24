package com.hostelManagementSystem.HostelManagementSystem.dto;

import java.util.List;

public class AllocationRequest {

    private Integer hostelId;
    private List<String> students;
    private Integer academicYear;

    public AllocationRequest() {
    }

    public Integer getHostelId() {
        return hostelId;
    }

    public void setHostelId(Integer hostelId) {
        this.hostelId = hostelId;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }
}
