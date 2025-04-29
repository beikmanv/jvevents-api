package com.northcoders.jvevents.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/debug")
public class CookieDebugController {

    @GetMapping("/cookies")
    public ResponseEntity<String> debugCookies(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) {
                System.out.println("No cookies received.");
                return ResponseEntity.ok("No cookies sent.");
            }

            StringBuilder result = new StringBuilder("Cookies:\n");
            for (Cookie cookie : cookies) {
                System.out.println("🍪 " + cookie.getName() + " = " + cookie.getValue());
                result.append(cookie.getName())
                        .append(" = ")
                        .append(cookie.getValue())
                        .append("\n");
            }

            return ResponseEntity.ok(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("💥 Server error: " + e.getMessage());
        }
    }
}
