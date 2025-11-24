package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntakeRepository extends JpaRepository<Intake, Integer> {

    @Override
    Optional<Intake> findById(Integer integer);

    @Override
    List<Intake> findAll();
}
