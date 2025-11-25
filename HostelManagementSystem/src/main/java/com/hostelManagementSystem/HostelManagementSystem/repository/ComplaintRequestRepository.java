package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.ComplaintRequest;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRequestRepository extends JpaRepository<ComplaintRequest, Long> {

    List<ComplaintRequest> findByStudent(Student student);

    // Update: 'Sub Warden' හෝ 'SubWarden' යන දෙකම Receiver ලෙස තිබුණත් දත්ත ලබා ගනී.
    @Query("SELECT c FROM ComplaintRequest c WHERE (c.receiver = 'Sub Warden' OR c.receiver = 'SubWarden') AND c.student.currentHostel = :hostelName")
    List<ComplaintRequest> findBySubWardenHostel(@Param("hostelName") String hostelName);

    List<ComplaintRequest> findByReceiverOrderBySubmittedAtDesc(String receiver);
}