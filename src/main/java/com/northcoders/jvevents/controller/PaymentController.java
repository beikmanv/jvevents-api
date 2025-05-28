package com.northcoders.jvevents.controller;

import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.Payment;
import com.northcoders.jvevents.repository.AppUserRepository;
import com.northcoders.jvevents.repository.PaymentRepository;
import com.northcoders.jvevents.service.EmailServiceImpl;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.security.Principal;

//@CrossOrigin(origins = "*") // Allow all for testing; restrict later
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        System.out.println("✅ Stripe API key loaded.");
        Stripe.apiKey = stripeSecretKey;
    }

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/charge")
    public ResponseEntity<?> charge(@RequestBody Map<String, String> payload, Principal principal) {
        String token = payload.get("token");
        System.out.println("💳 Received token: " + token.substring(0, 8) + "...");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (principal != null) {
            System.out.println("🔐 Payment requested by: " + principal.getName());
        }

        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is missing"));
        }

        // PaymentIntent intent = PaymentIntent.create(params); // Alternative method

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", 500); // $5.00 in cents
            params.put("currency", "usd");
            params.put("source", token); // ✅ This is the token, e.g., "tok_visa"
            params.put("description", "Google Pay Donation");

            Charge charge = Charge.create(params); // ✅ Correct method for test tokens

            // 🟡 Get user by email from principal
            AppUser user = userRepository.findByEmail(principal.getName()).orElse(null);

            Payment payment = Payment.builder()
                    .chargeId(charge.getId())
                    .amount(500)
                    .currency("usd")
                    .email(principal.getName())
                    .status(charge.getStatus())
                    .build();

            // ✅ Save Payment to DB
            paymentRepository.save(payment);

            if (user != null) {
                emailService.sendPaymentConfirmation(user.getEmail(), charge.getId(), "$5.00");
            }

            return ResponseEntity.ok(Map.of(
                    "id", charge.getId(),
                    "status", charge.getStatus()
            ));

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
