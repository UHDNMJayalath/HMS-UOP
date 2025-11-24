package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Student {

    @Id
    private String studentId;

    private String indexNo;
    private String name;
    private String LName;
    private String initials;
    private String fullName;
    private String ALDistrict;
    private String sex;
    private String ZScore;
    private String medium;
    private String NIC;
    private String ADD1;
    private String ADD2;
    private String ADD3;
    private String address;
    private String email;
    private String phoneNo1;
    private String phoneNo2;
    private String GenEngMarks;
    private String intake;
    private String currentHostel;
    private String faculty;
    private String dateOfEnrollment;
    private Integer batchId;

    private String distance;
    private String contact;
    private String emergencyContact;
    private Integer academicYear;

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

    public String getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(String indexNo) {
        this.indexNo = indexNo;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getALDistrict() {
        return ALDistrict;
    }

    public void setALDistrict(String ALDistrict) {
        this.ALDistrict = ALDistrict;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getZScore() {
        return ZScore;
    }

    public void setZScore(String ZScore) {
        this.ZScore = ZScore;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getADD1() {
        return ADD1;
    }

    public void setADD1(String ADD1) {
        this.ADD1 = ADD1;
    }

    public String getADD2() {
        return ADD2;
    }

    public void setADD2(String ADD2) {
        this.ADD2 = ADD2;
    }

    public String getADD3() {
        return ADD3;
    }

    public void setADD3(String ADD3) {
        this.ADD3 = ADD3;
    }

    public String getPhoneNo1() {
        return phoneNo1;
    }

    public void setPhoneNo1(String phoneNo1) {
        this.phoneNo1 = phoneNo1;
    }

    public String getPhoneNo2() {
        return phoneNo2;
    }

    public void setPhoneNo2(String phoneNo2) {
        this.phoneNo2 = phoneNo2;
    }

    public String getGenEngMarks() {
        return GenEngMarks;
    }

    public void setGenEngMarks(String genEngMarks) {
        GenEngMarks = genEngMarks;
    }

    public String getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(String dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public boolean isEmpty(){
        if (this.studentId != null){
            return false;
        }
        else {
            return true;
        }
    }

}
