package com.hostelManagementSystem.HostelManagementSystem.repository;
import com.hostelManagementSystem.HostelManagementSystem.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    List<Message> findByRecipientRoleOrderBySentAtDesc(String recipientRole);


    long countByRecipientRoleAndIsRead(String recipientRole, boolean isRead);


    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.recipientRole = ?1 AND m.isRead = false")
    void markAllAsReadByRecipientRole(String recipientRole);
}