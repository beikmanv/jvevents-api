package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.dto.SignupResult;
import com.northcoders.jvevents.model.AppUser;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    EventDTO getEventById(Long eventId);
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long eventId, EventDTO eventDTO);
    void deleteEventById(Long eventId);
    SignupResult signupForEvent(Long eventId, String userEmail);
    List<AppUserDTO> getUsersForEvent(Long eventId);
    List<EventDTO> getEventsForUser(Long userId);
}
