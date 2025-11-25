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
                                // --- Static Resources & Common ---
                                "/", "/login", "/signup", "/css/**", "/js/**",
                                "/assistant.js", "/subWarden.js", "/images/**",

                                // --- Student Dashboard & History ---
                                "/student-dashboard",
                                "/Student_ComplainsAndRequests",
                                "/Student_History_Damage",
                                "/Student_History_Payment",
                                "/Student_History_Residence",
                                "/Student_Payments",
                                "/submitPayment",
                                "/submitForm",
                                "/student/uploadSlip",

                                // --- Common Dashboards & Auth ---
                                "/bursar-dashboard",
                                "/vc_dvc-dashboard",
                                "/dean-dashboard",
                                "/forgot-password",
                                "/verify-code",
                                "/reset-password",

                                // --- Dean Functions (NEW) ---
                                "/dean_manage-students",
                                "/dean/message/new",
                                "/dean-message-form",
                                "/dean/message/send",
                                "/dean-students",
                                "/Deans-dashboard",

                                // --- Director Functions (NEW) ---
                                "/Director-AccomodationDivision-dashboard",
                                "/Director-Allocation_History",
                                "/Director-User_management",
                                "/Director-User_management/**",
                                "/Director-StudentDetails",
                                "/Director-StudentDetails/**",
                                "/director/getNotificationCount",
                                "/director/message/**",
                                "/director/inbox",
                                "/Director-Inbox",
                                "/director/complaint/**",

                                // --- Excel & File Handling ---
                                "/upload-excel",
                                "/ExcelFileList",
                                "/download/{fileName}",
                                "/process/{filename}",
                                "/process",

                                // --- SRB & Assistant Functions ---
                                "/SRB_history",
                                "/assistant_dashboard",
                                "/distance_calculation",
                                "/auto_calculate_distance",
                                "/search_student",
                                "/edit_distance",

                                // --- SUB WARDEN PATHS (UPDATED/NEW) ---
                                "/SubWarden_Dash_Board",
                                "/subWarden_dashboard_id",
                                "/subWarden/searchStudent",
                                "/subwarden/assignHostel",
                                "/subwarden_select_hostel",
                                "/complaints/update/**",
                                "/subwarden_compaints",
                                "/subwarden_compaints.html",
                                "/subWardenPayments",
                                "/subWarden/payment/verify",
                                "/subWarden/addDamage",
                                "/subWarden/deallocate",

                                // --- Room Management ---
                                "/Rooms",
                                "/addRoom",
                                "/subWarden/searchRoom",
                                "/subwarden/saveRoom",

                                // --- Allocation & API ---
                                "/hostel_allocation",
                                "/room-allocation",
                                "/loadBatch",
                                "/api/room_allocate",
                                "/api/filter-students",
                                "/api/hostel-capacity/**",
                                "/api/allocate-students",
                                "/api/allocated-students/**"

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