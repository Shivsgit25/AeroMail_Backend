package com.aeromail.model.dto;
import java.time.LocalDateTime;
import lombok.Data;


@Data
public class DraftEmailResponse {

    private Long id;
    private String toEmail;
    private String subject;
    private String message;
    private LocalDateTime updatedAt;
}