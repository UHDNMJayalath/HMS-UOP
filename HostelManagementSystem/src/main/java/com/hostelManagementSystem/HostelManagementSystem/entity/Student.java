package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Student {

    @Id
    private String studentId;

    private String name;
    private String email;
    private String currentHostel;
    private String faculty;
    private String intake;
    private String distance;
    private String address;
    private String contact;
    private String emergencyContact;

    //  Relations with other entities

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Residence> histories;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Damage> damages;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplaintRequest> complaints;

    public Student() {}

    public Student(String studentId, String name, String email, String password, String currentHostel,
                   String faculty, String intake, String distance, String address, String contact,
                   String emergencyContact) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.currentHostel = currentHostel;
        this.faculty = faculty;
        this.intake = intake;
        this.distance = distance;
        this.address = address;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
    }

    // Getters & Setters for all fields including new lists

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getCurrentHostel() { return currentHostel; }
    public void setCurrentHostel(String currentHostel) { this.currentHostel = currentHostel; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public String getIntake() { return intake; }
    public void setIntake(String intake) { this.intake = intake; }

    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public List<Residence> getHistories() { return histories; }
    public void setHistories(List<Residence> histories) { this.histories = histories; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    public List<Damage> getDamages() { return damages; }
    public void setDamages(List<Damage> damages) { this.damages = damages; }

    public List<ComplaintRequest> getComplaints() { return complaints; }
    public void setComplaints(List<ComplaintRequest> complaints) { this.complaints = complaints; }
}
