package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.dto.ComplaintDTO; // DTO Import
import com.hostelManagementSystem.HostelManagementSystem.dto.RoomDetails;
import com.hostelManagementSystem.HostelManagementSystem.entity.*;
import com.hostelManagementSystem.HostelManagementSystem.repository.*;
import com.hostelManagementSystem.HostelManagementSystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SubWardenController {

    @Autowired
    private HostelService hostelService;
    @Autowired
    private SubWardenRepository subWardenRepo;
    @Autowired
    private HostelRepository hostelRepo;
    @Autowired
    private SubWardenService subWardenService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DamageRepository damageRepo;

    @Autowired
    private ComplaintRequestRepository complaintRepo; // පැමිණිලි සඳහා Repository එක

    // ============================================================
    // 1. MAIN DASHBOARD LOGIC (WITH HOSTEL SELECTION)
    // ============================================================
    @RequestMapping("/SubWarden_Dash_Board")
    public String getDashBoard(HttpSession session, Model model){

        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<SubWarden> subWardenOpt = subWardenRepo.findByEmailIgnoreCase(email);

        if (subWardenOpt.isPresent()){
            SubWarden subWarden = subWardenOpt.get();

            // 🔴 CHECK 1: If Hostel ID is NULL, redirect to Selection Page
            if (subWarden.getHostelId() == null) {
                List<Hostel> allHostels = hostelService.getAllHostels();
                model.addAttribute("hostels", allHostels);
                return "subwarden_select_hostel";
            }

            // 🔴 CHECK 2: If ID exists but Hostel not found (e.g. deleted)
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.getHostelId());
            if (hostel.isEmpty()){
                List<Hostel> allHostels = hostelService.getAllHostels();
                model.addAttribute("hostels", allHostels);
                return "subwarden_select_hostel";
            }

            // 🟢 SUCCESS: Go to Dashboard
            model.addAttribute("subwarden", subWarden);
            model.addAttribute("hostel", hostel.get());

            return "subWarden_dashboard_id";
        }

        return "redirect:/login";
    }

    // ============================================================
    // 2. ASSIGN HOSTEL ACTION (Saves the selection)
    // ============================================================
    @PostMapping("/subwarden/assignHostel")
    public String assignHostel(@RequestParam Integer hostelId, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<SubWarden> subWardenOpt = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWardenOpt.isPresent()) {
            SubWarden subWarden = subWardenOpt.get();
            subWarden.setHostelId(hostelId);
            subWardenRepo.save(subWarden);

            Optional<Hostel> hostelOpt = hostelRepo.findById(hostelId);
            if(hostelOpt.isPresent()){
                Hostel hostel = hostelOpt.get();
                hostel.setSubWardenEmail(email);
                hostelRepo.save(hostel);
            }
        }
        return "redirect:/SubWarden_Dash_Board";
    }

    // ============================================================
    // 3. ROOMS PAGE
    // ============================================================
    @RequestMapping("/Rooms")
    public String searchRoomPage(HttpSession session, Model model){
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent() && subWarden.get().getHostelId() != null){
            model.addAttribute("subwarden", subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());
                return "subwarden-rooms";
            }
        }
        return "redirect:/SubWarden_Dash_Board";
    }

    // ============================================================
    // 4. ALLOCATION PAGE
    // ============================================================
    @GetMapping("/room-allocation")
    public String getRoomAllocationPage(HttpSession session, Model model){
        String email = (String) session.getAttribute("loggedInUserEmail");
        if (email == null) return "redirect:/login";

        Optional<SubWarden> subWarden = subWardenRepo.findByEmailIgnoreCase(email);
        if (subWarden.isPresent() && subWarden.get().getHostelId() != null){
            model.addAttribute("subwarden", subWarden.get());
            Optional<Hostel> hostel = hostelRepo.findById(subWarden.get().getHostelId());

            if (hostel.isPresent()){
                model.addAttribute("hostel", hostel.get());

                List<Room> totalFreeRooms = hostelService.getFullyAvailableRooms(hostel.get().getId());
                List<Room> partiallyFreeRooms = hostelService.getPartiallyAvailableRooms(hostel.get().getId());
                List<Allocation> allocations = allocationService.findByHostelId(hostel.get().getId());

                List<Student> students = new ArrayList<>();
                for (Allocation allocation: allocations){
                    if (allocation.getRoomId() == null){
                        Student student = studentService.findById(allocation.getStudentId());
                        if(student != null) {
                            student.setAcademicYear(allocation.getAcademicYear());
                            students.add(student);
                        }
                    }
                }

                model.addAttribute("totalFreeRooms", totalFreeRooms);
                model.addAttribute("partiallyFreeRooms", partiallyFreeRooms);
                model.addAttribute("students", students);

                return "subwarden-room-allocation";
            }
        }
        return "redirect:/SubWarden_Dash_Board";
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
                List<ComplaintRequest> complaints = complaintRepo.findBySubWardenHostel(hostelName);

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

    // ============================================================
    // 6. UPDATE COMPLAINT STATUS (API for JavaScript)
    // ============================================================
    @PostMapping("/complaints/update/{id}")
    @ResponseBody
    public ResponseEntity<String> updateComplaintStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Optional<ComplaintRequest> complaintOpt = complaintRepo.findById(id);

        if (complaintOpt.isPresent()) {
            ComplaintRequest complaint = complaintOpt.get();
            String newState = payload.get("state");

            complaint.setStatus(newState);
            complaintRepo.save(complaint);

            return ResponseEntity.ok("Updated");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Complaint not found");
    }

    // ============================================================
    // 7. ADD ROOM
    // ============================================================
    @PostMapping("/addRoom")
    public String addRoom(@RequestParam String roomNo, @RequestParam Integer floor,
                          @RequestParam Integer capacity, @RequestParam Integer hostelId,
                          HttpSession session){
        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";
        hostelService.addRoom(hostelId, roomNo, floor, capacity);
        return "redirect:/SubWarden_Dash_Board";
    }

    // ============================================================
    // 8. SEARCH STUDENT
    // ============================================================
    @PostMapping("/subWarden/searchStudent")
    public String searchStudent(HttpSession session, @RequestParam String studentId,
                                @RequestParam Integer hostelId, RedirectAttributes ra) {

        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";

        List<Allocation> allocations = allocationService.findByStudentIdAndHostelId(studentId, hostelId);
        boolean found = false;

        if(allocations != null){
            for (Allocation allocation: allocations) {
                if (allocation != null && allocation.getDeallocatedDate() == null) {
                    Student student = studentService.findById(studentId);
                    if (student != null) {
                        ra.addFlashAttribute("student", student);
                        List<Damage> damages = damageRepo.findByStudent(student);
                        ra.addFlashAttribute("damages", damages);
                        found = true;
                        break;
                    }
                }
            }
        }

        if (!found) {
            ra.addFlashAttribute("error", "Student not found or not allocated to this hostel.");
        }
        return "redirect:/SubWarden_Dash_Board";
    }

    // ============================================================
    // 9. SEARCH ROOM
    // ============================================================
    @RequestMapping("/subWarden/searchRoom")
    public String searchRoom(HttpSession session, @RequestParam String roomNo,
                             @RequestParam Integer hostelId, RedirectAttributes ra) {

        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";

        Room room = roomService.findById(new RoomID(hostelId, roomNo));
        if(room != null) {
            List<Allocation> allocations = allocationService.findByRoomIdAndHostelId(roomNo,hostelId);
            List<Student> students = new ArrayList<>();
            if (allocations != null) {
                for (Allocation allocation : allocations) {
                    if (allocation.getDeallocatedDate() == null) {
                        Student s = studentService.findById(allocation.getStudentId());
                        if (s != null) students.add(s);
                    }
                }
            }

            RoomDetails roomDetails = new RoomDetails(
                    room.getRoomNo(), hostelId, room.getCapacity(),
                    room.getCurrentAllocations(), room.getStatus(), students
            );
            ra.addFlashAttribute("room", roomDetails);
        } else {
            ra.addFlashAttribute("error", "Room not found.");
        }
        return "redirect:/Rooms";
    }

    // ============================================================
    // 10. SAVE ROOM DETAILS
    // ============================================================
    @PostMapping("/subwarden/saveRoom")
    public String saveRoom(HttpSession session, @RequestParam String status,
                           @RequestParam Integer capacity, @RequestParam Integer hostelId,
                           @RequestParam String roomNo){

        if (session.getAttribute("loggedInUserEmail") == null) return "redirect:/login";

        Room room = roomService.findById(new RoomID(hostelId, roomNo));
        if(room != null) {
            room.setCapacity(capacity);
            room.setStatus(status);
            roomService.saveRoom(room);
        }
        return "redirect:/subWarden/searchRoom?roomNo=" + roomNo + "&hostelId=" + hostelId;
    }
}