package com.northcoders.jvevents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupResult {
    private String userEmail;
    private String eventTitle;
}
