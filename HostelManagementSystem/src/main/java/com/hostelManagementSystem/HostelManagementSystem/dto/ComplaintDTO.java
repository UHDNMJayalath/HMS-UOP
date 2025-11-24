package com.hostelManagementSystem.HostelManagementSystem.dto;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ComplaintDTO {
    private Long id;
    private String date;
    private String registrationNumber;
    private String shortDescription;
    private String state; // Status

    public ComplaintDTO(Long id, LocalDateTime dateTime, String registrationNumber, String description, String status) {
        this.id = id;
        // Date eka String ekak vidiyata format kireema
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = (dateTime != null) ? dateTime.format(formatter) : "N/A";

        this.registrationNumber = registrationNumber;
        this.state = (status != null) ? status : "Not Taken";

        // Description eka diga wadi nam mulu tika vitharak gannawa
        if (description != null && description.length() > 50) {
            this.shortDescription = description.substring(0, 50) + "...";
        } else {
            this.shortDescription = description;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getDate() { return date; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getShortDescription() { return shortDescription; }
    public String getState() { return state; }
}