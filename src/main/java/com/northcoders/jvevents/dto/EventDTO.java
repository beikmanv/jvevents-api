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

    public EventDTO(Long id, String title, String description, LocalDateTime eventDate, String location, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public EventDTO(Long id, String title, LocalDateTime eventDate, String location) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.location = location;
    }
}
