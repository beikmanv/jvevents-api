package com.northcoders.jvevents.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class FirebaseController {

    @PostMapping("/firebase/verify-token")
    public ResponseEntity<?> authenticate(@RequestParam("idToken") String idToken, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("📱 Token received: " + idToken);

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            session.setAttribute("email", email);
            System.out.println("📦 Session created: " + session.getId());
            System.out.println("✅ User authenticated: " + email);

            return ResponseEntity.ok(Map.of(
                    "uid", uid,
                    "email", email
            ));

        } catch (Exception e) {
            System.err.println("❌ Failed to verify token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token");
        }
    }
}
