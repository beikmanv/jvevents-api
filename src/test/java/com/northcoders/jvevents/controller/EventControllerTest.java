package com.northcoders.jvevents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.service.EventService;
import com.northcoders.jvevents.dto.SignupResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest {

    @Mock
    private EventService mockEventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    private EventDTO eventDTO;
    private AppUserDTO appUserDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        appUserDTO = AppUserDTO.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        eventDTO = EventDTO.builder()
                .id(1L)
                .title("Tech Meetup")
                .description("An event for tech enthusiasts")
                .eventDate(LocalDateTime.of(2025, 6, 15, 18, 30))
                .location("London")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .users(Set.of(appUserDTO))
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/events - returns all events")
    public void testGetAllEvents() throws Exception {
        when(mockEventService.getAllEvents()).thenReturn(List.of(eventDTO));

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(eventDTO.getId()))
                .andExpect(jsonPath("$[0].title").value(eventDTO.getTitle()))
                .andExpect(jsonPath("$[0].description").value(eventDTO.getDescription()));
    }

    @Test
    @DisplayName("GET /api/v1/events/{id} - returns single event")
    public void testGetEventById() throws Exception {
        when(mockEventService.getEventById(1L)).thenReturn(eventDTO);

        mockMvc.perform(get("/api/v1/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.title").value(eventDTO.getTitle()));
    }

    @Test
    @DisplayName("POST /api/v1/events/create - creates new event")
    public void testCreateEvent() throws Exception {
        when(mockEventService.createEvent(any(EventDTO.class))).thenReturn(eventDTO);

        mockMvc.perform(post("/api/v1/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(eventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Tech Meetup"));
    }

    @Test
    @DisplayName("PUT /api/v1/events/update/{id} - updates an event")
    public void testUpdateEvent() throws Exception {
        when(mockEventService.updateEvent(eq(1L), any(EventDTO.class))).thenReturn(eventDTO);

        mockMvc.perform(put("/api/v1/events/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSON(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.title").value("Tech Meetup"));
    }

    @Test
    @DisplayName("DELETE /api/v1/events/{id} - deletes event")
    public void testDeleteEvent() throws Exception {
        doNothing().when(mockEventService).deleteEventById(1L);

        mockMvc.perform(delete("/api/v1/events/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/v1/events/{id}/signup - user signs up for event")
    public void testSignupForEvent() throws Exception {
        SignupResultDTO mockResult = new SignupResultDTO("test@example.com", "Test Event");

        when(mockEventService.signupForEvent(1L, "test@example.com"))
                .thenReturn(mockResult);

        mockMvc.perform(post("/api/v1/events/1/signup")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/events/{id}/users - get users for event")
    public void testGetUsersForEvent() throws Exception {
        when(mockEventService.getUsersForEvent(1L)).thenReturn(List.of(appUserDTO));

        mockMvc.perform(get("/api/v1/events/1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    private String toJSON(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Could not convert to JSON", e);
        }
    }
}
