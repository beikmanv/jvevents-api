package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.SignupResult;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String emailPassword;

    public void sendPaymentConfirmation(String toEmail, String chargeId, String amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("JV Events - Payment Confirmation");
        message.setText("Thanks for your payment!\n\nCharge ID: " + chargeId + "\nAmount: " + amount);

        mailSender.send(message);
    }

    @Override
    public void sendEventSignupConfirmation(String recipientEmail, String eventName) {
        System.out.println("📧 Sending event signup confirmation to " + recipientEmail + " for: " + eventName);
        String subject = "Event Signup Confirmation";
        String body = "You’ve successfully signed up for the event: " + eventName;
        sendEmail(recipientEmail, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.debug", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("📤 Email sent to: " + to + " | Subject: " + subject);

        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + to);
            e.printStackTrace();
        }
    }
}

