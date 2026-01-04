package com.aeromail.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    @Email
    @NotBlank
    private String fromEmail;

    @Email
    @NotBlank
    private String toEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String message;

    // getters & setters
}
