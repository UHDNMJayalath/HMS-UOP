package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.dto.AllocationRequest;
import com.hostelManagementSystem.HostelManagementSystem.dto.AllocationResponse;
import com.hostelManagementSystem.HostelManagementSystem.service.AllocationService;
import com.hostelManagementSystem.HostelManagementSystem.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AllocationController {

    @Autowired
    private HostelService hostelService;

    @Autowired
    private AllocationService allocationService;

    @PostMapping("/allocate-students")
    public AllocationResponse allocateStudents(@RequestBody AllocationRequest request) {
        try {
            // call service method
            hostelService.allocate(request.getHostelId().intValue(), request.getStudents(), request.getAcademicYear());
            return new AllocationResponse(true, "Allocation successful");
        } catch (Exception e) {
            return new AllocationResponse(false, e.getMessage());
        }
    }

    @GetMapping("/allocated-students")
    public List<String> getAllocatedStudents(
            @RequestParam Long hostelId,
            @RequestParam Integer year) {

        List<String> stdId = allocationService.findStudentIdsByHostelIDAndYear(hostelId.intValue(), year);

        if (stdId.isEmpty()){
            System.out.println("Empty List");
        }
        else {
            for(String id: stdId){
                System.out.println(id);
            }
        }
        return stdId;
    }
}
