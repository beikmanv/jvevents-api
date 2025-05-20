package com.northcoders.jvevents.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentConfirmation(String toEmail, String chargeId, String amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("JV Events - Payment Confirmation");
        message.setText("Thanks for your payment!\n\nCharge ID: " + chargeId + "\nAmount: " + amount);

        mailSender.send(message);
    }
}

