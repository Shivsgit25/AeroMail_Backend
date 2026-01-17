package com.aeromail.model.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data

public class InboxEmailResponse {

	private Long id;
	private String fromEmail;
	private String subject;
	private String message;
	private LocalDateTime sentAt;

}
