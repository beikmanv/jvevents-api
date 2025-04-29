package com.northcoders.jvevents.controller;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // Get event by id
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        EventDTO eventDTO = eventService.getEventById(id);
        if (eventDTO != null) {
            return new ResponseEntity<>(eventDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new event - Only staff can access this
    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // Update event by id - Only staff can access this
    @PutMapping("/update/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        EventDTO updatedEvent = eventService.updateEvent(id, eventDTO);
        if (updatedEvent != null) {
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete event by id - Only staff can access this
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Sign up for event
    @PostMapping("/{id}/signup")
    public ResponseEntity<?> signupForEvent(@PathVariable Long id, @RequestParam String email) {
        try {
            eventService.signupForEvent(id, email);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            // Already signed up
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already signed up for this event.");
        } catch (Exception e) {
            // Other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<AppUserDTO>> getUsersForEvent(@PathVariable Long id) {
        List<AppUserDTO> users = eventService.getUsersForEvent(id);
        return ResponseEntity.ok(users);
    }
}
