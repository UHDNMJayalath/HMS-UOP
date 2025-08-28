package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.*;
import com.hostelManagementSystem.HostelManagementSystem.service.ExcelSheetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentDashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResidenceRepository residenceRepository;

    @Autowired
    private ComplaintRequestRepository complaintRequestRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DamageRepository damageRepository;

    private ExcelSheetService excelService;


    // ---------------- Dashboard ----------------
    @GetMapping("/student-dashboard")
    public String dashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "student-dashboard";
        }
        return "error";
    }

    // ---------------- Residence History ----------------
    @GetMapping("/Student_History_Residence")
    public String studentHistory(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("student", student);

            // Fetch residence history using registration number
            List<Residence> residences = residenceRepository.findByStudent(student);
            model.addAttribute("residences", residences);

            return "Student_History_Residence";
        }
        return "error";
    }


    // ---------------- Complaints/Requests ----------------
    @GetMapping("/Student_ComplainsAndRequests")
    public String studentComplaints(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "Student_ComplainsAndRequests";
        }
        return "error";
    }

    @PostMapping("/submitForm")
    public String submitComplaint(HttpSession session,
                                  @RequestParam String type,
                                  @RequestParam String receiver,
                                  @RequestParam String description,
                                  RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            ComplaintRequest complaint = new ComplaintRequest();
            complaint.setStudent(student);
            complaint.setType(type);
            complaint.setReceiver(receiver);
            complaint.setDescription(description);
            complaint.setSubmittedAt(LocalDateTime.now());

            complaintRequestRepository.save(complaint);
            redirectAttributes.addFlashAttribute("successMessage", "Your complaint/request has been submitted successfully.");
            return "redirect:/Student_ComplainsAndRequests";
        }
        return "error";
    }

    // ---------------- Payment History ----------------
    @GetMapping("/Student_History_Payment")
    public String paymentHistory(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("student", student);
            List<Payment> payments = paymentRepository.findByStudent(student);
            model.addAttribute("payments", payments);
            return "Student_History_Payment";
        }
        return "error";
    }

    // ---------------- Damage History ----------------
    @GetMapping("/Student_History_Damage")
    public String damageHistory(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            model.addAttribute("student", student);
            List<Damage> damages = damageRepository.findByStudent(student);
            model.addAttribute("damages", damages);
            return "Student_History_Damage";
        }
        return "error";
    }

    // ---------------- Student Payment Form ----------------
    @GetMapping("/Student_Payments")
    public String studentPaymentForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<Student> studentOpt = studentRepository.findByEmailIgnoreCase(email);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "Student_Payments";
        }
        return "error";
    }

    // ---------------- Excel Upload ----------------
    @PostMapping("/process/{filename}")
    public String uploadExcel(@PathVariable("filename") String fileName, RedirectAttributes redirectAttributes){
        this.excelService = new ExcelSheetService(studentRepository);
        excelService.saveExcelData(fileName);
        redirectAttributes.addFlashAttribute("message","Excel data inserted into database successfully!");
        return "redirect:/ExcelFileList";
    }

}
