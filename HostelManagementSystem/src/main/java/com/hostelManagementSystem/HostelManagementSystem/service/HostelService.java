package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Hostel;
import com.hostelManagementSystem.HostelManagementSystem.repository.HostelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostelService {

    private final HostelRepository hostelRepository;

    public HostelService(HostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    public List<Hostel> searchHostels(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return hostelRepository.findByNameContainingIgnoreCase(keyword);
        }
        return hostelRepository.findAll();
    }
}
