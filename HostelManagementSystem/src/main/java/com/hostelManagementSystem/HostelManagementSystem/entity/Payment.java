package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "payment")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "studentId")
    private Student student;

    public Payment() {}

    public Payment(String registrationNumber, String date, double amount, String paymentType,
                   String semester, String slipRefNumber, Student student) {
        this.registrationNumber = registrationNumber;
        this.date = date;
        this.amount = amount;
        this.paymentType = paymentType;
        this.semester = semester;
        this.slipRefNumber = slipRefNumber;
        this.student = student;
    }

    // ---------------- Getters and Setters ----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getSlipRefNumber() { return slipRefNumber; }
    public void setSlipRefNumber(String slipRefNumber) { this.slipRefNumber = slipRefNumber; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
