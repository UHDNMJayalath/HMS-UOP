package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    Optional<UserRoles> findByEmail(String email);
}


