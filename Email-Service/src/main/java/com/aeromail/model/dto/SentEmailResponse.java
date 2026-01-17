package com.aeromail.model.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data

public class SentEmailResponse {

	private Long id;
	private String toEmail;
	private String subject;
	private String message;
	private String status;
	private LocalDateTime sentAt;

}
