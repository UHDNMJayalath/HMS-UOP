package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
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
    public String deanDashboard(@RequestParam(value = "q", required = false) String studentId,
                                Model model, HttpSession session) {

        // Make sure dean faculty is detected and set
        String deanFaculty = getDeanFaculty(session);

        // Update session with detected faculty if not already set
        session.setAttribute("deanFaculty", deanFaculty);

        // Get all students in faculty
        List<Student> facultyStudents = "General".equalsIgnoreCase(deanFaculty)
                ? studentRepository.findAll()
                : studentRepository.findByFacultyIgnoreCase(deanFaculty);

        // Add faculty info + dean email + student count to model
        model.addAttribute("faculty", deanFaculty);
        model.addAttribute("deanEmail", session.getAttribute("loggedInUserEmail"));
        model.addAttribute("studentCount", facultyStudents.size());

        // Optional search
        if (studentId != null && !studentId.trim().isEmpty()) {
            studentId = studentId.trim();
            Optional<Student> foundStudent = "General".equalsIgnoreCase(deanFaculty)
                    ? studentRepository.findByStudentId(studentId)
                    : studentRepository.findByStudentIdAndFacultyIgnoreCase(studentId, deanFaculty);

            model.addAttribute("student", foundStudent.orElse(null));
            if (!foundStudent.isPresent()) {
                String errorMessage = "No student found with ID: " + studentId
                        + ("General".equalsIgnoreCase(deanFaculty) ? "" : " in " + deanFaculty + " faculty");
                model.addAttribute("error", errorMessage);
            }
            model.addAttribute("searchQuery", studentId);
        }

        model.addAttribute("facultyStudents", facultyStudents);

        return "dean-dashboard";
    }

    // ================== Faculty detect method ==================
    private String getDeanFaculty(HttpSession session) {
        // Check if session already has faculty
        String faculty = (String) session.getAttribute("deanFaculty");
        if (faculty != null) return faculty;

        // Detect from logged-in email
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email != null) {
            String lowerEmail = email.toLowerCase();

            if (lowerEmail.contains("@sci.") || lowerEmail.contains("science")) {
                faculty = "Science";
            } else if (lowerEmail.contains("@eng.") || lowerEmail.contains("engineering")) {
                faculty = "Engineering";
            } else if (lowerEmail.contains("@dental") || lowerEmail.contains("dent")) {
                faculty = "Dental";
            } else if (lowerEmail.contains("@med.") || lowerEmail.contains("medicine")) {
                faculty = "Medicine";
            } else if (lowerEmail.contains("@agri.") || lowerEmail.contains("agriculture")) {
                faculty = "Agriculture";
            } else if (lowerEmail.contains("@vet.") || lowerEmail.contains("veterinary")) {
                faculty = "Veterinary";
            } else if (lowerEmail.contains("@mgt.") || lowerEmail.contains("management")) {
                faculty = "Management";
            } else if (lowerEmail.contains("@arts.") || lowerEmail.contains("arts")) {
                faculty = "Arts";
            } else {
                faculty = "General"; // fallback
            }

            return faculty;
        }

        return "General"; // default fallback
    }
}