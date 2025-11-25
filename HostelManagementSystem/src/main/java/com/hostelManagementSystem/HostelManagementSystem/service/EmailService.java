package com.hostelManagementSystem.HostelManagementSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendCode(String toEmail, String code) {
        try {
            // 1. MimeMessage එකක් Create කිරීම (HTML යැවීමට)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password | HMS - UOP");
            helper.setFrom("yourgmail@gmail.com"); // ඔයාගේ email එක මෙතන දාන්න

            // 2. Professional HTML Design එක (CSS දාලා ලස්සන කරලා)
            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                        .container { max-width: 600px; margin: 30px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eee; }
                        .header h1 { color: #1e3a8a; margin: 0; font-size: 24px; }
                        .content { padding: 20px; text-align: center; color: #333; }
                        .code-box { 
                            background-color: #eff6ff; 
                            color: #1e3a8a; 
                            font-size: 32px; 
                            font-weight: bold; 
                            letter-spacing: 5px; 
                            padding: 15px; 
                            margin: 20px 0; 
                            border-radius: 5px; 
                            border: 1px dashed #1e3a8a;
                            display: inline-block;
                        }
                        .footer { text-align: center; font-size: 12px; color: #888; margin-top: 20px; border-top: 1px solid #eee; padding-top: 10px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Hostel Management System</h1>
                            <p style="margin: 5px 0 0; font-size: 14px; color: #666;">University of Peradeniya</p>
                        </div>
                        <div class="content">
                            <p>Hello,</p>
                            <p>We received a request to reset your password. Please use the verification code below to proceed:</p>
                            
                            <div class="code-box">
                                %s
                            </div>
                            
                            <p style="font-size: 14px; color: #555;">If you did not request this, please ignore this email.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2025 Hostel Management System. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(code); // මෙතනින් code එක HTML එක ඇතුලට දානවා

            // 3. HTML content එක set කිරීම (true කියන්නේ මෙය HTML බවයි)
            helper.setText(htmlContent, true);

            // 4. Send Email
            mailSender.send(message);
            System.out.println("Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}