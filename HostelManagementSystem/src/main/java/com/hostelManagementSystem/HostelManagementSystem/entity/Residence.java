package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;

@Entity
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String academicYear;
    private String hostel;
    private String allocationDate;
    private String leftDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Residence(Long id, String academicYear, String hostel, String allocationDate, String leftDate, Student student) {
        this.id = id;
        this.academicYear = academicYear;
        this.hostel = hostel;
        this.allocationDate = allocationDate;
        this.leftDate = leftDate;
        this.student = student;
    }

    public Residence() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(String leftDate) {
        this.leftDate = leftDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}

