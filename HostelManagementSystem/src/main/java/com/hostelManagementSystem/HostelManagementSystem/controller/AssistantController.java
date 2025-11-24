package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.dto.HostelReq;
import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.AssistantRepository;
import com.hostelManagementSystem.HostelManagementSystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AssistantController {

    @Autowired
    private AssistantRepository assistantRepo;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private IntakeService intakeService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private HostelService hostelService;

    // ==========================================
    // Hostel Allocation Page
    // ==========================================
    @RequestMapping("/hostel_allocation")
    public String getHostelAllocationPage(HttpSession session, Model model) {

        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Assistant> assistantOptional = assistantRepo.findByEmailIgnoreCase(email);

        if (assistantOptional.isPresent()) {
            model.addAttribute("assistant", assistantOptional.get());

            List<Faculty> faculties = facultyService.getAllFaculties();
            model.addAttribute("faculties", faculties);

            List<Intake> intakes = intakeService.getAllIntakes();
            model.addAttribute("intakes", intakes);

            return "assistant_hostel_allocation";
        } else {
            return "error";
        }
    }

    @PostMapping("/loadBatch")
    public String loadBatch(
            @RequestParam("facultyId") Integer facultyId,
            @RequestParam("intakeId") Integer intakeId,
            @RequestParam("gender") String gender,
            HttpSession session, Model model) {

        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }
        Optional<Assistant> assistantOptional = assistantRepo.findByEmailIgnoreCase(email);

        if (assistantOptional.isPresent()) {
            model.addAttribute("assistant", assistantOptional.get());

            Batch batch = batchService.findByFacultyAndIntake(facultyId, intakeId);
            if (batch != null) {
                List<Student> studentList = studentService.loadBatchByGender(batch.getId(), gender);
                model.addAttribute("students", studentList);
                model.addAttribute("facultyId", facultyId);
                model.addAttribute("intakeId", intakeId);
                model.addAttribute("gender", gender);

                List<Hostel> hostels = hostelService.getAllHostels();
                List<HostelReq> fullyAvailableHostels = new ArrayList<>();
                List<HostelReq> partiallyAvailableHostels = new ArrayList<>();

                if (hostels != null) {
                    for (Hostel hostel : hostels) {
                        int availableRooms = hostelService.FullyAvailableRoomCount(hostel.getId().intValue());
                        int totalCapacity = hostelService.FullyAvailableRoomSpace(hostel.getId().intValue());
                        int percentage = hostelService.FullyAvailablePercentage(hostel.getId().intValue());

                        fullyAvailableHostels.add(new HostelReq(hostel.getId().intValue(), hostel.getName(), availableRooms, totalCapacity, percentage));
                    }

                    for (Hostel hostel : hostels) {
                        int availableRooms = hostelService.partiallyAvailableRoomCount(hostel.getId().intValue());
                        int totalCapacity = hostelService.partiallyAvailableRoomSpace(hostel.getId().intValue());
                        int percentage = hostelService.partiallyAvailablePercentage(hostel.getId().intValue());

                        partiallyAvailableHostels.add(new HostelReq(hostel.getId().intValue(), hostel.getName(), availableRooms, totalCapacity, percentage));
                    }
                }

                model.addAttribute("fullyAvailableHostels", fullyAvailableHostels);
                model.addAttribute("partiallyAvailableHostels", partiallyAvailableHostels);
            }
            return "assistant_hostel_allocation";
        }
        return null;
    }

    // ==========================================
    // Distance Calculation Section (FIXED)
    // ==========================================

    // 1. Page Load
    @GetMapping("/distance_calculation")
    public String distancePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login"; // Security check


        return "distance_calculation";
    }

    // 2. Search Student
    @GetMapping("/search_student")
    public String searchStudent(@RequestParam("regNo") String regNo, Model model, HttpSession session) {
        // Security Check
        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";

        Optional<Student> student = studentService.getStudentById(regNo);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
        } else {
            model.addAttribute("error", "Student not found with ID: " + regNo);
        }
        model.addAttribute("searchedRegNo", regNo);

        return "distance_calculation";
    }

    // 3. Update Distance
    @PostMapping("/edit_distance")
    public String updateDistance(@RequestParam("regNo") String regNo,
                                 @RequestParam("manualDistance") String distanceStr,
                                 Model model, HttpSession session) {

        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";

        try {
            Double distance = Double.parseDouble(distanceStr);
            studentService.updateStudentDistance(regNo, distance.toString());


            Optional<Student> student = studentService.getStudentById(regNo);
            student.ifPresent(value -> model.addAttribute("student", value));

            model.addAttribute("message", "Distance updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating distance: " + e.getMessage());
        }

        return "distance_calculation";
    }


    @Autowired
    private DistanceService distanceService;
    @PostMapping("/auto_calculate_distance")
    public String autoCalculateDistance(@RequestParam("regNo") String regNo,
                                        Model model, HttpSession session) {

        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";


        Optional<Student> studentOpt = studentService.getStudentById(regNo);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();


            String distance = distanceService.calculateDistance(student.getAddress());

            if (distance != null) {

                studentService.updateStudentDistance(regNo, distance);
                model.addAttribute("message", "Distance calculated automatically: " + distance + " km");
            } else {
                model.addAttribute("error", "Failed to calculate distance. Please check the address.");
            }

            model.addAttribute("student", student); // Data පෙන්නන්න ආයේ යවනවා
        } else {
            model.addAttribute("error", "Student not found.");
        }

        return "distance_calculation";
    }
}


