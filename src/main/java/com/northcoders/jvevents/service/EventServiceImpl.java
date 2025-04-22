package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.exception.EventNotFoundException;
import com.northcoders.jvevents.exception.UserNotFoundException;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.Event;
import com.northcoders.jvevents.repository.EventRepository;
import com.northcoders.jvevents.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        eventRepository.findAll().forEach(eventList::add);

        return eventList.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return mapToDTO(event);
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        Event event = mapToEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return mapToDTO(savedEvent);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setLocation(eventDTO.getLocation());

        Event updatedEvent = eventRepository.save(event);
        return mapToDTO(updatedEvent);
    }

    @Override
    public void deleteEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    private EventDTO mapToDTO(Event event) {
        Set<AppUserDTO> userDTOs = event.getUsers().stream()
                .map(user -> new AppUserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toSet());

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());  // Mapping title
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEventDate(event.getEventDate());  // Mapping eventDate
        eventDTO.setLocation(event.getLocation());
        eventDTO.setCreatedAt(event.getCreatedAt());  // Mapping createdAt
        eventDTO.setModifiedAt(event.getModifiedAt());  // Mapping modifiedAt
        eventDTO.setUsers(userDTOs);  // Add users here
        return eventDTO;
    }

    private Event mapToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setId(eventDTO.getId());
        event.setTitle(eventDTO.getTitle());  // Mapping title
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());  // Mapping eventDate
        event.setLocation(eventDTO.getLocation());
        return event;
    }

    @Transactional
    public void signupForEvent(Long eventId, String userEmail) {
            System.out.println("Signing up user with email: " + userEmail);
            AppUser user = appUserRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));
            if (user.getEvents().contains(event)) {
                throw new IllegalStateException("User is already signed up for this event.");
            }
            user.getEvents().add(event); // Only update the owning side
            appUserRepository.save(user);
            System.out.println("✅ User added to event!");
    }
}
