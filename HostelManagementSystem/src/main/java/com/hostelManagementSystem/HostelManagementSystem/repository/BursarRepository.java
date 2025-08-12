package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.SubWarden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BursarRepository extends JpaRepository<SubWarden, Long> {
    Optional<SubWarden> findByEmail(String email);
}
