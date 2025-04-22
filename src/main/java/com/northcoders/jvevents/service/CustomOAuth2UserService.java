package com.northcoders.jvevents.service;

import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.model.CustomOAuth2User;
import com.northcoders.jvevents.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService was called!");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String username = (String) attributes.get("name");

        System.out.println("User details: " + attributes);

        // Save user to database if not present
        appUserRepository.findByEmail(email).orElseGet(() -> {
            AppUser newUser = AppUser.builder()
                    .email(email)
                    .username(username)
                    .build();
            return appUserRepository.save(newUser);
        });

        return new CustomOAuth2User(oAuth2User, oAuth2User.getAuthorities());
    }
}
