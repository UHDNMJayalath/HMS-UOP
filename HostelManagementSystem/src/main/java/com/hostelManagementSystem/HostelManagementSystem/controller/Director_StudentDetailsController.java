package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Director_StudentDetailsController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/Director-StudentDetails")
    public String loadStudentDetailsPage(Model model) {
        model.addAttribute("searchAttempted", false);
        return "Director-StudentDetails";
    }

    @GetMapping("/Director-StudentDetails/search")
    public String searchStudent(
            @RequestParam("regNo") String studentId,
            Model model) {

        Student student = studentRepository.findByStudentId(studentId.trim())
                .orElse(null);

        model.addAttribute("student", student);
        model.addAttribute("searchAttempted", true);

        return "Director-StudentDetails";
    }
}
