package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class deanController {

    @Autowired
    private StudentRepository studentRepository;


    @GetMapping("/dean-dashboard")
    public String DeanDashboard(@RequestParam(value = "q", required = false) String studentId, Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("studentCount", students.size());

        if (studentId != null && !studentId.isEmpty()) {
            Optional<Student> foundStudent = studentRepository.findByStudentId(studentId);
            if (foundStudent.isPresent()) {
                model.addAttribute("student", foundStudent.get());
            } else {
                model.addAttribute("student", null);
                model.addAttribute("error", "No student found with ID: " + studentId);
            }
            model.addAttribute("searchQuery", studentId);
        }

        return "dean-dashboard";
    }


}
