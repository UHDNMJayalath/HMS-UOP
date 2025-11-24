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

    // 1. Student Dashboard එක සඳහා (දැනට තියෙන එක එහෙමම තියන්න)
    // ශිෂ්‍යයාට තමන් දාපු Complaints බලාගන්න මේක ඕනේ.
    List<ComplaintRequest> findByStudent(Student student);

    // 2. Sub Warden Dashboard එක සඳහා (අලුතින් එකතු කරන කොටස)
    // පැමිණිල්ල 'Sub Warden' ට දාපු එකක් වෙන්න ඕනේ.
    // ඒ වගේම පැමිණිල්ල දාපු ළමයා ඉන්නේ Sub Warden බාරව ඉන්න Hostel එකේ වෙන්න ඕනේ.
    @Query("SELECT c FROM ComplaintRequest c WHERE c.receiver = 'Sub Warden' AND c.student.currentHostel = :hostelName")
    List<ComplaintRequest> findBySubWardenHostel(@Param("hostelName") String hostelName);

    // පහත method එක interface එකට එකතු කරන්න
    List<ComplaintRequest> findByReceiverOrderBySubmittedAtDesc(String receiver);

}