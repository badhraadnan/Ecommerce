package com.ecom.orderService.utils;



import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class emailSend {

    @Autowired
    private JavaMailSender mailSender;

    public void SendEmail(String to, String subject, String body, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            InternetAddress address = new InternetAddress("badhraadnan8128@gmail.com","E-commerce");
            helper.setFrom(address);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml); // Set isHtml = true for HTML email

            mailSender.send(message);
            System.out.println("Mail Send "+ LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

