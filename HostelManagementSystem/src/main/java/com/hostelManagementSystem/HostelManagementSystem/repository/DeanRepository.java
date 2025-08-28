package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Dean;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeanRepository extends JpaRepository<Dean, Long> {
    Optional<Dean> findByEmail(String email);
}
