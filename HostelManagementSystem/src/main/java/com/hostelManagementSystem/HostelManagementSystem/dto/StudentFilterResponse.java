package com.hostelManagementSystem.HostelManagementSystem.dto;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;

import java.util.List;

public class StudentFilterResponse {

    private int count;
    private List<Student> students;

    public StudentFilterResponse(int count, List<Student> students) {
        this.count = count;
        this.students = students;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
