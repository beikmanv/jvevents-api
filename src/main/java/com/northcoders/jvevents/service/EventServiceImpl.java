package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.exception.EventNotFoundException;
import com.northcoders.jvevents.model.Event;
import com.northcoders.jvevents.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

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
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());  // Mapping title
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEventDate(event.getEventDate());  // Mapping eventDate
        eventDTO.setLocation(event.getLocation());
        eventDTO.setCreatedAt(event.getCreatedAt());  // Mapping createdAt
        eventDTO.setModifiedAt(event.getModifiedAt());  // Mapping modifiedAt
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
}
