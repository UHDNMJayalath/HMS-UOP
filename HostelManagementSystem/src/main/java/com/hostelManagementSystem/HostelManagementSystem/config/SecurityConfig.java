package com.hostelManagementSystem.HostelManagementSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Static Resources & Common
                                "/", "/login", "/signup", "/css/**", "/js/**",
                                "/assistant.js", "/subWarden.js", "/images/**", // Added /subWarden.js

                                // Student Dashboard & History
                                "/student-dashboard", "/Student_ComplainsAndRequests",
                                "/Student_History_Damage", "/Student_History_Payment", "/Student_History_Residence",
                                "/Student_Payments", "/submitPayment", "/submitForm", "/student/uploadSlip",

                                // Common Dashboards & Auth
                                "/bursar-dashboard", "/vc_dvc-dashboard", "/dean-dashboard",
                                "/forgot-password", "/verify-code", "/reset-password",

                                // Dean & Director Functions
                                "/dean_manage-students", "/dean/message/new", "/dean-message-form", "/dean/message/send",
                                "/dean-students", "/Deans-dashboard",
                                "/Director-AccomodationDivision-dashboard", "/Director-Allocation_History",
                                "/Director-User_management", "/Director-User_management/**",
                                "/Director-StudentDetails", "/Director-StudentDetails/**","/director/getNotificationCount",
                                "/director/message/**", "/director/inbox", "/Director-Inbox","/director/complaint/**",

                                // Excel & File Handling
                                "/upload-excel", "/ExcelFileList", "/download/{fileName}", "/process/{filename}", "/process",

                                // SRB & Assistant Functions
                                "/SRB_history", "/assistant_dashboard",
                                "/distance_calculation", "/auto_calculate_distance", "/search_student", "/edit_distance",

                                // ====================================================
                                // SUB WARDEN PATHS (ADDED/UPDATED)
                                // ====================================================
                                "/SubWarden_Dash_Board", "/subWarden_dashboard_id",
                                "/subWarden/searchStudent", "/subwarden/assignHostel",
                                "/subwarden_select_hostel","/SubWarden_Dash_Board",
                                "/subWarden_dashboard_id","/subwarden/assignHostel","/subwarden_compaints","subwarden_compaints.html",




                                // Room Management
                                "/Rooms", "/addRoom",
                                "/subWarden/searchRoom", "/subwarden/saveRoom", // Fixes Room Search & Save errors

                                // Allocation
                                "/hostel_allocation", "/room-allocation",
                                "/loadBatch",
                                "/api/room_allocate", // Fixes "Allocate" button error (403)
                                "/api/filter-students", "/api/hostel-capacity/**",
                                "/api/allocate-students", "/api/allocated-students/**","/auto_calculate_distance"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}