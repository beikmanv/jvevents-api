package com.northcoders.jvevents.controller;

import com.northcoders.jvevents.exception.ResourceNotFoundException;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.repository.AppUserRepository;
import com.northcoders.jvevents.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @PutMapping("/set-staff/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> setStaffStatus(@PathVariable Long userId, @RequestParam boolean isStaff) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setStaff(isStaff);
        appUserRepository.save(user);

        return ResponseEntity.ok("User staff status updated successfully");
    }

    @GetMapping("/check-staff")
    public ResponseEntity<String> checkIfStaff(@AuthenticationPrincipal OAuth2User principal) {
        String email = appUserService.getAuthenticatedUserEmail(principal);
        AppUser user = appUserService.getUserByEmail(email);
        if (user != null && user.isStaff()) {
            return ResponseEntity.ok("You are a staff member!");
        } else {
            return ResponseEntity.ok("You are not a staff member.");
        }
    }

    // ✅ THIS WAS MISSING @GetMapping
    @GetMapping("/is-staff")
    public ResponseEntity<Boolean> isUserStaff(Authentication authentication) {
        String email = authentication.getName();
        AppUser user = appUserService.getUserByEmail(email);

        if (user == null) {
            System.out.println("User not found for email: " + email);
            return ResponseEntity.ok(false);
        }

        System.out.println("Checking staff status for email: " + email + ", isStaff=" + user.isStaff());
        return ResponseEntity.ok(user.isStaff());
    }
}
