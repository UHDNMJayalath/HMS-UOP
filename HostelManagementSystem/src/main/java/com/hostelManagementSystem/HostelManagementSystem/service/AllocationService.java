package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.AllocationRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.HostelRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AllocationService {

    @Autowired
    private AllocationRepository allocationRepo;

    @Autowired
    private RoomService roomService;

    @Autowired
    private HostelRepository hostelRepo;

    @Autowired
    private StudentService studentService;

    public void saveAllocation(Allocation allocation){
        allocationRepo.save(allocation);
    }

    public void addAllocation(String studentId, Integer hostelId, Integer academicYear){
        Allocation allocation = new Allocation(studentId, hostelId, academicYear);
        allocationRepo.save(allocation);
    }

    public void addAllAllocation(List<Allocation> allocations){
        allocationRepo.saveAll(allocations);
    }

    public List<String> findStudentIdsByHostelIDAndYear(Integer hostelId, Integer year){
        return allocationRepo.findStudentIdsByHostelIdAndAcademicYear(hostelId, year);
    }

    public Integer countByHostelId(Integer hostelId){
        return this.allocationRepo.countByHostelId(hostelId);
    }

    public List<Allocation> findByHostelId(Integer hostelId){
        return this.allocationRepo.findByHostelId(hostelId);
    }

    public void allocateRoom(String roomNo, List<String> studentsIds){


        for (String studentId : studentsIds) {
            Optional<Allocation> allocationOpt = allocationRepo.findByStudentId(studentId);

            if (allocationOpt.isPresent() && allocationOpt.get().getRoomId()==null) {
                Allocation allocation = allocationOpt.get();
                allocation.setRoomId(roomNo);
                allocation.setAllocatedDate(LocalDate.now());
                allocationRepo.save(allocation);

                int hostelId = allocation.getHostelId();

                Room room = this.roomService.findRoom(new RoomID(hostelId, roomNo));
                int currentAllocation = room.getCurrentAllocations();
                if (currentAllocation < room.getCapacity()){
                    currentAllocation++;
                    room.setCurrentAllocations(currentAllocation);
                }
                roomService.saveRoom(room);

                Hostel hostel = hostelRepo.findById(hostelId).get();
                int residentialStudents = hostel.getResidentialStudents();
                residentialStudents++;
                hostel.setResidentialStudents(residentialStudents);
                if (room.getCurrentAllocations() == room.getCapacity()){
                    int fullyAllocatedRooms = hostel.getFullyAllocatedRooms();
                    fullyAllocatedRooms++;
                    hostel.setFullyAllocatedRooms(fullyAllocatedRooms);

                    int availableRooms = hostel.getAvailableRooms();
                    availableRooms--;
                    hostel.setAvailableRooms(availableRooms);


                }
                hostelRepo.save(hostel);

                Student student = this.studentService.findById(studentId);
                student.setCurrentHostel(hostel.getName());
                studentService.saveStudent(student);

            }
        }
    }

    public List<Allocation> findByStudentIdAndHostelId(String studentId, Integer hostelId){
        return this.allocationRepo.findByStudentIdAndHostelId(studentId, hostelId);
    }

    public List<Allocation> findByRoomIdAndHostelId(String roomId, Integer hostelId){
        return this.allocationRepo.findByRoomIdAndHostelId(roomId, hostelId);
    }

    public Allocation findById(AllocationID allocationID){
        Optional<Allocation> allocation = this.allocationRepo.findByStudentIdAndHostelIdAndAcademicYear(allocationID.getStudentId(),
                allocationID.getHostelId(), allocationID.getAcademicYear());

        return allocation.orElse(null);
    }

    public List<Allocation> currentAllocationByHostel(Integer hostelId){
        List<Allocation> allocations = allocationRepo.findByHostelId(hostelId);
        List<Allocation> currentAllocations = new ArrayList<>();
        for (Allocation allocation: allocations){
            if (allocation.getDeallocatedDate() == null){
                currentAllocations.add(allocation);
            }
        }
        return currentAllocations;
    }

    public List<Allocation> findByHostelIdAndDeallocatedDateIsNull(Integer hostelId){
        return this.allocationRepo.findByHostelIdAndDeallocatedDateIsNull(hostelId);
    }

    public List<Allocation> findByStudentIdAndDeallocatedDateIsNull(String studentId) {
        return allocationRepo.findByStudentIdAndDeallocatedDateIsNull(studentId);
    }

}
