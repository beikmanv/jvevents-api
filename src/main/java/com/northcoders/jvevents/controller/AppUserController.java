package com.northcoders.jvevents.controller;

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

    // Get all users
    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        List<AppUserDTO> users = appUserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Get current user
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        String email = appUserService.getAuthenticatedUserEmail(principal);
        return ResponseEntity.ok(Map.of("email", email));
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(appUserService.getUserById(id), HttpStatus.OK);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        appUserService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get events assigned to a user
    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventDTO>> getEventsForUser(@PathVariable Long userId) {
        List<EventDTO> events = eventService.getEventsForUser(userId);
        if (events != null && !events.isEmpty()) {
            return ResponseEntity.ok(events);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
