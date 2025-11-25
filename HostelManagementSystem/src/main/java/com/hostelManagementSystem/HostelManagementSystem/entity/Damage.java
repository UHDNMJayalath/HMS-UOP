package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;

@Entity
public class Damage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostel;
    private double fine;
    private String paymentStatus;
    private String description;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Damage(Long id, String hostel, double fine, String paymentStatus, Student student) {
        this.id = id;
        this.hostel = hostel;
        this.fine = fine;
        this.paymentStatus = paymentStatus;
        this.student = student;
    }

    public Damage(String hostel, double fine, String description, Student student) {
        this.hostel = hostel;
        this.fine = fine;
        this.description = description;
        this.student = student;
    }

    public Damage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

