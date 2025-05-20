package com.quocanh.finance_manager_plan_backend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    public void sendVerificationEmail(String to) throws MessagingException {
        String subject = "Email Verification";

        // Create context and add variables
        Context context = new Context();
        context.setVariable("name", "User");

        String emailContent = templateEngine.process("email-template", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String otpCode) throws MessagingException {
        String subject = "Your OTP Code";
        Context context = new Context();
        context.setVariable("otpCode", otpCode);
        String emailContent = templateEngine.process("otp_template", context);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

}
