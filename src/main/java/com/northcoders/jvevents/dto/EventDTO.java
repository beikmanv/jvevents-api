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
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Set<AppUserDTO> users;
}
