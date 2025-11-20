package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.PaymentRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import com.hostelManagementSystem.HostelManagementSystem.service.S3Service;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired // <<< Autowire S3Service
    private S3Service s3Service;

    // ---------------- Submit Payment ----------------
    @PostMapping("/submitPayment")
    public String submitPayment(HttpSession session,
                                @RequestParam String registrationNumber,
                                @RequestParam String semester,
                                @RequestParam String paymentType,
                                @RequestParam Double amount,
                                @RequestParam String slipRefNumber,
                                @RequestParam("pdfImageFile") MultipartFile pdfImageFile, // <<< FILE PARAMETER එක එකතු කළා
                                RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";


        try {

            if (pdfImageFile.isEmpty() || pdfImageFile.getSize() == 0) {
                throw new IllegalArgumentException("Payment slip file is missing or empty.");
            }


            String s3Key = s3Service.uploadFile(pdfImageFile, "payment-slips");


            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                    s3Service.getBucketName(),
                    s3Service.getRegion(),
                    s3Key);


            // 2. Find Student and Save to DB
            Student student = studentRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new RuntimeException("Student not found."));

            Payment payment = new Payment();
            payment.setStudent(student);
            payment.setRegistrationNumber(registrationNumber);
            payment.setSemester(semester);
            payment.setPaymentType(paymentType);
            payment.setAmount(amount);
            payment.setSlipRefNumber(slipRefNumber);
            payment.setSlipUrl(fileUrl);

            payment.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            paymentRepository.save(payment);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Your Payment has been submitted successfully and slip uploaded.");
            return "redirect:/Student_Payments";

        } catch (IOException e) {
            log.error("S3 Upload Error for registration: {}", registrationNumber, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading slip. Please check file size/format.");
            return "redirect:/Student_Payments";
        } catch (Exception e) {
            // Includes RuntimeException from student find or JPA errors
            log.error("Error processing payment/DB save for registration: {}", registrationNumber, e);
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while processing your payment or saving data.");
            return "redirect:/Student_Payments";
        }
    }

}