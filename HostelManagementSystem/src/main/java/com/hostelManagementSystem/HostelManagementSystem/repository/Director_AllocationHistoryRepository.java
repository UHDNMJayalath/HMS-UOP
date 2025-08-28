package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_AllocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Director_AllocationHistoryRepository extends JpaRepository<Director_AllocationHistory, Long> {

    // Method to filter allocation history based on multiple criteria
    List<Director_AllocationHistory> findByFacultyContainingAndIntakeContainingAndAcademicYearContainingAndGenderContaining(
            String faculty, String intake, String academicYear, String gender
    );

    // Custom query to fetch distinct faculty names for the filter dropdown
    @Query("SELECT DISTINCT a.faculty FROM Director_AllocationHistory a")
    List<String> findDistinctFaculty();

    // Custom query to fetch distinct intake years for the filter dropdown
    @Query("SELECT DISTINCT a.intake FROM Director_AllocationHistory a")
    List<String> findDistinctIntake();

    // Custom query to fetch distinct academic years for the filter dropdown
    @Query("SELECT DISTINCT a.academicYear FROM Director_AllocationHistory a")
    List<String> findDistinctAcademicYear();
}