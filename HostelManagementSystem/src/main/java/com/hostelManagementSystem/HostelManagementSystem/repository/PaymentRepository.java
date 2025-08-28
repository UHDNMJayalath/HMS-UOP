package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByRegistrationNumber(String registrationNumber);

    List<Payment> findByPaymentType(String paymentType);

    List<Payment> findByRegistrationNumberIgnoreCase(String registrationNumber);

    List<Payment> findByStudent_StudentId(String studentId);

    List<Payment> findBySemester(String semester);

    @Query("SELECT p FROM Payment p WHERE p.registrationNumber = :regNo AND p.semester = :semester")
    List<Payment> findByRegistrationNumberAndSemester(@Param("regNo") String registrationNumber,
                                                      @Param("semester") String semester);

    List<Payment> findByStudent(Student student);

}
