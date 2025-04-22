package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.model.AppUser;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    EventDTO getEventById(Long id);
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long id, EventDTO eventDTO);
    void deleteEventById(Long id);
    void signupForEvent(Long eventId, String userEmail);
    List<AppUserDTO> getUsersForEvent(Long id);
    List<EventDTO> getEventsForUser(Long userId);
}
