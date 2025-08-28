package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Hostel;
import com.hostelManagementSystem.HostelManagementSystem.service.HostelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DirectorDashboardController {

    private final HostelService hostelService;

    public DirectorDashboardController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @GetMapping("/Director-AccomodationDivision-dashboard")
    public String dashboard(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Hostel> hostels = hostelService.searchHostels(search);
        model.addAttribute("hostels", hostels);
        model.addAttribute("search", search);
        return "Director-AccomodationDivision-dashboard"; // Thymeleaf template name
    }
}
