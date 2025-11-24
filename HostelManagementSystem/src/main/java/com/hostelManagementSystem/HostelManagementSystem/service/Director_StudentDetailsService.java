package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class Director_StudentDetailsService {
    private final StudentRepository repo;

    public Director_StudentDetailsService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student getStudentBystudentId(String studentId) {
        return repo.findById(studentId).orElse(null);
    }
}
