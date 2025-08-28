package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.UserRoles;
import com.hostelManagementSystem.HostelManagementSystem.repository.UserRolesRepository;
import com.hostelManagementSystem.HostelManagementSystem.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRolesRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/verify-code")
    public String showVerifyPage() {
        return "verify-code";
    }

    @GetMapping("/reset-password")
    public String showResetPage() {
        return "reset-password";
    }

    // Step 1: Send Verification Code to Email
    @PostMapping("/forgot-password")
    public String sendVerificationCode(@RequestParam String email, HttpSession session, Model model) {
        Optional<UserRoles> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Email not found.");
            return "forgot-password";
        }

        // Generate 6-digit random code
        String code = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("verificationCode", code);
        session.setAttribute("email", email);

        //  Send email
        try {
            emailService.sendCode(email, code);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to send verification email.");
            return "forgot-password";
        }

        return "verify-code";
    }

    // Step 2: Verify the Code
    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam String code, HttpSession session, Model model) {
        String sessionCode = (String) session.getAttribute("verificationCode");

        if (sessionCode == null || !sessionCode.equals(code)) {
            model.addAttribute("error", "Invalid verification code.");
            return "verify-code";
        }

        return "reset-password";
    }

    // Step 3: Reset the Password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String password,
                                @RequestParam String confirmPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password";
        }

        String email = (String) session.getAttribute("email");
        Optional<UserRoles> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserRoles user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            session.invalidate(); // Clear session
            redirectAttributes.addFlashAttribute("success", "Password changed successfully. Please log in.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("error", "An error occurred. Try again.");
        return "redirect:/reset-password";
    }
}
