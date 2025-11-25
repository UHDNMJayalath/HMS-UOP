package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Damage;
import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.DamageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageService {

    @Autowired
    private DamageRepository damageRepo;

    public void confirmDamage(Student student, String hostel, Payment payment){
        List<Damage> damages = this.damageRepo.findByStudentAndHostel(student, hostel);

        for (Damage damage: damages){
            if (damage.getFine() == payment.getAmount()){
                damage.setPaymentStatus("paid");
                damageRepo.save(damage);
                break;
            }
        }
    }
}
