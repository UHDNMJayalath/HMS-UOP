package com.hostelManagementSystem.HostelManagementSystem.controller;

import com.hostelManagementSystem.HostelManagementSystem.dto.DirectorInboxDTO;
import com.hostelManagementSystem.HostelManagementSystem.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/director")
public class Director_MassageController {

    private final MessagingService messagingService;

    @Autowired
    public Director_MassageController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    // --- INBOX PAGE ---
    @GetMapping("/inbox")
    public String showDirectorInbox(Model model) {
        // 1. Unified Inbox (Dean Messages + Student Complaints) ලබා ගැනීම
        List<DirectorInboxDTO> unifiedMessages = messagingService.getUnifiedInbox();

        // 2. Unread Count එක ලබා ගැනීම (Service එකේ අලුත් Logic එක අනුව)
        long unreadCount = messagingService.getUnreadMessageCountForDirector();

        model.addAttribute("messages", unifiedMessages);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("tab", "inbox");

        return "Director-Inbox";
    }

    // ----------------------------------------------------------------
    // DEAN MESSAGES ACTIONS
    // ----------------------------------------------------------------

    @PostMapping("/message/read/{id}")
    @ResponseBody
    public ResponseEntity<String> markMessageAsRead(@PathVariable Long id) {
        messagingService.markMessageAsRead(id);
        return ResponseEntity.ok("Read");
    }

    @PostMapping("/message/delete/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messagingService.deleteMessage(id);
        return "redirect:/director/inbox";
    }

    // ----------------------------------------------------------------
    // STUDENT COMPLAINTS ACTIONS (NEW)
    // ----------------------------------------------------------------

    @PostMapping("/complaint/read/{id}")
    @ResponseBody
    public ResponseEntity<String> markComplaintAsRead(@PathVariable Long id) {
        // Service එකේ අලුත් Logic එකට අනුව Status එක 'Viewed' ලෙස වෙනස් කරයි
        messagingService.markComplaintAsRead(id);
        return ResponseEntity.ok("Read");
    }

    @PostMapping("/complaint/delete/{id}")
    public String deleteComplaint(@PathVariable Long id) {
        // Student Complaints මැකීම සඳහා
        messagingService.deleteComplaint(id);
        return "redirect:/director/inbox";
    }

    // ----------------------------------------------------------------
    // AJAX NOTIFICATION COUNT (SIDEBAR/NAVBAR)
    // ----------------------------------------------------------------

    @GetMapping("/getNotificationCount")
    @ResponseBody
    public long getNotificationCount() {
        return messagingService.getUnreadMessageCountForDirector();
    }
}