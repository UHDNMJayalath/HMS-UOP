package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private double amount;

    private String type; // paymentType

    private String fileUrl;

    private String semester;

    private String slipRefNumber;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Constructor with all fields except id (id auto-generated)
    public Payment(String date, double amount, String type, String registrationNumber,
                   String semester, String slipRefNumber, Student student) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.semester = semester;
        this.slipRefNumber = slipRefNumber;
        this.student = student;
    }

    public Payment() {
    }

    // getters and setters for all fields

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getSlipRefNumber() { return slipRefNumber; }
    public void setSlipRefNumber(String slipRefNumber) { this.slipRefNumber = slipRefNumber; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
