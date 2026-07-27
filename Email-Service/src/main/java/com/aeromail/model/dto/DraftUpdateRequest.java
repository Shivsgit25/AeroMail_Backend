package com.aeromail.model.dto;
import lombok.Data;

@Data
public class DraftUpdateRequest {

    private String toEmail;
    private String subject;
    private String message;
}