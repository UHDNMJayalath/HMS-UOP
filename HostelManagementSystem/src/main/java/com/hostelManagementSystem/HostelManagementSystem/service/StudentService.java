package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Batch;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private BatchService batchService;

    // =========================================================
    // 1. Existing Method (Used by SubWardenController)
    // =========================================================
    public Student findById(String studentId){
        Optional<Student> student = studentRepo.findByStudentId(studentId);
        if (student.isPresent()){
            return student.get();
        }
        else {
            return null;
        }
    }

    // =========================================================
    // 2. NEW METHOD: Fixes Error in AssistantController (Line 142)
    // =========================================================
    public Optional<Student> getStudentById(String regNo) {
        return studentRepo.findByStudentId(regNo);
    }

    // =========================================================
    // 3. NEW METHOD: Fixes Error in AssistantController (Distance Update)
    // =========================================================
    public void updateStudentDistance(String regNo, String distance) {
        Optional<Student> studentOpt = studentRepo.findByStudentId(regNo);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setDistance(distance);
            studentRepo.save(student);
        }
    }

    // =========================================================
    // 4. NEW METHOD: Helper to get all students (Good for Dashboard)
    // =========================================================
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    // =========================================================
    // Existing Methods...
    // =========================================================
    public List<Student> loadBatch(Integer batchId){
        Optional<Batch> batchOpt = batchService.findById(batchId);

        if (batchOpt.isPresent()){
            return studentRepo.findByBatchId(batchId);
        }
        return null;
    }

    public List<Student> loadBatchByGender(Integer batchId, String gender){
        Optional<Batch> batchOptional = batchService.findById(batchId);
        if (batchOptional.isPresent()){
            List<Student> batch = this.loadBatch(batchId);
            List<Student> filteredList = new ArrayList<>();
            for (Student student: batch){
                if (student.getSex() != null && student.getSex().equalsIgnoreCase(gender)){
                    filteredList.add(student);
                }
            }
            return filteredList;
        }
        return null;
    }

    public List<Student> filterStudent(String facultyId,
                                       String intakeId,
                                       String gender,
                                       String criteriaType,
                                       Integer distanceThreshold,
                                       Integer studentCount){

        Batch batch = batchService.findByFacultyAndIntake(Integer.parseInt(facultyId),Integer.parseInt(intakeId));

        if(batch == null) return new ArrayList<>(); // Null pointer check

        List<Student> stdList = this.loadBatchByGender(batch.getId(),gender);
        if(stdList == null) stdList = new ArrayList<>();

        List<Student> filteredList = new ArrayList<>();

        if (criteriaType.equalsIgnoreCase("distance")){
            for (Student student: stdList){
                // Distance null නම් error එන එක වලක්වන්න check එකක්
                if (student.getDistance() != null && !student.getDistance().isEmpty()) {
                    try {
                        if (Double.parseDouble(student.getDistance()) >= distanceThreshold){
                            filteredList.add(student);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid distance format for student: " + student.getStudentId());
                    }
                }
            }
            return filteredList;
        }
        else if (criteriaType.equalsIgnoreCase("count")) {
            return stdList.stream()
                    .filter(s -> s.getDistance() != null) // null distance අයින් කිරීම
                    .sorted(Comparator.comparing(s -> Double.parseDouble(s.getDistance()), Comparator.reverseOrder())) // වැඩිම දුර අනුව
                    .limit(studentCount)
                    .toList();
        }
        return stdList;
    }

    public void saveStudent(Student student){
        studentRepo.save(student);
    }
}