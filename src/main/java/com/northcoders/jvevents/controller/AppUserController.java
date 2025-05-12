package com.northcoders.jvevents.controller;

import com.google.firebase.auth.FirebaseToken;
import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.service.AppUserService;
import com.northcoders.jvevents.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        String email = appUserService.getAuthenticatedUserEmail(principal);
        return ResponseEntity.ok(Map.of("email", email));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(appUserService.getUserById(userId));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        appUserService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventDTO>> getEventsForUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal FirebaseToken token // Token can be null
    ) {
        if (token != null) {
            String email = token.getEmail();
            Long authenticatedUserId = appUserService.getUserByEmail(email).getId();

            // Ensure the authenticated user is requesting their own events
            if (!authenticatedUserId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        // Fetch events for the user regardless of authentication
        List<EventDTO> events = eventService.getEventsForUser(userId);
        return events.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(events);
    }

}
