package com.northcoders.jvevents.config.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.northcoders.jvevents.model.AppUser;
import com.northcoders.jvevents.service.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);

            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String email = decodedToken.getEmail();
                String name = decodedToken.getName();

                logger.info("🔥 Firebase decoded email: {}", email);

                AppUser user;
                try {
                    user = appUserService.getUserByEmail(email);
                } catch (Exception e) {
                    // User doesn't exist, create one
                    user = new AppUser();
                    user.setEmail(email);
                    user.setUsername(name != null ? name : "User");
                    user = appUserService.saveUser(user);
                    logger.info("🆕 Created new user in DB: {}", email);
                }

                List<SimpleGrantedAuthority> authorities = user.isStaff()
                        ? List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_STAFF"))
                        : List.of(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                logger.warn("❌ JWT token verification or user creation failed: {}", e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
