package com.hostelManagementSystem.HostelManagementSystem.repository;

import com.hostelManagementSystem.HostelManagementSystem.entity.Room;
import com.hostelManagementSystem.HostelManagementSystem.entity.RoomID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, RoomID> {

    @Override
    Optional<Room> findById(RoomID roomID);

    Integer countByHostelId(Integer hostelId);

    List<Room> findByHostelId(Integer hostelId);
}
