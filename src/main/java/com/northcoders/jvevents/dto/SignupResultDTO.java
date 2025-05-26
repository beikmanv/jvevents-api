package com.northcoders.jvevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResultDTO {
    private String userEmail;
    private String eventTitle;
}
