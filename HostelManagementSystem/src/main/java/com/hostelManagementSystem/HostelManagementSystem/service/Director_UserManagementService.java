package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_UserManagement;
import com.hostelManagementSystem.HostelManagementSystem.entity.SubWarden;
import com.hostelManagementSystem.HostelManagementSystem.repository.Director_UserManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Director_UserManagementService {

    @Autowired
    private SubWardenService subWardenService;

    private final Director_UserManagementRepository userRepository;

    public Director_UserManagementService(Director_UserManagementRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add new user
    public Director_UserManagement addUser(Director_UserManagement user) {
        if (user.getRole().equalsIgnoreCase("SubWarden")){
            SubWarden subWarden = new SubWarden(user.getEmail(), user.getName());
            subWardenService.saveSubWarden(subWarden);
            System.out.println("Sub warden created : " + subWarden.getName());
        }
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Search + Role filter
    public List<Director_UserManagement> searchUsers(String keyword, String role) {
        if ((keyword == null || keyword.trim().isEmpty()) && (role == null || role.isEmpty())) {
            return userRepository.findAll();
        } else if (role == null || role.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
        } else if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findByRole(role);
        } else {

            return userRepository.searchByRoleAndKeyword(role, keyword);
        }
    }
}
