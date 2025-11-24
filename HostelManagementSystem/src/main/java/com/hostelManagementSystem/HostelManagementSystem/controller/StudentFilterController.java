package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.service.HostelService;
import com.hostelManagementSystem.HostelManagementSystem.service.StudentService;
import com.hostelManagementSystem.HostelManagementSystem.dto.StudentFilterRequest;
import com.hostelManagementSystem.HostelManagementSystem.dto.StudentFilterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentFilterController {

    @Autowired
    private StudentService studentService;

    private HostelService hostelService;

    @PostMapping("/filter-students")
    public StudentFilterResponse filterStudent(@RequestBody StudentFilterRequest request){

        List<Student> filteredList = studentService.filterStudent(request.getFaculty(), request.getIntake(), request.getGender(),
                request.getCriteriaType(), request.getDistanceThreshold(), request.getStudentCount());

        request.printRequest();
        return new StudentFilterResponse(filteredList.size(), filteredList);
    }
}
