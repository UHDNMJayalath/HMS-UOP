package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.entity.Hostel;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.HostelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class vc_dvcController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private HostelRepository hostelRepository;

    @GetMapping("/vc_dvc-dashboard")
    public String viewVcDvcDashboard(@RequestParam(value = "q", required = false) String studentId, Model model) {
        List<Student> students = studentRepository.findAll();
        List<Hostel> hostels = hostelRepository.findAll();

        model.addAttribute("studentCount", students.size());
        model.addAttribute("hostelCount", hostels.size());

        if (studentId != null && !studentId.isEmpty()) {
            Optional<Student> foundStudent = studentRepository.findByStudentId(studentId);
            if (foundStudent.isPresent()) {
                model.addAttribute("student", foundStudent.get()); //  Rename here
            } else {
                model.addAttribute("student", null); //  Still bind to 'student'
            }
            model.addAttribute("searchQuery", studentId); // Send query back
        }

        return "vc_dvc-dashboard";
    }

}
