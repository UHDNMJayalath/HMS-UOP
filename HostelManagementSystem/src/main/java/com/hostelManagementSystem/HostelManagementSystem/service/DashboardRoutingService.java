package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.entity.UserRoles;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.UserRolesRepository;
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

                case "sub warden":
                    return "sub warden-dashboard";

                case "management_assistant":
                    return "management-dashboard";

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

    public boolean existsByEmail(String email) {
        return userRolesRepo.findByEmail(email).isPresent();
    }

    private String determineRoleByEmail(String email) {

        return switch (email.toLowerCase()) {
            case "vc@pdn.ac.lk", "dvc@pdn.ac.lk" -> "vc_dvc";
            case "bursar@pdn.ac.lk" -> "student_services_bursar";
            case "registration@pdn.ac.lk" -> "registration_branch";
            case "dean@pdn.ac.lk", "dean@dental.pdn.ac.lk" -> "dean";
            case "director@pdn.ac.lk" -> "director_accommodation_division";
            case "assistant@pdn.ac.lk" -> "management_assistant";
            case "subwarden@pdn.ac.lk" -> "sub warden";
            default -> "student";
        };
    }


    public void saveNewUser(String email, String rawPassword) {
        UserRoles newUser = new UserRoles();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole(determineRoleByEmail(email)); // dynamic role
        userRolesRepo.save(newUser);
    }

    @Transactional
    public UserRoles findOrCreateOAuthUser(String email) {
        return userRolesRepo.findByEmail(email).orElseGet(() -> {
            String randomPass = UUID.randomUUID().toString();
            UserRoles u = new UserRoles();
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode(randomPass));
            u.setRole(determineRoleByEmail(email));
            return userRolesRepo.save(u);
        });
    }



}
