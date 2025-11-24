package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_AllocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Director_AllocationHistoryRepository extends JpaRepository<Director_AllocationHistory, Long> {


    @Query("SELECT d FROM Director_AllocationHistory d WHERE " +
            "(:faculty IS NULL OR d.faculty = :faculty) AND " +
            "(:intake IS NULL OR d.intake = :intake) AND " +
            "(:academicYear IS NULL OR d.academicYear = :academicYear) AND " +
            "(:gender IS NULL OR d.gender = :gender)")
    List<Director_AllocationHistory> findWithFilters(
            @Param("faculty") String faculty,
            @Param("intake") String intake,
            @Param("academicYear") String academicYear,
            @Param("gender") String gender
    );

    // Queries to populate dropdown lists dynamically

    @Query("SELECT DISTINCT d.faculty FROM Director_AllocationHistory d")
    List<String> findDistinctFaculty();

    @Query("SELECT DISTINCT d.intake FROM Director_AllocationHistory d")
    List<String> findDistinctIntake();

    @Query("SELECT DISTINCT d.academicYear FROM Director_AllocationHistory d")
    List<String> findDistinctAcademicYear();
}