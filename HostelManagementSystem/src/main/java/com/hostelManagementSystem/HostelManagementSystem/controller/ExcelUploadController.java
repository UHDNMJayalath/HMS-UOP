package com.hostelManagementSystem.HostelManagementSystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class ExcelUploadController {

    private static final String UPLOAD_DIR = "uploads/excelSheets";

    @PostMapping("/upload-excel")
    public String handleExcelUpload(@RequestParam("excelFile") MultipartFile excelSheet,
                                    @RequestParam("faculty") String faculty,
                                    @RequestParam("intake") String intake,
                                    HttpServletRequest request,
                                    Model model) {

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", csrf);

        try {
            // Create upload directory if not exists
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Sanitize inputs
            String safeFaculty = faculty.replaceAll("[^a-zA-Z0-9]", "_");
            String safeIntake = intake.replaceAll("[^a-zA-Z0-9]", "_");

            // Get original extension
            String originalFileName = StringUtils.cleanPath(
                    Objects.requireNonNull(excelSheet.getOriginalFilename()));
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            // Create new file name
            String newFileName = safeFaculty + "_" + safeIntake + extension;

            // Save the file
            Path destination = Paths.get(UPLOAD_DIR).resolve(newFileName);
            Files.copy(excelSheet.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            // Success message
            model.addAttribute("message", "File uploaded successfully: " + newFileName);

        } catch (Exception e) {
            e.printStackTrace();
            // Error message
            model.addAttribute("error", "Failed to upload file: " + e.getMessage());
        }

        // Always return to the same dashboard page
        return "srb-dashboard";
    }
}
