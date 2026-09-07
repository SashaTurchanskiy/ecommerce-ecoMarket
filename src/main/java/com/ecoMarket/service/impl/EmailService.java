package com.ecoMarket.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail,
                                         String otp, String subject, String text) throws MessagingException {
        try {
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);

        }catch (MailException ex){
            throw new MailSendException("Failed to send email to " + userEmail, ex);
        }
    }
}
