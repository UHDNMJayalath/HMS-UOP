package com.hostelManagementSystem.HostelManagementSystem.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComplaintDTO {
    private Long id;
    private String date;
    private String registrationNumber;
    private String shortDescription;
    private String state; // This holds the Status

    public ComplaintDTO(Long id, LocalDateTime dateTime, String registrationNumber, String description, String status) {
        this.id = id;

        // Date format logic
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.date = (dateTime != null) ? dateTime.format(formatter) : "N/A";

        this.registrationNumber = registrationNumber;

        // Status logic
        this.state = (status != null) ? status : "Not Taken";

        // Description shortening logic
        if (description != null && description.length() > 50) {
            this.shortDescription = description.substring(0, 50) + "...";
        } else {
            this.shortDescription = description;
        }
    }

    // Getters are crucial for Thymeleaf to access data
    public Long getId() { return id; }
    public String getDate() { return date; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getShortDescription() { return shortDescription; }
    public String getState() { return state; }
}