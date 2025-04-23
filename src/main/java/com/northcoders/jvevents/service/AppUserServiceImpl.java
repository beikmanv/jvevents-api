package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.exception.*;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.Event;
import com.northcoders.jvevents.repository.AppUserRepository;
import com.northcoders.jvevents.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public String getAuthenticatedUserEmail(OAuth2User principal) {
        if (principal == null) {
            throw new UnauthenticatedUserException("User is not authenticated");
        }
        String email = principal.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new UnauthenticatedUserException("Email not found in OAuth2 user attributes");
        }
        return email;
    }

    @Override
    public List<AppUserDTO> getAllUsers() {
        List<AppUser> appUserList = new ArrayList<>();
        appUserRepository.findAll().forEach(appUserList::add);

        return appUserList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppUserDTO getUserById(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException("User not found"));
        return mapToDTO(appUser);
    }

    @Override
    public void deleteUserById(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new AppUserNotFoundException("User not found"));
        appUserRepository.delete(appUser);
    }

    private AppUserDTO mapToDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setEventIds(appUser.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet()));
        appUserDTO.setCreatedAt(appUser.getCreatedAt());
        appUserDTO.setModifiedAt(appUser.getModifiedAt());
        return appUserDTO;
    }

    private AppUser mapToEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setId(appUserDTO.getId());
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setEmail(appUserDTO.getEmail());

        if (appUserDTO.getEventIds() != null) {
            List<Event> events = appUserDTO.getEventIds().stream()
                    .map(eventId -> eventRepository.findById(eventId)
                            .orElseThrow(() -> new RuntimeException("Event not found")))
                    .collect(Collectors.toList());
            appUser.setEvents(events);
        }
        return appUser;
    }

    // Get user by email
    @Override
    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
