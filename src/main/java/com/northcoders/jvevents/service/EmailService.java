package com.northcoders.jvevents.service;

public interface EmailService {
    void sendPaymentConfirmation(String recipientEmail, String chargeId, String amount);
    void sendEventSignupConfirmation(String recipientEmail, String eventName);
}

