package com.northcoders.jvevents.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    private Long id;
    private String title;  // renamed from 'name' to 'title'
    private String description;
    private LocalDateTime eventDate;  // renamed from 'startDate' to 'eventDate'
    private String location;
    private LocalDateTime createdAt;  // added for tracking creation time
    private LocalDateTime modifiedAt;  // added for tracking modification time
}
