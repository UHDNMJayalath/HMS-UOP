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
        }

        return view;
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam("confirm_password") String confirmPassword,
                                Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }

        if (!email.endsWith("pdn.ac.lk")) {
            model.addAttribute("error", "Email must end with pdn.ac.lk");
            return "signup";
        }

        if (dashboardRoutingService.existsByEmail(email)) {
            model.addAttribute("error", "Email is already registered");
            return "signup";
        }

        dashboardRoutingService.saveNewUser(email, password);
        return "redirect:/?signupSuccess"; // Optional: Add query param to indicate success
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
