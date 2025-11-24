package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Room;
import com.hostelManagementSystem.HostelManagementSystem.entity.RoomID;
import com.hostelManagementSystem.HostelManagementSystem.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepo;

    public void saveRoom(Room room){
        roomRepo.save(room);
    }

    public Room findRoom(RoomID roomID){
        Optional<Room> room = roomRepo.findById(roomID);
        if (room.isPresent()) {
            return room.get();
        }
        else {
            return null;
        }
    }

    public Integer getHostelRoomCount(Integer hostelID){
        return this.roomRepo.countByHostelId(hostelID);
    }

    public List<Room> getRoomsByHostel(Integer hostelId){
        return this.roomRepo.findByHostelId(hostelId);
    }

    public List<Room> getAvailableRooms(Integer hostelId){
        List<Room> allRooms = this.getRoomsByHostel(hostelId);
        List<Room> availableRooms = new ArrayList<>();
        for (Room room: allRooms){
            if (room.getCurrentAllocations() < room.getCapacity()){
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Room> getFullyAvailableRooms(Integer hostelId){
        List<Room> availableRooms = this.getAvailableRooms(hostelId);
        List<Room> fullyAvailableRooms = new ArrayList<>();
        for (Room room: availableRooms){
            if (room.getCurrentAllocations() == 0){
                fullyAvailableRooms.add(room);
            }
        }
        return fullyAvailableRooms;
    }

    public List<Room> getPartiallyAvailableRooms(Integer hostelId){
        List<Room> availableRooms = this.getAvailableRooms(hostelId);
        List<Room> partiallyAvailableRooms = new ArrayList<>();
        for (Room room: availableRooms){
            if (room.getCurrentAllocations() != 0){
                partiallyAvailableRooms.add(room);
            }
        }
        return partiallyAvailableRooms;
    }

    public int FullyAvailableRoomCount(Integer hostelId){
        return this.getFullyAvailableRooms(hostelId).size();
    }

    public int partiallyAvailableRoomCount(Integer hostelId){
        return this.getPartiallyAvailableRooms(hostelId).size();
    }

    public Room findById(RoomID roomId){
        Optional<Room> room = roomRepo.findById(roomId);
        if (room.isPresent()){
            return room.get();
        }
        else {
            return null;
        }
    }
}
