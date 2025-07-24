package com.example.demo.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtil {
    private final JavaMailSender mailSender;

    public void send(String subject, String to, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // helper.setFrom("your_email@gmail.com");
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error(ex.getMessage());
        }
    }

}
