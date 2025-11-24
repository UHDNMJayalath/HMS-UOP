package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.SubWarden;
import com.hostelManagementSystem.HostelManagementSystem.repository.SubWardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubWardenService {

    @Autowired
    private SubWardenRepository subWardenRepo;


    public boolean validateSubWarden(){
        return false;
    }

    public Optional<SubWarden> getSubWardenByEmail(String email){
        Optional<SubWarden> subWardenOptional = subWardenRepo.findByEmailIgnoreCase(email);
        return subWardenOptional;
    }

    public void saveSubWarden(SubWarden subWarden){
        subWardenRepo.save(subWarden);
    }


}
