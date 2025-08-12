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

<<<<<<< HEAD
    @GetMapping("/Director-AccommodationDivision-dashboard")
=======
    @GetMapping("/Director-AccomodationDivision-dashboard")
>>>>>>> 63e3adfa2dc9af1c32c00a7b9273d8ccb513c943
    public String dashboard(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Hostel> hostels = hostelService.searchHostels(search);
        model.addAttribute("hostels", hostels);
        model.addAttribute("search", search);
<<<<<<< HEAD
        return "Director-AccommodationDivision-dashboard"; // Thymeleaf template name
=======
        return "Director-AccomodationDivision-dashboard"; // Thymeleaf template name
>>>>>>> 63e3adfa2dc9af1c32c00a7b9273d8ccb513c943
    }
}
