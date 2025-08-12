package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.entity.SubWarden;
import com.hostelManagementSystem.HostelManagementSystem.repository.BursarRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.PaymentRepository;
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
public class BursarDashboardController {


        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private PaymentRepository paymentRepository;

    @GetMapping("/bursar-dashboard")
    public String bursarDashboard(
            @RequestParam(value = "q", required = false) String studentId,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) {
            return "redirect:/login";
        }
        model.addAttribute("bursarEmail", email);

        if (studentId != null && !studentId.isEmpty()) {
            Optional<Student> studentOpt = studentRepository.findById(studentId);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                model.addAttribute("students", List.of(student));
                List<Payment> payments = paymentRepository.findByStudent(student);
                model.addAttribute("payments", payments);
            } else {
                model.addAttribute("students", List.of());
                model.addAttribute("payments", List.of());
                model.addAttribute("message", "Student ID not found");
            }
        } else {
            model.addAttribute("students", List.of());
            model.addAttribute("payments", List.of());
        }

        return "bursar-dashboard";
    }




}
