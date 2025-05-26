package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.exception.AppUserNotFoundException;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.Event;
import com.northcoders.jvevents.repository.AppUserRepository;
import com.northcoders.jvevents.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    @Mock
    private AppUserRepository mockUserRepository;

    @Mock
    private EventRepository mockEventRepository;

    @InjectMocks
    private AppUserServiceImpl userService;

    private AppUser user;
    private Event event;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        event = Event.builder().id(100L).title("Hackathon").build();

        user = AppUser.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .events(List.of(event))
                .build();
    }

    @Test
    @DisplayName("getUserById: should return user DTO")
    public void testGetUserById() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        AppUserDTO result = userService.getUserById(1L);
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getEventIds()).contains(100L);
    }

    @Test
    @DisplayName("getUserById: should throw if not found")
    public void testGetUserByIdThrows() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AppUserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("getAllUsers: should return all users as DTOs")
    public void testGetAllUsers() {
        when(mockUserRepository.findAll()).thenReturn(List.of(user));

        List<AppUserDTO> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("deleteUserById: should delete existing user")
    public void testDeleteUserById() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(mockUserRepository).delete(user);

        userService.deleteUserById(1L);

        verify(mockUserRepository).delete(user);
    }
}
