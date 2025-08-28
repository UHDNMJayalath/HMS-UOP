package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.PaymentRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ---------------- Submit Payment ----------------
    @PostMapping("/submitPayment")
    public String submitPayment(HttpSession session,
                                @RequestParam String registrationNumber,
                                @RequestParam String semester,
                                @RequestParam String paymentType,
                                @RequestParam Double amount,
                                @RequestParam String slipRefNumber,
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        try {
            Student student = studentRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Student not found."));

            Payment payment = new Payment();
            payment.setStudent(student);
            payment.setRegistrationNumber(registrationNumber);
            payment.setSemester(semester);
            payment.setPaymentType(paymentType);
            payment.setAmount(amount);
            payment.setSlipRefNumber(slipRefNumber);
            payment.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            paymentRepository.save(payment);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Your Payment has been submitted successfully.");
            return "redirect:/Student_Payments";

        } catch (Exception e) {
            log.error("Error processing payment for registration: {}", registrationNumber, e);
            redirectAttributes.addFlashAttribute("errorMessage", " An error occurred while processing your payment.");
            return "redirect:/Student_Payments";
        }
    }

}
