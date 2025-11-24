package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.dto.DirectorInboxDTO;
import com.hostelManagementSystem.HostelManagementSystem.entity.ComplaintRequest;
import com.hostelManagementSystem.HostelManagementSystem.entity.Message;
import com.hostelManagementSystem.HostelManagementSystem.repository.ComplaintRequestRepository;
import com.hostelManagementSystem.HostelManagementSystem.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessagingService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ComplaintRequestRepository complaintRequestRepository;

    private static final String DIRECTOR_ROLE = "DIRECTOR_ACCOMMODATION";
    private static final String COMPLAINT_RECEIVER_NAME = "Accommodation Director";

    // --- COMMON ---
    public void saveMessage(Long senderId, String senderFaculty, String subject, String body) {
        if (senderFaculty == null || senderFaculty.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender faculty cannot be empty.");
        }
        messageRepository.save(new Message(senderId, senderFaculty, subject, body));
    }

    // --- INBOX LOGIC (Director Inbox සඳහා DTO සෑදීම) ---
    public List<DirectorInboxDTO> getUnifiedInbox() {
        List<DirectorInboxDTO> inboxList = new ArrayList<>();

        // 1. DEAN MESSAGES
        List<Message> messages = messageRepository.findByRecipientRoleOrderBySentAtDesc(DIRECTOR_ROLE);
        for (Message msg : messages) {
            inboxList.add(new DirectorInboxDTO(
                    msg.getId(),
                    msg.getSubject(),
                    msg.getBody(),
                    "Faculty: " + msg.getSenderFaculty(),
                    "DEAN",
                    msg.getSentAt(),
                    msg.isRead()
            ));
        }

        // 2. STUDENT COMPLAINTS
        List<ComplaintRequest> complaints = complaintRequestRepository.findByReceiverOrderBySubmittedAtDesc(COMPLAINT_RECEIVER_NAME);
        for (ComplaintRequest comp : complaints) {
            String studentInfo = (comp.getStudent() != null) ? "Student: " + comp.getStudent().getStudentId() : "Student";

            // Logic: Status එක "Viewed" හෝ "Resolved" නම් පමණක් Read (true) ලෙස සලකයි.
            // නැත්නම් (NULL, "Not Taken", "Pending" ආදිය) Unread (false) ලෙස සලකයි.
            boolean isComplaintRead = comp.getStatus() != null &&
                    (comp.getStatus().equalsIgnoreCase("Viewed") ||
                            comp.getStatus().equalsIgnoreCase("Resolved"));

            inboxList.add(new DirectorInboxDTO(
                    comp.getId(),
                    comp.getType(),
                    comp.getDescription(),
                    studentInfo,
                    "STUDENT",
                    comp.getSubmittedAt(),
                    isComplaintRead
            ));
        }

        // දිනය අනුව Sort කිරීම (අලුත් ඒවා උඩට)
        inboxList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return inboxList;
    }

    // --- UTILITIES & COUNTS ---
    public long getUnreadMessageCountForDirector() {
        // Dean Messages Unread Count
        long deanUnread = messageRepository.countByRecipientRoleAndIsRead(DIRECTOR_ROLE, false);

        // Student Complaints Unread Count
        // Status එක NULL නම් හෝ ("Viewed" සහ "Resolved" නොවන) ඕනෑම අවස්ථාවක් Unread ලෙස ගණන් ගනී.
        List<ComplaintRequest> complaints = complaintRequestRepository.findByReceiverOrderBySubmittedAtDesc(COMPLAINT_RECEIVER_NAME);
        long studentUnread = complaints.stream()
                .filter(c -> c.getStatus() == null ||
                        (!c.getStatus().equalsIgnoreCase("Viewed") && !c.getStatus().equalsIgnoreCase("Resolved")))
                .count();

        return deanUnread + studentUnread;
    }

    // --- MARK AS READ ---

    public void markMessageAsRead(Long id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            message.setRead(true);
            messageRepository.save(message);
        }
    }

    // *** UPDATED METHOD: මෙය තමයි Highlight ප්‍රශ්නය විසඳන්නේ ***
    @Transactional
    public void markComplaintAsRead(Long id) {
        ComplaintRequest comp = complaintRequestRepository.findById(id).orElse(null);
        if (comp != null) {
            String currentStatus = comp.getStatus();

            // Status එක දැනටමත් "Viewed" හෝ "Resolved" නෙවෙයි නම්, එය "Viewed" කරන්න.
            // මෙහිදී "Not Taken", NULL, "Pending" හෝ හිස් text එකක් තිබුණත් එය "Viewed" බවට පත්වේ.
            if (currentStatus == null ||
                    (!currentStatus.equalsIgnoreCase("Viewed") && !currentStatus.equalsIgnoreCase("Resolved"))) {

                comp.setStatus("Viewed");
                complaintRequestRepository.save(comp);
            }
        }
    }

    // --- DELETE ---

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public void deleteComplaint(Long id) {
        complaintRequestRepository.deleteById(id);
    }

    // --- OTHER EXISTING METHODS ---
    public List<Message> findAllMessagesForDirector() {
        return messageRepository.findByRecipientRoleOrderBySentAtDesc(DIRECTOR_ROLE);
    }

    @Transactional
    public void markAllMessagesAsReadForDirector() {
        messageRepository.markAllAsReadByRecipientRole(DIRECTOR_ROLE);
    }
}