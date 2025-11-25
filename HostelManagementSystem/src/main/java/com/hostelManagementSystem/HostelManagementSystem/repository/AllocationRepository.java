package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Allocation;
import com.hostelManagementSystem.HostelManagementSystem.entity.AllocationID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AllocationRepository extends JpaRepository<Allocation, AllocationID> {

    //List<String> findStudentIdsByHostelIDAndAcademicYear(Integer hostelId,Integer academicYear);

    @Query("SELECT a.studentId FROM Allocation a WHERE a.hostelId = :hostelId AND a.academicYear = :academicYear")
    List<String> findStudentIdsByHostelIdAndAcademicYear(
            @Param("hostelId") Integer hostelId,
            @Param("academicYear") Integer academicYear
    );

    Integer countByHostelId(
            @Param("hostelId") Integer hostelId
    );

    List<Allocation> findByHostelId(Integer hostelId);

    //Optional<Allocation> findByHostelIdAndStudentId(Integer hostelId, String studentId);

    Optional<Allocation> findByStudentId(String studentId);

    @Query("SELECT a FROM Allocation a WHERE a.studentId = :studentId AND a.hostelId = :hostelId")
    List<Allocation> findByStudentAndHostel(@Param("studentId") String studentId,
                                            @Param("hostelId") Integer hostelId);

    List<Allocation> findByStudentIdAndHostelId(String studentId, Integer hostelId);

    List<Allocation> findByRoomIdAndHostelId(String roomId, Integer hostelId);

    Optional<Allocation> findByStudentIdAndHostelIdAndAcademicYear(
            String studentId, Integer hostelId, Integer academicYear);

    List<Allocation> findByHostelIdAndDeallocatedDateIsNull(Integer hostelId);

    List<Allocation> findByStudentIdAndDeallocatedDateIsNull(String studentId);
}
