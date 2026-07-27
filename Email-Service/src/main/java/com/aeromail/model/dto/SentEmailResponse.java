package com.aeromail.model.dto;


import java.time.LocalDateTime;

import com.aeromail.model.enums.EmailStatus;

import lombok.Data;

@Data

public class SentEmailResponse {

	private Long id;
	private String toEmail;
	private String subject;
	private String message;
	private EmailStatus status;
	private LocalDateTime sentAt;

}
