package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Dean;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.DeanRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DeanManageStudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DeanRepository deanRepository;

    @GetMapping("/dean-students")
    public String getStudents(@RequestParam(required = false) String hostel,
                              @RequestParam(required = false) String batch,
                              HttpSession session,
                              Model model) {

        // Get faculty from session
        String faculty = (String) session.getAttribute("deanFaculty");
        if (faculty == null) {
            model.addAttribute("error", "Faculty not found in session.");
            return "dean_manage-students";
        }

        List<Student> students;

        if (hostel != null && !hostel.isEmpty() && batch != null && !batch.isEmpty()) {
            students = studentRepository.findByFacultyAndCurrentHostelContainingIgnoreCaseAndIntakeContainingIgnoreCase(faculty, hostel, batch);
        } else if (hostel != null && !hostel.isEmpty()) {
            students = studentRepository.findByFacultyAndCurrentHostelContainingIgnoreCase(faculty, hostel);
        } else if (batch != null && !batch.isEmpty()) {
            students = studentRepository.findByFacultyAndIntakeContainingIgnoreCase(faculty, batch);
        } else {
            students = studentRepository.findByFaculty(faculty);
        }

        model.addAttribute("students", students);
        model.addAttribute("faculty", faculty); // <-- Ensure this is added for Thymeleaf

        return "dean_manage-students";
    }


}
