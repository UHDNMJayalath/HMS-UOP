package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.ExcelSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExcelSheetRepository extends JpaRepository<ExcelSheet, Integer> {

    @Override
    Optional<ExcelSheet> findById(Integer integer);

    Optional<ExcelSheet> findByName(String name);
}
