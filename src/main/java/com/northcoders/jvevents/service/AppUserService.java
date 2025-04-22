package com.northcoders.jvevents.service;

import com.northcoders.jvevents.dto.AppUserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface AppUserService {

    String getAuthenticatedUserEmail(OAuth2User principal);

    List<AppUserDTO> getAllUsers();

    AppUserDTO getUserById(Long id);

    void deleteUserById(Long id);
}
