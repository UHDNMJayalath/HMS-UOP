package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Batch;
import com.hostelManagementSystem.HostelManagementSystem.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BatchService {

    @Autowired
    private BatchRepository batchRepo;

    public Optional<Batch> findById(int id){
        return this.batchRepo.findById(id);
    }

    public List<Batch> getAllBatches(){
        return this.batchRepo.findAll();
    }

    public List<Batch> findByFaculty(int facultyId){
        List<Batch> allBatches = this.getAllBatches();
        List<Batch> batchesSelectedByFaculty = new ArrayList<>();

        for (Batch batch: allBatches){
            if (batch.getFacultyId() == facultyId){
                batchesSelectedByFaculty.add(batch);
            }
        }
        return batchesSelectedByFaculty;
    }

    public List<Batch> findByIntake(int intakeId){
        List<Batch> allBatches = this.getAllBatches();
        List<Batch> batchesSelectedByIntake = new ArrayList<>();

        for (Batch batch: allBatches){
            if (batch.getIntakeId() == intakeId){
                batchesSelectedByIntake.add(batch);
            }
        }
        return batchesSelectedByIntake;
    }

    public Batch findByFacultyAndIntake(int facultyId, int intakeId){
        List<Batch> allBatches = this.getAllBatches();

        for (Batch batch: allBatches){
            if (batch.getFacultyId() == facultyId && batch.getIntakeId() == intakeId){
                return batch;
            }

        }
        return null;
    }




}
