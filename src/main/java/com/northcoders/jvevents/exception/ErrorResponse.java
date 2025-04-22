package com.northcoders.jvevents.exception;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
