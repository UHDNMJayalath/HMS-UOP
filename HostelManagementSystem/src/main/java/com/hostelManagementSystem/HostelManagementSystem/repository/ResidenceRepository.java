package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Residence;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidenceRepository extends JpaRepository<Residence, Long> {

    // Method to find residence history by student
    List<Residence> findByStudent(Student student);
    // Find residence by student's registration number
    @Query("SELECT r FROM Residence r WHERE r.student.studentId = :regNo")
    List<Residence> findByStudentRegistrationNumber(@Param("regNo") String regNo);
}
