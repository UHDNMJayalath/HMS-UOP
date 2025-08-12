package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Damage;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DamageRepository extends JpaRepository<Damage, Long> {
    List<Damage> findByStudent(Student student);
}
