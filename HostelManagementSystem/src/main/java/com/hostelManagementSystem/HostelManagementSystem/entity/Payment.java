package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "date")
    private String date;

    @Column(name = "amount")
    private double amount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "semester")
    private String semester;

    @Column(name = "slip_ref_number")
    private String slipRefNumber;

    @Column(name = "slip_url") // <<< නව Field එක
    private String slipUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "studentId")
    private Student student;

    private boolean verified;



    // Constructor with all fields except id (id auto-generated)
    public Payment(String date, double amount, String type, String registrationNumber,
                   String semester, String slipRefNumber, Student student) {
        this.date = date;
        this.amount = amount;
        this.paymentType = type;
        this.semester = semester;
        this.slipRefNumber = slipRefNumber;
        this.student = student;
    }

    public Payment() {
    }

    // getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSlipRefNumber() {
        return slipRefNumber;
    }

    public void setSlipRefNumber(String slipRefNumber) {
        this.slipRefNumber = slipRefNumber;
    }

    public String getSlipUrl() {
        return slipUrl;
    }

    public void setSlipUrl(String slipUrl) {
        this.slipUrl = slipUrl;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
