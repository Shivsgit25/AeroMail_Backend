package com.aeromail.model.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private String status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.status = "FAILURE";
        this.timestamp = LocalDateTime.now();
    }
}
