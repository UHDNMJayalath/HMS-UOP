package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.service.DashboardRoutingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private DashboardRoutingService dashboardRoutingService;

    @GetMapping("/")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        String view = dashboardRoutingService.getDashboardByEmailAndPassword(email, password, model);

        if (!view.equals("login")) {
            session.setAttribute("loggedInUserEmail", email);

            // Store dean faculty information if user is a dean
            if (view.equals("dean-dashboard")) {
                String faculty = determineFacultyFromEmail(email);
                session.setAttribute("deanFaculty", faculty);
                session.setAttribute("userRole", "dean");
            }
        } else {
            // DEBUG: Login failed
            System.out.println("Login failed for email: " + email);
        }

        return view;
    }

    // Improved faculty detection method
    private String determineFacultyFromEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "General";
        }

        String lowerEmail = email.toLowerCase();

        // Check for faculty-specific email patterns
        if (lowerEmail.contains("@sci.pdn.ac.lk") || lowerEmail.contains("science") || lowerEmail.contains("sci_")) {
            return "Science";
        } else if (lowerEmail.contains("@eng.pdn.ac.lk") || lowerEmail.contains("engineering") || lowerEmail.contains("eng_")) {
            return "Engineering";
        } else if (lowerEmail.contains("@dental.pdn.ac.lk") || lowerEmail.contains("dent")) {
            return "Dental";
        } else if (lowerEmail.contains("@med.pdn.ac.lk") || lowerEmail.contains("medicine")) {
            return "Medicine";
        } else if (lowerEmail.contains("@agri.pdn.ac.lk") || lowerEmail.contains("agriculture")) {
            return "Agriculture";
        } else if (lowerEmail.contains("@vet.pdn.ac.lk") || lowerEmail.contains("veterinary")) {
            return "Veterinary";
        } else if (lowerEmail.contains("@mgt.pdn.ac.lk") || lowerEmail.contains("management")) {
            return "Management";
        } else if (lowerEmail.contains("@arts.pdn.ac.lk") || lowerEmail.contains("arts")) {
            return "Arts";
        } else if (lowerEmail.contains("@ahs.pdn.ac.lk") || lowerEmail.contains("allied")) {
            return "Allied Health Sciences";
        } else {
            // Check for dean-specific emails
            if (lowerEmail.contains("dean")) {
                if (lowerEmail.contains("sci")) return "Science";
                if (lowerEmail.contains("eng")) return "Engineering";
                if (lowerEmail.contains("dent")) return "Dental";
                if (lowerEmail.contains("med")) return "Medicine";
                if (lowerEmail.contains("agri")) return "Agriculture";
                if (lowerEmail.contains("vet")) return "Veterinary";
                if (lowerEmail.contains("mgt")) return "Management";
                if (lowerEmail.contains("arts")) return "Arts";
                if (lowerEmail.contains("ahs")) return "Allied Health Sciences";
            }
            return "General";
        }
    }
}