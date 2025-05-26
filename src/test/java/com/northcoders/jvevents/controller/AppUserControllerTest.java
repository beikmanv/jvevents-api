package com.northcoders.jvevents.controller;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.service.AppUserService;
import com.northcoders.jvevents.service.EmailService;
import com.northcoders.jvevents.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AppUserControllerTest {

    @Mock
    private AppUserService mockUserService;

    @Mock
    private EventService mockEventService;

    @Mock
    private EmailService mockEmailService;

    @InjectMocks
    private AppUserController appUserController;

    private MockMvc mockMvc;

    private AppUserDTO userDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();

        userDTO = AppUserDTO.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .eventIds(Set.of(1L, 2L))
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/users - returns all users")
    public void testGetAllUsers() throws Exception {
        when(mockUserService.getAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Alice"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId} - returns single user by ID")
    public void testGetUserById() throws Exception {
        when(mockUserService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("GET /api/v1/users/{userId}/events - returns user's events")
    public void testGetEventsForUser() throws Exception {
        EventDTO eventDTO = EventDTO.builder().id(1L).title("JVConf").build();
        when(mockEventService.getEventsForUser(1L)).thenReturn(List.of(eventDTO));

        mockMvc.perform(get("/api/v1/users/1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("JVConf"));
    }
}
