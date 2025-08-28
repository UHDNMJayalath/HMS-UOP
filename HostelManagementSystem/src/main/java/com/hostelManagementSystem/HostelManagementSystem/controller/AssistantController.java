package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.Assistant;
import com.hostelManagementSystem.HostelManagementSystem.repository.AssistantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

public class AssistantController {

    private AssistantRepository assistantRepo;

    @GetMapping("/distance_calculation")
    public String getDistanceCalculationPage(HttpSession session, Model model){

        System.out.println("Excecuting started");
        String email = (String) session.getAttribute("loggedInUserEmail") ;

        if (email == null) {

            return "redirect:/login";
        }

        Optional<Assistant> assistantOptional = assistantRepo.findByEmailIgnoreCase(email);

        if (assistantOptional.isPresent()){
            model.addAttribute("assistant", assistantOptional.get());
            return "distance_calculation";
        }

        else {
            return "error";
        }

    }


}
