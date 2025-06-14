package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.dto.SignupResultDTO;
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

    @Autowired
    private EmailService emailService;

    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        eventRepository.findAll().forEach(eventList::add);

        return eventList.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
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
    public EventDTO updateEvent(Long eventId, EventDTO eventDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setLocation(eventDTO.getLocation());

        Event updatedEvent = eventRepository.save(event);
        return mapToDTO(updatedEvent);
    }

    @Override
    public void deleteEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    private EventDTO mapToDTO(Event event) {
        Set<AppUserDTO> userDTOs = event.getUsers() != null
                ? event.getUsers().stream()
                .map(user -> new AppUserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toSet())
                : new HashSet<>();

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setLocation(event.getLocation());
        eventDTO.setCreatedAt(event.getCreatedAt());
        eventDTO.setModifiedAt(event.getModifiedAt());
        eventDTO.setUsers(userDTOs);
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

    @Override
    @Transactional
    public SignupResultDTO signupForEvent(Long eventId, String userEmail) {
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        if (user.getEvents().contains(event)) {
            throw new IllegalStateException("User is already signed up for this event.");
        }

        user.getEvents().add(event);
        event.getUsers().add(user);
        appUserRepository.save(user);

        emailService.sendEventSignupConfirmation(user.getEmail(), event.getTitle());

        return new SignupResultDTO(user.getEmail(), event.getTitle());
    }

    public List<AppUserDTO> getUsersForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        return event.getUsers().stream()
                .map(user -> AppUserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .eventIds(user.getEvents().stream()
                                .map(Event::getId)
                                .collect(Collectors.toSet()))
                        .createdAt(user.getCreatedAt())
                        .modifiedAt(user.getModifiedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // Get all events assigned to a specific user
    public List<EventDTO> getEventsForUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        return user.getEvents().stream()
                .map(event -> new EventDTO(event.getId(), event.getTitle(), event.getDescription(),
                        event.getEventDate(), event.getLocation(), event.getCreatedAt(), event.getModifiedAt()))
                .collect(Collectors.toList());
    }
}
