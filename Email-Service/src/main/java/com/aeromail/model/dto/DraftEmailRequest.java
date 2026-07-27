package com.aeromail.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DraftEmailRequest {

    @NotBlank
    private String fromEmail;

    private String toEmail;

    private String subject;

    private String message;
}

