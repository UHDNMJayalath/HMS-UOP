package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.model.MessageForm;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import com.hostelManagementSystem.HostelManagementSystem.service.MessagingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class deanController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MessagingService messagingService;

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

    // ---------------------------------------------------------------------
    // Messaging Methods
    // ---------------------------------------------------------------------

    @GetMapping("/dean/message/new")
    public String showMessageForm(Model model, HttpSession session) {
        model.addAttribute("messageForm", new MessageForm());
        model.addAttribute("faculty", session.getAttribute("deanFaculty"));
        model.addAttribute("deanEmail", session.getAttribute("loggedInUserEmail"));
        return "dean-message-form";
    }

    @PostMapping("/dean/message/send")
    public String sendMessage(@ModelAttribute("messageForm") MessageForm messageForm,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        // Retrieve actual Dean Faculty and ID from session
        String deanFaculty = (String) session.getAttribute("deanFaculty");
        Long deanId = (Long) session.getAttribute("loggedInUserId");

        // Fallback/Validation for Faculty
        if (deanFaculty == null) {
            deanFaculty = getDeanFaculty(session);
            session.setAttribute("deanFaculty", deanFaculty);
        }

        // Fallback for ID (Temporary measure if not set during login)
        if (deanId == null) {
            deanId = 1L;
        }

        try {
            // Check if faculty is invalid
            if (deanFaculty == null || deanFaculty.trim().isEmpty() || "General".equalsIgnoreCase(deanFaculty)) {
                throw new IllegalArgumentException("Cannot send message. Faculty information is missing or general.");
            }

            messagingService.saveMessage(deanId, deanFaculty, messageForm.getSubject(), messageForm.getBody());

            // Add success message to RedirectAttributes
            redirectAttributes.addFlashAttribute("successMessage", "✅ Notification successfully submitted to the Director of Accommodation.");

        } catch (IllegalArgumentException e) {
            // Add error message to RedirectAttributes
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dean/message/new"; // Redirect back to form on error
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ An unexpected error occurred while submitting the notification.");
        }
        // Redirect to dashboard to display the flash success message
        return "redirect:/dean-dashboard";
    }
}