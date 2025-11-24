package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.SubWarden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubWardenRepository extends JpaRepository<SubWarden, Integer> {

    Optional<SubWarden> findByEmailIgnoreCase(String email);
}
