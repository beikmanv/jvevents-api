package com.northcoders.jvevents.service;

import com.northcoders.jvevents.exception.ResourceNotFoundException;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class StaffCheckService {

    @Autowired
    private AppUserRepository appUserRepository;

    // Method to check if the authenticated user has staff privileges
    public boolean hasStaffPrivileges(Authentication authentication) {
        String email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return user.isStaff(); // Check the 'isStaff' flag
    }
}
