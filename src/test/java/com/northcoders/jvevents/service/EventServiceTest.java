package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.dto.SignupResultDTO;
import com.northcoders.jvevents.exception.EventNotFoundException;
import com.northcoders.jvevents.exception.UserNotFoundException;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.Event;
import com.northcoders.jvevents.repository.AppUserRepository;
import com.northcoders.jvevents.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EventServiceImpl eventService;

    private AppUser user;
    private Event event;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = AppUser.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .events(new ArrayList<>())
                .build();

        event = Event.builder()
                .id(1L)
                .title("JV Conference")
                .description("Annual Tech Meetup")
                .eventDate(LocalDateTime.now())
                .location("London")
                .users(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("signupForEvent: should add user to event and send email")
    public void testSignupForEvent() {
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(appUserRepository.save(any(AppUser.class))).thenReturn(user);

        SignupResultDTO result = eventService.signupForEvent(event.getId(), user.getEmail());

        assertThat(result.getUserEmail()).isEqualTo(user.getEmail());
        assertThat(user.getEvents()).contains(event);
        verify(emailService).sendEventSignupConfirmation(user.getEmail(), event.getTitle());
    }

    @Test
    @DisplayName("signupForEvent: should throw if user already signed up")
    public void testSignupAlreadyRegistered() {
        user.getEvents().add(event);
        when(appUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(IllegalStateException.class, () ->
                eventService.signupForEvent(event.getId(), user.getEmail()));
    }

    @Test
    @DisplayName("getEventById: should throw if event not found")
    public void testGetEventByIdNotFound() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(999L));
    }

    @Test
    @DisplayName("getAllEvents: should return list of EventDTOs")
    public void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(event));
        var result = eventService.getAllEvents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("JV Conference");
    }

    @Test
    @DisplayName("updateEvent: should update and return event")
    public void testUpdateEvent() {
        Event updatedEvent = Event.builder()
                .id(1L)
                .title("Updated Event")
                .description("Updated Description")
                .eventDate(LocalDateTime.now())
                .location("Manchester")
                .build();

        EventDTO dto = new EventDTO(1L, "Updated Event", "Updated Description", LocalDateTime.now(), "Manchester", null, null);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        EventDTO result = eventService.updateEvent(1L, dto);

        assertThat(result.getTitle()).isEqualTo("Updated Event");
        assertThat(result.getLocation()).isEqualTo("Manchester");
    }

    @Test
    @DisplayName("deleteEventById: should delete event if found")
    public void testDeleteEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);

        eventService.deleteEventById(1L);

        verify(eventRepository).delete(event);
    }

    @Test
    @DisplayName("getUsersForEvent: should return all users for event")
    public void testGetUsersForEvent() {
        event.setUsers(List.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        List<AppUserDTO> users = eventService.getUsersForEvent(1L);

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("getEventsForUser: should return all events for user")
    public void testGetEventsForUser() {
        user.setEvents(List.of(event));
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(user));

        List<EventDTO> events = eventService.getEventsForUser(1L);

        assertThat(events).hasSize(1);
        assertThat(events.get(0).getTitle()).isEqualTo("JV Conference");
    }
}
