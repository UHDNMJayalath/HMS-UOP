package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Registrar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrarRepository extends JpaRepository<Registrar,Integer> {

    Optional<Registrar> findByEmailIgnoreCase(String email);

}
