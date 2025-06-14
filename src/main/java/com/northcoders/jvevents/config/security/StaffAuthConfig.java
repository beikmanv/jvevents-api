package com.northcoders.jvevents.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class StaffAuthConfig {

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> staffOnlyAuthorizationManager() {
        return (authenticationSupplier, context) -> {
            Authentication auth = authenticationSupplier.get();
            boolean isAllowed = auth != null &&
                    auth.isAuthenticated() &&
                    auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"));
            return new AuthorizationDecision(isAllowed);
        };
    }
}
