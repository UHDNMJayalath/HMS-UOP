package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ComplaintRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Complaint or Request
    private String receiver; // "Sub Warden" or "Director"
    private String description;

    // 🟢 NEW FIELD: Status (Not Taken, Pending, Completed)
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDateTime submittedAt;

    public ComplaintRequest() {
        // Default status
        this.status = "Not Taken";
    }

    // Getters and Setters for new field
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ... (Other Getters and Setters remain the same)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}