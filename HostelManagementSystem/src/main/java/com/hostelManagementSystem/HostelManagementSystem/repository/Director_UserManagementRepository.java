package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Director_UserManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Director_UserManagementRepository extends JpaRepository<Director_UserManagement, Long> {

    // Search by name OR email (case-insensitive)
    List<Director_UserManagement> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    // Filter by role
    List<Director_UserManagement> findByRole(String role);

    // ✅ Custom query: Search by role AND (name OR email)
    @Query("SELECT u FROM Director_UserManagement u " +
            "WHERE u.role = :role AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Director_UserManagement> searchByRoleAndKeyword(String role, String keyword);
}
