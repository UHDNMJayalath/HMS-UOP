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
                                "/bursar-dashboard", "/vc_dvc-dashboard", "/dean-dashboard", "/Deans-dashboard",
                                "/forgot-password", "/verify-code", "/reset-password",
                                "/Student_Payments", "/submitPayment", "/submitForm", "/student/uploadSlip",
                                "/upload-excel", "/SRB_history","/Director-AccomodationDivision-dashboard"
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
