package com.hostelManagementSystem.HostelManagementSystem.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DirectorInboxDTO {
    private Long id;
    private String subject;
    private String content;
    private String sender;
    private String senderType;
    private String date;
    private boolean isRead;

    // Constructor එක හරියටම මෙහෙම තියෙන්න ඕන
    public DirectorInboxDTO(Long id, String subject, String content, String sender,
                            String senderType, LocalDateTime dateTime, boolean isRead) {
        this.id = id;
        this.subject = subject;

        // Content එක දිග වැඩි නම් කෙටි කිරීම
        if (content != null && content.length() > 60) {
            this.content = content.substring(0, 60) + "...";
        } else {
            this.content = content;
        }

        this.sender = sender;
        this.senderType = senderType;
        this.isRead = isRead;

        // Date Time එක String බවට හැරවීම
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.date = (dateTime != null) ? dateTime.format(formatter) : "N/A";
    }

    // Getters
    public Long getId() { return id; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public String getSender() { return sender; }
    public String getSenderType() { return senderType; }
    public String getDate() { return date; }
    public boolean isRead() { return isRead; }
}