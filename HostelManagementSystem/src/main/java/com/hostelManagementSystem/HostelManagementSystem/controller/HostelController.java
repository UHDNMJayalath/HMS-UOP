package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HostelController {

    @Autowired
    private HostelService hostelService;

    @GetMapping("/hostel-capacity/{hostelId}")
    public ResponseEntity<Map<String, Integer>> getHostelCapacity(@PathVariable int hostelId) {

        int availableSpaces = hostelService.FullyAvailableRoomSpace(hostelId);

        Map<String, Integer> response = new HashMap<>();
        response.put("availableSpaces", availableSpaces);

        return ResponseEntity.ok(response);
    }
}
