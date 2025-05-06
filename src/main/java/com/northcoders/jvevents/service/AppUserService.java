package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import com.northcoders.jvevents.dto.EventDTO;
import com.northcoders.jvevents.model.AppUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface AppUserService {

    String getAuthenticatedUserEmail(OAuth2User principal);

    List<AppUserDTO> getAllUsers();

    AppUserDTO getUserById(Long userId);

    void deleteUserById(Long userId);

    AppUser getUserByEmail(String email);

    AppUser saveUser(AppUser user);

}
