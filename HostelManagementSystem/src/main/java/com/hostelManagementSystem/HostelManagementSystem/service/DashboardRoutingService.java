package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;
import java.util.UUID;

@Service
public class DashboardRoutingService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private UserRolesRepository userRolesRepo;

    @Autowired
    private AssistantRepository assistantRepo;

    @Autowired
    private SubWardenRepository subWardenRepo;

    @Autowired
    private HostelRepository hostelRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getDashboardByEmailAndPassword(String email, String rawPassword, Model model) {
        Optional<UserRoles> roleOpt = userRolesRepo.findByEmail(email);
        if (roleOpt.isPresent()) {
            UserRoles user = roleOpt.get();

            // Password check
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                model.addAttribute("error", "Incorrect password.");
                return "login";
            }

            String role = user.getRole().toLowerCase();

            switch (role) {
                case "vc_dvc":
                    return "vc_dvc-dashboard";

                case "student_services_bursar":
                    return "bursar-dashboard";

                case "dean":
                    return "dean-dashboard";

                // ===============================================================
                // 🔴 මෙන්න මේ කොටස තමයි වෙනස් කළේ (FIXED)
                // ===============================================================
                case "sub warden":
                    // මෙතනදී Hostel ID එක චෙක් කරන්න එපා. Sub Warden කෙනෙක්ද කියලා විතරක් බලන්න.
                    Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
                    if (subWarden.isPresent()){
                        // Hostel ID තිබුනත් නැතත් Dashboard Controller එකට යවන්න.
                        // එතනින් (Controller එකෙන්) බලාගනී එයාට ID එකක් තියෙනවද නැද්ද කියලා.
                        return "redirect:/SubWarden_Dash_Board";
                    }
                    else {
                        model.addAttribute("error", "Sub Warden details not found in database.");
                        return "login";
                    }
                    // ===============================================================

                case "management_assistant":
                    Optional<Assistant> assistant = assistantRepo.findByEmailIgnoreCase(email);
                    if (assistant.isPresent()){
                        model.addAttribute("assistant",assistant.get());
                        return "redirect:/ExcelFileList";
                    }
                    else {
                        model.addAttribute("error", "MA details not found.");
                        return "login";
                    }

                case "registration_branch":
                    return "srb-dashboard";

                case "director_accommodation_division":
                    return "Director-AccomodationDivision-dashboard";

                case "student":
                    Optional<Student> studentOpt = studentRepo.findByEmailIgnoreCase(email);
                    if (studentOpt.isPresent()) {
                        model.addAttribute("student", studentOpt.get());
                        return "student-dashboard";
                    } else {
                        model.addAttribute("error", "Student details not found.");
                        return "login";
                    }

                default:
                    model.addAttribute("error", "Invalid role.");
                    return "login";
            }
        }

        model.addAttribute("error", "Invalid email.");
        return "login";
    }

    // ... (අනිත් Methods එහෙමම තියන්න) ...
    public boolean existsByEmail(String email) {
        return userRolesRepo.findByEmail(email).isPresent();
    }

    public boolean isStudentExists(String email) {
        return studentRepo.findByEmailIgnoreCase(email).isPresent();
    }

    public boolean isAllowedStaff(String email) {
        String role = determineRoleByEmail(email);
        if ("student".equals(role)) return false;
        if ("management_assistant".equals(role)) return assistantRepo.findByEmailIgnoreCase(email).isPresent();
        return true;
    }

    private String determineRoleByEmail(String email) {
        if (email == null) return "student";
        String lowerEmail = email.toLowerCase();

        if (lowerEmail.contains("vc@") || lowerEmail.contains("dvc@")) return "vc_dvc";
        else if (lowerEmail.contains("bursar")) return "student_services_bursar";
        else if (lowerEmail.contains("registration")) return "registration_branch";
        else if (lowerEmail.contains("dean")) return "dean";
        else if (lowerEmail.contains("director")) return "director_accommodation_division";
        else if (lowerEmail.contains("assistant")) return "management_assistant";
        else if (lowerEmail.contains("subwarden") || lowerEmail.contains("warden")) return "sub warden";

        return "student";
    }

    public void saveNewUser(String email, String rawPassword) {
        UserRoles newUser = new UserRoles();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole(determineRoleByEmail(email));
        userRolesRepo.save(newUser);
    }
}