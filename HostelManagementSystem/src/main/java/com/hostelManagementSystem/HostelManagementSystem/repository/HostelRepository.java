package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Hostel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, Long> {

    long count();

    // Corrected method names that match entity properties
    List<Hostel> findByFaculty(String faculty);  // Now matches the faculty field

    Hostel findByName(String name);  // Changed from findByHostelName to findByName

    // Additional useful query methods
    List<Hostel> findByLocation(String location);

    List<Hostel> findByFacultyAndLocation(String faculty, String location);
    List<Hostel> findByNameContainingIgnoreCase(String name);
}