package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.ComplaintRequest;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRequestRepository extends JpaRepository<ComplaintRequest, Long> {
    List<ComplaintRequest> findByStudent(Student student);
}
