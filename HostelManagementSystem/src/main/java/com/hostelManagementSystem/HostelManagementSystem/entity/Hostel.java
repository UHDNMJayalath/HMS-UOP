package com.hostelManagementSystem.HostelManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;                // Hostel name
    private String type;                // Type of hostel (Male/Female/Mixed)
    private String subWardenEmail;      // Sub Warden's Email
    private Integer totalRooms;             // Total rooms in hostel
    private Integer availableRooms;         // Available rooms
    private Integer fullyAllocatedRooms;    // Fully allocated rooms
    private Integer repairRooms;
    private String faculty;// Rooms under repair
    private String location;
    private Integer ResidentialStudents;


    public Hostel() {
    }

    public Hostel(String name, String type) {
        this.name = name;
        this.type = type;
    }
//
//    public Hostel(String name, String type, String subWardenEmail, int totalRooms, int availableRooms, int fullyAllocatedRooms, int repairRooms) {
//        this.name = name;
//        this.type = type;
//        this.subWardenEmail = subWardenEmail;
//        this.totalRooms = totalRooms;
//        this.availableRooms = availableRooms;
//        this.fullyAllocatedRooms = fullyAllocatedRooms;
//        this.repairRooms = repairRooms;
//    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSubWardenEmail() { return subWardenEmail; }
    public void setSubWardenEmail(String subWardenEmail) { this.subWardenEmail = subWardenEmail; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public int getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(int availableRooms) { this.availableRooms = availableRooms; }

    public int getFullyAllocatedRooms() { return fullyAllocatedRooms; }
    public void setFullyAllocatedRooms(int fullyAllocatedRooms) { this.fullyAllocatedRooms = fullyAllocatedRooms; }

    public int getRepairRooms() { return repairRooms; }
    public void setRepairRooms(int repairRooms) { this.repairRooms = repairRooms; }

    public String getFaculty() {
        return faculty;
    }
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getResidentialStudents() {
        return ResidentialStudents;
    }

    public void setResidentialStudents(Integer residentialStudents) {
        ResidentialStudents = residentialStudents;
    }
}
