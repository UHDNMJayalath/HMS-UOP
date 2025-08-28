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
                                "/", "/login", "/signup", "/css/**", "/js/**",
                                "/images/**", "/student-dashboard", "/Student_ComplainsAndRequests",
                                "/Student_History_Damage", "/Student_History_Payment", "/Student_History_Residence",
                                "/bursar-dashboard", "/vc_dvc-dashboard", "/dean-dashboard",
                                "/forgot-password", "/verify-code", "/reset-password","dean_manage-students",
                                "/Student_Payments", "/submitPayment", "/submitForm", "/student/uploadSlip",
                                "/upload-excel", "/SRB_history","/Director-AccomodationDivision-dashboard","/Director-Allocation_History",
                                "/Director-User_management","/ExcelFileList", "/download/{fileName}",
                                "/process/{filename}", "/assistant_dashboard", "/process", "/distance_calculation","/dean-students"
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
