package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Intake;
import com.hostelManagementSystem.HostelManagementSystem.repository.IntakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IntakeService {

    @Autowired
    private IntakeRepository intakeRepo;

    public List<Intake> getAllIntakes(){
        return intakeRepo.findAll();
    }

    public Optional<Intake> findById(int id){
        return intakeRepo.findById(id);
    }

    public Integer getIntakeId(String intake){
        List<Intake> intakes = this.getAllIntakes();
        for (Intake intake1 : intakes){
            if (intake1.getIntake().equals(intake)){
                return intake1.getId();
            }
        }
        return 0;
    }
}
