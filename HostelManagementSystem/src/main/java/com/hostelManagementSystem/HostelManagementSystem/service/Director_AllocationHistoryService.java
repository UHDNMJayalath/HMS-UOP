package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_AllocationHistory;
import com.hostelManagementSystem.HostelManagementSystem.repository.Director_AllocationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Director_AllocationHistoryService {
    private final Director_AllocationHistoryRepository repository;

    public Director_AllocationHistoryService(Director_AllocationHistoryRepository repository) {
        this.repository = repository;
    }

    public List<Director_AllocationHistory> getFilteredAllocations(String faculty, String intake, String academicYear, String gender) {
        // Handle null values for filtering
        if (faculty == null) faculty = "";
        if (intake == null) intake = "";
        if (academicYear == null) academicYear = "";
        if (gender == null) gender = "";

        return repository.findByFacultyContainingAndIntakeContainingAndAcademicYearContainingAndGenderContaining(
                faculty, intake, academicYear, gender
        );
    }

    public List<String> getAllFaculties() {
        return repository.findDistinctFaculty();
    }

    public List<String> getAllIntakes() {
        return repository.findDistinctIntake();
    }

    public List<String> getAllAcademicYears() {
        return repository.findDistinctAcademicYear();
    }
}