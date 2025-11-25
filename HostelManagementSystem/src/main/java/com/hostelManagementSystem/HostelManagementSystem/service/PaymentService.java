package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Allocation;
import com.hostelManagementSystem.HostelManagementSystem.entity.Payment;
import com.hostelManagementSystem.HostelManagementSystem.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private AllocationService allocationService;

    @Autowired PaymentRepository paymentRepo;

    @Autowired
    private DamageService damageService;

    @Autowired
    private HostelService hostelService;


    public List<Payment> getPaymentsByCurrentStudents(Integer  hostelId){
        List<Allocation> allocations = allocationService.findByHostelIdAndDeallocatedDateIsNull(hostelId);
        List<Payment> payments = paymentRepo.findByVerifiedFalse();
        List<Payment> paymentsByCurrentStudents = new ArrayList<>();
        for (Allocation allocation: allocations){
            for (Payment payment: payments){
                if (payment.getStudent().getStudentId().equalsIgnoreCase(allocation.getStudentId())){
                    paymentsByCurrentStudents.add(payment);
                }
            }
        }
        return paymentsByCurrentStudents;
    }

    public Payment findById(Long id){
        Optional<Payment> paymentOpt = paymentRepo.findById(id);
        return paymentOpt.orElse(null);
    }

    public boolean verifyPayment(Long paymentId, String studentId, Integer hostelId){
        Payment payment = this.findById(paymentId);
        if (payment != null){
            payment.setVerified(true);
            paymentRepo.save(payment);

            if (payment.getPaymentType().equalsIgnoreCase("damage fee")){
                this.damageService.confirmDamage(payment.getStudent(),hostelService.getHostelById(hostelId).get().getName(),payment);
            }
            return true;
        }
        else {
            return false;
        }
    }
}
