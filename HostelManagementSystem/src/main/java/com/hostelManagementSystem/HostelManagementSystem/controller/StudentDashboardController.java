package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.*;
import com.hostelManagementSystem.HostelManagementSystem.service.GoogleDriveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentDashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/student-dashboard")
    public String dashboard(HttpSession session, Model model) {

        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {

            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);

        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "/student-dashboard";
        } else {

            return "error";
        }
    }




    @Autowired
    private ResidenceRepository residenceRepository;

    @GetMapping("/Student_History_Residence")
    public String studentHistory(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            model.addAttribute("student", student);

            List<Residence> residenceList = residenceRepository.findByStudent(student);
            model.addAttribute("residences", residenceList); // ✅ Data pass

            return "Student_History_Residence";
        } else {
            return "error";
        }
    }



    @GetMapping("Student_ComplainsAndRequests")
    public String studentComplaints(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "Student_ComplainsAndRequests"; // <-- Thymeleaf HTML file name
        } else {
            return "error";
        }
    }




    @Autowired
    private ComplaintRequestRepository complaintRequestRepository;

    @PostMapping("/submitForm")
    public String submitComplaint(HttpSession session,
                                  @RequestParam("type") String type,
                                  @RequestParam("receiver") String receiver,
                                  @RequestParam("description") String description,
                                  RedirectAttributes redirectAttributes) {

        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            ComplaintRequest complaint = new ComplaintRequest();
            complaint.setStudent(student);
            complaint.setType(type);
            complaint.setReceiver(receiver);
            complaint.setDescription(description);
            complaint.setSubmittedAt(LocalDateTime.now());

            complaintRequestRepository.save(complaint);

            // Add flash attribute for success message
            redirectAttributes.addFlashAttribute("successMessage", "Your complaint/request has been submitted successfully.");

            return "redirect:/Student_ComplainsAndRequests"; // Redirect to GET method
        } else {
            return "error";
        }
    }



    @Autowired
    private PaymentRepository paymentRepository;


    @GetMapping("/Student_History_Payment")
    public String paymentPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            model.addAttribute("student", student);

            List<Payment> payments = paymentRepository.findByStudent(student);
            model.addAttribute("payments", payments); // <-- IMPORTANT!

            return "Student_History_Payment";
        } else {
            return "error";
        }
    }



    @Autowired
    private DamageRepository damageRepository;

    @GetMapping("/Student_History_Damage")
    public String damagePage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            model.addAttribute("student", student);

            List<Damage> damages = damageRepository.findByStudent(student);
            model.addAttribute("damages", damages); // ✅ send to view

            return "Student_History_Damage";
        } else {
            return "error";
        }
    }

    @GetMapping("/Student_Payments")
    public String studentPaymentForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
        if (studentOptional.isPresent()) {
            model.addAttribute("student", studentOptional.get());
            return "Student_Payments"; // <-- Thymeleaf HTML file name
        } else {
            return "error";
        }
    }

    @PostMapping("/submitPayment")
    public String submitPayment(
            @RequestParam("registrationNumber") String registrationNumber,
            @RequestParam("semester") String semester,
            @RequestParam("paymentType") String paymentType,
            @RequestParam("amount") double amount,
            @RequestParam("slipRefNumber") String slipRefNumber,
            @RequestParam("pdfImageFile") MultipartFile file,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            String email = (String) session.getAttribute("loggedInUserEmail");
            if (email == null) {
                return "redirect:/login";
            }

            Optional<Student> studentOptional = studentRepository.findByEmailIgnoreCase(email);
            if (!studentOptional.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Student not found");
                return "redirect:/Student_Payments";
            }

            Student student = studentOptional.get();

            // Validate file upload
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload");
                return "redirect:/Student_Payments";
            }

            // Check file size (5MB limit)
            if (file.getSize() > 5 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("errorMessage", "File size must be less than 5MB");
                return "redirect:/Student_Payments";
            }

            // Upload file to Google Drive
            String fileUrl = null;
            java.io.File convFile = null;

            try {
                // Create temp file with unique name to avoid conflicts
                String tempFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                convFile = new java.io.File(System.getProperty("java.io.tmpdir"), tempFileName);
                file.transferTo(convFile);

                String mimeType = file.getContentType();
                fileUrl = googleDriveService.uploadFileToDrive(convFile, mimeType);

            } catch (Exception e) {
                // Log the error for debugging
                System.err.println("File upload error: " + e.getMessage());
                e.printStackTrace();

                redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
                return "redirect:/Student_Payments";

            } finally {
                // Always clean up temp file
                if (convFile != null && convFile.exists()) {
                    convFile.delete();
                }
            }

            // Create and save payment
            Payment payment = new Payment();
            payment.setDate(LocalDate.now().toString());
            payment.setAmount(amount);
            payment.setType(paymentType);
            payment.setSemester(semester);
            payment.setSlipRefNumber(slipRefNumber);
            payment.setStudent(student);
            payment.setFileUrl(fileUrl);

            try {
                paymentRepository.save(payment);
                redirectAttributes.addFlashAttribute("successMessage", "Payment submitted successfully!");

            } catch (Exception e) {
                System.err.println("Database save error: " + e.getMessage());
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to save payment: " + e.getMessage());
            }

            return "redirect:/Student_Payments";

        } catch (Exception e) {
            // Catch any unexpected errors
            System.err.println("Unexpected error in submitPayment: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred");
            return "redirect:/Student_Payments";
        }
    }


    @Autowired
    private GoogleDriveService googleDriveService;

    @PostMapping("/student/uploadSlip")
    public String uploadSlip(@RequestParam("file") MultipartFile multipartFile,
                             @RequestParam("studentId") String studentId,
                             Model model) throws Exception {

        java.io.File convFile = new java.io.File("temp_" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(convFile);

        String mimeType = multipartFile.getContentType();
        String fileUrl = googleDriveService.uploadFileToDrive(convFile, mimeType);

        // Save fileUrl to DB with studentId (you can use JPA or JDBC here)

        model.addAttribute("message", "Uploaded Successfully. URL: " + fileUrl);
        return "Student_Payments"; // or your appropriate view
    }













}