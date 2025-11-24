package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Faculty;
import com.hostelManagementSystem.HostelManagementSystem.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepo;

    public List<Faculty> getAllFaculties(){
        return  facultyRepo.findAll();
    }

    public Optional<Faculty> findById(int id){
        return facultyRepo.findById(id);
    }

    public Integer getFacultyId(String faculty){
        List<Faculty> faculties = this.getAllFaculties();
        for (Faculty faculty1: faculties){
            if (faculty1.getName().equalsIgnoreCase(faculty)){
                return faculty1.getId();
            }
        }
        return 0;
    }
}
