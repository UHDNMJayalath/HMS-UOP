package com.hostelManagementSystem.HostelManagementSystem.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private String senderFaculty; // NEW: Faculty of the Dean
    private String recipientRole = "DIRECTOR_ACCOMMODATION";
    private String subject;

    @Lob
    private String body;
    private LocalDateTime sentAt = LocalDateTime.now();
    private boolean isRead = false;

    // --- Constructors ---
    public Message() {}

    public Message(Long senderId, String senderFaculty, String subject, String body) {
        this.senderId = senderId;
        this.senderFaculty = senderFaculty;
        this.subject = subject;
        this.body = body;

    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderFaculty() {
        return senderFaculty;
    }

    public void setSenderFaculty(String senderFaculty) {
        this.senderFaculty = senderFaculty;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }


    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}