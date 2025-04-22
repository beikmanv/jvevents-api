package com.northcoders.jvevents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDTO {

    private Long id;
    private String username;
    private String email;
    private Set<Long> eventIds;  // We'll send event IDs as references, not the full event object.
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public AppUserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
