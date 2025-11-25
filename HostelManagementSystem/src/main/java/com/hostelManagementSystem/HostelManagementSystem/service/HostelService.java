package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Allocation;
import com.hostelManagementSystem.HostelManagementSystem.entity.Hostel;
import com.hostelManagementSystem.HostelManagementSystem.entity.Room;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.HostelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HostelService {

    @Autowired
    private final HostelRepository hostelRepo;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AllocationService allocationService;

    public HostelService(HostelRepository hostelRepository) {
        this.hostelRepo = hostelRepository;
    }

    public void saveHostel(Hostel hostel){
        this.hostelRepo.save(hostel);
    }

    public List<Hostel> getAllHostels() {
        return hostelRepo.findAll();
    }

    public List<Hostel> searchHostels(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return hostelRepo.findByNameContainingIgnoreCase(keyword);
        }
        return hostelRepo.findAll();
    }

    public boolean addRoom(Integer hostelId,String roomNo, Integer floor, Integer capacity){

        Room room = new Room(hostelId, roomNo, floor, capacity);
        roomService.saveRoom(room);
        return false;
    }

    public void addRoom(Room room){
        roomService.saveRoom(room);
        Optional<Hostel> hostelOpt = hostelRepo.findById(room.getHostelId());
        if (hostelOpt.isPresent()){
            Hostel hostel = hostelOpt.get();
            int totalRooms = hostel.getTotalRooms();
            int availableRooms = hostel.getAvailableRooms();
            totalRooms ++;
            availableRooms++;
            hostel.setTotalRooms(totalRooms);
            hostel.setAvailableRooms(availableRooms);
            hostelRepo.save(hostel);
        }
    }

    public Optional<Hostel> getHostelById(Integer hostelID){
        return hostelRepo.findById(hostelID);
    }

    public int getRoomCount(Integer hostelID){
        return this.roomService.getHostelRoomCount(hostelID);
    }

    public List<Room> getAllRooms(Integer hostelId){
        return this.roomService.getRoomsByHostel(hostelId);
    }

    public List<Room> getFullyAvailableRooms(Integer hostelId){
        return this.roomService.getFullyAvailableRooms(hostelId);
    }

    public List<Room> getPartiallyAvailableRooms(Integer hostelId){
        return this.roomService.getPartiallyAvailableRooms(hostelId);
    }

    public int FullyAvailableRoomCount(Integer hostelId){
        return this.getFullyAvailableRooms(hostelId).size();
    }

    public int partiallyAvailableRoomCount(Integer hostelId){
        return this.getPartiallyAvailableRooms(hostelId).size();
    }

    public int totalRoomSpace(Integer hostelId){
        List<Room> rooms = this.getAllRooms(hostelId);
        int totalSpace = 0;
        for (Room room: rooms){
            totalSpace = totalSpace + room.getCapacity();
        }
        return totalSpace;
    }

    public int FullyAvailableRoomSpace(Integer hostelId){
        List<Room> rooms = this.getFullyAvailableRooms(hostelId);
        int totalSpace = 0;
        for (Room room: rooms){
            totalSpace = totalSpace + room.getCapacity();
        }

        int allocations = this.allocationService.countByHostelId(hostelId);
        return totalSpace-allocations;
    }

    public int partiallyAvailableRoomSpace(Integer hostelId){
        List<Room> rooms = this.getPartiallyAvailableRooms(hostelId);
        int totalSpace = 0;
        for (Room room: rooms){
            int space = room.getCapacity() - room.getCurrentAllocations();
            totalSpace = totalSpace + space;
        }
        return totalSpace;
    }

    public int FullyAvailablePercentage(Integer hostelId){
        if (this.totalRoomSpace(hostelId) != 0){
            return (this.FullyAvailableRoomSpace(hostelId) * 100)/this.totalRoomSpace(hostelId);
        }
        else {
            return 0;
        }
    }

    public int partiallyAvailablePercentage(Integer hostelId){
        if (this.totalRoomSpace(hostelId) != 0){
            return (this.FullyAvailableRoomSpace(hostelId) * 100)/this.totalRoomSpace(hostelId);
        }
        else {
            return 0;
        }
    }

    public void allocate(Integer hostelId, List<String> studentRegNos, Integer academicYear) {

        Hostel hostel = hostelRepo.findById(hostelId)
                .orElseThrow(() -> new RuntimeException("Hostel not found"));

        List<Allocation> allocations = new ArrayList<>();

        for (String regNo : studentRegNos) {
            Student student = studentService.findById(regNo);
            student.setAcademicYear(academicYear);
            studentService.saveStudent(student);
            Allocation allocation = new Allocation(student.getStudentId(),hostelId,academicYear);
            allocations.add(allocation);
        }

        // also update available spaces in hostel
        //hostel.setAvailableSpaces(hostel.getAvailableSpaces() - studentRegNos.size());
        //hostelRepository.save(hostel);
        allocationService.addAllAllocation(allocations);
    }

}
