package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.google.api.client.util.DateTime;
import com.hostelManagementSystem.HostelManagementSystem.dto.ComplaintDTO;
import com.hostelManagementSystem.HostelManagementSystem.dto.RoomDetails;
import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.*; // Updated to include all repos
import com.hostelManagementSystem.HostelManagementSystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Added
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // Added
import java.util.Optional;

@Controller
public class SubWardenController {

    @Autowired
    private HostelService hostelService;

    @Autowired
    private SubWardenService subWardenService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SubWardenRepository subWardenRepo;

    @Autowired
    private HostelRepository hostelRepo;

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DamageRepository damageRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private PaymentService paymentService;

    // Added: Complaint Repository Autowiring
    @Autowired
    private ComplaintRequestRepository complaintRequestRepo;

    @RequestMapping("/SubWarden_Dash_Board")
    public String getDashBoard(HttpSession session, Model model){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                System.out.println("Total rooms : " + hostel.get().getTotalRooms());
                return "subWarden_dashboard_id";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @RequestMapping("/Rooms")
    public String searchRoom(HttpSession session, Model model){
        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                return "subwarden-rooms";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @GetMapping("/room-allocation")
    public String getRoomAllocationPage(HttpSession session, Model model){
        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                System.out.println("Total rooms : " + hostel.get().getTotalRooms());
                List<Room> totalFreeRooms = hostelService.getFullyAvailableRooms(hostel.get().getId());
                List<Room> partiallyFreeRooms = hostelService.getPartiallyAvailableRooms(hostel.get().getId());
                List<Allocation> allocations = allocationService.findByHostelId(hostel.get().getId());

                List<Student> students = new ArrayList<>();

                for (Allocation allocation: allocations){
                    if (allocation.getRoomId() == null){
                        Student student = this.studentService.findById(allocation.getStudentId());
                        student.setAcademicYear(allocation.getAcademicYear());
                        students.add(student);
                    }
                }

                model.addAttribute("totalFreeRooms",totalFreeRooms);
                model.addAttribute("partiallyFreeRooms",partiallyFreeRooms);
                model.addAttribute("students", students);

                return "subwarden-room-allocation";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    // ============================================================
    // 5. COMPLAINTS PAGE (Fully Implemented)
    // ============================================================
    @GetMapping("/subwarden_compaints")
    public String getComplaintsPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden", subWarden.get());

            // Hostel Name එක ගන්නවා
            Optional<Hostel> hostelOpt = hostelRepo.findById(subWarden.get().getHostelId());

            if(hostelOpt.isPresent()){
                String hostelName = hostelOpt.get().getName();

                // අදාළ Hostel එකේ Complaints ටික ගන්නවා (Repository method එකෙන්)
                List<ComplaintRequest> complaints = complaintRequestRepo.findBySubWardenHostel(hostelName);

                // DTO ලිස්ට් එකට හරවනවා (HTML එකට යවන්න)
                List<ComplaintDTO> complaintDTOS = new ArrayList<>();
                for (ComplaintRequest c : complaints) {
                    complaintDTOS.add(new ComplaintDTO(
                            c.getId(),
                            c.getSubmittedAt(),
                            c.getStudent().getStudentId(),
                            c.getDescription(),
                            c.getStatus()
                    ));
                }

                model.addAttribute("complaints", complaintDTOS);
            }
        }
        return "subwarden_compaints";
    }

    // --- NEW METHOD: Update Complaint Status ---
    @PostMapping("/complaints/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Optional<ComplaintRequest> complaintOpt = complaintRequestRepo.findById(id);

        if (complaintOpt.isPresent()) {
            ComplaintRequest complaint = complaintOpt.get();
            String newStatus = payload.get("state"); // The JS sends 'state', we map it to 'status'

            complaint.setStatus(newStatus);
            complaintRequestRepo.save(complaint);

            return ResponseEntity.ok().body(Map.of("message", "Status updated successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Complaint not found"));
        }
    }

    @RequestMapping("/subWardenPayments")
    public String getPaymentPage(HttpSession session,
                                 @RequestParam(required = false) String studentId,
                                 @RequestParam(required = false) String paymentType,
                                 Model model){
        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostelOpt = hostelRepo.findById(subWarden.get().getHostelId());

            List<Payment> payments = new ArrayList<>();
            List<Payment> properPayments = new ArrayList<>();

            if (hostelOpt.isPresent()){
                Hostel hostel = hostelOpt.get();
                model.addAttribute("hostel", hostel);
                payments = paymentService.getPaymentsByCurrentStudents(hostel.getId());

                // Apply filters based on parameters
                if (studentId != null && !studentId.isEmpty()) {
                    for (Payment payment: payments){
                        if (studentId.equalsIgnoreCase(payment.getStudent().getStudentId())){
                            properPayments.add(payment);
                        }
                    }
                }
                else if (paymentType != null && !paymentType.isEmpty()) {
                    for (Payment payment: payments){
                        if (paymentType.equalsIgnoreCase(payment.getPaymentType())){
                            properPayments.add(payment);
                        }
                    }
                }
                else {
                    properPayments = payments;
                }

                model.addAttribute("payments", properPayments);
                model.addAttribute("studentId", studentId);
                model.addAttribute("paymentType", paymentType);

                return "subwarden_payment_verification";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @PostMapping("/addRoom")
    public String addRoom(
            @RequestParam(value = "roomNo", required = true) String roomNo,
            @RequestParam(value = "floor", required = true) Integer floor,
            @RequestParam(value = "capacity", required = true) Integer capacity,
            @RequestParam(value = "hostelId", required = true) Integer hostelId,
            @RequestParam(value = "subWardenId", required = true) Integer subWardenId,
            HttpSession session,
            Model model){

        System.out.println("Request Received");

        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<SubWarden> subWardenOptional = subWardenService.getSubWardenByEmail(email);

        if (subWardenOptional.isPresent()){
            System.out.println("Sub warden found : " + subWardenOptional.get().getName());
            Optional<Hostel> hostelOptional = hostelService.getHostelById(hostelId);

            System.out.println("Hostel found : " + hostelOptional.get().getName());

            Room room = new Room(hostelId, roomNo, floor, capacity);
            hostelService.addRoom(room);
            model.addAttribute("subwarden", subWardenOptional.get());
            model.addAttribute("hostel", hostelOptional.get());
            System.out.println("Room saved");

            return "redirect:/SubWarden_Dash_Board";
        }

        return null;
    }

    @GetMapping("/SubWardon-dashboard-allocation")
    public String getAllocationPage(HttpSession session, Model model){
        return "hostel_allocation";
    }

    @GetMapping("/subWarden/searchStudent")
    public String getStudent(HttpSession session, @RequestParam String studentId,
                             @RequestParam Integer hostelId, Model model,RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());
            System.out.println("Hostel found :" + hostel.get().getName());
            System.out.println("Student : " + studentId);

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());

                List<Allocation> allocations = allocationService.findByStudentIdAndHostelId(studentId,hostelId);

                if(allocations != null){
                    System.out.println("Allocation list is not null");
                    System.out.println("length : " + allocations.size());

                }
                else {
                    System.out.println("Allocation list is null");
                }
                if (allocations.size() == 0){
                    redirectAttributes.addFlashAttribute("error", "Student not found");
                }

                for (Allocation allocation: allocations) {
                    System.out.println("enter to for loop");
                    if (allocation != null && allocation.getDeallocatedDate() == null) {
                        System.out.println("entering if statement");
                        Student student = studentService.findById(studentId);
                        System.out.println("Allocation found : " + allocation.getStudentId());
                        System.out.println("Student found : " + student.getStudentId());
                        model.addAttribute("student", student);
                        redirectAttributes.addFlashAttribute("student", student);
                        redirectAttributes.addFlashAttribute("allocation", allocation);
                        List<Damage> damages = damageRepo.findByStudent(student);
                        redirectAttributes.addFlashAttribute("damages", damages);
                        //break;
                    } else {
                        model.addAttribute("error", "Student details not found.");
                        System.out.println("Student not found");
                    }

                }
                return "redirect:/SubWarden_Dash_Board";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @PostMapping("/subWarden/searchRoom")
    public String rooms(HttpSession session, @RequestParam String roomNo,
                        @RequestParam Integer hostelId, Model model, RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());
            System.out.println("Hostel found : " + hostel.get().getName());
            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                Room room = roomService.findById(new RoomID(hostelId, roomNo));

                List<Allocation> allocations = allocationService.findByRoomIdAndHostelId(roomNo,hostelId);
                List<Student> students = new ArrayList<>();
                if (allocations != null) {
                    for (Allocation allocation : allocations) {
                        if (allocation.getDeallocatedDate() == null) {
                            Student student = studentService.findById(allocation.getStudentId());
                            students.add(student);
                        }
                    }
                }

                if (room != null){
                    System.out.println("Room Found : " + room.getRoomNo());
                    model.addAttribute("room", room);
                    RoomDetails roomDetails = new RoomDetails(room.getRoomNo(), room.getHostelId(), room.getCapacity(), room.getCurrentAllocations(), room.getStatus(), students);
                    redirectAttributes.addFlashAttribute("room", roomDetails);
                }
                else {
                    System.out.println("Room is null");
                    redirectAttributes.addFlashAttribute("error", "room not found.");
                }

                return "redirect:/Rooms";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @GetMapping("/subWarden/searchRoom")
    public String roomsGet(HttpSession session, @RequestParam String roomNo,
                           @RequestParam Integer hostelId, Model model, RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());
            System.out.println("Hostel found : " + hostel.get().getName());
            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                Room room = roomService.findById(new RoomID(hostelId, roomNo));

                List<Allocation> allocations = allocationService.findByRoomIdAndHostelId(roomNo,hostelId);
                List<Student> students = new ArrayList<>();
                if (allocations != null) {
                    for (Allocation allocation : allocations) {
                        if (allocation.getDeallocatedDate() == null) {
                            Student student = studentService.findById(allocation.getStudentId());
                            students.add(student);
                        }
                    }
                }

                if (room != null){
                    System.out.println("Room Found : " + room.getRoomNo());
                }
                else {
                    System.out.println("Room is null");
                }
                model.addAttribute("room", room);
                RoomDetails roomDetails = new RoomDetails(room.getRoomNo(), room.getHostelId(), room.getCapacity(), room.getCurrentAllocations(), room.getStatus(), students);
                redirectAttributes.addFlashAttribute("room", roomDetails);
                return "redirect:/Rooms";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @PostMapping("/subwarden/saveRoom")
    public String saveRoom(HttpSession session,
                           @RequestParam("status") String status,
                           @RequestParam("capacity") Integer capacity,
                           @RequestParam Integer hostelId,
                           @RequestParam String roomNo,
                           Model model){

        Room room = roomService.findById(new RoomID(hostelId, roomNo));
        if (room != null){
            room.setCapacity(capacity);
            room.setStatus(status);
            roomService.saveRoom(room);
        }
        return "redirect:/subWarden/searchRoom?roomNo=" + roomNo + "&hostelId=" + hostelId;
    }

    @PostMapping("subWarden/addDamage")
    public String addDamage( HttpSession session, Model model,
                             @RequestParam String studentId,
                             @RequestParam("hostel") Integer hostelId,
                             @RequestParam Double fine,
                             @RequestParam String description,
                             RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                Student student = studentService.findById(studentId);
                Damage damage = new Damage(hostel.get().getName(), fine, description, student);
                damage.setPaymentStatus("unpaid");
                damageRepo.save(damage);
                redirectAttributes.addFlashAttribute("successMessage", "Damage report submitted successfully!");

                return "redirect:/subWarden/searchStudent?studentId=" + studentId + "&hostelId=" + hostelId;
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @PostMapping("/subWarden/deallocate")
    public String deallocateStudent(HttpSession session, Model model,
                                    @RequestParam String studentId,
                                    @RequestParam Integer hostelId,
                                    @RequestParam String roomId,
                                    RedirectAttributes redirectAttributes){

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostelOpt = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostelOpt.isPresent()){

                Hostel hostel = hostelOpt.get();
                model.addAttribute("hostel", hostel);

                Student student = studentService.findById(studentId);
                student.setCurrentHostel(null);
                studentService.saveStudent(student);

                int residentialStudents = hostel.getResidentialStudents();
                residentialStudents --;
                hostel.setResidentialStudents(residentialStudents);
                hostelService.saveHostel(hostel);

                // Room modification
                Room room = roomService.findById(new RoomID(hostelId, roomId));
                int currentAllocations = room.getCurrentAllocations();
                currentAllocations--;
                room.setCurrentAllocations(currentAllocations);
                roomService.saveRoom(room);

                Allocation allocation = allocationService.findById(new AllocationID(studentId, hostelId, student.getAcademicYear()));
                allocation.setDeallocatedDate(LocalDate.now());
                allocationService.saveAllocation(allocation);
                return "redirect:/subWarden/searchStudent?studentId=" + studentId + "&hostelId=" + hostelId;
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    @PostMapping("/subWarden/payment/verify")
    public String verifyPayment( HttpSession session, Model model,
                                 @RequestParam String regNum,
                                 @RequestParam Long paymentId,
                                 RedirectAttributes redirectAttributes) {

        String email = (String) session.getAttribute("loggedInUserEmail");

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent()){
            model.addAttribute("subwarden",subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());

                try {
                    System.out.println("executed");
                    boolean success = paymentService.verifyPayment(paymentId,regNum,hostel.get().getId());

                    if (success) {
                        redirectAttributes.addFlashAttribute("successMessage",
                                "Payment verified successfully!");
                    } else {
                        redirectAttributes.addFlashAttribute("errorMessage",
                                "Payment verification failed. Payment may already be verified.");
                    }
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Error verifying payment: " + e.getMessage());
                }

                return "redirect:/subWardenPayments";
            }
            else {
                model.addAttribute("error", "SW details not found.");
                return "login";
            }
        }
        else {
            model.addAttribute("error", "SW details not found.");
            return "login";
        }
    }

    /**
     * API endpoint to get payment details (for AJAX requests)
     */
    @GetMapping("/api/{paymentId}")
    @ResponseBody
    public Payment getPaymentDetails(@PathVariable Long paymentId) {
        return paymentService.findById(paymentId);
    }

    /**
     * Download payment slip
     */
    @GetMapping("/download/{paymentId}")
    public String downloadSlip(@PathVariable Long paymentId, RedirectAttributes redirectAttributes) {
        Payment payment = paymentService.findById(paymentId);

        if (payment != null && payment.getSlipUrl() != null) {
            return "redirect:" + payment.getSlipUrl();
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Payment slip not found");
        return "redirect:/subwarden/payments";
    }
}