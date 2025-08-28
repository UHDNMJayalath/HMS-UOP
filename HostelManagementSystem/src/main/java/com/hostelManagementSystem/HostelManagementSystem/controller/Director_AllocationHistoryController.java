package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_AllocationHistory;
import com.hostelManagementSystem.HostelManagementSystem.service.Director_AllocationHistoryService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class Director_AllocationHistoryController {
    private final Director_AllocationHistoryService service;

    public Director_AllocationHistoryController(Director_AllocationHistoryService service) {
        this.service = service;
    }

    @GetMapping("/Director-Allocation_History")
    public String getAllocationHistory(
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String intake,
            @RequestParam(required = false) String academicYear,
            @RequestParam(required = false) String gender,
            Model model) {

        List<Director_AllocationHistory> allocations = service.getFilteredAllocations(faculty, intake, academicYear, gender);

        model.addAttribute("allocations", allocations);
        model.addAttribute("faculties", service.getAllFaculties());
        model.addAttribute("intakes", service.getAllIntakes());
        model.addAttribute("academicYears", service.getAllAcademicYears());

        return "Director-Allocation_History";
    }
}






