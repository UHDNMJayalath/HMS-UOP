package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Residence;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidenceRepository extends JpaRepository<Residence, Long> {

    // Method to find residence history by student
    List<Residence> findByStudent(Student student);
}
