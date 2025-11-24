package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByEmailIgnoreCase(String email);

    Optional<Student> findByStudentId(String studentId);

    long count();

    List<Student> findByFaculty(String faculty);
    Optional<Student> findByStudentIdAndFaculty(String studentId, String faculty);

    List<Student> findByFacultyIgnoreCase(String faculty);
    Optional<Student> findByStudentIdAndFacultyIgnoreCase(String studentId, String faculty);

    long countByFaculty(String faculty);


    List<Student> findByFacultyAndCurrentHostelContainingIgnoreCase(String faculty, String hostel);

    List<Student> findByFacultyAndIntakeContainingIgnoreCase(String faculty, String batch);

    List<Student> findByFacultyAndCurrentHostelContainingIgnoreCaseAndIntakeContainingIgnoreCase(String faculty, String hostel, String batch);

    List<Student> findByBatchId(Integer batchId);

    Optional<Student> findByStudentIdIgnoreCase(String studentId);

}

