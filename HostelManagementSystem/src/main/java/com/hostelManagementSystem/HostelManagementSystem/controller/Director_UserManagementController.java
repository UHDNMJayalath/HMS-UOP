package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_UserManagement;
import com.hostelManagementSystem.HostelManagementSystem.service.Director_UserManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Director-User_management")
public class Director_UserManagementController {
    private final Director_UserManagementService userService;

    public Director_UserManagementController(Director_UserManagementService userService) {
        this.userService = userService;
    }

    // Display all users with search + role filter
    @GetMapping
    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "role", required = false) String role,
                            Model model) {
        List<Director_UserManagement> users = userService.searchUsers(keyword, role);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);
        return "Director-User_management";
    }

    // Add user
    @PostMapping("/add")
    public String addUser(@ModelAttribute Director_UserManagement user) {
        userService.addUser(user);
        System.out.println(user.getEmail());
        return "redirect:/Director-User_management";
    }

    // Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/Director-User_management";
    }
}
